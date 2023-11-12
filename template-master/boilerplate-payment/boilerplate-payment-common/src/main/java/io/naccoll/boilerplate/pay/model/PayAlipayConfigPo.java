package io.naccoll.boilerplate.pay.model;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.naccoll.boilerplate.core.persistence.model.JpaAuditable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付宝支付配置
 */
@Getter
@Setter
@Entity
@Table(name = "t_pay_alipay_config")
public class PayAlipayConfigPo extends JpaAuditable implements Serializable {

	@Serial
	private static final long serialVersionUID = 7764781396463981439L;

	/**
	 * null default value: null
	 */
	@Id
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	@Schema(description = "null")
	private Long id;

	@Schema(description = "配置名称")
	private String name;

	@Schema(description = "配置编码")
	private String code;

	/**
	 * 合作者id default value: null
	 */
	@Schema(description = "合作者id")
	private String partnerId;

	@Schema(description = "服务商id")
	private String providerId = "";

	/**
	 * 商户id default value: null
	 */
	@Schema(description = "商户id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long payMerchantId;

	/**
	 * 应用id default value: null
	 */
	@Schema(description = "应用id")
	private String appid;

	/**
	 * 应用私钥 default value: null
	 */
	@Schema(description = "应用私钥")
	private byte[] appKey;

	/**
	 * 应用公钥证书 default value: null
	 */
	@Schema(description = "应用公钥证书")
	private byte[] appCert;

	/**
	 * 支付宝公钥证书 default value: null
	 */
	@Schema(description = "支付宝公钥证书")
	private byte[] alipayCert;

	/**
	 * 支付宝根证书 default value: null
	 */
	@Schema(description = "支付宝根证书")
	private byte[] alipayRootCert;

	/**
	 * 签名方式，RSA2等 default value: null
	 */
	@Schema(description = "签名方式，RSA2等")
	private String signType;

	/**
	 * 收款账号 default value: null
	 */
	@Schema(description = "收款账号")
	private String seller;

	@Schema(description = "状态")
	private Integer status;

}
