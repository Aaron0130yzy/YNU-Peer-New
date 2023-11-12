package io.naccoll.boilerplate.customer.interfaces.api.platform;

import java.util.Collection;

import io.naccoll.boilerplate.customer.constant.CustomerApiConstant;
import io.naccoll.boilerplate.customer.convert.CustomerUserBindConvert;
import io.naccoll.boilerplate.customer.dto.CustomerUserBindDto;
import io.naccoll.boilerplate.customer.dto.CustomerUserBindQueryCondition;
import io.naccoll.boilerplate.customer.model.CustomerUserBindPo;
import io.naccoll.boilerplate.customer.service.CustomerUserBindQueryService;
import io.naccoll.boilerplate.customer.service.CustomerUserBindService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户绑定关系管理
 *
 * @author NaccOll
 */
@RestController
@Tag(name = "用户绑定关系管理")
@RequestMapping(CustomerApiConstant.PlatformApiV1.USER_BIND)
public class CustomerPlatformApiV1UserBind {

	@Resource
	private CustomerUserBindService customerUserBindService;

	@Resource
	private CustomerUserBindQueryService customerUserBindQueryService;

	@Resource
	private CustomerUserBindConvert customerUserBindConvert;

	/**
	 * 分页查询用户绑定关系
	 */
	@GetMapping("/page")
	@Operation(summary = "分页查询用户绑定关系")
	@PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-bind:read')")
	public ResponseEntity<Page<CustomerUserBindDto>> page(CustomerUserBindQueryCondition condition, Pageable pageable) {
		Page<CustomerUserBindPo> page = customerUserBindQueryService.page(condition, pageable);
		Page<CustomerUserBindDto> dtoPage = customerUserBindConvert.convertCustomerUserBindDtoPage(page);
		return new ResponseEntity<>(dtoPage, HttpStatus.OK);
	}

	/**
	 * 分页查询用户绑定关系
	 */
	@GetMapping("/{id}")
	@Operation(summary = "查询用户绑定关系")
	@PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-bind:read')")
	public ResponseEntity<CustomerUserBindDto> queryOne(@PathVariable Long id) {
		CustomerUserBindPo po = customerUserBindQueryService.findByIdNotNull(id);
		CustomerUserBindDto dto = customerUserBindConvert.convertCustomerUserBindDto(po);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	/**
	 * 删除用户绑定关系
	 */
	@DeleteMapping("/{id}")
	@Operation(summary = "删除用户绑定关系")
	@PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-bind:unbind')")
	public ResponseEntity<Void> deleteCustomerUserBind(@PathVariable Long id) {
		customerUserBindService.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 批量删除用户绑定关系
	 */
	@DeleteMapping("/batch")
	@Operation(summary = "批量删除用户绑定关系")
	@PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-bind:unbind')")
	@Transactional(rollbackFor = Throwable.class)
	public ResponseEntity<Void> batchDeleteCustomerUserBind(@RequestParam Collection<Long> ids) {
		for (Long id : ids) {
			customerUserBindService.deleteById(id);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
