package io.naccoll.boilerplate.pay.interfaces.handler;

import io.naccoll.boilerplate.pay.enums.PayWay;
import org.springframework.stereotype.Component;

/**
 * The type Wechat pay success notify handler.
 */
@Component
public class WechatPaySuccessNotifyHandler extends AbstractPaySuccessNotifyHandler {

	@Override
	public PayWay getPayWay() {
		return PayWay.WECHAT;
	}

}
