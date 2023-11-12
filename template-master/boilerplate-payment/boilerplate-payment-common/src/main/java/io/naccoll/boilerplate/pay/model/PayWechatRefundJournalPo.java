package io.naccoll.boilerplate.pay.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.naccoll.boilerplate.core.persistence.model.JpaAuditable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * 微信退款流水
 */
@Schema(description = "微信退款流水")
@Entity
@Getter
@Setter
@Table(name = "t_pay_wechat_refund_journal")
public class PayWechatRefundJournalPo extends JpaAuditable implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	@Schema(description = "")
	private Long id;

	/**
	 * 由微信生成的应用ID，全局唯一
	 */
	@Schema(description = "由微信生成的应用ID，全局唯一")
	private String appid;

	/**
	 * 直连商户的商户号
	 */
	@Schema(description = "直连商户的商户号")
	private String mchid;

	/**
	 * 直连商户的子商户号
	 */
	@Schema(description = "直连商户的子商户号")
	private String subMchid;

	/**
	 * 由微信生成的子应用ID
	 */
	@Schema(description = "由微信生成的子应用ID")
	private String subAppid;

	@Schema(description = "发生交易使用的微信支付配置ID")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long wechatConfigId;

	/**
	 * 商户退款号
	 */
	@Schema(description = "商户退款号")
	private String outRefundNo;

	/**
	 * 商户订单号
	 */
	@Schema(description = "商户订单号")
	private String outTradeNo;

	/**
	 * 通知地址
	 */
	@Schema(description = "通知地址")
	private String notifyUrl;

	/**
	 * 总金额
	 */
	@Schema(description = "总金额")
	private Integer totalPrice;

	/**
	 * 退款金额
	 */
	@Schema(description = "退款金额")
	private Integer refundPrice;

	/**
	 * 错误描述
	 */
	@Schema(description = "错误描述")
	private String errCodeDes;

	/**
	 * 商户id
	 */
	@Schema(description = "商户id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long payMerchantId;

	/**
	 * 组织id
	 */
	@Schema(description = "组织id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long organizationId;

	@Schema(description = "科室id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long departId;

	/**
	 * 支付中心统一支付流水Id
	 */
	@Schema(description = "支付中心统一支付流水Id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long payJournalId;

	/**
	 * 支付中心统一退款流水id
	 */
	@Schema(description = "支付中心统一退款流水id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long refundJournalId;

	/**
	 * 微信支付订单号
	 */
	@Schema(description = "微信支付订单号")
	private String transactionId;

	/**
	 * 微信退款订单号
	 */
	@Schema(description = "微信退款订单号")
	private String refundId;

	/**
	 * 退款状态
	 */
	@Schema(description = "退款状态")
	private String status;

	@Schema(description = "退款成功时间")
	private Date successTime;

	@Schema(description = "支付中心门店id")
	private Long payStoreId = 0L;

	@Schema(description = "支付中心应用id")
	private Long payAppId = 0L;

	@Schema(description = "商户业务订单号")
	private String merchantBusinessNo = "";

}
