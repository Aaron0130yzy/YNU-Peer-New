package io.naccoll.boilerplate.pay.dto;

import io.naccoll.boilerplate.pay.model.PayJournalPo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The type Pay journal simple dto.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayJournalSimpleDto extends PayJournalPo {

	@Schema(description = "组织名称")
	private String organizationName;

	@Schema(description = "部门名称")
	private String departName;

	@Schema(description = "商户名称")
	private String payMerchantName;

	@Schema(description = "支付配置名称")
	private String payWayConfigName;

}
