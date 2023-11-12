package io.naccoll.boilerplate.customer.utils;

import java.time.Duration;

import io.naccoll.boilerplate.core.cache.CacheTemplate;
import me.chanjar.weixin.common.enums.TicketType;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;

public class WxMpConfigImpl extends WxMpDefaultConfigImpl {

	private static final String ACCESS_TOKEN_KEY_TPL = "%s:access_token:%s";

	private static final String TICKET_KEY_TPL = "%s:ticket:key:%s:%s";

	private static final String LOCK_KEY_TPL = "%s:lock:%s:";

	private final CacheTemplate cacheTemplate;

	private final String keyPrefix;

	private String accessTokenKey;

	private String lockKey;

	public WxMpConfigImpl(CacheTemplate cacheTemplate, String keyPrefix) {
		this.cacheTemplate = cacheTemplate;
		this.keyPrefix = keyPrefix;
	}

	/**
	 * 每个公众号生成独有的存储key.
	 */
	@Override
	public void setAppId(String appId) {
		super.setAppId(appId);
		this.accessTokenKey = String.format(ACCESS_TOKEN_KEY_TPL, this.keyPrefix, appId);
		this.lockKey = String.format(LOCK_KEY_TPL, this.keyPrefix, appId);

		accessTokenLock = this.cacheTemplate.getLock(lockKey.concat("accessTokenLock"));
		jsapiTicketLock = this.cacheTemplate.getLock(lockKey.concat("jsapiTicketLock"));
		sdkTicketLock = this.cacheTemplate.getLock(lockKey.concat("sdkTicketLock"));
		cardApiTicketLock = this.cacheTemplate.getLock(lockKey.concat("cardApiTicketLock"));
	}

	private String getTicketRedisKey(TicketType type) {
		return String.format(TICKET_KEY_TPL, this.keyPrefix, appId, type.getCode());
	}

	@Override
	public String getAccessToken() {
		Object o = cacheTemplate.get(this.accessTokenKey);
		if (o == null) {
			return null;
		}
		return o.toString();
	}

	@Override
	public boolean isAccessTokenExpired() {
		Duration expire = cacheTemplate.getExpire(this.accessTokenKey);
		return expire == null || expire.toSeconds() < 2;
	}

	@Override
	public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
		cacheTemplate.set(this.accessTokenKey, accessToken, Duration.ofSeconds(expiresInSeconds - 200));
	}

	@Override
	public void expireAccessToken() {
		cacheTemplate.expire(this.accessTokenKey, Duration.ZERO);
	}

	@Override
	public String getTicket(TicketType type) {
		Object o = cacheTemplate.get(this.getTicketRedisKey(type));
		if (o == null) {
			return null;
		}
		return o.toString();
	}

	@Override
	public boolean isTicketExpired(TicketType type) {
		String key = this.getTicketRedisKey(type);
		Duration duration = cacheTemplate.getExpire(this.getTicketRedisKey(type));
		return duration == null || duration.toSeconds() < 2;
	}

	@Override
	public synchronized void updateTicket(TicketType type, String jsapiTicket, int expiresInSeconds) {
		cacheTemplate.set(this.getTicketRedisKey(type), jsapiTicket, Duration.ofSeconds(expiresInSeconds - 200));
	}

	@Override
	public void expireTicket(TicketType type) {
		cacheTemplate.expire(this.getTicketRedisKey(type), Duration.ofSeconds(0));
	}

}
