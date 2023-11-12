package io.naccoll.boilerplate.customer.model;

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
 * 微信小程序用户
 *
 * @author NaccOll
 */
@Getter
@Setter
@Entity
@Table(name = "t_customer_user_wx_miniapp")
public class CustomerUserWxMiniappPo extends JpaAuditable implements Serializable {

	/**
	 * id
	 */
	@Id
	@Schema(description = "id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long id;

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long customerUserId;

	/**
	 * 第三方应用Id
	 */
	@Schema(description = "第三方应用Id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long thirdId;

	/**
	 * 微信openid
	 */
	@Schema(description = "微信openid")
	private String openid;

	/**
	 * 微信UnionId
	 */
	@Schema(description = "微信UnionId")
	private String unionId;

	/**
	 * 上次授权的时间
	 */
	@Schema(description = "上次授权的时间")
	private Date lastAuthDate;

}
