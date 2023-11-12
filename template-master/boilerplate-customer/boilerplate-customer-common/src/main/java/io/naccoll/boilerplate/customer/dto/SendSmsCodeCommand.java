package io.naccoll.boilerplate.customer.dto;

import io.naccoll.boilerplate.core.validate.Phone;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SendSmsCodeCommand {

	@Schema(description = "手机号")
	@Phone
	private String tel;

}
