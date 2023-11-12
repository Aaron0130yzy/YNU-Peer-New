package io.naccoll.boilerplate.customer.config;

import java.util.Arrays;

import io.naccoll.boilerplate.core.security.customizer.ApiSecurityConfigCustomizer;
import io.naccoll.boilerplate.core.security.properties.SecurityProperties;
import io.naccoll.boilerplate.customer.constant.CustomerApiConstant;
import io.naccoll.boilerplate.customer.security.CustomerAuthorizationManager;
import jakarta.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

/**
 * 系统管理 安全配置
 */
@Configuration
public class CustomerSecurityConfig implements InitializingBean, ApiSecurityConfigCustomizer {

	@Resource
	private SecurityProperties securityProperties;

	@Resource
	private CustomerAuthorizationManager customerAuthorizationManager;

	@Override
	public void customize(
			AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
		// @formatter:off
        registry
                .requestMatchers(getIgnoreToken()).permitAll()
                .requestMatchers(CustomerApiConstant.PlatformApiV1.PREFIX + "/**").fullyAuthenticated()
                .requestMatchers(CustomerApiConstant.UserApiV1.PREFIX + "/**")
				.access(customerAuthorizationManager);
        // @formatter:on
	}

	private String[] getIgnoreToken() {
		return new String[] { CustomerApiConstant.PublicApiV1.PREFIX + "/**" };
	}

	@Override
	public void afterPropertiesSet() {
		for (String path : getIgnoreToken()) {
			for (HttpMethod method : Arrays.asList(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT,
					HttpMethod.DELETE)) {
				securityProperties.getIgnoreTokenUrls().add(new SecurityProperties.IgnorePath(path, method));
			}
		}
	}

}
