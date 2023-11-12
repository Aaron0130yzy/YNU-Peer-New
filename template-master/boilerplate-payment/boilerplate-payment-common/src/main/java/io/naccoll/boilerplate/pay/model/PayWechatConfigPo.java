package io.naccoll.boilerplate.pay.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.naccoll.boilerplate.core.enums.EnumHelper;
import io.naccoll.boilerplate.core.persistence.model.JpaAuditable;
import io.naccoll.boilerplate.pay.enums.wx.WechatConfigType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付配置
 */
@Schema(description = "支付配置")
@Getter
@Setter
@Table(name = "t_pay_wechat_config")
@Entity
public class PayWechatConfigPo extends JpaAuditable implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	@Schema(description = "")
	private Long id;

	@Schema(description = "配置名称")
	private String name;

	@Schema(description = "配置编码")
	private String code;

	/**
	 * 商户Id
	 */
	@Schema(description = "商户Id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
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
	@Schema(description = "证书内容")
	private byte[] keyContent;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "配置类型1: 直连商户 2:服务商 3: 特约商户")
	private Integer configType;

	@Schema(description = "子应用ID")
	private String subAppid;

	@Schema(description = "子商户ID")
	private String subMchid;

	/**
	 * Gets config type name.
	 * @return the config type name
	 */
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public String getConfigTypeName() {
		return Optional.ofNullable(configType)
			.filter(i -> EnumHelper.isValid(WechatConfigType.class, i))
			.map(WechatConfigType::fromId)
			.map(WechatConfigType::getName)
			.orElse("");
	}

}
