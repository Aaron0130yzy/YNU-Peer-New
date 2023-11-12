package io.naccoll.boilerplate.customer.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 创建CustomerUser
 *
 * @author NaccOll
 */
@Data
public class CustomerUserCreateCommand {

	/**
	 * 用户名
	 */
	@Schema(description = "用户名")
	private String username = "";

	/**
	 * 密码
	 */
	@Schema(description = "密码")
	private String password = "";

	/**
	 * 联系方式
	 */
	@Schema(description = "联系方式")
	private String tel = "";

	/**
	 * 昵称
	 */
	@Schema(description = "昵称")
	private String nickname = "";

	/**
	 * 头像
	 */
	@Schema(description = "头像")
	private String avatar = "";

	/**
	 * 邮箱
	 */
	@Schema(description = "邮箱")
	private String email = "";

	/**
	 * 账户可用
	 */
	@Schema(description = "账户可用")
	private Boolean enabled = true;

	/**
	 * 凭据未过期
	 */
	@Schema(description = "凭据未过期")
	private Boolean credentialsNonExpired = true;

	/**
	 * 账户未锁定
	 */
	@Schema(description = "账户未锁定")
	private Boolean accountNonLocked = true;

	/**
	 * 账户未过期
	 */
	@Schema(description = "账户未过期")
	private Boolean accountNonExpired = true;

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
	 * @see io.naccoll.boilerplate.core.security.enums.ClientId
	 */
	@Schema(description = "注册渠道")
	private Integer registerChannel;

}
