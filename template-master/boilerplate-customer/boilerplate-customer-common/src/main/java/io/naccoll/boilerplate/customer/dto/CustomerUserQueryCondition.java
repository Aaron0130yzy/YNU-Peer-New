package io.naccoll.boilerplate.customer.dto;

import io.naccoll.boilerplate.core.validate.Phone;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Email;
import lombok.Data;

/**
 * CustomerUser查询条件
 *
 * @author NaccOll
 */
@Data
public class CustomerUserQueryCondition {

	@Parameter(description = "用户名", example = "username")
	private String username;

	@Phone
	@Parameter(description = "手机号", example = "13888888888")
	private String tel;

	@Email
	@Parameter(description = "邮箱", example = "example@example.com")
	private String email;

}
