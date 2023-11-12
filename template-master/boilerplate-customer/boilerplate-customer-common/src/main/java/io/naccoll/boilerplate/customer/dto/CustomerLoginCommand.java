package io.naccoll.boilerplate.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CustomerLoginCommand {

	@Schema(description = "手机号")
	private String tel;

	@Schema(description = "验证码")
	private String code;

	@Schema(description = "客户端Id，MP_H5")
	private String clientId;

}
