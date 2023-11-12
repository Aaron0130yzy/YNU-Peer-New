// package io.naccoll.boilerplate.customer.interfaces.api.platform;
//
// import io.naccoll.boilerplate.core.audit.operate.OperateLog;
// import io.naccoll.boilerplate.customer.convert.CustomerUserBindThirdConvert;
// import io.naccoll.boilerplate.customer.dto.CustomerUserBindThirdCreateCommand;
// import io.naccoll.boilerplate.customer.dto.CustomerUserBindThirdDto;
// import io.naccoll.boilerplate.customer.dto.CustomerUserBindThirdQueryCondition;
// import io.naccoll.boilerplate.customer.dto.CustomerUserBindThirdUpdateCommand;
// import io.naccoll.boilerplate.customer.model.CustomerUserBindThirdPo;
// import io.naccoll.boilerplate.customer.service.CustomerUserBindThirdQueryService;
// import io.naccoll.boilerplate.customer.service.CustomerUserBindThirdService;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.web.bind.annotation.*;
//
// import jakarta.annotation.Resource;
// import java.util.Collection;
//
/// ***
// * 用户绑定的第三方应用接口管理**
// *
// * @author NaccOll
// */
// @RestController
// @Tag(name = "用户绑定的第三方应用接口管理")
// @RequestMapping("/customer/platform/api/v1/customer-user-bind-third")
// public class CustomerPlatformApiV1CustomerUserBindThird {
//
// @Resource
// private CustomerUserBindThirdService customerUserBindThirdService;
//
// @Resource
// private CustomerUserBindThirdQueryService customerUserBindThirdQueryService;
//
// @Resource
// private CustomerUserBindThirdConvert customerUserBindThirdConvert;
//
// /**
// * 分页查询用户绑定的第三方应用接口
// */
// @GetMapping("/page")
// @OperateLog("分页查询用户绑定的第三方应用接口")
// @Operation(summary = "分页查询用户绑定的第三方应用接口")
// @PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-bind-third:read')")
// public ResponseEntity<Page<CustomerUserBindThirdDto>>
// page(CustomerUserBindThirdQueryCondition condition,
// Pageable pageable) {
// Page<CustomerUserBindThirdPo> customerUserBindThirdPage =
// customerUserBindThirdQueryService.page(condition,
// pageable);
// Page<CustomerUserBindThirdDto> customerUserBindThirdDtoPage =
// customerUserBindThirdConvert
// .convertCustomerUserBindThirdDtoPage(customerUserBindThirdPage);
// return new ResponseEntity<>(customerUserBindThirdDtoPage, HttpStatus.OK);
// }
//
// /**
// * 分页查询用户绑定的第三方应用接口
// */
// @GetMapping("/{id}")
// @OperateLog("查询用户绑定的第三方应用接口")
// @Operation(summary = "查询用户绑定的第三方应用接口")
// @PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-bind-third:read')")
// public ResponseEntity<CustomerUserBindThirdDto> queryOne(@PathVariable Long id) {
// CustomerUserBindThirdPo customerUserBindThirdPo =
// customerUserBindThirdQueryService.findByIdNotNull(id);
// CustomerUserBindThirdDto customerUserBindThirdDto = customerUserBindThirdConvert
// .convertCustomerUserBindThirdDto(customerUserBindThirdPo);
// return new ResponseEntity<>(customerUserBindThirdDto, HttpStatus.OK);
// }
//
// /**
// * 新增用户绑定的第三方应用接口
// */
// @PostMapping
// @OperateLog("新增用户绑定的第三方应用接口")
// @Operation(summary = "新增用户绑定的第三方应用接口")
// @PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-bind-third:add')")
// public ResponseEntity<CustomerUserBindThirdDto> createCustomerUserBindThird(
// @RequestBody CustomerUserBindThirdCreateCommand command) {
// CustomerUserBindThirdPo customerUserBindThirdPo =
// customerUserBindThirdService.create(command);
// CustomerUserBindThirdDto customerUserBindThirdDto = customerUserBindThirdConvert
// .convertCustomerUserBindThirdDto(customerUserBindThirdPo);
// return new ResponseEntity<>(customerUserBindThirdDto, HttpStatus.CREATED);
// }
//
// /**
// * 修改用户绑定的第三方应用接口
// */
// @PutMapping
// @OperateLog("修改用户绑定的第三方应用接口")
// @Operation(summary = "修改用户绑定的第三方应用接口")
// @PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-bind-third:edit')")
// public ResponseEntity<CustomerUserBindThirdDto> updateCustomerUserBindThird(
// @RequestBody CustomerUserBindThirdUpdateCommand command) {
// CustomerUserBindThirdPo customerUserBindThirdPo =
// customerUserBindThirdService.update(command);
// CustomerUserBindThirdDto customerUserBindThirdDto = customerUserBindThirdConvert
// .convertCustomerUserBindThirdDto(customerUserBindThirdPo);
// return new ResponseEntity<>(customerUserBindThirdDto, HttpStatus.OK);
// }
//
// /**
// * 删除用户绑定的第三方应用接口
// */
// @DeleteMapping("/{id}")
// @OperateLog("删除用户绑定的第三方应用接口")
// @Operation(summary = "删除用户绑定的第三方应用接口")
// @PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-bind-third:del')")
// public ResponseEntity<Void> deleteCustomerUserBindThird(@PathVariable Long id) {
// customerUserBindThirdService.deleteById(id);
// return new ResponseEntity<>(HttpStatus.OK);
// }
//
// /**
// * 批量删除用户绑定的第三方应用接口
// */
// @DeleteMapping("/batch")
// @OperateLog("批量删除用户绑定的第三方应用接口")
// @Operation(summary = "批量删除用户绑定的第三方应用接口")
// @PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-bind-third:del')")
// public ResponseEntity<Void> batchDeleteCustomerUserBindThird(@RequestParam
// Collection<Long> ids) {
// customerUserBindThirdService.deleteByIds(ids);
// return new ResponseEntity<>(HttpStatus.OK);
// }
//
// }
