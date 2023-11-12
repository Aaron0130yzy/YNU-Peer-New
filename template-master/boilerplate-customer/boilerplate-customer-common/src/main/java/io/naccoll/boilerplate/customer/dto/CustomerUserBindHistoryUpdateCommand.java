package io.naccoll.boilerplate.customer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 修改CustomerUserBindHistory
 *
 * @author NaccOll
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerUserBindHistoryUpdateCommand extends CustomerUserBindHistoryCreateCommand {

	@NotNull
	private Long id;

}
