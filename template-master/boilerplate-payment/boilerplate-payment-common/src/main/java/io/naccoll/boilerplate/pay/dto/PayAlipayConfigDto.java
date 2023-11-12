package io.naccoll.boilerplate.pay.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.naccoll.boilerplate.pay.model.PayAlipayConfigPo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The type Pay alipay config dto.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayAlipayConfigDto extends PayAlipayConfigPo {

	@JsonIgnore
	@Schema(description = "应用私钥")
	private byte[] appKey;

	@JsonIgnore
	@Schema(description = "应用公钥证书")
	private byte[] appCert;

	@JsonIgnore
	@Schema(description = "支付宝公钥证书")
	private byte[] alipayCert;

	@JsonIgnore
	@Schema(description = "支付宝根证书")
	private byte[] alipayRootCert;

}
