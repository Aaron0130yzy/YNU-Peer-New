package io.naccoll.boilerplate.pay.interfaces.api.platform;

import java.util.List;

import io.naccoll.boilerplate.pay.constant.PayApiConstant;
import io.naccoll.boilerplate.pay.convert.PayRefundJournalConvert;
import io.naccoll.boilerplate.pay.dto.PayRefundJournalDto;
import io.naccoll.boilerplate.pay.dto.PayRefundJournalQueryCondition;
import io.naccoll.boilerplate.pay.model.PayRefundJournalPo;
import io.naccoll.boilerplate.pay.service.PayRefundJournalQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Pay platform api v 1 refund journal api.
 */
@RestController
@Tag(name = "退款流水")
@RequestMapping(PayApiConstant.PlatformApiV1.REFUND_JOURNAL)
public class PayPlatformApiV1RefundJournalApi {

	@Resource
	private PayRefundJournalQueryService payRefundJournalQueryService;

	@Resource
	private PayRefundJournalConvert payRefundJournalConvert;

	/**
	 * Query page page.
	 * @param condition the condition
	 * @param pageable the pageable
	 * @return the page
	 */
	@PreAuthorize("hasPermission(0L,'GLOBAL','pay/refund-journal:read')")
	@Operation(summary = "分页查询")
	@GetMapping("/page")
	Page<PayRefundJournalDto> queryPage(PayRefundJournalQueryCondition condition, Pageable pageable) {
		Page<PayRefundJournalPo> page = payRefundJournalQueryService.queryPage(condition, pageable);
		return payRefundJournalConvert.convertPayRefundJournalDtoPage(page);
	}

	/**
	 * Query pay refund journal dto.
	 * @param id the id
	 * @return the pay refund journal dto
	 */
	@PreAuthorize("hasPermission(0L,'GLOBAL','pay/refund-journal:read')")
	@Operation(summary = "查询")
	@GetMapping("/{id}")
	PayRefundJournalDto query(@PathVariable("id") Long id) {
		PayRefundJournalPo journal = payRefundJournalQueryService.findById(id);
		return payRefundJournalConvert.convertPayRefundJournalDetailDto(journal);
	}

	/**
	 * Query list list.
	 * @param payId the pay id
	 * @return the list
	 */
	@PreAuthorize("hasPermission(0L,'GLOBAL','pay/refund-journal:read')")
	@Operation(summary = "查询列表")
	@GetMapping("/list")
	public List<PayRefundJournalDto> queryList(@RequestParam("payId") Long payId) {
		List<PayRefundJournalPo> list = payRefundJournalQueryService.findByPayJournalId(payId);
		return payRefundJournalConvert.convertPayRefundJournalDtoList(list);
	}

}
