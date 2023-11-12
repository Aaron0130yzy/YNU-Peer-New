package io.naccoll.boilerplate.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MpMiniappAuthCommand {

	@Schema(description = "微信小程序授权码")
	private String code;

	@Schema(description = "第三方Id", hidden = true)
	private Long thirdId;

}
