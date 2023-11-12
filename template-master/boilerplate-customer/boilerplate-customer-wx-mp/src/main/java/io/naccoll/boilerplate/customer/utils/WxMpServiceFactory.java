package io.naccoll.boilerplate.customer.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.naccoll.boilerplate.core.cache.CacheTemplate;
import io.naccoll.boilerplate.customer.config.WxMpProperties;
import jakarta.annotation.Resource;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(WxMpProperties.class)
public class WxMpServiceFactory {

	@Resource
	private WxMpProperties wxMpProperties;

	@Resource
	private CacheTemplate cacheTemplate;

	private Map<String, WxMpService> mpServiceMap = new ConcurrentHashMap<>();

	public WxMpService create(String appid) {
		WxMpService old = mpServiceMap.get(appid);
		if (old != null) {
			return old;
		}
		Lock lock = new ReentrantLock();
		lock.lock();
		try {
			WxMpServiceImpl service = new WxMpServiceImpl();
			service.setWxMpConfigStorage(getConfigStorage(appid));
			return service;
		}
		finally {
			lock.unlock();
		}
	}

	private WxMpConfigStorage getConfigStorage(String appid) {
		WxMpProperties.MpItem item = wxMpProperties.getConfigs().get(appid);
		if (item == null) {
			throw new IllegalStateException(String.format("%s config does not exist", appid));
		}
		WxMpConfigImpl config = new WxMpConfigImpl(cacheTemplate, "wx-mp");
		config.setAppId(item.getAppId());
		config.setSecret(item.getSecret());
		config.setToken(item.getToken());
		config.setAesKey(item.getAesKey());
		return config;
	}

}
