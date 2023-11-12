package io.naccoll.boilerplate.pay.interfaces.api.callback;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.exception.WxPayException;
import io.naccoll.boilerplate.pay.constant.PayApiConstant;
import io.naccoll.boilerplate.pay.service.WechatPayService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Pay callback v 1 wechat api.
 */
@Slf4j
@Hidden
@RestController
@RequestMapping(PayApiConstant.CallbackV1.WECHAT)
public class PayCallbackV1WechatApi {

	@Resource
	private WechatPayService wechatPayService;

	/**
	 * Pay callback string.
	 * @param merchantId the merchant id
	 * @param configId the config id
	 * @param xmlData the xml data
	 * @return the string
	 * @throws WxPayException the wx pay exception
	 */
	@RequestMapping("/pay-callback/{merchantId}/config/{configId}")
	public String payCallback(@PathVariable Long merchantId, @PathVariable Long configId, @RequestBody String xmlData)
			throws WxPayException {
		log.info("商户({})下的微信配置({})接收支付回调: {}", merchantId, configId, xmlData);
		wechatPayService.completePay(merchantId, configId, xmlData);
		return WxPayNotifyResponse.success("成功");
	}

	/**
	 * Refund callback string.
	 * @param merchantId the merchant id
	 * @param configId the config id
	 * @param xmlData the xml data
	 * @return the string
	 * @throws WxPayException the wx pay exception
	 */
	@RequestMapping("/refund-callback/{merchantId}/config/{configId}")
	public String refundCallback(@PathVariable Long merchantId, @PathVariable Long configId,
			@RequestBody String xmlData) throws WxPayException {
		log.info("商户({})下的微信配置({})接收退款回调: {}", merchantId, configId, xmlData);
		wechatPayService.completeRefund(merchantId, configId, xmlData);
		return WxPayNotifyResponse.success("成功");
	}

}
