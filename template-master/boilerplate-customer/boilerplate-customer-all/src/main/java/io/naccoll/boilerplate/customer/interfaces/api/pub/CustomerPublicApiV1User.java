package io.naccoll.boilerplate.customer.interfaces.api.pub;

import java.io.IOException;
import java.time.Duration;

import io.naccoll.boilerplate.core.captch.CaptchaProvider;
import io.naccoll.boilerplate.core.captch.CaptchaReceiveType;
import io.naccoll.boilerplate.core.security.api.BaseAuthApi;
import io.naccoll.boilerplate.core.security.enums.ClientId;
import io.naccoll.boilerplate.core.security.enums.Realm;
import io.naccoll.boilerplate.core.security.enums.TokenType;
import io.naccoll.boilerplate.customer.constant.CustomerApiConstant;
import io.naccoll.boilerplate.customer.constant.CustomerOperateConstant;
import io.naccoll.boilerplate.customer.dto.CustomerLoginCommand;
import io.naccoll.boilerplate.customer.dto.SendSmsCodeCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户接口
 */
@RestController
@Tag(name = "用户接口")
@RequestMapping(CustomerApiConstant.PublicApiV1.USER)
@Slf4j
public class CustomerPublicApiV1User extends BaseAuthApi {

	@Resource
	private CaptchaProvider captchaProvider;

	/**
	 * 获取短信验证码
	 * @return the captcha
	 */
	@Operation(summary = "发送短信验证码")
	@PostMapping("/sms-code")
	public ResponseEntity<Void> getCaptcha(@RequestBody @Valid SendSmsCodeCommand command) {
		captchaProvider.sendCode(CustomerOperateConstant.CUSTOMER_CODE_LOGIN, command.getTel(), Duration.ofMinutes(10),
				CaptchaReceiveType.SMS);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Auth.
	 * @param command the command
	 * @throws IOException the io exception
	 * @throws ServletException the servlet exception
	 */
	@Operation(summary = "登录接口")
	@PostMapping("/auth-token")
	public void auth(@RequestBody @Valid CustomerLoginCommand command) throws IOException, ServletException {
		ClientId clientId = ObjectUtils.isEmpty(command.getClientId()) ? ClientId.BROWSER
				: ClientId.valueOf(command.getClientId().toUpperCase());
		Realm realm = Realm.CUSTOMER;
		TokenType tokenType = TokenType.JSON;
		captchaCodeAuth(command.getTel(), command.getCode(), realm, clientId, tokenType);
	}

}
