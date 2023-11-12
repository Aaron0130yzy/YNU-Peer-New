package io.naccoll.boilerplate.customer.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import io.naccoll.boilerplate.core.cache.CacheTemplate;
import io.naccoll.boilerplate.customer.config.WxMiniappProperties;
import jakarta.annotation.Resource;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(WxMiniappProperties.class)
public class WxMiniappServiceFactory {

	@Resource
	private WxMiniappProperties wxMiniappProperties;

	@Resource
	private CacheTemplate cacheTemplate;

	private Map<String, WxMaService> mpServiceMap = new ConcurrentHashMap<>();

	public WxMaService create(String appid) {
		WxMaService old = mpServiceMap.get(appid);
		if (old != null) {
			return old;
		}
		Lock lock = new ReentrantLock();
		lock.lock();
		try {
			WxMaServiceImpl service = new WxMaServiceImpl();
			service.setWxMaConfig(getConfigStorage(appid));
			return service;
		}
		finally {
			lock.unlock();
		}
	}

	private WxMaConfig getConfigStorage(String appid) {
		WxMiniappProperties.MiniappItem item = wxMiniappProperties.getConfigs().get(appid);
		if (item == null) {
			throw new IllegalStateException(String.format("%s config does not exist", appid));
		}
		WxMaConfigImpl config = new WxMaConfigImpl(cacheTemplate, "wx-miniapp");
		config.setAppid(item.getAppId());
		config.setSecret(item.getSecret());
		config.setToken(item.getToken());
		config.setAesKey(item.getAesKey());
		return config;
	}

}
