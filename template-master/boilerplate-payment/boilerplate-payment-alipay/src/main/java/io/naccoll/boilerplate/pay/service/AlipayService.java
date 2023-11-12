package io.naccoll.boilerplate.pay.service;

import java.math.BigDecimal;
import java.security.Security;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.Lock;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.egzosn.pay.ali.api.AliPayConfigStorage;
import com.egzosn.pay.ali.api.AliPayService;
import com.egzosn.pay.ali.api.CustomAlipayServiceImpl;
import com.egzosn.pay.ali.bean.AliRefundResult;
import com.egzosn.pay.ali.bean.AliTransactionType;
import com.egzosn.pay.ali.bean.CertEnvironment;
import com.egzosn.pay.ali.bean.CustomCertEnvironment;
import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.NoticeParams;
import com.egzosn.pay.common.bean.NoticeRequest;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.bean.PayOutMessage;
import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.http.UriVariables;
import io.naccoll.boilerplate.core.cache.CacheTemplate;
import io.naccoll.boilerplate.core.exception.BusinessError;
import io.naccoll.boilerplate.core.exception.BusinessException;
import io.naccoll.boilerplate.core.exception.ClientParamException;
import io.naccoll.boilerplate.core.exception.ResourceNotFoundException;
import io.naccoll.boilerplate.core.id.IdService;
import io.naccoll.boilerplate.core.properties.ApplicationProperties;
import io.naccoll.boilerplate.core.security.service.DataSecurityService;
import io.naccoll.boilerplate.pay.constant.PayLockConstant;
import io.naccoll.boilerplate.pay.dao.PayAlipayJournalDao;
import io.naccoll.boilerplate.pay.dao.PayAlipayRefundJournalDao;
import io.naccoll.boilerplate.pay.dto.AlipayTradePayResponse;
import io.naccoll.boilerplate.pay.dto.CreateOrderDto;
import io.naccoll.boilerplate.pay.dto.OrderCreateCommand;
import io.naccoll.boilerplate.pay.dto.PaySuccessNotifyMessage;
import io.naccoll.boilerplate.pay.dto.RefundOrderCreateCommand;
import io.naccoll.boilerplate.pay.dto.RefundSuccessNotifyMessage;
import io.naccoll.boilerplate.pay.dto.TradeStatusPollingMessage;
import io.naccoll.boilerplate.pay.enums.PayChannel;
import io.naccoll.boilerplate.pay.enums.PayStatus;
import io.naccoll.boilerplate.pay.enums.PayWay;
import io.naccoll.boilerplate.pay.enums.PaymentStatus;
import io.naccoll.boilerplate.pay.model.PayAlipayConfigPo;
import io.naccoll.boilerplate.pay.model.PayAlipayJournalPo;
import io.naccoll.boilerplate.pay.model.PayAlipayRefundJournalPo;
import io.naccoll.boilerplate.pay.model.PayJournalPo;
import io.naccoll.boilerplate.pay.model.PayMerchantPo;
import io.naccoll.boilerplate.pay.model.PayRefundJournalPo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.egzosn.pay.ali.bean.AliPayConst.CODE;
import static com.egzosn.pay.ali.bean.AliPayConst.SUCCESS_CODE;

/**
 * The type Alipay service.
 */
@Slf4j
@Service
public class AlipayService implements ThirdPaySubSerivce {

	public static final String SYS_SERVICE_PROVIDER_ID = "sys_service_provider_id";

	@Resource
	private ApplicationProperties applicationProperties;

	@Resource
	private PayAlipayConfigQueryService payAlipayConfigQueryService;

	@Resource
	private PayJournalQueryService payJournalQueryService;

	@Resource
	private PayJournalService payJournalService;

	@Resource
	private PayRefundJournalService payRefundJournalService;

	@Resource
	private CacheTemplate cacheTemplate;

	@Resource
	private PayAlipayJournalDao payAlipayJournalDao;

	@Resource
	private PayAlipayRefundJournalDao payAlipayRefundJournalDao;

	@Resource
	private PayAlipayJournalQueryService payAlipayJournalQueryService;

	@Resource(name = "tradeStatusPollingInputChannel")
	private QueueChannel tradeStatusPollingInputChannel;

	@Resource(name = "asyncNotifyInputChannel")
	private QueueChannel asyncNotifyInputChannel;

	@Resource
	private DataSecurityService dataSecurityService;

	@Resource
	private IdService idService;

	/**
	 * Gets ali pay service.
	 * @param config the config
	 * @return the ali pay service
	 */
	public AliPayService getAliPayService(PayAlipayConfigPo config) {
		if (config == null) {
			throw new BusinessException("支付宝配置异常");
		}
		try {
			AliPayConfigStorage configStorage = new AliPayConfigStorage();
			byte[] alipayCert = dataSecurityService.decode(config.getAlipayCert());
			byte[] alipayRootCert = dataSecurityService.decode(config.getAlipayRootCert());
			byte[] appCert = dataSecurityService.decode(config.getAppCert());
			byte[] appKey = dataSecurityService.decode(config.getAppKey());
			Security.addProvider(new BouncyCastleProvider());
			CertEnvironment certEnvironment = new CustomCertEnvironment(appCert, alipayCert, alipayRootCert);
			configStorage.setCertEnvironment(certEnvironment);
			configStorage.setAppid(config.getAppid());
			configStorage.setPid(config.getPartnerId());
			configStorage.setSeller(config.getSeller());
			configStorage.setSignType(config.getSignType());
			configStorage.setCertSign(true);
			configStorage.setKeyPrivate(new String(appKey));
			configStorage.setInputCharset("utf-8");
			String notifyUrl = String.format("%s/pay/callback/v1/alipay/pay-callback/%s/config/%s",
					applicationProperties.getHost(), config.getPayMerchantId(), config.getId());
			configStorage.setNotifyUrl(notifyUrl);
			AliPayService service = new CustomAlipayServiceImpl(configStorage);
			return service;
		}
		catch (Exception e) {
			log.error("加载AliPayService失败, alipayConfigId=[{}],异常信息：{}", config.getId(), e.getMessage());
			throw e;
		}
	}

	/**
	 * Gets ali pay service.
	 * @param alipayConfigId the alipay config id
	 * @return the ali pay service
	 */
	public AliPayService getAliPayService(Long alipayConfigId) {
		PayAlipayConfigPo alipayConfig = payAlipayConfigQueryService.findByIdNotNull(alipayConfigId);
		AliPayService aliPayService = getAliPayService(alipayConfig);
		return aliPayService;

	}

	public CreateOrderDto pay(PayJournalPo payJournal, OrderCreateCommand command, PayMerchantPo payMerchant) {
		PayChannel channel = PayChannel.fromId(payJournal.getChannel());
		String orderNo = payJournal.getOrderNo();
		if (PayChannel.ALIPAY_WAP.equals(channel)) {
			String form = wapPay(payJournal, command);
			CreateOrderDto dto = new CreateOrderDto();
			dto.setOrderNo(orderNo);
			dto.setPage(form);
			return dto;
		}

		if (PayChannel.ALIPAY_SWEEP.equals(channel)) {
			String qrCodeText = sweepPay(payJournal, command);
			CreateOrderDto dto = new CreateOrderDto();
			dto.setUrl(qrCodeText);
			dto.setOrderNo(orderNo);
			return dto;
		}

		if (PayChannel.ALIPAY_MICRO.equals(channel)) {
			microPay(payJournal, command);
			CreateOrderDto dto = new CreateOrderDto();
			dto.setOrderNo(orderNo);
			return dto;
		}

		if (PayChannel.ALIPAY_MINIAPP.equals(channel)) {
			PayAlipayJournalPo alipayJournalDo = miniappPay(payJournal, command);
			CreateOrderDto dto = new CreateOrderDto();
			dto.setTradeNo(alipayJournalDo.getTradeNo());
			dto.setThirdPayNo(alipayJournalDo.getTradeNo());
			dto.setOrderNo(orderNo);
			return dto;
		}
		throw new ClientParamException("该支付模式暂不支持");
	}

	/**
	 * Wap pay string.
	 * @param payJournal the pay journal
	 * @param command the command
	 * @return the string
	 */
	public String wapPay(PayJournalPo payJournal, OrderCreateCommand command) {

		OrderCreateCommand.Alipay alipay = command.getAlipay();
		PayAlipayConfigPo alipayConfig = payAlipayConfigQueryService.findByIdNotNull(payJournal.getPayWayConfigId());
		AliPayService aliPayService = getAliPayService(alipayConfig);

		PayOrder order = new PayOrder(alipay.getSubject(), alipay.getBody(), command.getPayPrice(),
				command.getOrderNo());
		order.setTransactionType(AliTransactionType.WAP);
		if (StringUtils.hasText(alipayConfig.getProviderId())) {
			Map<String, String> extendParams = new HashMap<>();
			extendParams.put(SYS_SERVICE_PROVIDER_ID, alipayConfig.getProviderId());
			order.addAttr("extend_params", extendParams);
		}
		// 获取支付订单信息
		Map orderInfo = aliPayService.orderInfo(order);

		PayAlipayJournalPo alipayJournal = new PayAlipayJournalPo();
		alipayJournal.setId(idService.getId());
		alipayJournal.setStoreId("");
		alipayJournal.setTerminalId(command.getDeviceInfo());
		alipayJournal.setOperatorId("");
		alipayJournal.setMerchantOrderNo(payJournal.getMerchantBusinessNo());
		alipayJournal.setStatus(PaymentStatus.WAIT_PAY.getId());
		alipayJournal.setBody(alipay.getBody());
		alipayJournal.setAppid(alipayConfig.getAppid());
		alipayJournal.setPartnerId(alipayConfig.getPartnerId());
		alipayJournal.setAlipayConfigId(payJournal.getPayWayConfigId());
		if (StringUtils.hasText(alipayConfig.getProviderId())) {
			alipayJournal.setProviderId(alipayConfig.getProviderId());
		}
		alipayJournal.setTradeType(AliTransactionType.WAP.getType());
		alipayJournal.setPayMerchantId(payJournal.getPayMerchantId());
		alipayJournal.setPayAppId(payJournal.getPayAppId());
		alipayJournal.setPayStoreId(payJournal.getPayStoreId());
		alipayJournal.setPayJournalId(payJournal.getId());
		alipayJournal.setOrganizationId(command.getOrganizationId());
		alipayJournal.setDepartId(command.getDepartId());
		alipayJournal.setOutTradeNo(command.getOrderNo());
		alipayJournal.setPayCurrency("CNY");
		alipayJournal.setSubject(alipay.getSubject());
		alipayJournal.setTotalAmount(command.getPayPrice());
		payAlipayJournalDao.save(alipayJournal);
		createSyncTask(payJournal, alipayJournal);
		return aliPayService.buildRequest(orderInfo, MethodType.POST);
	}

	/**
	 * Miniapp pay pay alipay journal po.
	 * @param payJournal the pay journal
	 * @param command the command
	 * @return the pay alipay journal po
	 */
	public PayAlipayJournalPo miniappPay(PayJournalPo payJournal, OrderCreateCommand command) {
		OrderCreateCommand.Alipay alipay = command.getAlipay();
		PayAlipayConfigPo alipayConfig = payAlipayConfigQueryService.findByIdNotNull(payJournal.getPayWayConfigId());
		AliPayService aliPayService = getAliPayService(alipayConfig);

		PayOrder order = new PayOrder(alipay.getSubject(), alipay.getBody(), command.getPayPrice(),
				command.getOrderNo());
		order.setOpenid(alipay.getOpenid());
		order.setTransactionType(AliTransactionType.MINAPP);

		if (StringUtils.hasText(alipayConfig.getProviderId())) {
			Map<String, String> extendParams = new HashMap<>();
			extendParams.put(SYS_SERVICE_PROVIDER_ID, alipayConfig.getProviderId());
			order.addAttr("extend_params", extendParams);
		}
		// 获取支付订单信息
		Map orderInfo = aliPayService.orderInfo(order);
		// 预订单
		JSONObject result = aliPayService.getHttpRequestTemplate()
			.postForObject(aliPayService.getReqUrl() + "?" + UriVariables.getMapToParameters(orderInfo), null,
					JSONObject.class);
		JSONObject response = result.getJSONObject("alipay_trade_create_response");
		if (!SUCCESS_CODE.equals(response.getStr(CODE))) {
			log.error("支付宝创建预订单失败，单号：{}，支付宝返回：{}", command.getOrderNo(), JSONUtil.toJsonStr(response));
			throw new BusinessException("支付单创建失败，异常信息：{0}", response.getStr("sub_msg"));
		}
		String tradeNo = response.getStr("trade_no");
		PayAlipayJournalPo alipayJournal = new PayAlipayJournalPo();
		alipayJournal.setId(idService.getId());
		alipayJournal.setStoreId("");
		alipayJournal.setTerminalId(command.getDeviceInfo());
		alipayJournal.setOperatorId("");
		alipayJournal.setMerchantOrderNo(payJournal.getMerchantBusinessNo());
		alipayJournal.setTradeNo(tradeNo);
		alipayJournal.setStatus(PaymentStatus.WAIT_PAY.getId());
		alipayJournal.setBody(alipay.getBody());
		alipayJournal.setAppid(alipayConfig.getAppid());
		alipayJournal.setPartnerId(alipayConfig.getPartnerId());
		alipayJournal.setAlipayConfigId(payJournal.getPayWayConfigId());
		if (StringUtils.hasText(alipayConfig.getProviderId())) {
			alipayJournal.setProviderId(alipayConfig.getProviderId());
		}
		alipayJournal.setTradeType(AliTransactionType.MINAPP.getType());
		alipayJournal.setPayMerchantId(payJournal.getPayMerchantId());
		alipayJournal.setPayAppId(payJournal.getPayAppId());
		alipayJournal.setPayStoreId(payJournal.getPayStoreId());
		alipayJournal.setPayJournalId(payJournal.getId());
		alipayJournal.setOrganizationId(command.getOrganizationId());
		alipayJournal.setDepartId(command.getDepartId());
		alipayJournal.setOutTradeNo(command.getOrderNo());
		alipayJournal.setPayCurrency("CNY");
		alipayJournal.setSubject(alipay.getSubject());
		alipayJournal.setTotalAmount(command.getPayPrice());
		alipayJournal.setBuyerUserId(alipay.getOpenid());
		payAlipayJournalDao.save(alipayJournal);
		createSyncTask(payJournal, alipayJournal);
		return alipayJournal;
	}

	/**
	 * Sweep pay string.
	 * @param payJournal the pay journal
	 * @param command the command
	 * @return the string
	 */
	public String sweepPay(PayJournalPo payJournal, OrderCreateCommand command) {
		OrderCreateCommand.Alipay alipay = command.getAlipay();
		PayAlipayConfigPo alipayConfig = payAlipayConfigQueryService.findByIdNotNull(payJournal.getPayWayConfigId());
		AliPayService aliPayService = getAliPayService(alipayConfig);
		PayAlipayJournalPo old = payAlipayJournalDao.findFirstByPayJournalIdOrderByIdDesc(payJournal.getId());
		if (old != null) {
			if (Objects.equals(old.getStatus(), PaymentStatus.WAIT_PAY.getId())) {
				return old.getPayUrl();
			}
			else if (Objects.equals(old.getStatus(), PaymentStatus.CANCEL.getId())) {
			}
			else {
				throw new ClientParamException("订单处于不可支付状态");
			}
		}
		PayOrder order = new PayOrder(alipay.getSubject(), alipay.getBody(), command.getPayPrice(),
				command.getOrderNo());
		order.setTransactionType(AliTransactionType.SWEEPPAY);
		if (StringUtils.hasText(alipayConfig.getProviderId())) {
			Map<String, String> extendParams = new HashMap<>();
			extendParams.put("sys_service_provider_id", alipayConfig.getProviderId());
			order.addAttr("extend_params", extendParams);
		}

		PayAlipayJournalPo alipayJournal = new PayAlipayJournalPo();
		alipayJournal.setId(idService.getId());
		alipayJournal.setStoreId("");
		alipayJournal.setTerminalId(command.getDeviceInfo());
		alipayJournal.setOperatorId("");
		alipayJournal.setMerchantOrderNo(payJournal.getMerchantBusinessNo());
		alipayJournal.setStatus(PaymentStatus.WAIT_PAY.getId());
		alipayJournal.setBody(alipay.getBody());
		alipayJournal.setPayMerchantId(payJournal.getPayMerchantId());
		alipayJournal.setPayAppId(payJournal.getPayAppId());
		alipayJournal.setPayStoreId(payJournal.getPayStoreId());
		alipayJournal.setPayJournalId(payJournal.getId());
		alipayJournal.setOrganizationId(command.getOrganizationId());
		alipayJournal.setDepartId(command.getDepartId());
		alipayJournal.setOutTradeNo(command.getOrderNo());
		alipayJournal.setPayCurrency("CNY");
		alipayJournal.setSubject(alipay.getSubject());
		alipayJournal.setTotalAmount(command.getPayPrice());
		alipayJournal.setTradeType("SWEEP");
		alipayJournal.setPartnerId(alipayConfig.getPartnerId());
		alipayJournal.setAlipayConfigId(payJournal.getPayWayConfigId());
		if (StringUtils.hasText(alipayConfig.getProviderId())) {
			alipayJournal.setProviderId(alipayConfig.getProviderId());
		}
		alipayJournal.setAppid(alipayConfig.getAppid());
		alipayJournal = payAlipayJournalDao.save(alipayJournal);
		String url = aliPayService.getQrPay(order);
		alipayJournal.setPayUrl(url);
		alipayJournal = payAlipayJournalDao.save(alipayJournal);

		createSyncTask(payJournal, alipayJournal);
		return url;
	}

	/**
	 * Cancel order boolean.
	 * @param alipayJournal the alipay journal
	 * @return the boolean
	 */
	public boolean cancelOrder(PayAlipayJournalPo alipayJournal) {
		AliPayService aliPayService = getAliPayService(alipayJournal.getAlipayConfigId());
		if (aliPayService == null) {
			log.error("撤销交易时获取AliPayService失败");
		}
		JSONObject result = JSONUtil
			.parseObj(aliPayService.cancel(alipayJournal.getTradeNo(), alipayJournal.getOutTradeNo()));
		JSONObject response = result.getJSONObject("alipay_trade_cancel_response");
		if (ReturnCode.SUCCESS.equals(response.getStr(CODE))) {
			payAlipayJournalDao.updateStatus(alipayJournal.getId(), PaymentStatus.CANCEL.getId(), null);
			return true;
		}
		log.error("撤销支付宝支付流水失败，单号：{}，撤销结果：{}", alipayJournal.getOutTradeNo(), JSONUtil.toJsonStr(result));
		throw new BusinessException("撤销支付失败");
	}

	public boolean cancelOrder(PayJournalPo payJournal) {
		PayAlipayJournalPo alipayJournal = payAlipayJournalDao.findFirstByPayJournalIdOrderByIdDesc(payJournal.getId());
		cancelOrder(alipayJournal);
		return true;
	}

	/**
	 * Micro pay pay alipay journal po.
	 * @param payJournal the pay journal
	 * @param command the command
	 * @return the pay alipay journal po
	 */
	public PayAlipayJournalPo microPay(PayJournalPo payJournal, OrderCreateCommand command) {
		if (!StringUtils.hasText(command.getAuthCode())) {
			throw new BusinessException("授权码不能为空");
		}
		OrderCreateCommand.Alipay alipay = command.getAlipay();
		PayAlipayConfigPo alipayConfig = payAlipayConfigQueryService.findByIdNotNull(payJournal.getPayWayConfigId());
		AliPayService aliPayService = getAliPayService(alipayConfig);
		PayAlipayJournalPo old = payAlipayJournalDao.findFirstByPayJournalIdOrderByIdDesc(payJournal.getId());
		if (old != null) {
			if (PaymentStatus.WAIT_PAY.getId().equals(old.getStatus())) {
				return old;
				// throw new BusinessException("用户支付中，等待用户输入密码或银行扣款",
				// BusinessError.PROCESSING);
			}
			else if (PaymentStatus.PAY.getId().equals(old.getStatus())) {
				return old;
			}
			else {
				throw new BusinessException("支付失败");
			}
		}
		PayOrder order = new PayOrder(alipay.getSubject(), alipay.getBody(), command.getPayPrice(),
				command.getOrderNo());
		order.setTransactionType(AliTransactionType.BAR_CODE);
		order.setAuthCode(command.getAuthCode());
		if (StringUtils.hasText(alipayConfig.getProviderId())) {
			Map<String, String> extendParams = new HashMap<>();
			extendParams.put(SYS_SERVICE_PROVIDER_ID, alipayConfig.getProviderId());
			order.addAttr("extend_params", extendParams);
		}

		PayAlipayJournalPo alipayJournal = new PayAlipayJournalPo();
		alipayJournal.setId(idService.getId());
		alipayJournal.setStoreId("");
		alipayJournal.setTerminalId(command.getDeviceInfo());
		alipayJournal.setOperatorId("");
		alipayJournal.setMerchantOrderNo(payJournal.getMerchantBusinessNo());
		alipayJournal.setStatus(PaymentStatus.WAIT_PAY.getId());
		alipayJournal.setBody(alipay.getBody());
		alipayJournal.setPayMerchantId(payJournal.getPayMerchantId());
		alipayJournal.setPayAppId(payJournal.getPayAppId());
		alipayJournal.setPayStoreId(payJournal.getPayStoreId());
		alipayJournal.setPayJournalId(payJournal.getId());
		alipayJournal.setOrganizationId(command.getOrganizationId());
		alipayJournal.setDepartId(command.getDepartId());
		alipayJournal.setOutTradeNo(command.getOrderNo());
		alipayJournal.setPayCurrency("CNY");
		alipayJournal.setSubject(alipay.getSubject());
		alipayJournal.setTradeType("MICRO");
		alipayJournal.setTotalAmount(command.getPayPrice());
		alipayJournal.setPartnerId(alipayConfig.getPartnerId());
		alipayJournal.setAlipayConfigId(payJournal.getPayWayConfigId());
		if (StringUtils.hasText(alipayConfig.getProviderId())) {
			alipayJournal.setProviderId(alipayConfig.getProviderId());
		}
		alipayJournal.setAppid(alipayConfig.getAppid());
		payAlipayJournalDao.save(alipayJournal);
		log.info("支付宝付款码支付，参数：{},", JSONUtil.toJsonStr(order));
		Map<String, Object> params = aliPayService.microPay(order);
		log.info("支付宝付款码支付，结果：{},", JSONUtil.toJsonStr(params));
		if (!aliPayService.verify(params)) {
			log.error("付款码支付响应验签失败，响应参数：{}", JSONUtil.toJsonStr(params));
			alipayJournal.setErrMsg("请求异常，响应验签失败");
			payAlipayJournalDao.save(alipayJournal);
			throw new BusinessException("请求异常，响应验签失败");
		}
		JSONObject result = JSONUtil.parseObj(params);
		JSONObject response = result.getJSONObject("alipay_trade_pay_response");
		AlipayTradePayResponse payResponse = JSONUtil.toBean(response, AlipayTradePayResponse.class);
		alipayJournal.setTradeNo(payResponse.getTradeNo());
		alipayJournal.setReceiptAmount(payResponse.getReceiptAmount());
		alipayJournal.setInvoiceAmount(payResponse.getInvoiceAmount());
		alipayJournal.setBuyerPayAmount(payResponse.getBuyerPayAmount());
		alipayJournal.setBuyerUserId(payResponse.getBuyerUserId());
		alipayJournal.setBuyerLogonId(payResponse.getBuyerLogonId());
		alipayJournal.setTotalAmount(payResponse.getTotalAmount());
		if (ReturnCode.SUCCESS.equals(payResponse.getCode())) {
			Date date = new Date();
			alipayJournal.setErrMsg("");
			alipayJournal.setSuccessTime(date);
			alipayJournal.setStatus(PaymentStatus.PAY.getId());
			payAlipayJournalDao.save(alipayJournal);
			payJournalService.updateSuccessPayStatus(payJournal.getId(), PayStatus.PAY.getId(),
					alipayJournal.getTradeNo(), date);
			return alipayJournal;
		}
		if (ReturnCode.PROCESSING.equals(payResponse.getCode())
				|| ReturnCode.UNKNOWN_ERROR.equals(payResponse.getCode())) {
			alipayJournal.setErrMsg(payResponse.getMsg());
			payAlipayJournalDao.save(alipayJournal);

			createSyncTask(payJournal, alipayJournal);
			// throw new BusinessException("用户支付中，等待用户输入密码或银行扣款",
			// BusinessError.PROCESSING);
			return alipayJournal;
		}
		else if (ReturnCode.FAIL.equals(payResponse.getCode())) {
			alipayJournal.setErrMsg(payResponse.getMsg());
			alipayJournal.setStatus(PaymentStatus.CANCEL.getId());
			payAlipayJournalDao.save(alipayJournal);
			payJournalService.updatePayStatus(payJournal.getId(), PayStatus.OTHER_ERROR.getId());
			log.error("支付宝付款码支付失败，单号：{}，支付宝返回：{}", command.getOrderNo(), JSONUtil.toJsonStr(result));
			throw new BusinessException("支付失败，请重试");
		}
		alipayJournal.setErrMsg(payResponse.getMsg());
		alipayJournal.setStatus(PaymentStatus.CANCEL.getId());
		payAlipayJournalDao.save(alipayJournal);
		log.error("支付宝付款码支付失败，单号：{}，支付宝返回：{}", command.getOrderNo(), JSONUtil.toJsonStr(result));
		throw new BusinessException("付款失败");
	}

	private boolean checkAndUpdatePayOrderStatus(PayAlipayJournalPo alipayJournal, AlipayTradePayResponse payResponse,
			boolean throwException, boolean writeBack) {
		Optional<PayAlipayJournalPo> journalDoOptional = payAlipayJournalDao.findById(alipayJournal.getId());
		alipayJournal = journalDoOptional.get();
		Long payJournalId = alipayJournal.getPayJournalId();
		alipayJournal.setTradeStatus(payResponse.getTradeStatus());

		if (TradeStatus.TRADE_SUCCESS.equals(payResponse.getTradeStatus())
				|| TradeStatus.TRADE_FINISHED.equals(payResponse.getTradeStatus())) {
			alipayJournal.setTradeNo(payResponse.getTradeNo());
			alipayJournal.setReceiptAmount(payResponse.getReceiptAmount());
			alipayJournal.setInvoiceAmount(payResponse.getInvoiceAmount());
			alipayJournal.setBuyerPayAmount(payResponse.getBuyerPayAmount());
			alipayJournal.setBuyerUserId(payResponse.getBuyerUserId());
			alipayJournal.setBuyerLogonId(payResponse.getBuyerLogonId());
			alipayJournal.setTotalAmount(payResponse.getTotalAmount());
			alipayJournal.setErrMsg("");
			Date date = new Date();
			alipayJournal.setSuccessTime(date);
			alipayJournal.setStatus(PaymentStatus.PAY.getId());
			payAlipayJournalDao.saveAndFlush(alipayJournal);
			payJournalService.updateSuccessPayStatus(payJournalId, PayStatus.PAY.getId(), alipayJournal.getTradeNo(),
					date);
			createPaySuccessNotifyTask(payJournalQueryService.findByIdNotNull(payJournalId), alipayJournal);
			return true;
		}
		else if (TradeStatus.TRADE_CLOSED.equals(payResponse.getTradeStatus())) {
			alipayJournal.setStatus(PaymentStatus.CANCEL.getId());
			alipayJournal.setErrMsg("未付款交易超时关闭，或支付完成后全额退款");
			payAlipayJournalDao.saveAndFlush(alipayJournal);
			if (writeBack) {
				payJournalService.updatePayStatus(payJournalId, PayStatus.OTHER_ERROR.getId());
			}
			if (throwException) {
				throw new BusinessException("支付失败,该笔交易已关闭", BusinessError.CLOSED);
			}
		}
		else if (TradeStatus.WAIT_BUYER_PAY.equals(payResponse.getTradeStatus())) {
			if (throwException) {
				throw new BusinessException("用户支付中，等待用户输入密码或银行扣款", BusinessError.PROCESSING);
			}
		}
		else if (payResponse.getTradeStatus() == null && ReturnCode.FAIL.equals(payResponse.getCode())
				&& "ACQ.TRADE_NOT_EXIST".equals(payResponse.getSubCode())) {
			if (throwException) {
				throw new BusinessException("用户支付中", BusinessError.PROCESSING);
			}
		}
		else {
			alipayJournal.setStatus(PaymentStatus.CANCEL.getId());
			alipayJournal.setErrMsg("付款失败，未知异常");
			payAlipayJournalDao.save(alipayJournal);
			if (writeBack) {
				payJournalService.updatePayStatus(payJournalId, PayStatus.OTHER_ERROR.getId());
			}
			if (throwException) {
				throw new BusinessException("支付失败");
			}
		}
		return false;
	}

	/**
	 * Query order alipay trade pay response.
	 * @param orderNo the order no
	 * @param payService the pay service
	 * @return the alipay trade pay response
	 */
	public AlipayTradePayResponse queryOrder(String orderNo, AliPayService payService) {
		JSONObject result = JSONUtil.parseObj(payService.query(null, orderNo));
		String jsonString = result.get("alipay_trade_query_response").toString();
		AlipayTradePayResponse payResponse = JSONUtil.toBean(jsonString, AlipayTradePayResponse.class);
		return payResponse;
	}

	@Override
	public void refund(PayJournalPo payJournal, PayRefundJournalPo refundJournal,
			RefundOrderCreateCommand refundOrderCommand, PayMerchantPo payMerchantPo) {
		PayAlipayJournalPo old = payAlipayJournalDao.findFirstByPayJournalIdOrderByIdDesc(payJournal.getId());
		if (old == null) {
			throw new ResourceNotFoundException("不存在该订单");
		}
		RefundOrder refundOrder = new RefundOrder();
		refundOrder.setOutTradeNo(payJournal.getOrderNo());
		refundOrder.setRefundAmount(refundOrderCommand.getRefundPrice());
		refundOrder.setRefundNo(refundOrderCommand.getRefundOrderNo());

		try {
			AliPayService aliPayService = getAliPayService(payJournal.getPayWayConfigId());
			AliRefundResult result = aliPayService.refund(refundOrder);
			PayAlipayRefundJournalPo alipayRefundJournal = new PayAlipayRefundJournalPo();
			alipayRefundJournal.setId(idService.getId());
			alipayRefundJournal.setTerminalId(refundJournal.getDeviceInfo());
			alipayRefundJournal.setOperatorId("");
			alipayRefundJournal.setMerchantOrderNo(payJournal.getMerchantBusinessNo());
			alipayRefundJournal.setAppid(old.getAppid());
			alipayRefundJournal.setPartnerId(old.getPartnerId());
			alipayRefundJournal.setAlipayConfigId(old.getAlipayConfigId());
			// 该通知地址可考虑用于接收银行卡退款接收到账通知
			alipayRefundJournal.setNotifyUrl("");
			alipayRefundJournal.setSubMerchantId("");
			alipayRefundJournal.setOutRefundNo(refundOrderCommand.getRefundOrderNo());
			// 发现字段冗余了
			alipayRefundJournal.setOutRequestNo(refundOrderCommand.getRefundOrderNo());
			alipayRefundJournal.setOutTradeNo(refundJournal.getOrderNo());
			alipayRefundJournal.setRefundAmount(refundOrder.getRefundAmount());
			if ("Y".equals(result.getFundChange())) {
				alipayRefundJournal.setErrMsg("");
			}
			else {
				alipayRefundJournal.setErrMsg(result.getResultMsg() + "#" + result.getMsg() + "#" + result.getSubMsg());
			}
			alipayRefundJournal.setPayMerchantId(refundJournal.getPayMerchantId());
			alipayRefundJournal.setPayAppId(refundJournal.getPayAppId());
			alipayRefundJournal.setPayStoreId(refundJournal.getPayStoreId());
			alipayRefundJournal.setPayJournalId(refundJournal.getPayJournalId());
			alipayRefundJournal.setOrganizationId(refundJournal.getOrganizationId());
			alipayRefundJournal.setDepartId(refundJournal.getDepartId());
			alipayRefundJournal.setTradeNo(result.getTradeNo());
			alipayRefundJournal.setOutRequestNo(result.getOutRequestNo());
			alipayRefundJournal.setRefundJournalId(refundJournal.getId());
			alipayRefundJournal.setRefundReason(refundOrderCommand.getRefundReason());
			alipayRefundJournal.setStatus(result.getFundChange());
			alipayRefundJournal.setTotalAmount(old.getTotalAmount());
			alipayRefundJournal.setErrMsg(result.getSubMsg());
			payAlipayRefundJournalDao.save(alipayRefundJournal);
			if ("Y".equals(result.getFundChange())) {
				alipayRefundJournal.setSuccessTime(result.getGmtRefundPay());
				payAlipayRefundJournalDao.save(alipayRefundJournal);

				payJournalService.completeRefund(payJournal.getId(), refundJournal.getRefundPrice(),
						payJournal.getRefundFreezePrice(), payJournal.getRefundPrice());
				payRefundJournalService.updateSuccessRefundStatus(refundJournal.getId(),
						alipayRefundJournal.getOutRefundNo(), result.getGmtRefundPay());
				RefundSuccessNotifyMessage message = new RefundSuccessNotifyMessage(refundJournal.getId());
				asyncNotifyInputChannel.send(new GenericMessage<>(message));
			}
			else {
				log.error("退款失败，退款单号：{}，支付宝返回：{}", refundOrderCommand.getRefundOrderNo(), JSONUtil.toJsonStr(result));
				throw new BusinessException("退款失败:" + result.getSubMsg());
			}

		}
		catch (Exception e) {
			payJournalService.submitRefund(payJournal.getId(),
					refundJournal.getRefundPrice().multiply(new BigDecimal(-1)), payJournal.getRefundFreezePrice(),
					payJournal.getRefundPrice());
			payRefundJournalService.updateFailRefundStatus(refundJournal.getId(), e.getMessage());
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * Complete pay pay out message.
	 * @param merchantId the merchant id
	 * @param configId the config id
	 * @param noticeRequest the notice request
	 * @return the pay out message
	 */
	public PayOutMessage completePay(Long merchantId, Long configId, NoticeRequest noticeRequest) {
		PayAlipayConfigPo payAlipayConfig = payAlipayConfigQueryService.findByIdNotNull(configId);
		if (!Objects.equals(merchantId, payAlipayConfig.getPayMerchantId())) {
			return PayOutMessage.TEXT().content("alipay config not exists").build();
		}
		AliPayService service = getAliPayService(configId);
		NoticeParams noticeParams = service.getNoticeParams(noticeRequest);

		if (!service.verify(noticeParams)) {
			return PayOutMessage.TEXT().content("sign verify fail").build();
		}
		Map<String, Object> params = noticeParams.getBody();
		String orderNo = params.getOrDefault("out_trade_no", "").toString();
		String key = String.format(PayLockConstant.PAY_ORDER_PREFIX, merchantId, orderNo);
		Lock lock = cacheTemplate.getLock(key);
		lock.lock();
		try {
			PayJournalPo payJournal = payJournalQueryService.findMerchantJournal(merchantId, orderNo);
			if (!Objects.equals(payJournal.getPayStatus(), PayStatus.WAIT_PAY.getId())) {
				return PayOutMessage.TEXT().content("success").build();
			}
			PayAlipayJournalPo alipayJournal = payAlipayJournalDao
				.findFirstByPayJournalIdOrderByIdDesc(payJournal.getId());
			String tradeStatus = params.getOrDefault("trade_status", "") + "";
			if (TradeStatus.TRADE_SUCCESS.equals(tradeStatus) || TradeStatus.TRADE_FINISHED.equals(tradeStatus)) {
				Object gmtPayment = params.get("gmt_payment");
				String gmtPaymentTime = gmtPayment.toString();
				DateTime dateTime = DateUtil.parse(gmtPaymentTime, DatePattern.NORM_DATETIME_PATTERN);
				Date successTime = dateTime.toJdkDate();
				alipayJournal.setSuccessTime(successTime);
				alipayJournal.setStatus(PaymentStatus.PAY.getId());
				alipayJournal.setTradeStatus(tradeStatus);
				alipayJournal.setTradeNo(params.get("trade_no").toString());
				alipayJournal.setBuyerUserId(params.getOrDefault("buyer_id", "").toString());
				payAlipayJournalDao.save(alipayJournal);
				payJournalService.updateSuccessPayStatus(payJournal.getId(), PayStatus.PAY.getId(),
						alipayJournal.getTradeNo(), successTime);
				createPaySuccessNotifyTask(payJournal, alipayJournal);

				return PayOutMessage.TEXT().content("success").build();
			}
			else if (TradeStatus.TRADE_CLOSED.equals(tradeStatus)) {
				alipayJournal.setStatus(PaymentStatus.CANCEL.getId());
				alipayJournal.setTradeStatus(tradeStatus);
				alipayJournal.setTradeNo(params.getOrDefault("trade_no", "").toString());
				alipayJournal.setBuyerUserId(params.getOrDefault("buyer_id", "").toString());
				alipayJournal.setErrMsg("交易关闭");
				payAlipayJournalDao.save(alipayJournal);
				payJournalService.updatePayStatus(payJournal.getId(), PayStatus.CANCEL.getId());
				return PayOutMessage.TEXT().content("success").build();
			}

		}
		finally {
			lock.unlock();
		}
		return PayOutMessage.TEXT().content("fail").build();
	}

	private void createSyncTask(PayJournalPo payJournal, PayAlipayJournalPo alipayJournal) {
		TradeStatusPollingMessage pollingMessage = new TradeStatusPollingMessage(payJournal.getId(),
				alipayJournal.getId());
		Map<String, Object> headers = new HashMap<>();
		headers.put("delay", DateUtil.offsetSecond(new Date(), 5));
		tradeStatusPollingInputChannel.send(new GenericMessage<>(pollingMessage, headers));
	}

	private void createPaySuccessNotifyTask(PayJournalPo payJournal, PayAlipayJournalPo alipayJournal) {
		PaySuccessNotifyMessage message = new PaySuccessNotifyMessage(payJournal.getId());
		asyncNotifyInputChannel.send(new GenericMessage<>(message));
	}

	private boolean syncPayOrderStatus(PayAlipayJournalPo alipayJournal) {
		AliPayService aliPayService = getAliPayService(alipayJournal.getAlipayConfigId());
		if (aliPayService == null) {
			return false;
		}
		String key = String.format(PayLockConstant.PAY_ORDER_PREFIX, alipayJournal.getPayMerchantId(),
				alipayJournal.getOutTradeNo());
		Lock lock = cacheTemplate.getLock(key);
		lock.lock();
		try {
			AlipayTradePayResponse result = queryOrder(alipayJournal.getOutTradeNo(), aliPayService);
			return checkAndUpdatePayOrderStatus(alipayJournal, result, false, true);
		}
		catch (Exception e) {
			log.error("同步支付宝支付流水状态失败，单号，{}，错误：{}", alipayJournal.getOutTradeNo(), e.getMessage());
			return false;
		}
		finally {
			lock.unlock();
		}
	}

	@Override
	public boolean syncPayOrderStatus(PayJournalPo payJournal) {
		PayAlipayJournalPo payAlipayJournal = payAlipayJournalDao
			.findFirstByPayJournalIdOrderByIdDesc(payJournal.getId());
		if (payAlipayJournal == null) {
			return false;
		}
		else {
			return syncPayOrderStatus(payAlipayJournal);
		}
	}

	/**
	 * Async pay order status.
	 * @param payJournal the pay journal
	 */
	@Async
	public void asyncPayOrderStatus(PayJournalPo payJournal) {
		syncPayOrderStatus(payJournal);
	}

	@Override
	public PayWay getPayWay() {
		return PayWay.ALIPAY;
	}

	/**
	 * The type Trade status.
	 */
	public static class TradeStatus {

		/**
		 * 交易创建，等待买家付款
		 */
		public static final String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";

		/**
		 * 未付款交易超时关闭，或支付完成后全额退款
		 */
		public static final String TRADE_CLOSED = "TRADE_CLOSED";

		/**
		 * 交易支付成功
		 */
		public static final String TRADE_SUCCESS = "TRADE_SUCCESS";

		/**
		 * 交易结束，不可退款
		 */
		public static final String TRADE_FINISHED = "TRADE_FINISHED";

	}

	/**
	 * The type Return code.
	 */
	public static class ReturnCode {

		/**
		 * 支付成功
		 */
		public static final String SUCCESS = "10000";

		/**
		 * 支付失败
		 */
		public static final String FAIL = "40004";

		/**
		 * 等待用户付款
		 */
		public static final String PROCESSING = "10003";

		/**
		 * 未知异常
		 */
		public static final String UNKNOWN_ERROR = "20000";

	}

}
