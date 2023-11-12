package io.naccoll.boilerplate.pay.dto;

import java.util.List;

import io.naccoll.boilerplate.pay.model.PayJournalPo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The type Pay journal dto.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayJournalDto extends PayJournalPo {

	@Schema(description = "退款流水")
	private List<PayRefundJournalDto> refundJournals;

}
