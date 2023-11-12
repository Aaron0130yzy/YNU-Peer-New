package io.naccoll.boilerplate.pay.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.naccoll.boilerplate.core.validate.IsIdableEnum;
import io.naccoll.boilerplate.pay.enums.wx.WechatConfigType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * The type Create pay wechat config command.
 */
@Data
public class PayWechatConfigCreateCommand {

	@Schema(description = "配置名称")
	private String name;

	@Schema(description = "配置编码")
	private String code;

	/**
	 * 商户Id
	 */
	@Schema(description = "商户Id", hidden = true)
	private Long payMerchantId;

	/**
	 * 商户Id
	 */
	@Schema(description = "商户Id")
	private String mchid;

	/**
	 * 应用ID
	 */
	@Schema(description = "应用ID")
	private String appid;

	/**
	 * 商户密钥
	 */
	@Schema(description = "商户密钥")
	private String mchKey;

	/**
	 * 证书地址
	 */
	@Schema(description = "证书内容", hidden = true)
	@JsonIgnore
	private byte[] keyContent;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "配置类型1: 直连商户 2:服务商 3: 特约商户")
	private Integer configType;

	@Schema(description = "子应用ID")
	private String subAppid;

	@Schema(description = "子商户ID")
	@IsIdableEnum(support = WechatConfigType.class, message = "")
	private String subMchid;

}
