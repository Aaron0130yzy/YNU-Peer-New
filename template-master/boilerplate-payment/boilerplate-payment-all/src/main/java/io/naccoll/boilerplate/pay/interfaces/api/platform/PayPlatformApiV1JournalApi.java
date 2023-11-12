package io.naccoll.boilerplate.pay.interfaces.api.platform;

import io.naccoll.boilerplate.core.audit.operate.OperateLog;
import io.naccoll.boilerplate.pay.constant.PayApiConstant;
import io.naccoll.boilerplate.pay.convert.PayJournalConvert;
import io.naccoll.boilerplate.pay.dto.PayJournalDetailDto;
import io.naccoll.boilerplate.pay.dto.PayJournalQueryCondition;
import io.naccoll.boilerplate.pay.dto.PayJournalSimpleDto;
import io.naccoll.boilerplate.pay.dto.RefundCommand;
import io.naccoll.boilerplate.pay.model.PayJournalPo;
import io.naccoll.boilerplate.pay.service.PayJournalQueryService;
import io.naccoll.boilerplate.pay.service.PayJournalService;
import io.naccoll.boilerplate.pay.service.PayServiceFacadeImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Pay platform api v 1 journal api.
 */
@RestController
@Tag(name = "支付流水")
@RequestMapping(PayApiConstant.PlatformApiV1.JOURNAL)
public class PayPlatformApiV1JournalApi {

	@Resource
	private PayJournalQueryService payJournalQueryService;

	@Resource
	private PayJournalService payJournalService;

	@Resource
	private PayJournalConvert payJournalConvert;

	@Resource
	private PayServiceFacadeImpl payServiceFacadeImpl;

	/**
	 * Query page page.
	 * @param condition the condition
	 * @param pageable the pageable
	 * @return the page
	 */
	@PreAuthorize("hasPermission(0L,'GLOBAL','pay/journal:read')")
	@Operation(summary = "分页查询")
	@GetMapping("/page")
	public Page<PayJournalSimpleDto> queryPage(PayJournalQueryCondition condition, Pageable pageable) {
		Page<PayJournalPo> page = payJournalQueryService.queryPage(condition, pageable);
		return payJournalConvert.convertPayJournalDtoPage(page);
	}

	/**
	 * Query pay journal detail dto.
	 * @param id the id
	 * @return the pay journal detail dto
	 */
	@PreAuthorize("hasPermission(0L,'GLOBAL','pay/journal:read')")
	@Operation(summary = "查询")
	@GetMapping("/{id}")
	public PayJournalDetailDto query(@PathVariable("id") Long id) {
		PayJournalPo journalPo = payJournalQueryService.findByIdNotNull(id);
		return payJournalConvert.convertToPayJournalDetailDto(journalPo);
	}

	/**
	 * Refund journal response entity.
	 * @param command the command
	 * @return the response entity
	 */
	@PreAuthorize("hasPermission(0L,'GLOBAL','pay/journal:refund')")
	@Operation(summary = "退款")
	@OperateLog(value = "发起退款", id = "#command.id")
	@PutMapping("/{id}")
	public ResponseEntity refundJournal(@RequestBody RefundCommand command) {
		payServiceFacadeImpl.refund(command);
		return ResponseEntity.ok().build();
	}

}
