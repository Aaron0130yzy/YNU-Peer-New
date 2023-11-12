package io.naccoll.boilerplate.pay.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 退款流水.
 *
 * @author NaccOll
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayRefundJournalDetailDto extends PayRefundJournalDto {

	@Schema(description = "微信退款流水")
	private PayWechatRefundJournalDto wechat;

	@Schema(description = "支付宝退款流水")
	private PayAlipayRefundJournalDto alipay;

}
