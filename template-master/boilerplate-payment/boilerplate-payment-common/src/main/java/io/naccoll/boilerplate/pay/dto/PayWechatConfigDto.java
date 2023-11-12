package io.naccoll.boilerplate.pay.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.naccoll.boilerplate.pay.model.PayWechatConfigPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The type Pay wechat config dto.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayWechatConfigDto extends PayWechatConfigPo {

	@JsonIgnore
	private String mchKey;

	@JsonIgnore
	private byte[] keyContent;

}
