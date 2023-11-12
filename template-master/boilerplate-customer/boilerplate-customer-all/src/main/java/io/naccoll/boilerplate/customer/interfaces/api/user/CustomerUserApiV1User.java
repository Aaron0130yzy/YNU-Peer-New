package io.naccoll.boilerplate.customer.interfaces.api.user;

import java.time.Duration;
import java.util.Objects;

import io.naccoll.boilerplate.core.captch.CaptchaProvider;
import io.naccoll.boilerplate.core.captch.CaptchaReceiveType;
import io.naccoll.boilerplate.core.exception.ClientParamException;
import io.naccoll.boilerplate.core.security.context.SessionHelper;
import io.naccoll.boilerplate.core.security.entity.UserDetailsImpl;
import io.naccoll.boilerplate.core.security.service.DataSecurityService;
import io.naccoll.boilerplate.customer.constant.CustomerApiConstant;
import io.naccoll.boilerplate.customer.constant.CustomerOperateConstant;
import io.naccoll.boilerplate.customer.convert.CustomerUserConvert;
import io.naccoll.boilerplate.customer.dto.BindResultDto;
import io.naccoll.boilerplate.customer.dto.BindTelCommand;
import io.naccoll.boilerplate.customer.dto.CustomerUserWithBindDto;
import io.naccoll.boilerplate.customer.dto.RebindCommand;
import io.naccoll.boilerplate.customer.dto.RebindDto;
import io.naccoll.boilerplate.customer.dto.SendSmsCodeCommand;
import io.naccoll.boilerplate.customer.model.CustomerUserPo;
import io.naccoll.boilerplate.customer.service.CustomerUserQueryService;
import io.naccoll.boilerplate.customer.service.CustomerUserService;
import io.naccoll.boilerplate.oss.OssServiceHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 个人信息管理
 *
 * @author NaccOll
 */
@RestController
@Tag(name = "个人用户信息管理")
@RequestMapping(CustomerApiConstant.UserApiV1.PREFIX)
public class CustomerUserApiV1User {

	@Resource
	private SessionHelper sessionHelper;

	@Resource
	private CaptchaProvider captchaProvider;

	@Resource
	private CustomerUserQueryService customerUserQueryService;

	@Resource
	private CustomerUserService customerUserService;

	@Resource
	private DataSecurityService dataSecurityService;

	@Resource
	private CustomerUserConvert customerUserConvert;

	@Resource
	private OssServiceHelper ossServiceHelper;

	@GetMapping("/self-info")
	@Operation(description = "获取用户信息")
	public CustomerUserWithBindDto getSelfInfo() {
		UserDetailsImpl userDetails = sessionHelper.getCurrentLoginUser();
		Long userId = Long.parseLong(userDetails.getId());
		CustomerUserPo user = customerUserQueryService.findByIdNotNull(userId);
		return customerUserConvert.convertCustomerUserWithBindDto(user);
	}

	@PostMapping("/self-avatar")
	@Operation(description = "更新用户头像")
	public CustomerUserWithBindDto updateAvatar(@RequestPart MultipartFile avatar) {
		UserDetailsImpl userDetails = sessionHelper.getCurrentLoginUser();
		Long userId = Long.parseLong(userDetails.getId());
		String url = ossServiceHelper.uploadPublicMultipartFile("customer/avatar/" + userId, avatar);
		return customerUserConvert.convertCustomerUserWithBindDto(customerUserService.updateAvatar(userId, url));
	}

	@PostMapping("/bind-sms-code")
	@Operation(description = "获取手机号验证码")
	public ResponseEntity<Void> getBindSmsCode(@RequestBody SendSmsCodeCommand command) {
		UserDetailsImpl userDetails = sessionHelper.getCurrentLoginUser();
		Long userDetailsId = Long.parseLong(userDetails.getId());
		CustomerUserPo curuse = customerUserQueryService.findByIdNotNull(userDetailsId);
		if (StringUtils.hasText(curuse.getTel())) {
			throw new ClientParamException("已绑定手机号");
		}
		captchaProvider.sendCode(CustomerOperateConstant.CUSTOMER_BIND_TEL, command.getTel(), Duration.ofMinutes(5),
				Duration.ofMinutes(1), CaptchaReceiveType.SMS);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/rebind-sms-code")
	@Operation(description = "获取换绑手机号验证码")
	public ResponseEntity<Void> getRebindSmsCode(@RequestBody SendSmsCodeCommand command) {
		UserDetailsImpl userDetails = sessionHelper.getCurrentLoginUser();
		Long userDetailsId = Long.parseLong(userDetails.getId());
		CustomerUserPo curuse = customerUserQueryService.findByIdNotNull(userDetailsId);
		if (!StringUtils.hasText(curuse.getTel())) {
			throw new ClientParamException("未绑定手机号");
		}
		String originTel = dataSecurityService.decryptStr(curuse.getTel());
		if (Objects.equals(originTel, command.getTel())) {
			throw new ClientParamException("要绑定的手机号与当前绑定手机相同");
		}
		CustomerUserPo telUser = customerUserQueryService.findByTel(command.getTel());
		if (telUser != null) {
			throw new ClientParamException("手机号已被其他用户绑定");
		}
		captchaProvider.sendCode(CustomerOperateConstant.CUSTOMER_BIND_TEL, command.getTel(), Duration.ofMinutes(5),
				Duration.ofMinutes(1), CaptchaReceiveType.SMS);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/bind-tel")
	@Operation(description = "绑定手机号")
	public BindResultDto bindTel(@RequestBody BindTelCommand command) {
		UserDetailsImpl userDetails = sessionHelper.getCurrentLoginUser();
		if (!Objects.equals(captchaProvider.validateCode(CustomerOperateConstant.CUSTOMER_BIND_TEL, command.getTel(),
				command.getCode()), 0L)) {
			throw new ClientParamException("验证码错误");
		}
		return customerUserService.bindTel(command.getTel(), userDetails);
	}

	@PostMapping("/rebind")
	@Operation(description = "换绑（需要前置的绑定操作遭遇冲突获得secure才可调用）")
	public RebindDto bindTelRebind(@RequestBody RebindCommand command) {
		UserDetailsImpl userDetails = sessionHelper.getCurrentLoginUser();
		command.setUserDetails(userDetails);
		return customerUserService.rebind(command);
	}

	@PostMapping("/rebind-tel")
	@Operation(description = "换绑手机号")
	public ResponseEntity<Void> rebindTel(@RequestBody BindTelCommand command) {
		UserDetailsImpl userDetails = sessionHelper.getCurrentLoginUser();
		if (!Objects.equals(captchaProvider.validateCode(CustomerOperateConstant.CUSTOMER_BIND_TEL, command.getTel(),
				command.getCode()), 0L)) {
			throw new ClientParamException("验证码错误");
		}
		customerUserService.rebindTel(command.getTel(), userDetails);
		return ResponseEntity.ok().build();
	}

}
