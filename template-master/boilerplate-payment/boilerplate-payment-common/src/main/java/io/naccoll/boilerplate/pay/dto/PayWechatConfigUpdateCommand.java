package io.naccoll.boilerplate.pay.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The type Update pay wechat config command.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayWechatConfigUpdateCommand extends PayWechatConfigCreateCommand {

	@Schema(description = "配置id")
	private Long id;

}
