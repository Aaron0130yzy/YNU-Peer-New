package io.naccoll.boilerplate.pay.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * The type Pay merchant query condition.
 */
@Data
public class PayMerchantQueryCondition {

	@Schema(description = "组织id")
	private Long organizationId;

	@Schema(description = "商户名称")
	private String name;

}
