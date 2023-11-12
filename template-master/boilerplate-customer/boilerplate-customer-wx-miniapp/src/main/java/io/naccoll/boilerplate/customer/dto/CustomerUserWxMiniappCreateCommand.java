package io.naccoll.boilerplate.customer.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 微信小程序用户创建对象
 *
 * @author NaccOll
 */
@Data
public class CustomerUserWxMiniappCreateCommand {

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	private Long customerUserId;

	/**
	 * 第三方应用Id
	 */
	@Schema(description = "第三方应用Id")
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
