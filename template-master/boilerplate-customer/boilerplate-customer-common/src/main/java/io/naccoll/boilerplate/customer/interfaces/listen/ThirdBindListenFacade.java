package io.naccoll.boilerplate.customer.interfaces.listen;

import java.util.HashMap;
import java.util.Map;

import io.naccoll.boilerplate.customer.enums.ThirdType;
import io.naccoll.boilerplate.customer.event.ThirdBindEvent;
import io.naccoll.boilerplate.customer.event.ThirdUnbindEvent;
import io.naccoll.boilerplate.customer.model.CustomerUserBindThirdPo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 第三方渠道绑定解绑事件处理入口类
 */
@Component
public class ThirdBindListenFacade {

	private Map<ThirdType, ThirdBindListen> listenMap;

	public ThirdBindListenFacade(ApplicationContext applicationContext) {
		Map<String, ThirdBindListen> beans = applicationContext.getBeansOfType(ThirdBindListen.class);
		Map<ThirdType, ThirdBindListen> ossTypeOssServiceMap = new HashMap<>(beans.keySet().size());
		for (ThirdBindListen value : beans.values()) {
			ossTypeOssServiceMap.put(value.getThirdType(), value);
		}
		this.listenMap = ossTypeOssServiceMap;
	}

	@EventListener
	public void unbind(ThirdUnbindEvent event) {
		CustomerUserBindThirdPo third = event.getSource();
		ThirdBindListen thirdBindListen = listenMap.get(ThirdType.fromId(third.getThirdType()));
		if (thirdBindListen != null) {
			thirdBindListen.unbind(event);
		}
	}

	@EventListener
	public void bind(ThirdBindEvent event) {
		CustomerUserBindThirdPo third = event.getSource();
		ThirdBindListen thirdBindListen = listenMap.get(ThirdType.fromId(third.getThirdType()));
		if (thirdBindListen != null) {
			thirdBindListen.bind(event);
		}
	}

}
