package io.naccoll.boilerplate.pay.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * The type Alipay notify.
 */
@Data
public class AlipayNotify {

	@JsonProperty("notify_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Schema(description = "通知的发送时间。格式为yyyy-MM-dd HH:mm:ss")
	private Date notifyTime;

	@JsonProperty("notify_type")
	@Schema(description = "通知的类型")
	private String notifyType;

	@JsonProperty("notify_id")
	@Schema(description = "通知校验ID")
	private String notifyId;

	@JsonProperty("app_id")
	@Schema(description = "支付宝分配给开发者的应用ID")
	private String appId;

	@Schema(description = "编码格式，如utf-8、gbk、gb2312等")
	private String charset;

	@Schema(description = "调用的接口版本，固定为：1")
	private String version;

	@JsonProperty("sign_type")
	@Schema(description = "商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2")
	private String signType;

	@Schema(description = "签名")
	private String sign;

	@JsonProperty("trade_no")
	@Schema(description = "支付宝交易凭证号")
	private String tradeNo;

	@JsonProperty("out_trade_no")
	@Schema(description = "原支付请求的商户订单号")
	private String outTradeNo;

	@JsonProperty("out_biz_no")
	@Schema(description = "商户业务ID，主要是退款通知中返回退款申请的流水号")
	private String outBizNo;

	@JsonProperty("buyer_id")
	@Schema(description = "买家支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字")
	private String buyerId;

	@JsonProperty("buyer_logon_id")
	@Schema(description = "买家支付宝账号")
	private String buyerLogonId;

	@JsonProperty("seller_id")
	@Schema(description = "卖家支付宝用户号")
	private String sellerId;

	@JsonProperty("seller_email")
	@Schema(description = "卖家支付宝账号")
	private String sellerEmail;

	@JsonProperty("trade_status")
	@Schema(description = "交易目前所处的状态。")
	private String tradeStatus;

	@JsonProperty("total_amount")
	@Schema(description = "本次交易支付的订单金额，单位为人民币（元）")
	private BigDecimal totalAmount;

	@JsonProperty("receipt_amount")
	@Schema(description = "商家在交易中实际收到的款项，单位为人民币（元）")
	private BigDecimal receiptAmount;

	@JsonProperty("invoice_amount")
	@Schema(description = "用户在交易中支付的可开发票的金额。")
	private BigDecimal invoiceAmount;

	@JsonProperty("buyer_pay_amount")
	@Schema(description = "用户在交易中支付的金额。")
	private BigDecimal buyerPayAmount;

	@JsonProperty("point_amount")
	@Schema(description = "使用集分宝支付的金额。")
	private BigDecimal pointAmount;

	@JsonProperty("refund_fee")
	@Schema(description = "退款通知中，返回总退款金额，单位为人民币（元），支持两位小数。")
	private BigDecimal refundFee;

	@Schema(description = "商品的标题/交易标题/订单标题/订单关键字等，是请求时对应的参数，原样通知回来。")
	private String subject;

	@Schema(description = "该订单的备注、描述、明细等。对应请求时的body参数，原样通知回来。")
	private String body;

	@JsonProperty("gmt_create")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Schema(description = "该笔交易创建的时间。格式为yyyy-MM-dd HH:mm:ss")
	private Date gmtCreate;

	@JsonProperty("gmt_payment")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Schema(description = "该笔交易的买家付款时间。格式为yyyy-MM-dd HH:mm:ss")
	private Date gmtPayment;

	@JsonProperty("gmt_refund")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.S", timezone = "GMT+8")
	@Schema(description = "该笔交易的退款时间。格式为yyyy-MM-dd HH:mm:ss.S")
	private Date gmtRefund;

	@JsonProperty("gmt_close")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Schema(description = "该笔交易结束时间。格式为yyyy-MM-dd HH:mm:ss")
	private Date gmtClose;

}
