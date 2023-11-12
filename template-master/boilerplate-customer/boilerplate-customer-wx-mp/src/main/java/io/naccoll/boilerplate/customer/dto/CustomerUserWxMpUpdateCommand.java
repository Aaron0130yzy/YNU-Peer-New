package io.naccoll.boilerplate.customer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 修改CustomerUserMp
 *
 * @author NaccOll
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerUserWxMpUpdateCommand extends CustomerUserWxMpCreateCommand {

	@NotNull
	private Long id;

}
