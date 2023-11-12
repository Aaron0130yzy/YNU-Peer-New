package io.naccoll.boilerplate.customer.interfaces.api.pub;

import java.io.IOException;

import io.naccoll.boilerplate.core.security.api.BaseAuthApi;
import io.naccoll.boilerplate.core.security.enums.ClientId;
import io.naccoll.boilerplate.core.security.enums.Realm;
import io.naccoll.boilerplate.core.security.enums.TokenType;
import io.naccoll.boilerplate.customer.constant.CustomerApiConstant;
import io.naccoll.boilerplate.customer.convert.CustomerUserWxMiniappConvert;
import io.naccoll.boilerplate.customer.dto.MpMiniappAuthCommand;
import io.naccoll.boilerplate.customer.service.CustomerUserWxMiniappQueryService;
import io.naccoll.boilerplate.customer.service.CustomerUserWxMiniappService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信小程序用户授权
 *
 * @author NaccOll
 */
@RestController
@Tag(name = "微信小程序用户授权")
@RequestMapping(CustomerApiConstant.PublicApiV1.USER_WX_MINIAPP)
public class CustomerPublicApiV1UserMpMiniapp extends BaseAuthApi {

	@Resource
	private CustomerUserWxMiniappService customerUserWxMiniappService;

	@Resource
	private CustomerUserWxMiniappQueryService customerUserWxMiniappQueryService;

	@Resource
	private CustomerUserWxMiniappConvert customerUserWxMiniappConvert;

	@Operation(description = "授权验证")
	@PostMapping("/{thirdId}/auth")
	public void auth(@PathVariable Long thirdId, @RequestBody MpMiniappAuthCommand command)
			throws ServletException, IOException {
		command.setThirdId(thirdId);
		Realm realm = Realm.CUSTOMER;
		TokenType tokenType = TokenType.JSON;
		ClientId clientId = ClientId.MP_MINIAPP;
		authCodeAuth(command.getCode(), "", command.getThirdId(), realm, clientId, tokenType);
	}

}
