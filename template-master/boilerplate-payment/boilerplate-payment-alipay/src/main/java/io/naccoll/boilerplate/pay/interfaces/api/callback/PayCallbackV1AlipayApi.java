package io.naccoll.boilerplate.pay.interfaces.api.callback;

import java.io.IOException;
import java.util.Map;

import cn.hutool.json.JSONUtil;
import com.egzosn.pay.common.bean.DefaultNoticeRequest;
import com.egzosn.pay.common.bean.PayOutMessage;
import io.naccoll.boilerplate.pay.constant.PayApiConstant;
import io.naccoll.boilerplate.pay.service.AlipayService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Pay callback v 1 alipay api.
 */
@Slf4j
@Hidden
@RestController
@RequestMapping(PayApiConstant.CallbackV1.ALIPAY)
public class PayCallbackV1AlipayApi {

	@Resource
	private AlipayService alipayService;

	/**
	 * Pay callback string.
	 * @param merchantId the merchant id
	 * @param configId the config id
	 * @param request the request
	 * @return the string
	 * @throws IOException the io exception
	 */
	@RequestMapping("/pay-callback/{merchantId}/config/{configId}")
	public String payCallback(@PathVariable Long merchantId, @PathVariable Long configId, HttpServletRequest request)
			throws IOException {
		Map<String, String[]> parameterMap = request.getParameterMap();
		log.info("商户({})下的支付宝配置({})接收支付回调:{}", merchantId, configId, JSONUtil.toJsonStr(parameterMap));
		PayOutMessage payOutMessage = alipayService.completePay(merchantId, configId,
				new DefaultNoticeRequest(parameterMap, request.getInputStream()));
		return payOutMessage.getContent();
	}

	/**
	 * Refund callback pay out message.
	 * @param merchantId the merchant id
	 * @param configId the config id
	 * @param request the request
	 * @return the pay out message
	 */
	@RequestMapping("/refund-callback/{merchantId}/config/{configId}")
	public PayOutMessage refundCallback(@PathVariable Long merchantId, @PathVariable Long configId,
			HttpServletRequest request) {
		log.info("商户({})下的支付宝配置({})接收退款回调", merchantId, configId);

		return null;
	}

}
