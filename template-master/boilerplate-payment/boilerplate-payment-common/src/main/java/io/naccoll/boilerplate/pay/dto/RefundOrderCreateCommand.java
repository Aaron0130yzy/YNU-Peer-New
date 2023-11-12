package io.naccoll.boilerplate.pay.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * The type Create refund order command.
 */
@Data
public class RefundOrderCreateCommand {

	@NotEmpty
	@Size(min = 1, max = 32)
	@Schema(description = "支付订单号", required = true)
	private String orderNo;

	@NotNull
	@Schema(description = "支付中心商户Id", hidden = true)
	private Long payMerchantId;

	@Schema(description = "支付商户应用id")
	private Long payAppId = 0L;

	@Size(max = 32)
	@Schema(description = "退款订单号, 可以不传，由支付中心生成", required = true)
	private String refundOrderNo;

	@Size(max = 10)
	@Schema(description = "退款订单号前缀，当不传递orderNo时，建议传递，最长10位，若不传递则支付中心规则为准")
	private String refundOrderNoPrefix;

	@NotEmpty
	@Schema(description = "退款原因", required = true)
	private String refundReason;

	@NotNull
	@Min(value = 0)
	@Schema(description = "退款金额", required = true)
	private BigDecimal refundPrice;

	@Schema(description = "通知渠道")
	private String notifyType;

	@Schema(description = "终端设备号")
	private String deviceInfo = "";

}
