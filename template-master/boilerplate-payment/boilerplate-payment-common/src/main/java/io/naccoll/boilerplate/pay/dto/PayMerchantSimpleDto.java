package io.naccoll.boilerplate.pay.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.naccoll.boilerplate.pay.model.PayMerchantPo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The type Pay merchant simple dto.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayMerchantSimpleDto extends PayMerchantPo {

	@Schema(description = "组织名称")
	private String organizationName;

	@Schema(description = "科室名称")
	private String departName;

	@JsonIgnore
	private String secret;

	private Integer wechatConfigCount;

}
