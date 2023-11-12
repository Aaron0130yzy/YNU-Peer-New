package io.naccoll.boilerplate.pay.dto;

import java.util.Map;

import com.egzosn.pay.common.bean.PayMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The type Pay back message.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class PayBackMessage extends PayMessage {

	private Long payMerchantId;

	private String pid;

	private String appid;

	/**
	 * Instantiates a new Pay back message.
	 * @param payMerchantId the pay merchant id
	 * @param pid the pid
	 * @param appid the appid
	 * @param payMessage the pay message
	 * @param payType the pay type
	 */
	public PayBackMessage(Long payMerchantId, String pid, String appid, Map<String, Object> payMessage,
			String payType) {
		super(payMessage, payType);
		this.payMerchantId = payMerchantId;
		this.pid = pid;
		this.appid = appid;
	}

}
