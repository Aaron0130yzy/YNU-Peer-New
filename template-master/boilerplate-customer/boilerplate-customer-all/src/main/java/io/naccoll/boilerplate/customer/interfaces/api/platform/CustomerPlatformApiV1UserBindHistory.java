package io.naccoll.boilerplate.customer.interfaces.api.platform;

import io.naccoll.boilerplate.customer.constant.CustomerApiConstant;
import io.naccoll.boilerplate.customer.convert.CustomerUserBindHistoryConvert;
import io.naccoll.boilerplate.customer.dto.CustomerUserBindHistoryDto;
import io.naccoll.boilerplate.customer.dto.CustomerUserBindHistoryQueryCondition;
import io.naccoll.boilerplate.customer.model.CustomerUserBindHistoryPo;
import io.naccoll.boilerplate.customer.service.CustomerUserBindHistoryQueryService;
import io.naccoll.boilerplate.customer.service.CustomerUserBindHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户绑定历史管理
 *
 * @author NaccOll
 */
@RestController
@Tag(name = "用户绑定历史管理")
@RequestMapping(CustomerApiConstant.PlatformApiV1.USER_BIND_HISTORY)
public class CustomerPlatformApiV1UserBindHistory {

	@Resource
	private CustomerUserBindHistoryService customerUserBindHistoryService;

	@Resource
	private CustomerUserBindHistoryQueryService customerUserBindHistoryQueryService;

	@Resource
	private CustomerUserBindHistoryConvert customerUserBindHistoryConvert;

	/**
	 * 分页查询用户绑定历史
	 */
	@GetMapping("/page")
	// @OperateLog("分页查询用户绑定历史")
	@Operation(summary = "分页查询用户绑定历史")
	@PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-bind-history:read')")
	public ResponseEntity<Page<CustomerUserBindHistoryDto>> page(CustomerUserBindHistoryQueryCondition condition,
			Pageable pageable) {
		Page<CustomerUserBindHistoryPo> page = customerUserBindHistoryQueryService.page(condition, pageable);
		Page<CustomerUserBindHistoryDto> dtoPage = customerUserBindHistoryConvert
			.convertCustomerUserBindHistoryDtoPage(page);
		return new ResponseEntity<>(dtoPage, HttpStatus.OK);
	}

	/**
	 * 分页查询用户绑定历史
	 */
	@GetMapping("/{id}")
	// @OperateLog("查询用户绑定历史")
	@Operation(summary = "查询用户绑定历史")
	@PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-bind-history:read')")
	public ResponseEntity<CustomerUserBindHistoryDto> queryOne(@PathVariable Long id) {
		CustomerUserBindHistoryPo po = customerUserBindHistoryQueryService.findByIdNotNull(id);
		CustomerUserBindHistoryDto dto = customerUserBindHistoryConvert.convertCustomerUserBindHistoryDto(po);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

}
