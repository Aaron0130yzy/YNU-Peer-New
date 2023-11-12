package io.naccoll.boilerplate.customer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 修改CustomerUser
 *
 * @author NaccOll
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerUserUpdateCommand extends CustomerUserCreateCommand {

	@NotNull
	private Long id;

}
