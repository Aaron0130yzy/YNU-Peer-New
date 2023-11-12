package io.naccoll.boilerplate.customer.model;

import java.io.Serializable;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.naccoll.boilerplate.core.enums.EnumHelper;
import io.naccoll.boilerplate.core.persistence.model.JpaAuditable;
import io.naccoll.boilerplate.core.security.enums.ClientId;
import io.naccoll.boilerplate.customer.enums.ThirdType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户绑定的第三方应用接口
 *
 * @author NaccOll
 */
@Getter
@Setter
@Entity
@Table(name = "t_customer_user_bind_third")
public class CustomerUserBindThirdPo extends JpaAuditable implements Serializable {

	/**
	 * id
	 */
	@Id
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	@Schema(description = "id")
	private Long id;

	/**
	 * 第三方类型
	 * @see io.naccoll.boilerplate.customer.enums.ThirdType
	 */
	@Schema(description = "第三方类型")
	private Integer thirdType;

	/**
	 * 第三方名称
	 */
	@Schema(description = "第三方名称")
	private String thirdName;

	/**
	 * 第三方应用的自身的编码
	 */
	@Schema(description = "第三方应用的自身的编码")
	private String thirdAppId;

	/**
	 * 允许登录
	 */
	@Schema(description = "允许登录")
	private Boolean allowLogin;

	/**
	 * 允许绑定
	 */
	@Schema(description = "允许绑定")
	private Boolean allowBind;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public String getThirdTypeName() {
		if (thirdType == null) {
			return "";
		}
		return Optional.of(thirdType)
			.filter(i -> EnumHelper.isValid(ClientId.class, i))
			.map(ThirdType::fromId)
			.map(ThirdType::getName)
			.orElse("");
	}

}
