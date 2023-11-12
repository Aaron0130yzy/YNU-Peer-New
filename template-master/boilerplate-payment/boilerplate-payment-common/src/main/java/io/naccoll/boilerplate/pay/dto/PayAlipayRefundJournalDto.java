package io.naccoll.boilerplate.pay.dto;

import io.naccoll.boilerplate.pay.model.PayWechatRefundJournalPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付宝退款流水响应
 *
 * @author NaccOll
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayAlipayRefundJournalDto extends PayWechatRefundJournalPo {

}
