package io.naccoll.boilerplate.customer.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 创建CustomerUserBindThird
 *
 * @author NaccOll
 */
@Data
public class CustomerUserBindThirdCreateCommand {

	/**
	 * 第三方类型
	 */
	@Schema(description = "第三方类型")
	private Integer thirdType;

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
