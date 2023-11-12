package io.naccoll.boilerplate.pay.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * The type Cancel order command.
 */
@Data
public class OrderCancelCommand {

	@Schema(description = "商户订单号")
	@NotEmpty
	@Size(min = 1, max = 32)
	private String orderNo;

	@Schema(description = "商户id", hidden = true)
	private Long payMerchantId;

}
