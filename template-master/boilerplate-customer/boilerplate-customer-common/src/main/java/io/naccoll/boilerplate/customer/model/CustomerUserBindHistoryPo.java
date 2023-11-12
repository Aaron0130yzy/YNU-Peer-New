package io.naccoll.boilerplate.customer.model;

import java.io.Serializable;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.naccoll.boilerplate.core.enums.EnumHelper;
import io.naccoll.boilerplate.core.persistence.model.JpaAuditable;
import io.naccoll.boilerplate.customer.enums.BindStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户绑定历史
 *
 * @author NaccOll
 */
@Getter
@Setter
@Entity
@Table(name = "t_customer_user_bind_history")
public class CustomerUserBindHistoryPo extends JpaAuditable implements Serializable {

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
	 * 第三方Id
	 */
	@Schema(description = "第三方Id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long thirdId;

	/**
	 * 第三方用户标识
	 */
	@Schema(description = "第三方用户标识")
	private String thirdUserId;

	/**
	 * 绑定状态 1: 绑定 2: 解绑
	 */
	@Schema(description = "绑定状态 1: 绑定 2: 解绑")
	private Integer bindStatus;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public String getBindStatusName() {
		if (bindStatus == null) {
			return "";
		}
		return Optional.of(bindStatus)
			.filter(i -> EnumHelper.isValid(BindStatus.class, i))
			.map(BindStatus::fromId)
			.map(BindStatus::getName)
			.orElse("");
	}

}
