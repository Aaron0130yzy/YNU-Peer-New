package io.naccoll.boilerplate.customer.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

/**
 * CustomerUserBind查询条件
 *
 * @author NaccOll
 */
@Data
public class CustomerUserBindQueryCondition {

	@Parameter(description = "用户Id")
	private Long customerUserId;

}
