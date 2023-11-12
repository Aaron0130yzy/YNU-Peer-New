package io.naccoll.boilerplate.customer.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.naccoll.boilerplate.core.enums.EnumHelper;
import io.naccoll.boilerplate.core.persistence.model.JpaAuditable;
import io.naccoll.boilerplate.core.security.enums.ClientId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户
 *
 * @author NaccOll
 */
@Getter
@Setter
@Entity
@Table(name = "t_customer_user")
public class CustomerUserPo extends JpaAuditable implements Serializable {

	/**
	 * id
	 */
	@Id
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	@Schema(description = "id")
	private Long id;

	/**
	 * 用户名
	 */
	@Schema(description = "用户名")
	private String username;

	/**
	 * 密码
	 */
	@Schema(description = "密码")
	private String password;

	/**
	 * 联系方式
	 */
	@Schema(description = "联系方式")
	private String tel;

	/**
	 * 昵称
	 */
	@Schema(description = "昵称")
	private String nickname;

	/**
	 * 头像
	 */
	@Schema(description = "头像")
	private String avatar;

	/**
	 * 邮箱
	 */
	@Schema(description = "邮箱")
	private String email;

	/**
	 * 账户可用
	 */
	@Schema(description = "账户可用")
	private Boolean enabled;

	/**
	 * 凭据未过期
	 */
	@Schema(description = "凭据未过期")
	private Boolean credentialsNonExpired;

	/**
	 * 账户未锁定
	 */
	@Schema(description = "账户未锁定")
	private Boolean accountNonLocked;

	/**
	 * 账户未过期
	 */
	@Schema(description = "账户未过期")
	private Boolean accountNonExpired;

	/**
	 * 凭据过期时间
	 */
	@Schema(description = "凭据过期时间")
	private Date credentialsExpiredDate;

	/**
	 * 账户过期时间
	 */
	@Schema(description = "账户过期时间")
	private Date accountExpiredDate;

	/**
	 * 注册渠道
	 */
	@Schema(description = "注册渠道")
	private Integer registerChannel;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public String getRegisterChannelName() {
		if (registerChannel == null) {
			return null;
		}
		return Optional.ofNullable(registerChannel)
			.filter(i -> EnumHelper.isValid(ClientId.class, i))
			.map(ClientId::fromId)
			.map(ClientId::getName)
			.orElse("");
	}

}
