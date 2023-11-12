// package io.naccoll.boilerplate.customer.interfaces.api.platform;
//
// import io.naccoll.boilerplate.core.audit.operate.OperateLog;
// import io.naccoll.boilerplate.customer.constant.CustomerApiConstant;
// import io.naccoll.boilerplate.customer.convert.CustomerUserWxMpConvert;
// import io.naccoll.boilerplate.customer.dto.CustomerUserWxMpCreateCommand;
// import io.naccoll.boilerplate.customer.dto.CustomerUserWxMpDto;
// import io.naccoll.boilerplate.customer.dto.CustomerUserWxMpQueryCondition;
// import io.naccoll.boilerplate.customer.dto.CustomerUserWxMpUpdateCommand;
// import io.naccoll.boilerplate.customer.model.CustomerUserWxMpPo;
// import io.naccoll.boilerplate.customer.service.CustomerUserWxMpQueryService;
// import io.naccoll.boilerplate.customer.service.CustomerUserWxMpService;
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
// * 微信公众号用户接口管理**
// *
// * @author NaccOll
// */
// @RestController
// @Tag(name = "微信公众号用户接口管理")
// @RequestMapping(CustomerApiConstant.PlatformApiV1.USER_WX_MP)
// public class CustomerPlatformApiV1UserWxMp {
//
// @Resource
// private CustomerUserWxMpService customerUserWxMpService;
//
// @Resource
// private CustomerUserWxMpQueryService customerUserWxMpQueryService;
//
// @Resource
// private CustomerUserWxMpConvert customerUserWxMpConvert;
//
// /**
// * 分页查询微信公众号用户接口
// */
// @GetMapping("/page")
// @OperateLog("分页查询微信公众号用户接口")
// @Operation(summary = "分页查询微信公众号用户接口")
// @PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-mp:read')")
// public ResponseEntity<Page<CustomerUserWxMpDto>> page(CustomerUserWxMpQueryCondition
// condition, Pageable pageable) {
// Page<CustomerUserWxMpPo> customerUserMpPage =
// customerUserWxMpQueryService.page(condition, pageable);
// Page<CustomerUserWxMpDto> customerUserMpDtoPage =
// customerUserWxMpConvert.convertCustomerUserWxMpDtoPage(customerUserMpPage);
// return new ResponseEntity<>(customerUserMpDtoPage, HttpStatus.OK);
// }
//
// /**
// * 分页查询微信公众号用户接口
// */
// @GetMapping("/{id}")
// @OperateLog("查询微信公众号用户接口")
// @Operation(summary = "查询微信公众号用户接口")
// @PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-mp:read')")
// public ResponseEntity<CustomerUserWxMpDto> queryOne(@PathVariable Long id) {
// CustomerUserWxMpPo customerUserMpPo = customerUserWxMpQueryService.findByIdNotNull(id);
// CustomerUserWxMpDto customerUserMpDto =
// customerUserWxMpConvert.convertCustomerUserWxMpDto(customerUserMpPo);
// return new ResponseEntity<>(customerUserMpDto, HttpStatus.OK);
// }
//
// /**
// * 新增微信公众号用户接口
// */
// @PostMapping
// @OperateLog("新增微信公众号用户接口")
// @Operation(summary = "新增微信公众号用户接口")
// @PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-mp:add')")
// public ResponseEntity<CustomerUserWxMpDto> createCustomerUserMp(
// @RequestBody CustomerUserWxMpCreateCommand command) {
// CustomerUserWxMpPo customerUserMpPo = customerUserWxMpService.create(command);
// CustomerUserWxMpDto customerUserMpDto =
// customerUserWxMpConvert.convertCustomerUserWxMpDto(customerUserMpPo);
// return new ResponseEntity<>(customerUserMpDto, HttpStatus.CREATED);
// }
//
// /**
// * 修改微信公众号用户接口
// */
// @PutMapping
// @OperateLog("修改微信公众号用户接口")
// @Operation(summary = "修改微信公众号用户接口")
// @PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-mp:edit')")
// public ResponseEntity<CustomerUserWxMpDto> updateCustomerUserMp(
// @RequestBody CustomerUserWxMpUpdateCommand command) {
// CustomerUserWxMpPo customerUserMpPo = customerUserWxMpService.update(command);
// CustomerUserWxMpDto customerUserMpDto =
// customerUserWxMpConvert.convertCustomerUserWxMpDto(customerUserMpPo);
// return new ResponseEntity<>(customerUserMpDto, HttpStatus.OK);
// }
//
// /**
// * 删除微信公众号用户接口
// */
// @DeleteMapping("/{id}")
// @OperateLog("删除微信公众号用户接口")
// @Operation(summary = "删除微信公众号用户接口")
// @PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-mp:del')")
// public ResponseEntity<Void> deleteCustomerUserMp(@PathVariable Long id) {
// customerUserWxMpService.deleteById(id);
// return new ResponseEntity<>(HttpStatus.OK);
// }
//
// /**
// * 批量删除微信公众号用户接口
// */
// @DeleteMapping("/batch")
// @OperateLog("批量删除微信公众号用户接口")
// @Operation(summary = "批量删除微信公众号用户接口")
// @PreAuthorize("hasPermission(0L,'GLOBAL','customer/user-mp:del')")
// public ResponseEntity<Void> batchDeleteCustomerUserMp(@RequestParam Collection<Long>
// ids) {
// customerUserWxMpService.deleteByIds(ids);
// return new ResponseEntity<>(HttpStatus.OK);
// }
//
// }
