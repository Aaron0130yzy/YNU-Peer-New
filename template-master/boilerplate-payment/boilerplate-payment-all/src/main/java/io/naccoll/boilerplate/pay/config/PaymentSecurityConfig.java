package io.naccoll.boilerplate.pay.config;

import java.util.Arrays;

import io.naccoll.boilerplate.core.security.customizer.ApiSecurityConfigCustomizer;
import io.naccoll.boilerplate.core.security.properties.SecurityProperties;
import io.naccoll.boilerplate.pay.constant.PayApiConstant;
import jakarta.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * 支付 安全配置
 */
@Configuration
public class PaymentSecurityConfig implements InitializingBean, ApiSecurityConfigCustomizer {

	@Resource
	private SecurityProperties securityProperties;

	@Override
	public void customize(
			AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
		// @formatter:off
		registry
			.requestMatchers(getIgnoreToken()).permitAll()
			.requestMatchers(PayApiConstant.PlatformApiV1.PREFIX+"/**").fullyAuthenticated();
		// @formatter:on
	}

	private String[] getIgnoreToken() {
		return new String[] { PayApiConstant.CallbackV1.PREFIX + "/**", PayApiConstant.OpenapiApiV1.PREFIX + "/**" };
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (String path : getIgnoreToken()) {
			for (HttpMethod method : Arrays.asList(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT,
					HttpMethod.DELETE)) {
				securityProperties.getIgnoreTokenUrls().add(new SecurityProperties.IgnorePath(path, method));
			}
		}
	}

}
