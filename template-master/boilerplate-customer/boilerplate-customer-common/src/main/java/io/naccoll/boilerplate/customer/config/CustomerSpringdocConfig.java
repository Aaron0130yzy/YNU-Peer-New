package io.naccoll.boilerplate.customer.config;

import io.naccoll.boilerplate.core.openapi.springdoc.AbstractWebMvcSpringdocConfig;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * C端用户 API文档
 */
@Configuration
public class CustomerSpringdocConfig extends AbstractWebMvcSpringdocConfig {

	/**
	 * @return C端用户管理接口
	 */
	@Bean
	public GroupedOpenApi customerPlatformApiDoc() {
		return GroupedOpenApi.builder()
			.group("C端用户-平台")
			.packagesToScan("io.naccoll.boilerplate.customer.interfaces.api.platform")
			.addOpenApiCustomizer(jwtHeaderOpenApiCustomiser())
			.build();
	}

	/**
	 * @return C端用户需授权接口文档
	 */
	@Bean
	public GroupedOpenApi customerUserApiDoc() {
		return GroupedOpenApi.builder()
			.group("C端用户-用户")
			.packagesToScan("io.naccoll.boilerplate.customer.interfaces.api.user")
			.addOpenApiCustomizer(jwtHeaderOpenApiCustomiser())
			.build();
	}

	/**
	 * @return C端用户公开接口文档
	 */
	@Bean
	public GroupedOpenApi customerPublicApiDoc() {
		return GroupedOpenApi.builder()
			.group("C端用户-公开")
			.packagesToScan("io.naccoll.boilerplate.customer.interfaces.api.pub")
			.build();
	}

}
