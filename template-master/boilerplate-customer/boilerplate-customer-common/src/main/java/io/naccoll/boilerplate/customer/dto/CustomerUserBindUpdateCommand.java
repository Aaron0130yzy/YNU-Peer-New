package io.naccoll.boilerplate.customer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 修改CustomerUserBind
 *
 * @author NaccOll
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerUserBindUpdateCommand extends CustomerUserBindCreateCommand {

	@NotNull
	private Long id;

}
