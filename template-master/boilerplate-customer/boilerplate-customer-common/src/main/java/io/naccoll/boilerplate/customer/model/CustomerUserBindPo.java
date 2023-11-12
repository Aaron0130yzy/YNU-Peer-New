package io.naccoll.boilerplate.customer.model;

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
 * 用户绑定关系
 *
 * @author NaccOll
 */
@Getter
@Setter
@Entity
@Table(name = "t_customer_user_bind")
public class CustomerUserBindPo extends JpaAuditable implements Serializable {

	/**
	 * id
	 */
	@Id
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	@Schema(description = "id")
	private Long id;

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long customerUserId;

	/**
	 * 第三方ID
	 */
	@Schema(description = "第三方ID")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long thirdId;

	/**
	 * 第三方用户标识
	 */
	@Schema(description = "第三方用户标识")
	private String thirdUserId;

}
