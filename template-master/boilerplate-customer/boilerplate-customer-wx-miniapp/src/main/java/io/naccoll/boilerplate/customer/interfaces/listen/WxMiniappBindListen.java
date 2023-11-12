package io.naccoll.boilerplate.customer.interfaces.listen;

import io.naccoll.boilerplate.customer.enums.ThirdType;
import io.naccoll.boilerplate.customer.event.ThirdBindEvent;
import io.naccoll.boilerplate.customer.event.ThirdUnbindEvent;
import io.naccoll.boilerplate.customer.model.CustomerUserBindPo;
import io.naccoll.boilerplate.customer.service.CustomerUserWxMiniappService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class WxMiniappBindListen implements ThirdBindListen {

	@Resource
	private CustomerUserWxMiniappService customerUserWxMiniappService;

	public void unbind(ThirdUnbindEvent event) {
		CustomerUserBindPo bind = event.getPayload();
		customerUserWxMiniappService.unbind(bind.getCustomerUserId(), bind.getThirdId(), bind.getThirdUserId());
	}

	@Override
	public ThirdType getThirdType() {
		return ThirdType.WX_MINIAPP;
	}

	public void bind(ThirdBindEvent event) {
		CustomerUserBindPo bind = event.getPayload();
		customerUserWxMiniappService.bind(bind.getCustomerUserId(), bind.getThirdId(), bind.getThirdUserId());
	}

}
