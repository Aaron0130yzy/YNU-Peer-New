package io.naccoll.boilerplate.customer.interfaces.api.platform;

import java.util.Collection;

import io.naccoll.boilerplate.core.audit.operate.OperateLog;
import io.naccoll.boilerplate.customer.constant.CustomerApiConstant;
import io.naccoll.boilerplate.customer.convert.CustomerUserConvert;
import io.naccoll.boilerplate.customer.dto.CustomerUserCreateCommand;
import io.naccoll.boilerplate.customer.dto.CustomerUserDto;
import io.naccoll.boilerplate.customer.dto.CustomerUserQueryCondition;
import io.naccoll.boilerplate.customer.model.CustomerUserPo;
import io.naccoll.boilerplate.customer.service.CustomerUserQueryService;
import io.naccoll.boilerplate.customer.service.CustomerUserService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理
 *
 * @author NaccOll
 */
@RestController
@Tag(name = "用户管理")
@RequestMapping(CustomerApiConstant.PlatformApiV1.USER)
public class CustomerPlatformApiV1User {

	@Resource
	private CustomerUserService customerUserService;

	@Resource
	private CustomerUserQueryService customerUserQueryService;

	@Resource
	private CustomerUserConvert customerUserConvert;

	/**
	 * 分页查询用户
	 */
	@GetMapping("/page")
	// @OperateLog("分页查询用户")
	@Operation(summary = "分页查询用户")
	@PreAuthorize("hasPermission(0L,'GLOBAL','customer/user:read')")
	public ResponseEntity<Page<CustomerUserDto>> page(CustomerUserQueryCondition condition, Pageable pageable) {
		Page<CustomerUserPo> page = customerUserQueryService.page(condition, pageable);
		Page<CustomerUserDto> dtoPage = customerUserConvert.convertCustomerUserDtoPage(page);
		return new ResponseEntity<>(dtoPage, HttpStatus.OK);
	}

	/**
	 * 分页查询用户
	 */
	@GetMapping("/{id}")
	// @OperateLog("查询用户")
	@Operation(summary = "查询用户")
	@PreAuthorize("hasPermission(0L,'GLOBAL','customer/user:read')")
	public ResponseEntity<CustomerUserDto> queryOne(@PathVariable Long id) {
		CustomerUserPo po = customerUserQueryService.findByIdNotNull(id);
		CustomerUserDto dto = customerUserConvert.convertCustomerUserDto(po);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	/**
	 * 新增用户
	 */
	@PostMapping
	@OperateLog("新增用户")
	@Operation(summary = "新增用户")
	@PreAuthorize("hasPermission(0L,'GLOBAL','customer/user:add')")
	public ResponseEntity<CustomerUserDto> createCustomerUser(@RequestBody CustomerUserCreateCommand command) {
		CustomerUserPo customerUserPo = customerUserService.create(command);
		CustomerUserDto customerUserDto = customerUserConvert.convertCustomerUserDto(customerUserPo);
		return new ResponseEntity<>(customerUserDto, HttpStatus.CREATED);
	}

	// /**
	// * 修改用户
	// */
	// @PutMapping
	// @OperateLog("修改用户")
	// @Operation(summary = "修改用户")
	// @PreAuthorize("hasPermission(0L,'GLOBAL','customer/user:edit')")
	// public ResponseEntity<CustomerUserDto> updateCustomerUser(@RequestBody
	// CustomerUserUpdateCommand command) {
	// CustomerUserPo customerUserPo = customerUserService.update(command);
	// CustomerUserDto customerUserDto =
	// customerUserConvert.convertCustomerUserDto(customerUserPo);
	// return new ResponseEntity<>(customerUserDto, HttpStatus.OK);
	// }

	/**
	 * 启用用户
	 */
	@PutMapping("/{id}/enable")
	@OperateLog("启用用户")
	@Operation(summary = "启用用户")
	@PreAuthorize("hasPermission(0L,'GLOBAL','customer/user:edit')")
	public ResponseEntity<CustomerUserDto> enableCustomerUser(@PathVariable Long id) {
		CustomerUserPo customerUserPo = customerUserService.updateStatus(id, true);
		CustomerUserDto customerUserDto = customerUserConvert.convertCustomerUserDto(customerUserPo);
		return new ResponseEntity<>(customerUserDto, HttpStatus.OK);
	}

	/**
	 * 禁用用户
	 */
	@PutMapping("/{id}/disable")
	@OperateLog(value = "禁用用户", id = "#id")
	@Operation(summary = "禁用用户")
	@PreAuthorize("hasPermission(0L,'GLOBAL','customer/user:edit')")
	public ResponseEntity<CustomerUserDto> disableCustomerUser(@PathVariable Long id) {
		CustomerUserPo customerUserPo = customerUserService.updateStatus(id, false);
		CustomerUserDto customerUserDto = customerUserConvert.convertCustomerUserDto(customerUserPo);
		return new ResponseEntity<>(customerUserDto, HttpStatus.OK);
	}

	@PutMapping("/{id}/unbind-tel")
	@OperateLog(value = "解绑用户手机", id = "#id")
	@Operation(summary = "解绑用户手机")
	@PreAuthorize("hasPermission(0L,'GLOBAL','customer/user:unbind-tel')")
	public ResponseEntity<CustomerUserDto> unbindCustomerUserTel(@PathVariable Long id) {
		CustomerUserPo customerUserPo = customerUserService.unbindTel(id, false);
		CustomerUserDto customerUserDto = customerUserConvert.convertCustomerUserDto(customerUserPo);
		return new ResponseEntity<>(customerUserDto, HttpStatus.OK);
	}

	@PutMapping("/{id}/unbind-tel-force")
	@OperateLog(value = "解绑用户手机", id = "#id")
	@Operation(summary = "解绑用户手机")
	@PreAuthorize("hasPermission(0L,'GLOBAL','customer/user:unbind-tel')")
	public ResponseEntity<CustomerUserDto> forceUnbindCustomerUserTel(@PathVariable Long id) {
		CustomerUserPo customerUserPo = customerUserService.unbindTel(id, true);
		CustomerUserDto customerUserDto = customerUserConvert.convertCustomerUserDto(customerUserPo);
		return new ResponseEntity<>(customerUserDto, HttpStatus.OK);
	}

	/**
	 * 删除用户
	 */
	@DeleteMapping("/{id}")
	@Operation(summary = "删除用户")
	@PreAuthorize("hasPermission(0L,'GLOBAL','customer/user:del')")
	public ResponseEntity<Void> deleteCustomerUser(@PathVariable Long id) {
		customerUserService.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 批量删除用户
	 */
	@DeleteMapping("/batch")
	@OperateLog("批量删除用户")
	@PreAuthorize("hasPermission(0L,'GLOBAL','customer/user:del')")
	@Transactional(rollbackFor = Throwable.class)
	public ResponseEntity<Void> batchDeleteCustomerUser(@RequestParam Collection<Long> ids) {
		for (Long id : ids) {
			customerUserService.deleteById(id);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
