package io.naccoll.boilerplate.customer.config;

import java.util.Map;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wx.miniapp")
@Data
public class WxMiniappProperties {

	/**
	 * key也是appid，用于防重
	 */
	private Map<String, MiniappItem> configs;

	@Data
	public static class MiniappItem {

		/**
		 * 设置微信小程序的appid
		 */
		private String appId;

		/**
		 * 设置微信小程序的app secret
		 */
		private String secret;

		/**
		 * 设置微信小程序的token
		 */
		private String token;

		/**
		 * 设置微信小程序的EncodingAESKey
		 */
		private String aesKey;

	}

}
