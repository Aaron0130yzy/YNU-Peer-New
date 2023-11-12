package com.egzosn.pay.ali.api;

import java.util.Map;
import java.util.TreeMap;

import cn.hutool.json.JSONUtil;
import com.egzosn.pay.ali.bean.AliPayConst;
import com.egzosn.pay.ali.bean.AliTransactionType;
import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.OrderParaStructure;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.common.util.DateUtils;
import com.egzosn.pay.common.util.Util;

import static com.egzosn.pay.ali.bean.AliPayConst.BIZ_CONTENT;
import static com.egzosn.pay.ali.bean.AliPayConst.NOTIFY_URL;
import static com.egzosn.pay.ali.bean.AliPayConst.PASSBACK_PARAMS;
import static com.egzosn.pay.ali.bean.AliPayConst.PRODUCT_CODE;
import static com.egzosn.pay.ali.bean.AliPayConst.RETURN_URL;

/**
 * The type Custom alipay service.
 */
public class CustomAlipayServiceImpl extends AliPayService {

	/**
	 * Instantiates a new Custom alipay service.
	 * @param payConfigStorage the pay config storage
	 * @param configStorage the config storage
	 */
	public CustomAlipayServiceImpl(AliPayConfigStorage payConfigStorage, HttpConfigStorage configStorage) {
		super(payConfigStorage, configStorage);
	}

	/**
	 * Instantiates a new Custom alipay service.
	 * @param payConfigStorage the pay config storage
	 */
	public CustomAlipayServiceImpl(AliPayConfigStorage payConfigStorage) {
		super(payConfigStorage);
	}

	/**
	 * 该核心方法实现有问题，重写
	 * @param order
	 * @return
	 */
	@Override
	protected Map<String, Object> getOrder(PayOrder order) {
		Map<String, Object> orderInfo = getPublicParameters(order.getTransactionType());
		setNotifyUrl(orderInfo, order);
		orderInfo.put("format", "json");
		setAppAuthToken(orderInfo, order.getAttrs());

		Map<String, Object> bizContent = new TreeMap<>();
		bizContent.put("body", order.getBody());
		OrderParaStructure.loadParameters(bizContent, "seller_id", payConfigStorage.getSeller());
		bizContent.put("subject", order.getSubject());
		bizContent.put("out_trade_no", order.getOutTradeNo());
		bizContent.put("total_amount", Util.conversionAmount(order.getPrice()).toString());
		switch ((AliTransactionType) order.getTransactionType()) {
			case PAGE:
				bizContent.put(PASSBACK_PARAMS, order.getAddition());
				bizContent.put(PRODUCT_CODE, "FAST_INSTANT_TRADE_PAY");
				bizContent.put(AliPayConst.REQUEST_FROM_URL, payConfigStorage.getReturnUrl());
				OrderParaStructure.loadParameters(bizContent, AliPayConst.REQUEST_FROM_URL, order);
				setReturnUrl(orderInfo, order);
				break;
			case WAP:
				bizContent.put(PASSBACK_PARAMS, order.getAddition());
				// 产品码。
				// 商家和支付宝签约的产品码。 枚举值（点击查看签约情况）：
				// QUICK_WAP_WAY：无线快捷支付产品。
				// 默认值为QUICK_WAP_PAY。
				bizContent.put(PRODUCT_CODE, "QUICK_WAP_PAY");
				OrderParaStructure.loadParameters(bizContent, PRODUCT_CODE, order);

				bizContent.put(AliPayConst.QUIT_URL, payConfigStorage.getReturnUrl());
				OrderParaStructure.loadParameters(bizContent, AliPayConst.QUIT_URL, order);
				setReturnUrl(orderInfo, order);
				break;
			case APP:
				bizContent.put(PASSBACK_PARAMS, order.getAddition());
				bizContent.put(PRODUCT_CODE, "QUICK_MSECURITY_PAY");
				break;
			case MINAPP:
				bizContent.put("extend_params", order.getAttr("extend_params"));
				bizContent.put("buyer_id", order.getOpenid());
				bizContent.put(PRODUCT_CODE, "FACE_TO_FACE_PAYMENT");
				break;
			case BAR_CODE:
			case WAVE_CODE:
			case SECURITY_CODE:
				bizContent.put("scene", order.getTransactionType().toString().toLowerCase());
				bizContent.put(PRODUCT_CODE, "FACE_TO_FACE_PAYMENT");
				bizContent.put("auth_code", order.getAuthCode());
				break;
			default:
				break;

		}

		setExpirationTime(bizContent, order);

		bizContent.putAll(order.getAttrs());
		orderInfo.put(BIZ_CONTENT, JSONUtil.toJsonStr(bizContent));
		return preOrderHandler(orderInfo, order);
	}

	private Map<String, Object> setExpirationTime(Map<String, Object> bizContent, PayOrder order) {
		if (null == order.getExpirationTime()) {
			return bizContent;
		}
		bizContent.put("timeout_express", DateUtils.minutesRemaining(order.getExpirationTime()) + "m");
		switch ((AliTransactionType) order.getTransactionType()) {
			case SWEEPPAY:
				bizContent.put("qr_code_timeout_express", DateUtils.minutesRemaining(order.getExpirationTime()) + "m");
				break;
			case PAGE:
			case WAP:
			case APP:
				bizContent.put("time_expire",
						DateUtils.formatDate(order.getExpirationTime(), DateUtils.YYYY_MM_DD_HH_MM_SS));
				break;
			default:
		}
		return bizContent;
	}

	private void setNotifyUrl(Map<String, Object> orderInfo, AssistOrder order) {
		OrderParaStructure.loadParameters(orderInfo, NOTIFY_URL, payConfigStorage.getNotifyUrl());
		OrderParaStructure.loadParameters(orderInfo, NOTIFY_URL, order.getNotifyUrl());
		OrderParaStructure.loadParameters(orderInfo, NOTIFY_URL, order);
	}

	private void setReturnUrl(Map<String, Object> orderInfo, PayOrder order) {
		orderInfo.put(RETURN_URL, payConfigStorage.getReturnUrl());
		OrderParaStructure.loadParameters(orderInfo, RETURN_URL, order);
	}

}
