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
 * 支付商户
 */
@Schema(description = "支付商户")
@Getter
@Setter
@Table(name = "t_pay_merchant")
@Entity
public class PayMerchantPo extends JpaAuditable implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	@Schema(description = "")
	private Long id;

	/**
	 * 商户名称
	 */
	@Schema(description = "商户名称")
	private String name;

	@Schema(description = "密钥")
	private String secret;

	@Schema(description = "组织id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long organizationId;

	@Schema(description = "部门Id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long departId;

	@Schema(description = "验签算法")
	private Integer signAlgorithm;

}
