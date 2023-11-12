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
import io.naccoll.boilerplate.pay.enums.PayChannel;
import io.naccoll.boilerplate.pay.enums.PayRefundStatus;
import io.naccoll.boilerplate.pay.enums.PayStatus;
import io.naccoll.boilerplate.pay.enums.PayWay;
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
@Table(name = "t_pay_journal")
@Entity
public class PayJournalPo extends JpaAuditable implements Serializable {

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

	/**
	 * 支付金额
	 */
	@Schema(description = "支付金额")
	private BigDecimal payPrice;

	@Schema(description = "退款冻结金额")
	private BigDecimal refundFreezePrice;

	@Schema(description = "退款金额")
	private BigDecimal refundPrice;

	/**
	 * 支付渠道
	 */
	@Schema(description = "支付渠道")
	private Integer channel;

	@Schema(description = "支付渠道类型1:微信 2:支付宝")
	private Integer payWay;

	@Schema(description = "支付渠道配置id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long payWayConfigId;

	@Schema(description = "通知类型")
	private String notifyType;

	@Schema(description = "支付状态 0:待支付 1: 已支付 -1: 已撤单 -2: 支付失败")
	private Integer payStatus;

	@Schema(description = "退款状态 0:未退款 1: 部分退款 2: 全额退款")
	private Integer refundStatus;

	@Schema(description = "支付中心门店id")
	private Long payStoreId = 0L;

	@Schema(description = "支付中心应用id")
	private Long payAppId = 0L;

	@Schema(description = "商户业务订单号")
	private String merchantBusinessNo = "";

	@Schema(description = "第三方支付流水号")
	private String thirdPayNo = "";

	@Schema(description = "支付完成时间")
	private Date successTime;

	/**
	 * Gets pay status name.
	 * @return the pay status name
	 */
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public String getPayStatusName() {
		if (payStatus == null) {
			return "";
		}
		return Optional.ofNullable(this.payStatus)
			.filter(i -> EnumHelper.isValid(PayStatus.class, i))
			.map(i -> PayStatus.fromId(i))
			.map(i -> i.getName())
			.orElse("");
	}

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
			.filter(i -> EnumHelper.isValid(PayRefundStatus.class, i))
			.map(i -> PayRefundStatus.fromId(i))
			.map(i -> i.getName())
			.orElse("");
	}

	/**
	 * Gets channel name.
	 * @return the channel name
	 */
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public String getChannelName() {
		if (channel == null) {
			return "";
		}
		return Optional.ofNullable(this.channel)
			.filter(i -> EnumHelper.isValid(PayChannel.class, i))
			.map(i -> PayChannel.fromId(i))
			.map(i -> i.getName())
			.orElse("");
	}

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public String getPayWayName() {
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
