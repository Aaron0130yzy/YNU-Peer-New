package io.naccoll.boilerplate.customer.event;

import io.naccoll.boilerplate.customer.model.CustomerUserBindPo;
import io.naccoll.boilerplate.customer.model.CustomerUserBindThirdPo;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.core.ResolvableType;

public class ThirdUnbindEvent extends PayloadApplicationEvent<CustomerUserBindPo> {

	public ThirdUnbindEvent(CustomerUserBindThirdPo source, CustomerUserBindPo payload) {
		super(source, payload);
	}

	@Override
	public ResolvableType getResolvableType() {
		return ResolvableType.forRawClass(ThirdUnbindEvent.class);
	}

	@Override
	public CustomerUserBindThirdPo getSource() {
		return (CustomerUserBindThirdPo) super.getSource();
	}

}
