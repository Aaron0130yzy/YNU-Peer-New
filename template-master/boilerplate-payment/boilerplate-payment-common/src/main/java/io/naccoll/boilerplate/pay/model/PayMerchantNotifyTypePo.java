package io.naccoll.boilerplate.pay.model;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.naccoll.boilerplate.core.persistence.model.JpaAuditable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付配置
 */
@Getter
@Setter
@Table(name = "t_pay_merchant_notify_config")
@Entity
public class PayMerchantNotifyTypePo extends JpaAuditable implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	@Schema(description = "")
	private Long id;

	/**
	 * 商户Id
	 */
	@Schema(description = "商户Id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long payMerchantId;

	private String notifyType;

	private String payNotifyUrl;

	private String refundNotifyUrl;

}
