package io.naccoll.boilerplate.customer.utils;

import java.time.Duration;

import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import io.naccoll.boilerplate.core.cache.CacheTemplate;
import me.chanjar.weixin.common.enums.TicketType;

public class WxMaConfigImpl extends WxMaDefaultConfigImpl {

	private static final String ACCESS_TOKEN_KEY_TPL = "%s:access_token:%s";

	private static final String TICKET_KEY_TPL = "%s:ticket:key:%s:%s";

	private static final String LOCK_KEY_TPL = "%s:lock:%s:";

	private final CacheTemplate cacheTemplate;

	private final String keyPrefix;

	private volatile String accessTokenKey;

	private volatile String lockKey;

	public WxMaConfigImpl(CacheTemplate cacheTemplate, String keyPrefix) {
		this.cacheTemplate = cacheTemplate;
		this.keyPrefix = keyPrefix;
	}

	@Override
	public void setAppid(String appId) {
		super.setAppid(appId);
		this.accessTokenKey = String.format(ACCESS_TOKEN_KEY_TPL, this.keyPrefix, appId);
		this.lockKey = String.format(LOCK_KEY_TPL, this.keyPrefix, appId);
		super.accessTokenLock = this.cacheTemplate.getLock(lockKey.concat("accessTokenLock"));
		super.jsapiTicketLock = this.cacheTemplate.getLock(lockKey.concat("jsapiTicketLock"));
		super.cardApiTicketLock = this.cacheTemplate.getLock(lockKey.concat("cardApiTicketLock"));
	}

	// ------------------------------------------------------------------------
	// token相关
	// ------------------------------------------------------------------------
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

	// ------------------------------------------------------------------------
	// ticket相关
	// ------------------------------------------------------------------------
	@Override
	public String getJsapiTicket() {
		return doGetTicket(TicketType.JSAPI);
	}

	@Override
	public boolean isJsapiTicketExpired() {
		return doIsTicketExpired(TicketType.JSAPI);
	}

	@Override
	public void expireJsapiTicket() {
		doExpireTicket(TicketType.JSAPI);
	}

	@Override
	public synchronized void updateJsapiTicket(String jsapiTicket, int expiresInSeconds) {
		doUpdateTicket(TicketType.JSAPI, jsapiTicket, expiresInSeconds);
	}

	@Override
	public String getCardApiTicket() {
		return doGetTicket(TicketType.WX_CARD);
	}

	@Override
	public boolean isCardApiTicketExpired() {
		return doIsTicketExpired(TicketType.WX_CARD);
	}

	@Override
	public void expireCardApiTicket() {
		doExpireTicket(TicketType.WX_CARD);
	}

	@Override
	public synchronized void updateCardApiTicket(String cardApiTicket, int expiresInSeconds) {
		doUpdateTicket(TicketType.WX_CARD, cardApiTicket, expiresInSeconds);
	}

	private String getTicketRedisKey(TicketType type) {
		return String.format(TICKET_KEY_TPL, this.keyPrefix, this.appid, type.getCode());
	}

	private String doGetTicket(TicketType type) {
		Object o = cacheTemplate.get(this.getTicketRedisKey(type));
		if (o == null) {
			return null;
		}
		return o.toString();
	}

	private boolean doIsTicketExpired(TicketType type) {
		Duration expire = cacheTemplate.getExpire(this.getTicketRedisKey(type));
		return expire == null || expire.toSeconds() < 2;
	}

	private void doUpdateTicket(TicketType type, String ticket, int expiresInSeconds) {
		cacheTemplate.set(this.getTicketRedisKey(type), ticket, Duration.ofSeconds(expiresInSeconds - 200));
	}

	private void doExpireTicket(TicketType type) {
		cacheTemplate.expire(this.getTicketRedisKey(type), Duration.ZERO);
	}

	@Override
	public String toString() {
		return "WxMaRedisBetterConfigImpl{" + "appid='" + appid + '\'' + ", token='" + token + '\'' + ", originalId='"
				+ originalId + '\'' + ", accessTokenLock=" + accessTokenLock + ", tmpDirFile=" + tmpDirFile
				+ ", jsapiTicketLock=" + jsapiTicketLock + ", cardApiTicketLock=" + cardApiTicketLock + ", redisOps="
				+ cacheTemplate + ", keyPrefix='" + keyPrefix + '\'' + ", accessTokenKey='" + accessTokenKey + '\''
				+ ", lockKey='" + lockKey + '\'' + '}';
	}

}
