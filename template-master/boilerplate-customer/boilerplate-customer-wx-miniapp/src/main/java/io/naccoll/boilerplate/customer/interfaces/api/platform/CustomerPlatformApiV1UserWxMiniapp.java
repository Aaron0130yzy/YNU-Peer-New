// package io.naccoll.boilerplate.customer.interfaces.api.platform;
//
// import io.naccoll.boilerplate.core.audit.operate.OperateLog;
// import io.naccoll.boilerplate.customer.constant.CustomerApiConstant;
// import io.naccoll.boilerplate.customer.convert.CustomerUserWxMiniappConvert;
// import io.naccoll.boilerplate.customer.dto.CustomerUserWxMiniappCreateCommand;
// import io.naccoll.boilerplate.customer.dto.CustomerUserWxMiniappDto;
// import io.naccoll.boilerplate.customer.dto.CustomerUserWxMiniappQueryCondition;
// import io.naccoll.boilerplate.customer.dto.CustomerUserWxMiniappUpdateCommand;
// import io.naccoll.boilerplate.customer.model.CustomerUserWxMiniappPo;
// import io.naccoll.boilerplate.customer.service.CustomerUserWxMiniappQueryService;
// import io.naccoll.boilerplate.customer.service.CustomerUserWxMiniappService;
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
/// **
// * 微信小程序用户管理
// *
// * @author NaccOll
// */
// @RestController
// @Tag(name = "微信小程序用户管理")
// @RequestMapping(CustomerApiConstant.PlatformApiV1.USER_WX_MINIAPP)
// public class CustomerPlatformApiV1UserWxMiniapp {
//
// @Resource
// private CustomerUserWxMiniappService customerUserWxMiniappService;
//
// @Resource
// private CustomerUserWxMiniappQueryService customerUserWxMiniappQueryService;
//
// @Resource
// private CustomerUserWxMiniappConvert customerUserWxMiniappConvert;
//
// /**
// * 分页查询微信小程序用户
// */
// @GetMapping("/page")
// @OperateLog("分页查询微信小程序用户")
// @Operation(summary = "分页查询微信小程序用户")
// @PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-mp-miniapp:read')")
// public ResponseEntity<Page<CustomerUserWxMiniappDto>>
// page(CustomerUserWxMiniappQueryCondition condition,
// Pageable pageable) {
// Page<CustomerUserWxMiniappPo> customerUserMpMiniappPage =
// customerUserWxMiniappQueryService.page(condition,
// pageable);
// Page<CustomerUserWxMiniappDto> customerUserMpMiniappDtoPage =
// customerUserWxMiniappConvert
// .convertCustomerUserWxMiniappDtoPage(customerUserMpMiniappPage);
// return new ResponseEntity<>(customerUserMpMiniappDtoPage, HttpStatus.OK);
// }
//
// /**
// * 分页查询微信小程序用户
// */
// @GetMapping("/{id}")
// @OperateLog("查询微信小程序用户")
// @Operation(summary = "查询微信小程序用户")
// @PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-mp-miniapp:read')")
// public ResponseEntity<CustomerUserWxMiniappDto> queryOne(@PathVariable Long id) {
// CustomerUserWxMiniappPo customerUserMpMiniappPo =
// customerUserWxMiniappQueryService.findByIdNotNull(id);
// CustomerUserWxMiniappDto customerUserMpMiniappDto = customerUserWxMiniappConvert
// .convertCustomerUserWxMiniappDto(customerUserMpMiniappPo);
// return new ResponseEntity<>(customerUserMpMiniappDto, HttpStatus.OK);
// }
//
// /**
// * 新增微信小程序用户
// */
// @PostMapping
// @OperateLog("新增微信小程序用户")
// @Operation(summary = "新增微信小程序用户")
// @PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-mp-miniapp:add')")
// public ResponseEntity<CustomerUserWxMiniappDto> createCustomerUserMpMiniapp(
// @RequestBody CustomerUserWxMiniappCreateCommand command) {
// CustomerUserWxMiniappPo customerUserMpMiniappPo =
// customerUserWxMiniappService.create(command);
// CustomerUserWxMiniappDto customerUserMpMiniappDto = customerUserWxMiniappConvert
// .convertCustomerUserWxMiniappDto(customerUserMpMiniappPo);
// return new ResponseEntity<>(customerUserMpMiniappDto, HttpStatus.CREATED);
// }
//
// /**
// * 修改微信小程序用户
// */
// @PutMapping
// @OperateLog("修改微信小程序用户")
// @Operation(summary = "修改微信小程序用户")
// @PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-mp-miniapp:edit')")
// public ResponseEntity<CustomerUserWxMiniappDto> updateCustomerUserMpMiniapp(
// @RequestBody CustomerUserWxMiniappUpdateCommand command) {
// CustomerUserWxMiniappPo customerUserMpMiniappPo =
// customerUserWxMiniappService.update(command);
// CustomerUserWxMiniappDto customerUserMpMiniappDto = customerUserWxMiniappConvert
// .convertCustomerUserWxMiniappDto(customerUserMpMiniappPo);
// return new ResponseEntity<>(customerUserMpMiniappDto, HttpStatus.OK);
// }
//
// /**
// * 删除微信小程序用户
// */
// @DeleteMapping("/{id}")
// @OperateLog("删除微信小程序用户")
// @Operation(summary = "删除微信小程序用户")
// @PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-mp-miniapp:del')")
// public ResponseEntity<Void> deleteCustomerUserMpMiniapp(@PathVariable Long id) {
// customerUserWxMiniappService.deleteById(id);
// return new ResponseEntity<>(HttpStatus.OK);
// }
//
// /**
// * 批量删除微信小程序用户
// */
// @DeleteMapping("/batch")
// @OperateLog("批量删除微信小程序用户")
// @Operation(summary = "批量删除微信小程序用户")
// @PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-mp-miniapp:del')")
// public ResponseEntity<Void> batchDeleteCustomerUserMpMiniapp(@RequestParam
// Collection<Long> ids) {
// customerUserWxMiniappService.deleteByIds(ids);
// return new ResponseEntity<>(HttpStatus.OK);
// }
//
// }
