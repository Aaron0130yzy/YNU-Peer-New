package io.naccoll.boilerplate.pay.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The type Update pay alipay config command.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayAlipayConfigUpdateCommand extends PayAlipayConfigCreateCommand {

	@Schema(description = "配置id")
	private Long id;

}
