package io.naccoll.boilerplate.pay.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Alipay trade pay response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlipayTradePayResponse {

	private String msg;

	private String code;

	@JsonProperty("sub_msg")
	private String subMsg;

	@JsonProperty("sub_code")
	private String subCode;

	@JsonProperty("buyer_user_id")
	private String buyerUserId;

	@JsonProperty("invoiceAmount")
	private BigDecimal invoiceAmount;

	@JsonProperty("out_trade_no")
	private String outTradeNo;

	/**
	 * 交易的订单金额，单位为元，两位小数。该参数的值为支付时传入的total_amount
	 */
	@JsonProperty("total_amount")
	private BigDecimal totalAmount;

	@JsonProperty("trade_no")
	private String tradeNo;

	/**
	 * 交易状态： WAIT_BUYER_PAY（交易创建，等待买家付款）、 TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
	 * TRADE_SUCCESS（交易支付成功）、 TRADE_FINISHED（交易结束，不可退款）
	 */
	@JsonProperty("trade_status")
	private String tradeStatus;

	@JsonProperty("buyer_logon_id")
	private String buyerLogonId;

	@JsonProperty("receipt_amount")
	private BigDecimal receiptAmount;

	@JsonProperty("point_amount")
	private BigDecimal pointAmount;

	@JsonProperty("buyer_pay_amount")
	private BigDecimal buyerPayAmount;

}
