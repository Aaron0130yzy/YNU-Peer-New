package io.naccoll.boilerplate.customer.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

/**
 * CustomerUserBindHistory查询条件
 *
 * @author NaccOll
 */
@Data
public class CustomerUserBindHistoryQueryCondition {

	@Parameter(description = "用户Id")
	private Long customerUserId;

}
