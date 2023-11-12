package io.naccoll.boilerplate.customer.event;

import io.naccoll.boilerplate.customer.model.CustomerUserBindPo;
import io.naccoll.boilerplate.customer.model.CustomerUserBindThirdPo;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.core.ResolvableType;

public class ThirdBindEvent extends PayloadApplicationEvent<CustomerUserBindPo> {

	public ThirdBindEvent(CustomerUserBindThirdPo source, CustomerUserBindPo payload) {
		super(source, payload);
	}

	@Override
	public ResolvableType getResolvableType() {
		return ResolvableType.forRawClass(ThirdBindEvent.class);
	}

	@Override
	public CustomerUserBindThirdPo getSource() {
		return (CustomerUserBindThirdPo) super.getSource();
	}

}
