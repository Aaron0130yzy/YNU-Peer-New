package io.naccoll.boilerplate.customer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 修改CustomerUserBindThird
 *
 * @author NaccOll
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerUserBindThirdUpdateCommand extends CustomerUserBindThirdCreateCommand {

	@NotNull
	private Long id;

}
