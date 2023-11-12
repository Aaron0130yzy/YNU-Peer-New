package io.naccoll.boilerplate.pay.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * The type Create pay alipay config command.
 */
@Data
public class PayAlipayConfigCreateCommand {

	@Schema(description = "配置名称")
	private String name;

	@Schema(description = "配置编码")
	private String code;

	@Schema(description = "合作者id")
	private String partnerId;

	@Schema(description = "商户id")
	private Long payMerchantId;

	@Schema(description = "应用id")
	private String appid;

	@JsonIgnore
	@Schema(description = "应用私钥", hidden = true)
	private byte[] appKeyByteArr;

	@JsonIgnore
	@Schema(description = "应用公钥证书", hidden = true)
	private byte[] appCertByteArr;

	@JsonIgnore
	@Schema(description = "支付宝公钥证书", hidden = true)
	private byte[] alipayCertByteArr;

	@JsonIgnore
	@Schema(description = "支付宝根证书", hidden = true)
	private byte[] alipayRootCertByteArr;

	@Schema(description = "签名方式，RSA2等")
	private String signType = "RSA2";

	@Schema(description = "收款账号")
	private String seller;

	@Schema(description = "状态")
	private Integer status;

}
