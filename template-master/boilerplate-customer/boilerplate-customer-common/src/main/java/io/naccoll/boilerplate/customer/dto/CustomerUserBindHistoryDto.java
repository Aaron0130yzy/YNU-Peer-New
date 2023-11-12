package io.naccoll.boilerplate.customer.dto;

import io.naccoll.boilerplate.customer.model.CustomerUserBindHistoryPo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author NaccOll
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerUserBindHistoryDto extends CustomerUserBindHistoryPo {

	@Schema(description = "第三方名称")
	private String thirdName;

	@Schema(description = "第三方类型")
	private String thirdTypeName;

}
