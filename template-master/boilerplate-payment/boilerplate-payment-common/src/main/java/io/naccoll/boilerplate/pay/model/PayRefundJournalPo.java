package io.naccoll.boilerplate.pay.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.naccoll.boilerplate.core.enums.EnumHelper;
import io.naccoll.boilerplate.core.persistence.model.JpaAuditable;
import io.naccoll.boilerplate.pay.enums.PayWay;
import io.naccoll.boilerplate.pay.enums.RefundStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * 统一支付流水
 */
@Schema(description = "统一支付流水")
@Getter
@Setter
@Table(name = "t_pay_refund_journal")
@Entity
public class PayRefundJournalPo extends JpaAuditable implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	@Schema(description = "")
	private Long id;

	/**
	 * 订单号
	 */
	@Schema(description = "订单号")
	private String orderNo;

	@Schema(description = "终端设备号")
	private String deviceInfo = "";

	/**
	 * 订单号
	 */
	@Schema(description = "退款订单号")
	private String refundOrderNo;

	@Schema(description = "组织id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long organizationId;

	@Schema(description = "科室id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long departId;

	/**
	 * 商户id
	 */
	@Schema(description = "商户id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long payMerchantId;

	@Schema(description = "支付中心门店id")
	private Long payStoreId = 0L;

	@Schema(description = "支付中心应用id")
	private Long payAppId = 0L;

	@Schema(description = "商户业务订单号")
	private String merchantBusinessNo = "";

	@Schema(description = "第三方支付流水号")
	private String thirdPayNo = "";

	@Schema(description = "第三方退款流水号")
	private String thirdRefundNo = "";

	/**
	 * 支付金额
	 */
	@Schema(description = "退款金额")
	private BigDecimal refundPrice;

	@Schema(description = "通知类型")
	private String notifyType;

	@Schema(description = "退款原因")
	private String refundReason;

	@Schema(description = "退款状态 0:退款中 1: 退款成功 -1: 退款失败")
	private Integer refundStatus;

	@Schema(description = "失败原因")
	private String errorRemark;

	@Schema(description = "支付订单id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long payJournalId;

	@Schema(description = "退款成功时间")
	private Date successTime;

	@Schema(description = "支付渠道类型1:微信 2:支付宝")
	private Integer payWay;

	@Schema(description = "支付渠道配置id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long payWayConfigId;

	/**
	 * Gets refund status name.
	 * @return the refund status name
	 */
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public String getRefundStatusName() {
		if (refundStatus == null) {
			return "";
		}
		return Optional.ofNullable(this.refundStatus)
			.filter(i -> EnumHelper.isValid(RefundStatus.class, i))
			.map(i -> RefundStatus.fromId(i))
			.map(i -> i.getName())
			.orElse("");
	}

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String getPayWayName() {
		if (payWay == null) {
			return "";
		}
		return Optional.ofNullable(this.payWay)
			.filter(i -> EnumHelper.isValid(PayWay.class, i))
			.map(i -> PayWay.fromId(i))
			.map(i -> i.getName())
			.orElse("");
	}

}
