package io.naccoll.boilerplate.customer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 修改微信小程序用户
 *
 * @author NaccOll
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerUserWxMiniappUpdateCommand extends CustomerUserWxMiniappCreateCommand {

	@NotNull
	private Long id;

}
