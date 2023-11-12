package io.naccoll.boilerplate.pay.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * The type Pay journal query condition.
 */
@Data
public class PayJournalQueryCondition {

	@Schema(description = "订单号")
	private String orderNo;

	@Schema(description = "组织id")
	private Long organizationId;

	@Schema(description = "商户id")
	private Long payMerchantId;

	@Schema(description = "支付渠道")
	private Integer channel;

	@Schema(description = "支付状态 0:待支付 1: 已支付 -1: 已撤单 -2: 支付失败")
	private Integer payStatus;

	@Schema(description = "退款状态 0:未退款 1: 部分退款 2: 全额退款")
	private Integer refundStatus;

}
