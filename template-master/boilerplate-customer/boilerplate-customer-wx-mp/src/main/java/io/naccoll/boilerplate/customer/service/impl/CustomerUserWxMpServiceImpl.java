package io.naccoll.boilerplate.customer.service.impl;

import java.util.Collection;
import java.util.Date;

import io.naccoll.boilerplate.core.exception.ClientParamException;
import io.naccoll.boilerplate.core.id.IdService;
import io.naccoll.boilerplate.core.security.entity.AuthCodeLoginAuthenticationToken;
import io.naccoll.boilerplate.core.security.entity.UserDetailsImpl;
import io.naccoll.boilerplate.core.security.enums.ClientId;
import io.naccoll.boilerplate.core.security.enums.Realm;
import io.naccoll.boilerplate.core.security.service.RealmUserDetailsAuthCodeAuthorizationService;
import io.naccoll.boilerplate.core.validate.Validation;
import io.naccoll.boilerplate.customer.dao.CustomerUserWxMpDao;
import io.naccoll.boilerplate.customer.dto.CustomerUserCreateCommand;
import io.naccoll.boilerplate.customer.dto.CustomerUserWxMpCreateCommand;
import io.naccoll.boilerplate.customer.dto.CustomerUserWxMpUpdateCommand;
import io.naccoll.boilerplate.customer.model.CustomerUserBindThirdPo;
import io.naccoll.boilerplate.customer.model.CustomerUserPo;
import io.naccoll.boilerplate.customer.model.CustomerUserWxMpPo;
import io.naccoll.boilerplate.customer.service.CustomerUserBindService;
import io.naccoll.boilerplate.customer.service.CustomerUserBindThirdQueryService;
import io.naccoll.boilerplate.customer.service.CustomerUserQueryService;
import io.naccoll.boilerplate.customer.service.CustomerUserService;
import io.naccoll.boilerplate.customer.service.CustomerUserWxMpQueryService;
import io.naccoll.boilerplate.customer.service.CustomerUserWxMpService;
import io.naccoll.boilerplate.customer.utils.WxMpServiceFactory;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author NaccOll
 */
@Service
@Validation
public class CustomerUserWxMpServiceImpl
		implements CustomerUserWxMpService, RealmUserDetailsAuthCodeAuthorizationService {

	@Resource
	private CustomerUserWxMpDao customerUserWxMpDao;

	@Resource
	private CustomerUserWxMpQueryService customerUserWxMpQueryService;

	@Resource
	private CustomerUserQueryService customerUserQueryService;

	@Resource
	private CustomerUserService customerUserService;

	@Resource
	private WxMpServiceFactory wxMpServiceFactory;

	@Resource
	private CustomerUserBindThirdQueryService customerUserBindThirdQueryService;

	@Resource
	private CustomerUserBindService customerUserBindService;

	@Resource
	private IdService idService;

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public CustomerUserWxMpPo create(@Valid CustomerUserWxMpCreateCommand command) {
		CustomerUserWxMpPo customerUserMpPo = new CustomerUserWxMpPo();
		BeanUtils.copyProperties(command, customerUserMpPo);
		customerUserMpPo.setId(idService.getId());
		return customerUserWxMpDao.save(customerUserMpPo);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public CustomerUserWxMpPo update(@Valid CustomerUserWxMpUpdateCommand command) {
		CustomerUserWxMpPo customerUserMpPo = customerUserWxMpQueryService.findByIdNotNull(command.getId());
		BeanUtils.copyProperties(command, customerUserMpPo);
		return customerUserWxMpDao.save(customerUserMpPo);
	}

	@Override
	public void deleteById(Long id) {
		customerUserWxMpDao.deleteById(id);
	}

	@Override
	public void deleteByIds(Collection<Long> ids) {
		ids.forEach(id -> {
			customerUserWxMpDao.deleteById(id);
		});
	}

	@Override
	public Realm getRealm() {
		return Realm.CUSTOMER;
	}

	@Override
	public ClientId getClientId() {
		return ClientId.MP_H5;
	}

	@Override
	@SneakyThrows
	@Transactional(rollbackFor = Throwable.class)
	public UserDetailsImpl loadUserByUsername(String code, AuthCodeLoginAuthenticationToken authentication)
			throws UsernameNotFoundException {
		Long thirdId = authentication.getThirdId();
		CustomerUserBindThirdPo thirdApp = customerUserBindThirdQueryService.findById(thirdId);
		if (thirdApp == null) {
			throw new BadCredentialsException("thirdId错误");
		}
		String appId = thirdApp.getThirdAppId();
		WxMpService wxMpService = wxMpServiceFactory.create(appId);
		if (wxMpService == null) {
			throw new BadCredentialsException("未配置appId的密钥");
		}
		WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(code);
		String openid = accessToken.getOpenId();
		CustomerUserWxMpPo customerUserMpPo = customerUserWxMpQueryService.findByThirdIdAndOpenid(thirdId, openid);
		CustomerUserPo customerUserPo = null;
		if (customerUserMpPo == null) {
			WxMpUser wxMpUser = wxMpService.getUserService().userInfo(openid);
			CustomerUserCreateCommand command = new CustomerUserCreateCommand();
			command.setRegisterChannel(authentication.getClientId().getId());
			customerUserPo = customerUserService.create(command);

			CustomerUserWxMpCreateCommand mpCreateCommand = new CustomerUserWxMpCreateCommand();
			mpCreateCommand.setCustomerUserId(customerUserPo.getId());
			mpCreateCommand.setThirdId(thirdId);
			mpCreateCommand.setOpenid(openid);
			mpCreateCommand.setNickname("");
			mpCreateCommand.setHeadImgUrl("");
			mpCreateCommand.setSex(0);
			mpCreateCommand.setCountry("");
			mpCreateCommand.setProvince("");
			mpCreateCommand.setCity("");
			mpCreateCommand.setSubscribe(wxMpUser.getSubscribe());
			mpCreateCommand.setSubscribeTime(
					wxMpUser.getSubscribeTime() != null ? new Date(wxMpUser.getSubscribeTime() * 1000) : null);
			mpCreateCommand.setUnionId(StringUtils.hasText(wxMpUser.getUnionId()) ? wxMpUser.getUnionId() : "");
			mpCreateCommand.setSubscribeScene(wxMpUser.getSubscribeScene());
			mpCreateCommand.setQrScene(wxMpUser.getQrScene());
			mpCreateCommand.setQrSceneStr(wxMpUser.getQrSceneStr());
			mpCreateCommand.setRemark(wxMpUser.getRemark());
			mpCreateCommand.setLastAuthDate(new Date());
			customerUserMpPo = create(mpCreateCommand);

			customerUserBindService.bind(customerUserMpPo.getCustomerUserId(), thirdId, customerUserMpPo.getOpenid());

		}
		customerUserPo = customerUserQueryService.findById(customerUserMpPo.getCustomerUserId());
		if (customerUserPo == null) {
			CustomerUserCreateCommand command = new CustomerUserCreateCommand();
			command.setRegisterChannel(authentication.getClientId().getId());
			customerUserPo = customerUserService.create(command);
			customerUserBindService.bindSync(customerUserPo.getId(), thirdId, customerUserMpPo.getOpenid());
		}

		return UserDetailsImpl.builder()
			.id(customerUserPo.getId().toString())
			.username(customerUserPo.getUsername())
			.password(customerUserPo.getPassword())
			.realm(Realm.CUSTOMER)
			.clientId(authentication.getClientId())
			.enabled(customerUserPo.getEnabled())
			.accountNonLocked(customerUserPo.getAccountNonLocked())
			.accountNonExpired(customerUserPo.getAccountNonExpired())
			.credentialsNonExpired(customerUserPo.getCredentialsNonExpired())
			.thirdUserId(openid)
			.thirdId(thirdId)
			.thirdRealId(appId)
			.build();
	}

	@Override
	public CustomerUserWxMpPo bind(Long userId, Long thirdId, String openid) {
		CustomerUserWxMpPo customerUserWxMpPo = customerUserWxMpQueryService.findByThirdIdAndUserId(thirdId, userId);
		if (customerUserWxMpPo != null) {
			throw new ClientParamException("用户已绑定公众号，无法再次绑定");
		}

		CustomerUserWxMpPo mp = customerUserWxMpQueryService.findByThirdIdAndOpenid(thirdId, openid);
		if (mp != null) {
			mp.setCustomerUserId(userId);
			return customerUserWxMpDao.save(mp);
		}
		CustomerUserWxMpCreateCommand mpCreateCommand = new CustomerUserWxMpCreateCommand();
		mpCreateCommand.setCustomerUserId(userId);
		mpCreateCommand.setThirdId(thirdId);
		mpCreateCommand.setOpenid(openid);
		return create(mpCreateCommand);
	}

	@Override
	public CustomerUserWxMpPo unbind(Long userId, Long thirdId, String openid) {

		CustomerUserWxMpPo customerUserWxMiniappByOpenid = customerUserWxMpQueryService.findByThirdIdAndOpenid(thirdId,
				openid);
		if (customerUserWxMiniappByOpenid == null) {
			return null;
		}

		customerUserWxMiniappByOpenid.setCustomerUserId(0L);
		return customerUserWxMpDao.save(customerUserWxMiniappByOpenid);
	}

}
