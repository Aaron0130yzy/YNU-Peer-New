package io.naccoll.boilerplate.pay.interfaces.api.platform;

import io.naccoll.boilerplate.core.audit.operate.OperateLog;
import io.naccoll.boilerplate.pay.constant.PayApiConstant;
import io.naccoll.boilerplate.pay.convert.PayMerchantConvert;
import io.naccoll.boilerplate.pay.dto.PayMerchantCreateCommand;
import io.naccoll.boilerplate.pay.dto.PayMerchantQueryCondition;
import io.naccoll.boilerplate.pay.dto.PayMerchantSimpleDto;
import io.naccoll.boilerplate.pay.service.PayMerchantQueryService;
import io.naccoll.boilerplate.pay.service.PayMerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Pay platform api v 1 merchant api.
 */
@RestController
@Tag(name = "商户管理")
@RequestMapping(PayApiConstant.PlatformApiV1.MERCHANT)
public class PayPlatformApiV1MerchantApi {

	@Resource
	private PayMerchantQueryService payMerchantQueryService;

	@Resource
	private PayMerchantService payMerchantService;

	@Resource
	private PayMerchantConvert payMerchantConvert;

	/**
	 * Page page.
	 * @param pageable the pageable
	 * @param condition the condition
	 * @return the page
	 */
	@PreAuthorize("hasPermission(0L,'GLOBAL','pay/merchant:read')")
	@Operation(summary = "分页查询支付商户")
	@GetMapping("/page")
	public Page<PayMerchantSimpleDto> page(Pageable pageable, PayMerchantQueryCondition condition) {
		return payMerchantConvert.convertPayMerchantDtoPage(payMerchantQueryService.page(pageable, condition));
	}

	/**
	 * Create pay merchant simple dto.
	 * @param command the command
	 * @return the pay merchant simple dto
	 */
	@PreAuthorize("hasPermission(0L,'GLOBAL','pay/merchant:add')")
	@Operation(summary = "创建支付商户")
	@OperateLog(value = "创建支付商户")
	@PostMapping
	public PayMerchantSimpleDto create(@RequestBody PayMerchantCreateCommand command) {
		return payMerchantConvert.convertPayMerchantDto(payMerchantService.create(command));
	}

	/**
	 * Detail pay merchant simple dto.
	 * @param id the id
	 * @return the pay merchant simple dto
	 */
	@PreAuthorize("hasPermission(0L,'GLOBAL','pay/merchant:read')")
	@Operation(summary = "查询支付商户")
	@GetMapping("/{id}")
	public PayMerchantSimpleDto detail(@PathVariable Long id) {
		return payMerchantConvert.convertPayMerchantDto(payMerchantQueryService.findByIdNotNull(id));
	}

}
