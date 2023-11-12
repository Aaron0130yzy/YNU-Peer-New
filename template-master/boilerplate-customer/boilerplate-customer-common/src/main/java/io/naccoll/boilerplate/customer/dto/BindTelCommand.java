package io.naccoll.boilerplate.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BindTelCommand {

	@Schema(description = "手机号")
	private String tel;

	@Schema(description = "验证码")
	private String code;

}
