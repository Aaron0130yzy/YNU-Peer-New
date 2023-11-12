package io.naccoll.boilerplate.pay.interfaces.handler;

import io.naccoll.boilerplate.pay.enums.PayWay;
import org.springframework.stereotype.Component;

/**
 * The type Alipay refund success notify handler.
 */
@Component
public class AlipayRefundSuccessNotifyHandler extends AbstractRefundSuccessNotifyHandler {

	@Override
	public PayWay getPayWay() {
		return PayWay.ALIPAY;
	}

}
