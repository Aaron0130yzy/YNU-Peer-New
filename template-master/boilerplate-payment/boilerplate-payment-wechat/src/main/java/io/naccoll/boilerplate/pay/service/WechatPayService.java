package io.naccoll.boilerplate.pay.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Lock;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayMicropayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayOrderCloseRequest;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayOrderReverseRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayMicropayResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.github.binarywang.wxpay.util.SignUtils;
import io.naccoll.boilerplate.core.cache.CacheTemplate;
import io.naccoll.boilerplate.core.exception.BusinessError;
import io.naccoll.boilerplate.core.exception.BusinessException;
import io.naccoll.boilerplate.core.exception.ClientParamException;
import io.naccoll.boilerplate.core.exception.ResourceNotFoundException;
import io.naccoll.boilerplate.core.id.IdService;
import io.naccoll.boilerplate.core.properties.ApplicationProperties;
import io.naccoll.boilerplate.core.security.service.DataSecurityService;
import io.naccoll.boilerplate.pay.constant.PayLockConstant;
import io.naccoll.boilerplate.pay.dao.PayWechatJournalDao;
import io.naccoll.boilerplate.pay.dao.PayWechatRefundJournalDao;
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
import io.naccoll.boilerplate.pay.enums.RefundStatus;
import io.naccoll.boilerplate.pay.enums.wx.WechatConfigType;
import io.naccoll.boilerplate.pay.model.PayJournalPo;
import io.naccoll.boilerplate.pay.model.PayMerchantPo;
import io.naccoll.boilerplate.pay.model.PayRefundJournalPo;
import io.naccoll.boilerplate.pay.model.PayWechatConfigPo;
import io.naccoll.boilerplate.pay.model.PayWechatJournalPo;
import io.naccoll.boilerplate.pay.model.PayWechatRefundJournalPo;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * The type Wechat pay service.
 */
@Slf4j
@Service
@EnableConfigurationProperties(ApplicationProperties.class)
public class WechatPayService implements ThirdPaySubSerivce {

	@Resource
	private CacheTemplate cacheTemplate;

	@Resource
	private PayJournalQueryService payJournalQueryService;

	@Resource
	private PayJournalService payJournalService;

	private PayWechatJournalQueryService payWechatJournalQueryService;

	@Resource
	private PayWechatConfigQueryService payWechatConfigQueryService;

	@Resource
	private PayWechatJournalDao payWechatJournalDao;

	@Resource
	private PayWechatRefundJournalDao payWechatRefundJournalDao;

	@Resource(name = "asyncNotifyInputChannel")
	private QueueChannel asyncNotifyInputChannel;

	@Resource(name = "tradeStatusPollingInputChannel")
	private QueueChannel tradeStatusPollingInputChannel;

	@Resource
	private ApplicationProperties applicationProperties;

	@Resource
	private PayRefundJournalService payRefundJournalService;

	@Resource
	private PayRefundJournalQueryService payRefundJournalQueryService;

	@Resource
	private DataSecurityService dataSecurityService;

	@Resource
	private IdService idService;

	/**
	 * Gets wx pay service.
	 * @param wechatConfigId the wechat config id
	 * @return the wx pay service
	 */
	public WxPayService getWxPayService(Long wechatConfigId) {
		PayWechatConfigPo payWechatConfig = payWechatConfigQueryService.findByIdNotNull(wechatConfigId);
		return getWxPayService(payWechatConfig);
	}

	/**
	 * Gets wx pay service.
	 * @param payWechatConfig the pay wechat config
	 * @return the wx pay service
	 */
	public WxPayService getWxPayService(PayWechatConfigPo payWechatConfig) {
		try {
			WxPayConfig wxPayConfig = new WxPayConfig();
			wxPayConfig.setAppId(payWechatConfig.getAppid());
			wxPayConfig.setMchId(payWechatConfig.getMchid());
			wxPayConfig.setMchKey(dataSecurityService.decryptStr(payWechatConfig.getMchKey()));
			wxPayConfig.setKeyContent(dataSecurityService.decode(payWechatConfig.getKeyContent()));
			if (StringUtils.hasText(payWechatConfig.getSubMchid())) {
				wxPayConfig.setSubMchId(payWechatConfig.getSubMchid());
			}
			if (StringUtils.hasText(payWechatConfig.getSubAppid())) {
				wxPayConfig.setSubAppId(payWechatConfig.getSubAppid());
			}
			WxPayService wxPayService = new WxPayServiceImpl();
			wxPayService.addConfig(payWechatConfig.getMchid(), wxPayConfig);
			return wxPayService;
		}
		catch (Throwable e) {
			log.error("加载WxPayService失败, wechatConfigId=[{}], 异常信息：{}", payWechatConfig.getId(), e.getMessage());
			throw new BusinessException("加载WxPayService失败, wechatConfigId=[{0}]", payWechatConfig.getId());
		}
	}

	@SneakyThrows
	public boolean cancelOrder(PayJournalPo payJournal) {
		PayWechatJournalPo old = payWechatJournalQueryService.findLastByPayJournalId(payJournal.getId());
		if (old == null) {
			throw new ResourceNotFoundException("不存在该订单");
		}
		return cancelOrder(old);
	}

	/**
	 * Cancel order boolean.
	 * @param wechatJournal the wechat journal
	 * @return the boolean
	 */
	public boolean cancelOrder(PayWechatJournalPo wechatJournal) {
		if (Objects.equals(wechatJournal.getTradeType(), WxPayConstants.TradeType.MICROPAY)) {
			reverseOrder(wechatJournal);
			return true;
		}
		PayWechatConfigPo config = payWechatConfigQueryService.findByIdNotNull(wechatJournal.getWechatConfigId());
		WxPayService wxPayService = getWxPayService(config);
		WxPayOrderCloseRequest request = new WxPayOrderCloseRequest();
		request.setOutTradeNo(wechatJournal.getOutTradeNo());
		request.setAppid(wechatJournal.getAppid());
		request.setMchId(wechatJournal.getMchid());
		if (StringUtils.hasText(wechatJournal.getSubMchid())) {
			request.setSubMchId(wechatJournal.getSubMchid());
		}
		if (StringUtils.hasText(wechatJournal.getSubAppid())) {
			request.setSubAppId(wechatJournal.getSubAppid());
		}
		try {
			wxPayService.closeOrder(request);
		}
		catch (WxPayException e) {
			throw new BusinessException("关闭订单失败，异常信息：{0}", e.getErrCodeDes());
		}
		wechatJournal.setStatus(PaymentStatus.CANCEL.getId());
		payWechatJournalDao.save(wechatJournal);
		return true;
	}

	private void reverseOrder(PayWechatJournalPo wechatJournal) {
		PayWechatConfigPo config = payWechatConfigQueryService.findByIdNotNull(wechatJournal.getWechatConfigId());
		WxPayService wxPayService = getWxPayService(config);
		WxPayOrderReverseRequest request = new WxPayOrderReverseRequest();
		request.setOutTradeNo(wechatJournal.getOutTradeNo());
		request.setAppid(wechatJournal.getAppid());
		request.setMchId(wechatJournal.getMchid());
		if (StringUtils.hasText(wechatJournal.getSubMchid())) {
			request.setSubMchId(wechatJournal.getSubMchid());
		}
		if (StringUtils.hasText(wechatJournal.getSubAppid())) {
			request.setSubAppId(wechatJournal.getSubAppid());
		}
		try {
			wxPayService.reverseOrder(request);
		}
		catch (WxPayException e) {
			throw new BusinessException("撤销订单失败，异常信息：{0}", e.getErrCodeDes());
		}
		wechatJournal.setStatus(PaymentStatus.CANCEL.getId());
		payWechatJournalDao.save(wechatJournal);
	}

	@SneakyThrows
	public CreateOrderDto pay(PayJournalPo payJournal, OrderCreateCommand command, PayMerchantPo payMerchant) {
		PayChannel channel = PayChannel.fromId(command.getChannel());
		OrderCreateCommand.Wechat wechat = command.getWechat();
		PayWechatConfigPo wechatConfig = payWechatConfigQueryService.findByIdNotNull(payJournal.getPayWayConfigId());
		WxPayService wxPayService = getWxPayService(wechatConfig);
		PayWechatJournalPo old = payWechatJournalDao.findFirstByPayJournalIdOrderByIdDesc(payJournal.getId());
		if (old != null) {
			if (Objects.equals(old.getStatus(), PaymentStatus.WAIT_PAY.getId())) {
				boolean less1Hours = DateUtil.between(new Date(), old.getLastModifiedDate(), DateUnit.MINUTE) < 60;
				createSyncTask(payJournal, old);
				if (Objects.equals(channel, PayChannel.WECHAT_JSAPI) && less1Hours) {
					return createFromWxMp(getJsApiPaySign(wxPayService, old), payJournal.getOrderNo());
				}
				if (Objects.equals(channel, PayChannel.WECHAT_NATIVE) && less1Hours) {
					return CreateOrderDto.createFromWxNative(old.getPayUrl(), payJournal.getOrderNo());
				}
			}
			else if (Objects.equals(old.getStatus(), PaymentStatus.CANCEL.getId())) {
				throw new ClientParamException("订单已取消，不可支付");
			}
			else {
				throw new ClientParamException("订单处于不可支付状态");
			}
		}
		switch (channel) {
			case WECHAT_JSAPI: {
				PayWechatJournalPo result = unifiedOrder(payJournal, wechat, payMerchant, wechatConfig, wxPayService,
						WxPayConstants.TradeType.JSAPI);
				return createFromWxMp(getJsApiPaySign(wxPayService, result), payJournal.getOrderNo());
			}
			case WECHAT_NATIVE: {
				PayWechatJournalPo result = unifiedOrder(payJournal, wechat, payMerchant, wechatConfig, wxPayService,
						WxPayConstants.TradeType.NATIVE);
				return CreateOrderDto.createFromWxNative(result.getPayUrl(), payJournal.getOrderNo());
			}
			case WECHAT_MICRO:
				microPay(payJournal, wechat, payMerchant, wechatConfig, wxPayService, command.getAuthCode());
				return CreateOrderDto.createOrderNo(payJournal.getOrderNo());
			default:
				throw new ClientParamException("不支持的支付类型");
		}
	}

	/**
	 * Unified order pay wechat journal po.
	 * @param payJournal the pay journal
	 * @param wechat the wechat
	 * @param payMerchant the pay merchant
	 * @param wechatConfig the wechat config
	 * @param wxPayService the wx pay service
	 * @param tradeType the trade type
	 * @return the pay wechat journal po
	 * @throws WxPayException the wx pay exception
	 */
	public PayWechatJournalPo unifiedOrder(PayJournalPo payJournal, OrderCreateCommand.Wechat wechat,
			PayMerchantPo payMerchant, PayWechatConfigPo wechatConfig, WxPayService wxPayService, String tradeType)
			throws WxPayException {
		WxPayUnifiedOrderRequest.WxPayUnifiedOrderRequestBuilder builder = WxPayUnifiedOrderRequest.newBuilder();
		String notifyUrl = String.format("%s/pay/callback/v1/wechat/pay-callback/%d/config/%d",
				applicationProperties.getHost(), payJournal.getPayMerchantId(), payJournal.getPayWayConfigId());
		String body = wechat.getBody() != null ? wechat.getBody() : payMerchant.getName() + payJournal.getOrderNo();
		Integer payPrice = payJournal.getPayPrice().multiply(new BigDecimal(100)).intValue();
		String attach = wechat.getAttach() != null ? wechat.getAttach() : null;
		String clientIp = wechat.getClientIp();
		String openid = wechat.getOpenid();
		String goodsTag = wechat.getGoodsTag() != null ? wechat.getGoodsTag() : null;
		String productId = StringUtils.hasText(wechat.getProductId()) ? wechat.getProductId() : payJournal.getOrderNo();
		builder.body(body)
			.productId(productId)
			.outTradeNo(payJournal.getOrderNo())
			.totalFee(payPrice)
			.spbillCreateIp(clientIp)
			.tradeType(tradeType)
			.goodsTag(goodsTag)
			.attach(attach)
			.notifyUrl(notifyUrl)
			.deviceInfo(payJournal.getDeviceInfo());
		WxPayUnifiedOrderRequest request = builder.build();

		if (Objects.equals(wechatConfig.getConfigType(), WechatConfigType.PROVIDER.getId())) {
			// 服务商自定义
			if (StringUtils.hasText(wechat.getSubMchId())) {
				request.setSubMchId(wechat.getSubMchId());
			}
			if (StringUtils.hasText(wechat.getSubAppId())) {
				request.setSubAppId(wechat.getSubAppId());
				request.setSubOpenid(openid);
			}
			else {
				request.setOpenid(openid);
			}
		}

		// 以配置为准
		WxPayConfig wxPayConfig = wxPayService.getConfig();
		if (StringUtils.hasText(wxPayConfig.getSubMchId())) {
			request.setSubMchId(wxPayConfig.getSubMchId());
		}
		if (StringUtils.hasText(wxPayConfig.getSubAppId())) {
			request.setSubAppId(wxPayConfig.getSubAppId());
			request.setSubOpenid(openid);
		}
		else {
			request.setOpenid(openid);
		}

		WxPayUnifiedOrderResult result = wxPayService.unifiedOrder(request);
		PayWechatJournalPo payment = new PayWechatJournalPo();
		payment.setId(idService.getId());
		payment.setAppid(wechatConfig.getAppid());
		payment.setMchid(wechatConfig.getMchid());
		payment.setDescription(body);
		payment.setOutTradeNo(payJournal.getOrderNo());
		payment.setTimeExpire(StringUtils.hasText(request.getTimeExpire()) ? request.getTimeExpire() : "");
		payment.setAttach(attach);
		payment.setGoodsTag(goodsTag);
		payment.setAmountTotal(payPrice);
		payment.setAmountCurrency("CNY");
		payment.setPayerOpenid(openid);
		payment.setSceneInfoPayerClientIp(wechat.getClientIp());
		payment.setSceneInfoDeviceId(payJournal.getDeviceInfo());
		payment.setSceneInfoStoreInfoId("");
		payment.setSceneInfoStoreInfoName("");
		payment.setSceneInfoStoreInfoAreaCode("");
		payment.setSceneInfoStoreInfoAddress("");
		payment.setPayMerchantId(payJournal.getPayMerchantId());
		payment.setPayAppId(payJournal.getPayAppId());
		payment.setPayStoreId(payJournal.getPayStoreId());
		payment.setOrganizationId(payJournal.getOrganizationId());
		payment.setDepartId(payJournal.getDepartId());
		payment.setPayJournalId(payJournal.getId());
		payment.setPrepayId(result.getPrepayId());
		payment.setTransactionId("");
		payment.setTradeType(tradeType);
		payment.setStatus(PaymentStatus.WAIT_PAY.getId());
		payment.setMerchantBusinessNo(payJournal.getMerchantBusinessNo());
		if (StringUtils.hasText(request.getSubMchId())) {
			payment.setSubMchid(request.getSubMchId());
		}
		if (StringUtils.hasText(request.getSubAppId())) {
			payment.setSubAppid(request.getSubAppId());
		}
		payment.setWechatConfigId(payJournal.getPayWayConfigId());
		payment.setPayUrl(result.getCodeURL());
		payWechatJournalDao.save(payment);
		createSyncTask(payJournal, payment);
		return payment;
	}

	/**
	 * Micro pay.
	 * @param payJournal the pay journal
	 * @param wechat the wechat
	 * @param payMerchant the pay merchant
	 * @param wechatConfig the wechat config
	 * @param wxPayService the wx pay service
	 * @param authCode the auth code
	 */
	@SneakyThrows
	public void microPay(PayJournalPo payJournal, OrderCreateCommand.Wechat wechat, PayMerchantPo payMerchant,
			PayWechatConfigPo wechatConfig, WxPayService wxPayService, String authCode) {
		String body = wechat.getBody() != null ? wechat.getBody() : payMerchant.getName() + payJournal.getOrderNo();
		WxPayMicropayRequest request = new WxPayMicropayRequest();
		if (!StringUtils.hasText(authCode)) {
			throw new ClientParamException("授权码不可为空");
		}
		if (ObjectUtils.isEmpty(wechat.getClientIp())) {
			throw new ClientParamException("客户端ip不可为空");
		}
		request.setMchId(wechatConfig.getMchid());
		request.setAppid(wechatConfig.getAppid());

		if (Objects.equals(wechatConfig.getConfigType(), WechatConfigType.PROVIDER.getId())) {
			// 服务商自定义
			if (StringUtils.hasText(wechat.getSubMchId())) {
				request.setSubMchId(wechat.getSubMchId());
			}
			if (StringUtils.hasText(wechat.getSubAppId())) {
				request.setSubAppId(wechat.getSubAppId());
			}
		}

		// 以配置为准
		WxPayConfig wxPayConfig = wxPayService.getConfig();
		if (StringUtils.hasText(wxPayConfig.getSubMchId())) {
			request.setSubMchId(wxPayConfig.getSubMchId());
		}
		if (StringUtils.hasText(wxPayConfig.getSubAppId())) {
			request.setSubAppId(wxPayConfig.getSubAppId());
		}
		request.setDeviceInfo(payJournal.getDeviceInfo());
		request.setAuthCode(authCode);
		request.setBody(body);
		request.setOutTradeNo(payJournal.getOrderNo());
		request.setTotalFee(payJournal.getPayPrice().multiply(new BigDecimal(100)).intValue());
		request.setSpbillCreateIp(wechat.getClientIp());
		WxPayMicropayResult result = null;
		try {
			result = wxPayService.micropay(request);
		}
		catch (WxPayException e) {
			String errCode = e.getErrCode();
			String errCodeDes = e.getErrCodeDes();
			log.error("收款失败，单号：{}，异常：{}", payJournal.getOrderNo(),
					e.getErrCodeDes() == null ? e.getCustomErrorMsg() : e.getErrCodeDes());
			if (WxPayConstants.ResultCode.SUCCESS.equals(e.getReturnCode())) {
				if ("SYSTEMERROR".equals(errCode) || "BANKERROR".equals(errCode)
						|| WxPayConstants.WxpayTradeStatus.USER_PAYING.equals(errCode)) {
					PayWechatJournalPo wechatJournal = saveMicroPayWechatJournal(payJournal, request,
							PaymentStatus.WAIT_PAY, "", errCodeDes);
					createSyncTask(payJournal, wechatJournal);
					// throw new BusinessException("用户支付中，等待用户输入密码或银行扣款",
					// BusinessError.PROCESSING);
					return;
				}
				else {
					saveMicroPayWechatJournal(payJournal, request, PaymentStatus.CANCEL, "", errCodeDes);
					payJournalService.updatePayStatus(payJournal.getId(), PayStatus.OTHER_ERROR.getId());
					throw new BusinessException("收款失败，请重试," + errCodeDes);
				}
			}
			else {
				saveMicroPayWechatJournal(payJournal, request, PaymentStatus.CANCEL, "", e.getReturnMsg());
				payJournalService.updatePayStatus(payJournal.getId(), PayStatus.OTHER_ERROR.getId());
				throw new BusinessException("收款失败" + e.getReturnMsg());
			}

		}
		// 支付成功
		if (WxPayConstants.TradeType.MICROPAY.equals(result.getTradeType())) {
			Date payEndTime = DateUtil.parse(result.getTimeEnd(), DatePattern.PURE_DATETIME_PATTERN).toJdkDate();
			saveMicroPayWechatJournal(payJournal, request, PaymentStatus.PAY, result.getTransactionId(), "",
					payEndTime);
			payJournalService.updateSuccessPayStatus(payJournal.getId(), PayStatus.PAY.getId(),
					result.getTransactionId(), payEndTime);
			return;
		}

		saveMicroPayWechatJournal(payJournal, request, PaymentStatus.CANCEL, "", "该单号已从其他渠道支付");
		payJournalService.updatePayStatus(payJournal.getId(), PayStatus.OTHER_ERROR.getId());
		throw new BusinessException("收款失败");

	}

	/**
	 * Query wx pay order wx pay order query result.
	 * @param payWechatJournal the pay wechat journal
	 * @param wxPayService the wx pay service
	 * @return the wx pay order query result
	 * @throws WxPayException the wx pay exception
	 */
	public WxPayOrderQueryResult queryWxPayOrder(PayWechatJournalPo payWechatJournal, WxPayService wxPayService)
			throws WxPayException {
		WxPayOrderQueryRequest request = new WxPayOrderQueryRequest();
		request.setOutTradeNo(payWechatJournal.getOutTradeNo());
		request.setAppid(payWechatJournal.getAppid());
		request.setMchId(payWechatJournal.getMchid());
		if (StringUtils.hasText(payWechatJournal.getSubMchid())) {
			request.setSubMchId(payWechatJournal.getSubMchid());
		}
		if (StringUtils.hasText(payWechatJournal.getSubAppid())) {
			request.setSubAppId(payWechatJournal.getSubAppid());
		}
		return wxPayService.queryOrder(request);
	}

	private boolean checkAndUpdatePayOrderStatus(PayWechatJournalPo wechatJournal, WxPayOrderQueryResult result,
			boolean throwException, boolean writeBack) {
		String tradeState = result.getTradeState();
		String orderNo = wechatJournal.getOutTradeNo();
		Long payJournalId = wechatJournal.getPayJournalId();
		if (WxPayConstants.ResultCode.SUCCESS.equals(tradeState)) {
			Date payEndTime = DateUtil.parse(result.getTimeEnd(), DatePattern.PURE_DATETIME_PATTERN).toJdkDate();
			wechatJournal.setStatus(PaymentStatus.PAY.getId());
			wechatJournal.setTransactionId(result.getTransactionId());
			wechatJournal.setTimeEnd(payEndTime);
			wechatJournal.setErrCodeDes("");
			String openid = StringUtils.hasText(result.getOpenid()) ? result.getOpenid() : result.getSubOpenid();
			if (StringUtils.hasText(result.getSubAppId())) {
				openid = result.getSubOpenid();
			}
			wechatJournal.setPayerOpenid(openid);
			payWechatJournalDao.save(wechatJournal);
			payJournalService.updateSuccessPayStatus(payJournalId, PayStatus.PAY.getId(),
					wechatJournal.getTransactionId(), payEndTime);
			createPaySuccessNotifyTask(payJournalQueryService.findByIdNotNull(payJournalId), wechatJournal);
			return true;
		}
		else if (WxPayConstants.WxpayTradeStatus.PAY_ERROR.equals(tradeState)) {
			log.error("付款码支付失败，单号：{}", wechatJournal.getOutTradeNo());
			try {
				cancelOrder(wechatJournal);
			}
			catch (Exception e) {
				log.error("撤销订单失败，单号：{}，异常：{}", orderNo, e.getMessage());
				e.printStackTrace();
			}
			wechatJournal.setStatus(PaymentStatus.CANCEL.getId());
			wechatJournal.setErrCodeDes(result.getTradeStateDesc());
			payWechatJournalDao.save(wechatJournal);
			if (writeBack) {
				payJournalService.updatePayStatus(payJournalId, PayStatus.OTHER_ERROR.getId());
			}
			if (throwException) {
				throw new BusinessException("支付失败,已自动撤销，请重新下单后重试", BusinessError.REVOKED);
			}
		}
		else if (WxPayConstants.WxpayTradeStatus.USER_PAYING.equals(tradeState) || "ACCEPT".equals(tradeState)
				|| WxPayConstants.WxpayTradeStatus.NOTPAY.equals(tradeState)) {
			if (throwException) {
				throw new BusinessException("用户支付中，等待用户输入密码或银行扣款", BusinessError.PROCESSING);
			}
		}
		else if (WxPayConstants.WxpayTradeStatus.REFUND.equals(tradeState)) {
			Date payEndTime = DateUtil.parse(result.getTimeEnd(), DatePattern.PURE_DATETIME_PATTERN).toJdkDate();
			wechatJournal.setStatus(PaymentStatus.PAY.getId());
			wechatJournal.setTransactionId(result.getTransactionId());
			wechatJournal.setTimeEnd(payEndTime);
			wechatJournal.setErrCodeDes("");
			String openid = StringUtils.hasText(result.getOpenid()) ? result.getOpenid() : result.getSubOpenid();
			if (StringUtils.hasText(result.getSubAppId())) {
				openid = result.getSubOpenid();
			}
			wechatJournal.setPayerOpenid(openid);
			payWechatJournalDao.save(wechatJournal);
			payJournalService.updateSuccessPayStatus(payJournalId, PayStatus.PAY.getId(),
					wechatJournal.getTransactionId(), payEndTime);
			return true;
		}
		else if (WxPayConstants.WxpayTradeStatus.REVOKED.equals(tradeState)) {
			wechatJournal.setStatus(PaymentStatus.CANCEL.getId());
			wechatJournal.setErrCodeDes(result.getTradeStateDesc());
			payWechatJournalDao.save(wechatJournal);
			if (writeBack) {
				payJournalService.updatePayStatus(payJournalId, PayStatus.OTHER_ERROR.getId());
			}
			if (throwException) {
				throw new BusinessException("支付失败,该笔交易已撤销,请重新下单后重试", BusinessError.REVOKED);
			}
		}
		else {
			// "CLOSED".equals(tradeState)
			wechatJournal.setStatus(PaymentStatus.CANCEL.getId());
			wechatJournal.setErrCodeDes(result.getTradeStateDesc());
			payWechatJournalDao.save(wechatJournal);
			if (writeBack) {
				payJournalService.updatePayStatus(payJournalId, PayStatus.OTHER_ERROR.getId());
			}
			if (throwException) {
				throw new BusinessException("支付失败,该笔交易已关闭", BusinessError.CLOSED);
			}
		}
		return false;
	}

	private boolean syncPayOrderStatus(PayWechatJournalPo payWechatJournal) {
		WxPayService wxPayService = getWxPayService(payWechatJournal.getWechatConfigId());
		if (wxPayService == null) {
			return false;
		}
		String key = String.format(PayLockConstant.PAY_ORDER_PREFIX, payWechatJournal.getPayMerchantId(),
				payWechatJournal.getOutTradeNo());
		Lock lock = cacheTemplate.getLock(key);
		lock.lock();
		try {
			WxPayOrderQueryResult result = queryWxPayOrder(payWechatJournal, wxPayService);
			return checkAndUpdatePayOrderStatus(payWechatJournal, result, false, true);
		}
		catch (WxPayException e) {
			// 查询失败不做状态处理
			log.error("查询微信支付流水失败，单号，{}，错误：{}", payWechatJournal.getOutTradeNo(),
					e.getCustomErrorMsg() == null ? e.getErrCodeDes() : e.getCustomErrorMsg());
			return false;
		}
		catch (Exception e) {
			log.error("同步微信支付状态出现异常, 单号=[{}], 错误: ", payWechatJournal.getOutTradeNo(), e);
			return false;
		}
		finally {
			lock.unlock();
		}
	}

	private PayWechatJournalPo saveMicroPayWechatJournal(PayJournalPo payJournal,
			WxPayMicropayRequest wxPayMicropayRequest, PaymentStatus status, String transactionId, String errorMsg,
			Date endTime) {
		Integer payPrice = payJournal.getPayPrice().multiply(new BigDecimal(100)).intValue();
		PayWechatJournalPo payment = new PayWechatJournalPo();
		payment.setId(idService.getId());
		payment.setAppid(wxPayMicropayRequest.getAppid());
		payment.setMchid(wxPayMicropayRequest.getMchId());
		payment.setDescription(wxPayMicropayRequest.getBody());
		payment.setOutTradeNo(payJournal.getOrderNo());
		payment.setTimeExpire("");
		payment.setAttach(wxPayMicropayRequest.getAttach());
		payment.setGoodsTag(wxPayMicropayRequest.getGoodsTag());
		payment.setAmountTotal(payPrice);
		payment.setAmountCurrency("CNY");
		payment.setPayerOpenid("");
		payment.setSceneInfoPayerClientIp(wxPayMicropayRequest.getSpbillCreateIp());
		payment.setSceneInfoDeviceId(payJournal.getDeviceInfo());
		payment.setSceneInfoStoreInfoId("");
		payment.setSceneInfoStoreInfoName("");
		payment.setSceneInfoStoreInfoAreaCode("");
		payment.setSceneInfoStoreInfoAddress("");
		payment.setPayMerchantId(payJournal.getPayMerchantId());
		payment.setPayAppId(payJournal.getPayAppId());
		payment.setPayStoreId(payJournal.getPayStoreId());
		payment.setOrganizationId(payJournal.getOrganizationId());
		payment.setDepartId(payJournal.getDepartId());
		payment.setPayJournalId(payJournal.getId());
		payment.setTransactionId(transactionId);
		payment.setErrCodeDes(errorMsg);
		payment.setTradeType(WxPayConstants.TradeType.MICROPAY);
		payment.setStatus(status.getId());
		payment.setMerchantBusinessNo(payJournal.getMerchantBusinessNo());
		if (endTime != null) {
			payment.setTimeEnd(endTime);
		}
		if (StringUtils.hasText(wxPayMicropayRequest.getSubMchId())) {
			payment.setSubMchid(wxPayMicropayRequest.getSubMchId());
		}
		if (StringUtils.hasText(wxPayMicropayRequest.getSubAppId())) {
			payment.setSubAppid(wxPayMicropayRequest.getSubAppId());
		}
		payment.setWechatConfigId(payJournal.getPayWayConfigId());
		payment.setPayUrl("");
		return payWechatJournalDao.save(payment);
	}

	private PayWechatJournalPo saveMicroPayWechatJournal(PayJournalPo payJournalPo, WxPayMicropayRequest request,
			PaymentStatus status, String transactionId, String errorMsg) {
		return saveMicroPayWechatJournal(payJournalPo, request, status, transactionId, errorMsg, null);
	}

	@SneakyThrows
	@Override
	public void refund(PayJournalPo payJournal, PayRefundJournalPo refundJournal,
			RefundOrderCreateCommand refundOrderCommand, PayMerchantPo payMerchant) {
		PayWechatJournalPo old = payWechatJournalDao.findFirstByPayJournalIdOrderByIdDesc(payJournal.getId());
		if (old == null) {
			throw new ResourceNotFoundException("不存在该订单");
		}
		PayWechatConfigPo config = payWechatConfigQueryService.findByIdNotNull(payJournal.getPayWayConfigId());
		WxPayService wxPayService = getWxPayService(config);
		WxPayRefundRequest request = new WxPayRefundRequest();
		request.setMchId(old.getMchid());
		request.setAppid(old.getAppid());
		request.setOutRefundNo(old.getOutTradeNo());
		request.setTransactionId(old.getTransactionId());
		request.setOutRefundNo(refundOrderCommand.getRefundOrderNo());
		request.setTotalFee(old.getAmountTotal());
		request.setRefundDesc(refundOrderCommand.getRefundReason());
		request.setRefundFee(refundOrderCommand.getRefundPrice().multiply(new BigDecimal(100)).intValue());
		request.setOpUserId(old.getMchid());
		request.setDeviceInfo(refundOrderCommand.getDeviceInfo());
		String notifyUrl = String.format("%s/pay/callback/v1/wechat/refund-callback/%s/config/%s",
				applicationProperties.getHost(), payJournal.getPayMerchantId(), payJournal.getPayWayConfigId());
		request.setNotifyUrl(notifyUrl);
		if (StringUtils.hasText(old.getSubAppid())) {
			request.setSubAppId(old.getSubAppid());
		}
		if (StringUtils.hasText(old.getSubMchid())) {
			request.setSubMchId(old.getSubMchid());
			request.setOpUserId(old.getSubMchid());
		}
		try {
			WxPayRefundResult wxPayRefundResult = wxPayService.refund(request);
			PayWechatRefundJournalPo wechatRefundJournal = new PayWechatRefundJournalPo();
			wechatRefundJournal.setId(idService.getId());
			wechatRefundJournal.setAppid(request.getAppid());
			wechatRefundJournal.setMchid(request.getMchId());
			wechatRefundJournal.setSubMchid(request.getSubMchId());
			wechatRefundJournal.setSubAppid(request.getAppid());
			wechatRefundJournal.setWechatConfigId(old.getWechatConfigId());
			wechatRefundJournal.setOutRefundNo(refundJournal.getRefundOrderNo());
			wechatRefundJournal.setOutTradeNo(refundJournal.getOrderNo());
			wechatRefundJournal.setNotifyUrl(notifyUrl);
			wechatRefundJournal.setRefundPrice(request.getRefundFee());
			wechatRefundJournal.setErrCodeDes("");
			wechatRefundJournal.setPayMerchantId(refundJournal.getPayMerchantId());
			wechatRefundJournal.setPayAppId(refundJournal.getPayAppId());
			wechatRefundJournal.setPayStoreId(refundJournal.getPayStoreId());
			wechatRefundJournal.setOrganizationId(refundJournal.getOrganizationId());
			wechatRefundJournal.setDepartId(refundJournal.getDepartId());
			wechatRefundJournal.setPayJournalId(refundJournal.getPayJournalId());
			wechatRefundJournal.setRefundJournalId(refundJournal.getId());
			wechatRefundJournal.setTransactionId(wxPayRefundResult.getTransactionId());
			wechatRefundJournal.setRefundId(wxPayRefundResult.getRefundId());
			wechatRefundJournal.setStatus("");
			wechatRefundJournal.setTotalPrice(old.getAmountTotal());
			wechatRefundJournal.setMerchantBusinessNo(refundJournal.getMerchantBusinessNo());
			payWechatRefundJournalDao.save(wechatRefundJournal);
			payRefundJournalService.updateThirdRefundNo(wechatRefundJournal.getRefundJournalId(),
					wechatRefundJournal.getRefundId());
		}
		catch (WxPayException e) {
			payJournalService.submitRefund(payJournal.getId(),
					refundJournal.getRefundPrice().multiply(new BigDecimal(-1)), payJournal.getRefundFreezePrice(),
					payJournal.getRefundPrice());
			payRefundJournalService.updateFailRefundStatus(refundJournal.getId(), e.getErrCodeDes());
			throw new BusinessException(e.getMessage());
		}
		catch (Throwable e) {
			log.error("wechat refund fail: ", e);
			throw new BusinessException("退款失败");
		}
	}

	private WxPayMpOrderResult getJsApiPaySign(WxPayService wxPayService, PayWechatJournalPo wechatJournal) {
		String signType = WxPayConstants.SignType.MD5;
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		String nonceStr = String.valueOf(System.currentTimeMillis());
		String appId = wechatJournal.getAppid();
		if (StringUtils.hasText(wechatJournal.getSubAppid())) {
			appId = wechatJournal.getSubAppid();
		}
		WxPayMpOrderResult payResult = WxPayMpOrderResult.builder()
			.appId(appId)
			.timeStamp(timestamp)
			.nonceStr(nonceStr)
			.packageValue("prepay_id=" + wechatJournal.getPrepayId())
			.signType(signType)
			.build();

		payResult.setPaySign(SignUtils.createSign(payResult, signType, wxPayService.getConfig().getMchKey(), null));
		return payResult;
	}

	/**
	 * Complete pay.
	 * @param merchantId the merchant id
	 * @param wechatConfigId the wechat config id
	 * @param xmlData the xml data
	 * @throws WxPayException the wx pay exception
	 */
	public void completePay(Long merchantId, Long wechatConfigId, String xmlData) throws WxPayException {
		PayWechatConfigPo payWechatConfig = payWechatConfigQueryService.findByIdNotNull(wechatConfigId);
		if (!Objects.equals(merchantId, payWechatConfig.getPayMerchantId())) {
			return;
		}
		WxPayService wxPayService = getWxPayService(payWechatConfig);
		WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlData);

		String key = String.format(PayLockConstant.PAY_ORDER_PREFIX, merchantId, result.getOutTradeNo());
		Lock lock = cacheTemplate.getLock(key);
		lock.lock();
		try {
			PayJournalPo payJournal = payJournalQueryService.findMerchantJournal(merchantId, result.getOutTradeNo());
			if (!Objects.equals(payJournal.getPayStatus(), PayStatus.WAIT_PAY.getId())) {
				return;
			}
			PayWechatJournalPo payWechatJournal = payWechatJournalDao
				.findFirstByPayJournalIdOrderByIdDesc(payJournal.getId());
			if (!Objects.equals(result.getResultCode(), WxPayConstants.ResultCode.SUCCESS)) {
				payWechatJournal.setErrCodeDes(result.getErrCodeDes());
				payWechatJournalDao.save(payWechatJournal);
				payJournalService.updatePayStatus(payJournal.getId(), PayStatus.OTHER_ERROR.getId());
				return;
			}
			String openid = StringUtils.hasText(result.getOpenid()) ? result.getOpenid() : result.getSubOpenid();
			Date payEndTime = DateUtil.parse(result.getTimeEnd(), DatePattern.PURE_DATETIME_PATTERN).toJdkDate();
			payWechatJournal.setTransactionId(result.getTransactionId());
			payWechatJournal.setStatus(PaymentStatus.PAY.getId());
			payWechatJournal.setTradeType(result.getTradeType());
			payWechatJournal.setTimeEnd(payEndTime);
			payWechatJournal.setPayerOpenid(openid);
			payWechatJournalDao.save(payWechatJournal);
			payJournalService.updateSuccessPayStatus(payJournal.getId(), PayStatus.PAY.getId(),
					payWechatJournal.getTransactionId(), payEndTime);
			createPaySuccessNotifyTask(payJournal, payWechatJournal);
		}
		finally {
			lock.unlock();
		}
	}

	/**
	 * Complete refund.
	 * @param merchantId the merchant id
	 * @param wechatConfigId the wechat config id
	 * @param xmlData the xml data
	 * @throws WxPayException the wx pay exception
	 */
	public void completeRefund(Long merchantId, Long wechatConfigId, String xmlData) throws WxPayException {
		PayWechatConfigPo payWechatConfig = payWechatConfigQueryService.findByIdNotNull(wechatConfigId);
		if (!Objects.equals(merchantId, payWechatConfig.getPayMerchantId())) {
			return;
		}
		WxPayService wxPayService = getWxPayService(payWechatConfig);
		WxPayRefundNotifyResult result = wxPayService.parseRefundNotifyResult(xmlData);
		WxPayRefundNotifyResult.ReqInfo reqInfo = result.getReqInfo();

		String key = String.format(PayLockConstant.PAY_ORDER_PREFIX, merchantId, reqInfo.getOutTradeNo());
		Lock lock = cacheTemplate.getLock(key);
		lock.lock();
		try {
			PayJournalPo payJournal = payJournalQueryService.findMerchantJournal(merchantId, reqInfo.getOutTradeNo());
			PayRefundJournalPo refundJournal = payRefundJournalQueryService.findByMerchantIdAndRefundOrderNo(merchantId,
					reqInfo.getOutRefundNo());
			PayWechatRefundJournalPo wechatRefundJournal = payWechatRefundJournalDao
				.findFirstByRefundJournalIdOrderByIdDesc(refundJournal.getId());
			if (!Objects.equals(refundJournal.getRefundStatus(), RefundStatus.REFUNDING.getId())) {
				return;
			}
			if (!Objects.equals(result.getReturnCode(), WxPayConstants.ResultCode.SUCCESS)
					|| !Objects.equals(WxPayConstants.ResultCode.SUCCESS, reqInfo.getRefundStatus())) {
				wechatRefundJournal.setErrCodeDes(result.getErrCodeDes());
				wechatRefundJournal.setStatus(reqInfo.getRefundStatus());
				payWechatRefundJournalDao.save(wechatRefundJournal);
				payRefundJournalService.updateFailRefundStatus(refundJournal.getId(), result.getErrCodeDes());
				payJournalService.submitRefund(payJournal.getId(),
						refundJournal.getRefundPrice().multiply(new BigDecimal(-1)), payJournal.getRefundFreezePrice(),
						payJournal.getRefundPrice());
				return;
			}

			String successTime = reqInfo.getSuccessTime();
			DateTime dateTime = DateUtil.parse(successTime, DatePattern.NORM_DATETIME_PATTERN);
			Date successDate = dateTime.toJdkDate();

			wechatRefundJournal.setStatus(result.getReqInfo().getRefundStatus());
			wechatRefundJournal.setSuccessTime(successDate);
			payWechatRefundJournalDao.save(wechatRefundJournal);
			payJournalService.completeRefund(payJournal.getId(), refundJournal.getRefundPrice(),
					payJournal.getRefundFreezePrice(), payJournal.getRefundPrice());
			payRefundJournalService.updateSuccessRefundStatus(refundJournal.getId(), wechatRefundJournal.getRefundId(),
					successDate);
			RefundSuccessNotifyMessage message = new RefundSuccessNotifyMessage(refundJournal.getId());
			asyncNotifyInputChannel.send(new GenericMessage<>(message));
		}
		finally {
			lock.unlock();
		}
	}

	/**
	 * Refund query wx pay refund query result.
	 * @param wechatJournal the WeChat journal
	 * @return the wx pay refund query result
	 * @throws WxPayException the wx pay exception
	 */
	public WxPayRefundQueryResult refundQuery(PayWechatRefundJournalPo wechatJournal) throws WxPayException {
		PayWechatConfigPo config = payWechatConfigQueryService.findByIdNotNull(wechatJournal.getWechatConfigId());
		WxPayService wxPayService = getWxPayService(config);
		WxPayRefundQueryRequest request = new WxPayRefundQueryRequest();
		request.setOutRefundNo(wechatJournal.getOutRefundNo());
		request.setAppid(wechatJournal.getAppid());
		if (StringUtils.hasText(wechatJournal.getSubAppid())) {
			request.setSubAppId(wechatJournal.getSubAppid());
		}
		request.setMchId(wechatJournal.getMchid());
		if (StringUtils.hasText(wechatJournal.getSubMchid())) {
			request.setSubMchId(wechatJournal.getSubMchid());
		}
		WxPayRefundQueryResult result = wxPayService.refundQuery(request);
		return result;
	}

	private void createSyncTask(PayJournalPo payJournal, PayWechatJournalPo wechatJournal) {
		TradeStatusPollingMessage pollingMessage = new TradeStatusPollingMessage(payJournal.getId(),
				wechatJournal.getId());
		Map<String, Object> headers = new HashMap<>();
		headers.put("delay", DateUtil.offsetSecond(new Date(), 5));
		tradeStatusPollingInputChannel.send(new GenericMessage<>(pollingMessage, headers));
	}

	private void createPaySuccessNotifyTask(PayJournalPo payJournal, PayWechatJournalPo wechatJournalPo) {
		PaySuccessNotifyMessage message = new PaySuccessNotifyMessage(payJournal.getId());
		asyncNotifyInputChannel.send(new GenericMessage<>(message));
	}

	@Override
	public boolean syncPayOrderStatus(PayJournalPo payJournal) {
		PayWechatJournalPo payWechatJournal = payWechatJournalDao
			.findFirstByPayJournalIdOrderByIdDesc(payJournal.getId());
		if (payWechatJournal == null) {
			return false;
		}
		return syncPayOrderStatus(payWechatJournal);
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
		return PayWay.WECHAT;
	}

	public CreateOrderDto createFromWxMp(WxPayMpOrderResult result, String orderNo) {
		CreateOrderDto createOrderDto = new CreateOrderDto();
		CreateOrderDto.WechatJsApi wechatJsApi = new CreateOrderDto.WechatJsApi();
		wechatJsApi.setAppId(result.getAppId());
		wechatJsApi.setTimeStamp(result.getTimeStamp());
		wechatJsApi.setNonceStr(result.getNonceStr());
		wechatJsApi.setPackageValue(result.getPackageValue());
		wechatJsApi.setSignType(result.getSignType());
		wechatJsApi.setPaySign(result.getPaySign());
		createOrderDto.setWechat(wechatJsApi);
		createOrderDto.setOrderNo(orderNo);
		return createOrderDto;
	}

}
