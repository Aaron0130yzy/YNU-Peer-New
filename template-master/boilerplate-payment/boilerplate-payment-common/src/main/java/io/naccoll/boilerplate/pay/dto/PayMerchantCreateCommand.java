package io.naccoll.boilerplate.pay.dto;

import io.naccoll.boilerplate.core.validate.IsIdableEnum;
import io.naccoll.boilerplate.pay.enums.PayMerchantSignAlgorithm;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * The type Create pay merchant command.
 */
@Data
public class PayMerchantCreateCommand {

	@Schema(description = "商户名称")
	@NotBlank(message = "商户名称不允许为空")
	private String name;

	@Schema(description = "组织id")
	@NotNull(message = "创建商户必须有组织")
	private Long organizationId;

	@Schema(description = "科室Id")
	@NotNull(message = "创建商户必须有科室")
	private Long departId = 0L;

	@Schema(description = "签名算法")
	@IsIdableEnum(support = PayMerchantSignAlgorithm.class)
	private Integer signAlgorithm = 3;

}
