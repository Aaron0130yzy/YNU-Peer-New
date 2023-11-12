package io.naccoll.boilerplate.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建CustomerUserBindHistory
 *
 * @author NaccOll
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerUserBindHistoryCreateCommand {

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
	 * 绑定状态 1: 绑定 2: 解绑
	 */
	@Schema(description = "绑定状态 1: 绑定 2: 解绑")
	private Integer bindStatus;

}
