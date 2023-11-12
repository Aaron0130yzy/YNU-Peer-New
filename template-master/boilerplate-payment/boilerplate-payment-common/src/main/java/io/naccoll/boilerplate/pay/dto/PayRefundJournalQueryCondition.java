package io.naccoll.boilerplate.pay.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * The type Pay refund journal query condition.
 */
@Data
public class PayRefundJournalQueryCondition {

	@Schema(description = "退款订单号")
	private String refundOrderNo;

	@Schema(description = "组织id")
	private Long organizationId;

	@Schema(description = "商户id")
	private Long payMerchantId;

	@Schema(description = "支付订单id")
	private Long payId;

	@Schema(description = "退款状态 0:未退款 1: 部分退款 2: 全额退款")
	private Integer refundStatus;

}
