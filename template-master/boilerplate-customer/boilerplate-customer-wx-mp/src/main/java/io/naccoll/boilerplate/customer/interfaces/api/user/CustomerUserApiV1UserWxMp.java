package io.naccoll.boilerplate.customer.interfaces.api.user;

import io.naccoll.boilerplate.core.exception.ClientParamException;
import io.naccoll.boilerplate.core.security.api.BaseAuthApi;
import io.naccoll.boilerplate.core.security.context.SessionHelper;
import io.naccoll.boilerplate.customer.constant.CustomerApiConstant;
import io.naccoll.boilerplate.customer.dto.MpAuthCommand;
import io.naccoll.boilerplate.customer.dto.MpAuthState;
import io.naccoll.boilerplate.customer.enums.ThirdType;
import io.naccoll.boilerplate.customer.model.CustomerUserBindPo;
import io.naccoll.boilerplate.customer.model.CustomerUserBindThirdPo;
import io.naccoll.boilerplate.customer.service.CustomerUserBindQueryService;
import io.naccoll.boilerplate.customer.service.CustomerUserBindService;
import io.naccoll.boilerplate.customer.service.CustomerUserBindThirdQueryService;
import io.naccoll.boilerplate.customer.service.CustomerUserQueryService;
import io.naccoll.boilerplate.customer.service.CustomerUserService;
import io.naccoll.boilerplate.customer.utils.WxMpServiceFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpService;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.naccoll.boilerplate.customer.constant.CustomerOperateConstant.CUSTOMER_MP_AUTH_STATE;

@RestController
@Tag(name = "微信用户公开接口")
@RequestMapping(CustomerApiConstant.UserApiV1.USER_WX_MP)
public class CustomerUserApiV1UserWxMp extends BaseAuthApi {

	@Resource
	private SessionHelper sessionHelper;

	@Resource
	private CustomerUserService customerUserService;

	@Resource
	private CustomerUserQueryService customerUserQueryService;

	@Resource
	private CustomerUserBindQueryService customerUserBindQueryService;

	@Resource
	private CustomerUserBindService customerUserBindService;

	@Resource
	private WxMpServiceFactory wxMpServiceFactory;

	@Resource
	private CustomerUserBindThirdQueryService customerUserBindThirdQueryService;

	@Operation(description = "获取随机码")
	@GetMapping("/state")
	public MpAuthState getState() {
		MpAuthState state = new MpAuthState();
		state.setState(generateRandom(CUSTOMER_MP_AUTH_STATE));
		return state;
	}

	@SneakyThrows
	@Operation(description = "授权绑定（code回调前端，前端调用）")
	@PostMapping("/{thirdId}/bind")
	public void auth(@PathVariable Long thirdId, @RequestBody MpAuthCommand command) {
		command.setThirdId(thirdId);
		if (!validateRandom(CUSTOMER_MP_AUTH_STATE, command.getState())) {
			throw new BadCredentialsException("非法的state");
		}
		WxMpService wxMpService = getWxMpService(thirdId);
		WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(command.getCode());
		String openid = accessToken.getOpenId();
		CustomerUserBindPo bind = customerUserBindQueryService.findByThirdIdAndThirdUserId(thirdId, openid);
		if (bind != null) {
			throw new ClientParamException("微信已被绑定");
		}
		Long userDetailsId = Long.parseLong(sessionHelper.getCurrentLoginUser().getId());
		customerUserBindService.bind(userDetailsId, thirdId, openid);
	}

	// @Operation(description = "授权验证（code回调后端，后端处理后回调前端")
	// @GetMapping("/{thirdId}/bind")
	// public void auth(@PathVariable Long thirdId, @RequestParam String code,
	// @RequestParam String state,
	// @RequestParam String url) throws ServletException, IOException {
	// if (!validateRandom(CUSTOMER_MP_AUTH_STATE, state)) {
	// throw new BadCredentialsException("非法的state");
	// }
	// Realm realm = Realm.CUSTOMER;
	// TokenType tokenType = TokenType.JSON;
	// ClientId clientId = ClientId.MP_H5;
	// authCodeAuth(code, state, thirdId, realm, clientId, tokenType, url);
	// }

	private WxMpService getWxMpService(Long thirdId) {
		CustomerUserBindThirdPo third = customerUserBindThirdQueryService.findById(thirdId);
		if (third == null) {
			throw new ClientParamException("第三方Id错误");
		}
		if (!ThirdType.WX_MP.getId().equals(third.getThirdType())) {
			throw new ClientParamException("第三方Id错误");
		}
		WxMpService wxMpService = wxMpServiceFactory.create(third.getThirdAppId());
		if (wxMpService == null) {
			throw new ClientParamException("第三方Id错误");
		}
		return wxMpService;
	}

}
