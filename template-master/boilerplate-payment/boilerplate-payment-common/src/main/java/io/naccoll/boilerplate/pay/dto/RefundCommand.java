package io.naccoll.boilerplate.pay.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * The type Refund command.
 */
@Data
public class RefundCommand {

	@Schema(description = "支付订单Id")
	private Long id;

	@Schema(description = "退款原因")
	private String reason;

	@Schema(description = "退款金额")
	private BigDecimal refundPrice;

	@Schema(description = "终端设备号")
	private String deviceInfo = "";

}
