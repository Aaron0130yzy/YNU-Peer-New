package io.naccoll.boilerplate.customer.interfaces.api.pub;

import java.io.IOException;

import io.naccoll.boilerplate.core.security.api.BaseAuthApi;
import io.naccoll.boilerplate.core.security.enums.ClientId;
import io.naccoll.boilerplate.core.security.enums.Realm;
import io.naccoll.boilerplate.core.security.enums.TokenType;
import io.naccoll.boilerplate.customer.constant.CustomerApiConstant;
import io.naccoll.boilerplate.customer.dto.MpAuthCommand;
import io.naccoll.boilerplate.customer.dto.MpAuthState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static io.naccoll.boilerplate.customer.constant.CustomerOperateConstant.CUSTOMER_MP_AUTH_STATE;

@RestController
@Tag(name = "微信用户公开接口")
@RequestMapping(CustomerApiConstant.PublicApiV1.USER_WX_MP)
public class CustomerPublicApiV1UserWxMp extends BaseAuthApi {

	@Operation(description = "获取随机码")
	@GetMapping("/state")
	public MpAuthState getState() {
		MpAuthState state = new MpAuthState();
		state.setState(generateRandom(CUSTOMER_MP_AUTH_STATE));
		return state;
	}

	@Operation(description = "授权验证（code回调前端，前端调用）")
	@PostMapping("/{thirdId}/auth")
	public void auth(@PathVariable Long thirdId, @RequestBody MpAuthCommand command)
			throws ServletException, IOException {
		if (!validateRandom(CUSTOMER_MP_AUTH_STATE, command.getState())) {
			throw new BadCredentialsException("非法的state");
		}
		command.setThirdId(thirdId);
		Realm realm = Realm.CUSTOMER;
		TokenType tokenType = TokenType.JSON;
		ClientId clientId = ClientId.MP_H5;
		authCodeAuth(command.getCode(), command.getState(), command.getThirdId(), realm, clientId, tokenType);
	}

	@Operation(description = "授权验证（code回调后端，后端处理后回调前端")
	@GetMapping("/{thirdId}/auth")
	public void auth(@PathVariable Long thirdId, @RequestParam String code, @RequestParam String state,
			@RequestParam String url) throws ServletException, IOException {
		if (!validateRandom(CUSTOMER_MP_AUTH_STATE, state)) {
			throw new BadCredentialsException("非法的state");
		}
		Realm realm = Realm.CUSTOMER;
		TokenType tokenType = TokenType.JSON;
		ClientId clientId = ClientId.MP_H5;
		authCodeAuth(code, state, thirdId, realm, clientId, tokenType, url);
	}

}
