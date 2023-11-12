package io.naccoll.boilerplate.pay.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 支付流水详情
 *
 * @author NaccOll
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "支付流水详情")
public class PayJournalDetailDto extends PayJournalSimpleDto {

	@Schema(description = "微信渠道流水")
	private PayWechatJournalDto wechatJournal;

	@Schema(description = "支付宝渠道流水")
	private PayAlipayJournalDto alipayJournal;

	@Schema(description = "退款流水")
	private List<PayRefundJournalDto> refundJournals;

}
