package io.naccoll.boilerplate.customer.interfaces.api.user;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import io.naccoll.boilerplate.core.exception.ClientParamException;
import io.naccoll.boilerplate.core.security.api.BaseAuthApi;
import io.naccoll.boilerplate.core.security.context.SessionHelper;
import io.naccoll.boilerplate.core.security.entity.UserDetailsImpl;
import io.naccoll.boilerplate.customer.constant.CustomerApiConstant;
import io.naccoll.boilerplate.customer.dto.BindResultDto;
import io.naccoll.boilerplate.customer.dto.MpMiniappAuthCommand;
import io.naccoll.boilerplate.customer.enums.ThirdType;
import io.naccoll.boilerplate.customer.model.CustomerUserBindPo;
import io.naccoll.boilerplate.customer.model.CustomerUserBindThirdPo;
import io.naccoll.boilerplate.customer.service.CustomerUserBindQueryService;
import io.naccoll.boilerplate.customer.service.CustomerUserBindService;
import io.naccoll.boilerplate.customer.service.CustomerUserBindThirdQueryService;
import io.naccoll.boilerplate.customer.service.CustomerUserQueryService;
import io.naccoll.boilerplate.customer.service.CustomerUserService;
import io.naccoll.boilerplate.customer.utils.WxMiniappServiceFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信小程序用户接口
 *
 * @author NaccOll
 */
@RestController
@Tag(name = "微信小程序用户接口")
@RequestMapping(CustomerApiConstant.UserApiV1.USER_WX_MINIAPP)
public class CustomerUserApiV1UserMpMiniapp extends BaseAuthApi {

	@Resource
	private SessionHelper sessionHelper;

	@Resource
	private CustomerUserBindThirdQueryService customerUserBindThirdQueryService;

	@Resource
	private WxMiniappServiceFactory wxMiniappServiceFactory;

	@Resource
	private CustomerUserBindQueryService customerUserBindQueryService;

	@Resource
	private CustomerUserBindService customerUserBindService;

	@Resource
	private CustomerUserQueryService customerUserQueryService;

	@Resource
	private CustomerUserService customerUserService;

	@SneakyThrows
	@Operation(description = "授权绑定")
	@PostMapping("/{thirdId}/bind")
	public void bind(@PathVariable Long thirdId, @RequestBody MpMiniappAuthCommand command) {
		command.setThirdId(thirdId);
		WxMaService wxMaService = getWxMaService(thirdId);
		WxMaJscode2SessionResult accessToken = wxMaService.getUserService().getSessionInfo(command.getCode());
		String openid = accessToken.getOpenid();
		CustomerUserBindPo bind = customerUserBindQueryService.findByThirdIdAndThirdUserId(thirdId, openid);
		if (bind != null) {
			throw new ClientParamException("微信小程序已被绑定");
		}
		Long userDetailsId = Long.parseLong(sessionHelper.getCurrentLoginUser().getId());
		customerUserBindService.bind(userDetailsId, thirdId, openid);
	}

	@SneakyThrows
	@Operation(description = "授权获取手机号并绑定到当前用户")
	@PostMapping("/{thirdId}/bind-tel")
	public BindResultDto bindTel(@PathVariable Long thirdId, @RequestBody MpMiniappAuthCommand command) {
		UserDetailsImpl userDetails = sessionHelper.getCurrentLoginUser();
		Long userDetailsId = Long.parseLong(userDetails.getId());
		command.setThirdId(thirdId);
		WxMaService wxMaService = getWxMaService(thirdId);
		WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService().getNewPhoneNoInfo(command.getCode());
		String tel = phoneNoInfo.getPhoneNumber();
		return customerUserService.bindTel(tel, userDetails);
	}

	private WxMaService getWxMaService(Long thirdId) {
		CustomerUserBindThirdPo third = customerUserBindThirdQueryService.findById(thirdId);
		if (third == null) {
			throw new ClientParamException("第三方Id错误");
		}
		if (!ThirdType.WX_MINIAPP.getId().equals(third.getThirdType())) {
			throw new ClientParamException("第三方Id错误");
		}
		WxMaService wxMpService = wxMiniappServiceFactory.create(third.getThirdAppId());
		if (wxMpService == null) {
			throw new ClientParamException("第三方Id错误");
		}
		return wxMpService;
	}

}
