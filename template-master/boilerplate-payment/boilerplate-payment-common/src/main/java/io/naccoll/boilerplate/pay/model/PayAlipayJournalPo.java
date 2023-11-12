package io.naccoll.boilerplate.pay.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.naccoll.boilerplate.core.persistence.model.JpaAuditable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 支付宝支付流水
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "t_pay_alipay_journal")
public class PayAlipayJournalPo extends JpaAuditable implements Serializable {

	@Serial
	private static final long serialVersionUID = 9195410127982068324L;

	/**
	 * null default value: null
	 */
	@Id
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	@Schema(description = "null")
	private Long id;

	/**
	 * 支付宝交易号 default value: null
	 */
	@Schema(description = "支付宝交易号")
	private String tradeNo;

	/**
	 * 商家订单号 default value: null
	 */
	@Schema(description = "商家订单号")
	private String outTradeNo;

	/**
	 * 买家支付宝账号 default value: null
	 */
	@Schema(description = "买家支付宝账号")
	private String buyerLogonId;

	/**
	 * 交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款）
	 * default value: null
	 */
	@Schema(description = "交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款）")
	private String tradeStatus;

	/**
	 * 交易的订单金额，单位为元，两位小数。该参数的值为支付时传入的total_amount default value: null
	 */
	@Schema(description = "交易的订单金额，单位为元，两位小数。该参数的值为支付时传入的total_amount")
	private java.math.BigDecimal totalAmount;

	/**
	 * 订单支付币种 default value: null
	 */
	@Schema(description = "订单支付币种")
	private String payCurrency;

	/**
	 * 买家实付金额 default value: null
	 */
	@Schema(description = "买家实付金额")
	private java.math.BigDecimal buyerPayAmount;

	/**
	 * 交易中用户可开具发票的金额 default value: null
	 */
	@Schema(description = "交易中用户可开具发票的金额")
	private java.math.BigDecimal invoiceAmount;

	/**
	 * 本次交易打款给卖家的时间 default value: null
	 */
	@Schema(description = "本次交易打款给卖家的时间(format:yyyy-MM-dd HH:mm:ss)")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private java.util.Date sendPayDate;

	/**
	 * 实收金额，单位为元，两位小数。该金额为本笔交易，商户账户能够实际收到的金额 default value: null
	 */
	@Schema(description = "实收金额，单位为元，两位小数。该金额为本笔交易，商户账户能够实际收到的金额")
	private java.math.BigDecimal receiptAmount;

	/**
	 * 商户门店编号 default value: null
	 */
	@Schema(description = "商户门店编号")
	private String storeId;

	/**
	 * 请求交易支付中的商户店铺的名称 default value: null
	 */
	@Schema(description = "请求交易支付中的商户店铺的名称")
	private String storeName;

	/**
	 * 买家在支付宝的用户id default value: null
	 */
	@Schema(description = "买家在支付宝的用户id")
	private String buyerUserId;

	/**
	 * 订单标题； default value: null
	 */
	@Schema(description = "订单标题；")
	private String subject;

	/**
	 * 订单描述; default value: null
	 */
	@Schema(description = "订单描述;")
	private String body;

	/**
	 * 间连商户在支付宝端的商户编号； default value: null
	 */
	@Schema(description = "间连商户在支付宝端的商户编号；")
	private String subMerchantId;

	/**
	 * null default value: null
	 */
	@Schema(description = "null")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long payMerchantId;

	/**
	 * null default value: null
	 */
	@Schema(description = "null")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long organizationId;

	@Schema(description = "科室id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long departId;

	/**
	 * 聚合支付订单id default value: null
	 */
	@Schema(description = "聚合支付订单id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long payJournalId;

	/**
	 * 支付状态，-1-支付失败，0-支付中，2-已支付 default value: null
	 */
	@Schema(description = "支付状态，-1-支付失败，0-支付中，2-已支付")
	private Integer status;

	/**
	 * 异常信息 default value: null
	 */
	@Schema(description = "异常信息")
	private String errMsg;

	@Schema(description = "交易类型")
	private String tradeType;

	@Schema(description = "扫码支付二维码地址")
	private String payUrl;

	@Schema(description = "合作伙伴身份PID")
	private String partnerId;

	@Schema(description = "服务商id")
	private String providerId = "";

	@Schema(description = "应用id")
	private String appid;

	@Schema(description = "支付完成时间")
	private Date successTime;

	@Schema(description = "支付宝配置id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long alipayConfigId;

	@Schema(description = "支付中心门店id")
	private Long payStoreId = 0L;

	@Schema(description = "支付中心应用id")
	private Long payAppId = 0L;

	@Schema(description = "商户业务订单号")
	private String merchantOrderNo = "";

	@Schema(description = "操作员编号")
	private String operatorId = "";

	@Schema(description = "商户机具终端编号")
	private String terminalId = "";

}
