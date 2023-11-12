package io.naccoll.boilerplate.pay.config;

import io.naccoll.boilerplate.core.openapi.springdoc.AbstractWebMvcSpringdocConfig;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Payment springdoc config.
 */
@Configuration
public class PaymentSpringdocConfig extends AbstractWebMvcSpringdocConfig {

	/**
	 * Pay platform api doc grouped open api.
	 * @return the grouped open api
	 */
	@Bean
	public GroupedOpenApi payPlatformApiDoc() {
		return GroupedOpenApi.builder()
			.group("支付-平台")
			.packagesToScan("io.naccoll.boilerplate.pay.interfaces.api.platform")
			.addOpenApiCustomizer(jwtHeaderOpenApiCustomiser())
			.build();
	}

	/**
	 * Pay open api doc grouped open api.
	 * @return the grouped open api
	 */
	@Bean
	public GroupedOpenApi payOpenApiDoc() {
		return GroupedOpenApi.builder()
			.group("支付-OpenApi")
			.packagesToScan("io.naccoll.boilerplate.pay.interfaces.api.openapi")
			.addOpenApiCustomizer(jwtHeaderOpenApiCustomiser())
			.build();
	}

}
