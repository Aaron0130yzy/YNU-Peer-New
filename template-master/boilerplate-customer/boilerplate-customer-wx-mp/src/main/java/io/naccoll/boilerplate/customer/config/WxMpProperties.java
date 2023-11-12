package io.naccoll.boilerplate.customer.config;

import java.util.Map;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wx.mp")
@Data
public class WxMpProperties {

	/**
	 * key也是appid，用于防重
	 */
	private Map<String, MpItem> configs;

	@Data
	public static class MpItem {

		/**
		 * 设置微信公众号的appid
		 */
		private String appId;

		/**
		 * 设置微信公众号的app secret
		 */
		private String secret;

		/**
		 * 设置微信公众号的token
		 */
		private String token;

		/**
		 * 设置微信公众号的EncodingAESKey
		 */
		private String aesKey;

	}

}
