package io.naccoll.boilerplate.pay.interfaces.api.openapi;

import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.json.JSONUtil;
import io.naccoll.boilerplate.core.aop.response.ResponseResultBody;
import io.naccoll.boilerplate.core.cache.CacheTemplate;
import io.naccoll.boilerplate.core.dto.BaseResponse;
import io.naccoll.boilerplate.core.enums.EnumHelper;
import io.naccoll.boilerplate.core.exception.BusinessError;
import io.naccoll.boilerplate.core.exception.BusinessException;
import io.naccoll.boilerplate.core.exception.ClientParamException;
import io.naccoll.boilerplate.core.i18n.LanguageHelper;
import io.naccoll.boilerplate.core.security.service.DataSecurityService;
import io.naccoll.boilerplate.pay.constant.PayApiConstant;
import io.naccoll.boilerplate.pay.convert.PayJournalConvert;
import io.naccoll.boilerplate.pay.convert.PayRefundJournalConvert;
import io.naccoll.boilerplate.pay.dto.*;
import io.naccoll.boilerplate.pay.enums.PayMerchantSignAlgorithm;
import io.naccoll.boilerplate.pay.model.PayJournalPo;
import io.naccoll.boilerplate.pay.model.PayMerchantPo;
import io.naccoll.boilerplate.pay.model.PayRefundJournalPo;
import io.naccoll.boilerplate.pay.service.PayJournalQueryService;
import io.naccoll.boilerplate.pay.service.PayMerchantQueryService;
import io.naccoll.boilerplate.pay.service.PayRefundJournalQueryService;
import io.naccoll.boilerplate.pay.service.PayServiceFacadeImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Optional;

/**
 * The type Pay openapi api v 1 merchant api.
 */
@Slf4j
@Tag(name = "商户OpenApi")
@RestController
@ResponseResultBody
@RequestMapping(PayApiConstant.OpenapiApiV1.MERCHANT)
public class PayOpenapiApiV1MerchantApi {

	@Resource
	private PayServiceFacadeImpl payServiceFacadeImpl;

	@Resource
	private PayJournalQueryService payJournalQueryService;

	@Resource
	private PayRefundJournalQueryService payRefundJournalQueryService;

	@Resource
	private PayMerchantQueryService payMerchantQueryService;

	@Resource
	private PayJournalConvert payJournalConvert;

	@Resource
	private PayRefundJournalConvert refundJournalConvert;

	@Resource
	private HttpServletRequest request;

	@Resource
	private LanguageHelper languageHelper;

	@Resource
	private CacheTemplate cacheTemplate;

	@Resource
	private DataSecurityService dataSecurityService;

	/**
	 * Process response entity.
	 * @param exception the exception
	 * @return the response entity
	 */
	@ExceptionHandler(BusinessException.class)
	@ResponseBody
	public ResponseEntity<BaseResponse> process(BusinessException exception) {
		String message = languageHelper.getMessage(exception.getMessageCode(), exception.getParameters());
		BaseResponse baseResponse = BaseResponse.newBuilder()
			.error(exception.getError().getAlias())
			.errorCode(exception.getError().getCode())
			.status(exception.getError().getStatus())
			.path(request.getServletPath())
			.message(message)
			.build();
		return ResponseEntity.status(baseResponse.getStatus()).body(baseResponse);
	}

	/**
	 * Pay create order dto.
	 * @param request the request
	 * @param command the command
	 * @return the create order dto
	 */
	@PostMapping("/order")
	public CreateOrderDto pay(HttpServletRequest request, @Validated @RequestBody OrderCreateCommand command) {
		log.info("请求支付，请求参数：{},merchantId:{}", JSONUtil.toJsonStr(command), getMerchantId(request));
		checkSign(request);
		command.setPayMerchantId(getMerchantId(request));
		CreateOrderDto createOrderDto = payServiceFacadeImpl.pay(command);
		log.info("支付结果：{}", JSONUtil.toJsonStr(createOrderDto));
		return createOrderDto;
	}

	/**
	 * Cancel order response entity.
	 * @param request the request
	 * @param command the command
	 * @return the response entity
	 */
	@PostMapping("/cancel-order")
	public ResponseEntity<Void> cancelOrder(HttpServletRequest request,
			@Validated @RequestBody OrderCancelCommand command) {
		checkSign(request);
		command.setPayMerchantId(getMerchantId(request));
		payServiceFacadeImpl.cancel(command);
		return ResponseEntity.ok().build();
	}

	/**
	 * Query order pay journal dto.
	 * @param request the request
	 * @param orderNo the order no
	 * @return the pay journal dto
	 */
	@GetMapping("/order")
	public PayJournalDto queryOrder(HttpServletRequest request, String orderNo) {
		checkSign(request);
		Long merchantId = getMerchantId(request);
		PayJournalPo payJournalPo = payJournalQueryService.findMerchantJournal(merchantId, orderNo);
		if (payJournalPo == null) {
			throw new ClientParamException("无法查询到对应交易流水，请检查商户订单号是否正确");
		}
		return payJournalConvert.convertPayJournalDto(payJournalPo);
	}

	/**
	 * Query refund journal pay refund journal dto.
	 * @param request the request
	 * @param command the command
	 * @return the pay refund journal dto
	 */
	@PostMapping("/refund-order")
	public PayRefundJournalDto queryRefundJournal(HttpServletRequest request,
			@RequestBody RefundOrderCreateCommand command) {
		log.info("请求退款，请求参数：{},merchantId:{}", JSONUtil.toJsonStr(command), getMerchantId(request));
		checkSign(request);
		command.setPayMerchantId(getMerchantId(request));
		PayRefundJournalPo refundJournal = payServiceFacadeImpl.refund(command);
		return refundJournalConvert.convertPayRefundJournalDto(refundJournal);
	}

	/**
	 * Refund pay refund journal dto.
	 * @param request the request
	 * @param refundOrderNo the refund order no
	 * @return the pay refund journal dto
	 */
	@GetMapping("/refund-order")
	public PayRefundJournalDto refund(HttpServletRequest request, String refundOrderNo) {
		checkSign(request);
		Long merchantId = getMerchantId(request);
		PayRefundJournalPo journalPo = payRefundJournalQueryService.findByMerchantIdAndRefundOrderNo(merchantId,
				refundOrderNo);
		if (journalPo == null) {
			throw new ClientParamException("无法查询到对应退款流水，请检查商户退款订单号是否正确");
		}
		return refundJournalConvert.convertPayRefundJournalDto(journalPo);
	}

	/**
	 * Check sign.
	 * @param request the request
	 */
	public void checkSign(HttpServletRequest request) {
		String merchantId = request.getHeader("pay-merchant-id");
		String timestamp = request.getHeader("pay-timestamp");
		String noncestr = request.getHeader("pay-noncestr");
		String signature = request.getHeader("pay-signature");
		log.trace("验证签名，merchantId:{}, timestamp:{}, sign: {}", merchantId, timestamp, signature);
		if (ObjectUtils.isEmpty(merchantId) || ObjectUtils.isEmpty(timestamp) || ObjectUtils.isEmpty(signature)) {
			throw new BusinessException("error.sign.check", BusinessError.SIGN_ERROR);
		}
		final int expireTime = 300;
		if (Math.abs(Integer.parseInt(timestamp) - (int) (System.currentTimeMillis() / 1000)) > expireTime) {
			throw new BusinessException("error.sign.check", BusinessError.SIGN_ERROR);
		}
		PayMerchantPo payMerchant = payMerchantQueryService.findByIdNotNull(Long.valueOf(merchantId));
		HmacAlgorithm algorithm = Optional.of(payMerchant.getSignAlgorithm())
			.filter(i -> EnumHelper.isValid(PayMerchantSignAlgorithm.class, i))
			.map(PayMerchantSignAlgorithm::fromId)
			.orElse(PayMerchantSignAlgorithm.HmacSHA256)
			.getAlgorithm();
		String secret = dataSecurityService.decryptStr(payMerchant.getSecret());
		HMac mac = new HMac(algorithm, secret.getBytes());
		String key = merchantId + noncestr + timestamp;
		String cacheKey = String.format("pay-noncestr:%s", key);
		if (!cacheTemplate.setIfAbsent(cacheKey, 1, Duration.ofSeconds(expireTime))) {
			throw new BusinessException("error.sign.check", BusinessError.SIGN_ERROR);
		}
		if (!signature.equals(mac.digestHex(merchantId + noncestr + timestamp))) {
			throw new BusinessException("error.sign.check", BusinessError.SIGN_ERROR);
		}
	}

	/**
	 * Gets merchant id.
	 * @param request the request
	 * @return the merchant id
	 */
	public Long getMerchantId(HttpServletRequest request) {
		String merchantId = request.getHeader("pay-merchant-id");
		return Long.valueOf(merchantId);
	}

}
