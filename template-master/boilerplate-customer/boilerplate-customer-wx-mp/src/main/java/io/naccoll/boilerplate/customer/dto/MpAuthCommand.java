package io.naccoll.boilerplate.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MpAuthCommand {

	@Schema(description = "微信授权的随机码，用于防止接口被恶意大量调用")
	private String state;

	@Schema(description = "微信授权码")
	private String code;

	@Schema(description = "第三方应用Id", hidden = true)
	private Long thirdId;

}
