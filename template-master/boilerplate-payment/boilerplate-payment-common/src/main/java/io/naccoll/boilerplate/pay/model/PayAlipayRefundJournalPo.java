package io.naccoll.boilerplate.pay.model;

import java.io.Serial;
import java.io.Serializable;

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
 * 支付宝退款流水
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "t_pay_alipay_refund_journal")
public class PayAlipayRefundJournalPo extends JpaAuditable implements Serializable {

	@Serial
	private static final long serialVersionUID = 5878454539094965280L;

	@Id
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	@Schema(description = "null")
	private Long id;

	/**
	 * 应用id default value: null
	 */
	@Schema(description = "应用id")
	private String appid;

	/**
	 * 合作者id default value: null
	 */
	@Schema(description = "合作者id")
	private String partnerId;

	/**
	 * 商户id default value: null
	 */
	@Schema(description = "商户id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long payMerchantId;

	/**
	 * 间连商户在支付宝端的商户编号 default value: null
	 */
	@Schema(description = "间连商户在支付宝端的商户编号")
	private String subMerchantId;

	/**
	 * 退款流水号 default value: null
	 */
	@Schema(description = "退款流水号")
	private String outRefundNo;

	/**
	 * 支付宝交易号 default value: null
	 */
	@Schema(description = "支付宝交易号")
	private String tradeNo;

	/**
	 * 创建交易传入的商户订单号 default value: null
	 */
	@Schema(description = "创建交易传入的商户订单号")
	private String outTradeNo;

	/**
	 * 本笔退款对应的退款请求号 default value: null
	 */
	@Schema(description = "本笔退款对应的退款请求号")
	private String outRequestNo;

	/**
	 * 发起退款时，传入的退款原因 default value: null
	 */
	@Schema(description = "发起退款时，传入的退款原因")
	private String refundReason;

	/**
	 * 该笔退款所对应的交易的订单金额 default value: null
	 */
	@Schema(description = "该笔退款所对应的交易的订单金额")
	private java.math.BigDecimal totalAmount;

	/**
	 * 本次退款请求，对应的退款金额 default value: null
	 */
	@Schema(description = "本次退款请求，对应的退款金额")
	private java.math.BigDecimal refundAmount;

	/**
	 * 退款状态 default value: null
	 */
	@Schema(description = "退款状态")
	private String status;

	/**
	 * 退款时间 default value: null
	 */
	@Schema(description = "退款时间(format:yyyy-MM-dd HH:mm:ss)")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private java.util.Date successTime;

	/**
	 * 异常信息 default value: null
	 */
	@Schema(description = "异常信息")
	private String errMsg;

	/**
	 * 组织id default value: null
	 */
	@Schema(description = "组织id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long organizationId;

	@Schema(description = "科室id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long departId;

	/**
	 * 聚合支付订单id default value: null
	 */
	@Schema(description = "支付中心统一支付流水Id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long payJournalId;

	/**
	 * 聚合退款订单id default value: null
	 */
	@Schema(description = "支付中心统一退款流水id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long refundJournalId;

	/**
	 * 通知地址 default value: null
	 */
	@Schema(description = "通知地址")
	private String notifyUrl;

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
	private String operatorId;

	@Schema(description = "商户机具终端编号")
	private String terminalId;

}
