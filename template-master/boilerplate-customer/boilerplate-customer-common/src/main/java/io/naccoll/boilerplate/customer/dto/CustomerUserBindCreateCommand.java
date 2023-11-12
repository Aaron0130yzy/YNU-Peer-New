package io.naccoll.boilerplate.customer.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 创建CustomerUserBind
 *
 * @author NaccOll
 */
@Data
public class CustomerUserBindCreateCommand {

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	private Long customerUserId;

	/**
	 * 第三方Id
	 */
	@Schema(description = "第三方Id")
	private Long thirdId;

	/**
	 * 第三方用户标识
	 */
	@Schema(description = "第三方用户标识")
	private String thirdUserId;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createdDate;

	/**
	 * 最后更新时间
	 */
	@Schema(description = "最后更新时间")
	private Date lastModifiedDate;

}
