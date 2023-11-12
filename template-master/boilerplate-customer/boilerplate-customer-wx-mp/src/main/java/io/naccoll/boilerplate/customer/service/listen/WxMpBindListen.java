package io.naccoll.boilerplate.customer.service.listen;

import io.naccoll.boilerplate.customer.enums.ThirdType;
import io.naccoll.boilerplate.customer.event.ThirdBindEvent;
import io.naccoll.boilerplate.customer.event.ThirdUnbindEvent;
import io.naccoll.boilerplate.customer.interfaces.listen.ThirdBindListen;
import io.naccoll.boilerplate.customer.model.CustomerUserBindPo;
import io.naccoll.boilerplate.customer.service.CustomerUserWxMpService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class WxMpBindListen implements ThirdBindListen {

	@Resource
	private CustomerUserWxMpService customerUserWxMpService;

	@Override
	public ThirdType getThirdType() {
		return ThirdType.WX_MP;
	}

	@Override
	public void bind(ThirdBindEvent event) {
		CustomerUserBindPo bind = event.getPayload();
		customerUserWxMpService.bind(bind.getCustomerUserId(), bind.getThirdId(), bind.getThirdUserId());
	}

	@Override
	public void unbind(ThirdUnbindEvent event) {
		CustomerUserBindPo bind = event.getPayload();
		customerUserWxMpService.unbind(bind.getCustomerUserId(), bind.getThirdId(), bind.getThirdUserId());
	}

}
