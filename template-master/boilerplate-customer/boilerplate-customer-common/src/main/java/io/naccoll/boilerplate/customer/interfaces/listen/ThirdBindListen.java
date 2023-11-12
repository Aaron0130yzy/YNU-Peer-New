package io.naccoll.boilerplate.customer.interfaces.listen;

import io.naccoll.boilerplate.customer.enums.ThirdType;
import io.naccoll.boilerplate.customer.event.ThirdBindEvent;
import io.naccoll.boilerplate.customer.event.ThirdUnbindEvent;

public interface ThirdBindListen {

	/**
	 * @return 第三方监听器的监听类型
	 */
	ThirdType getThirdType();

	/**
	 * 绑定事件处理
	 * @param event 绑定事件
	 */
	void bind(ThirdBindEvent event);

	/**
	 * 解绑事件处理
	 * @param event 解绑事件
	 */
	void unbind(ThirdUnbindEvent event);

}
