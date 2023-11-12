package io.naccoll.boilerplate.customer.service.impl;

import java.util.Collection;
import java.util.Date;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import io.naccoll.boilerplate.core.exception.ClientParamException;
import io.naccoll.boilerplate.core.id.IdService;
import io.naccoll.boilerplate.core.security.entity.AuthCodeLoginAuthenticationToken;
import io.naccoll.boilerplate.core.security.entity.UserDetailsImpl;
import io.naccoll.boilerplate.core.security.enums.ClientId;
import io.naccoll.boilerplate.core.security.enums.Realm;
import io.naccoll.boilerplate.core.security.service.RealmUserDetailsAuthCodeAuthorizationService;
import io.naccoll.boilerplate.core.validate.Validation;
import io.naccoll.boilerplate.customer.dao.CustomerUserWxMiniappDao;
import io.naccoll.boilerplate.customer.dto.CustomerUserCreateCommand;
import io.naccoll.boilerplate.customer.dto.CustomerUserWxMiniappCreateCommand;
import io.naccoll.boilerplate.customer.dto.CustomerUserWxMiniappUpdateCommand;
import io.naccoll.boilerplate.customer.model.CustomerUserBindThirdPo;
import io.naccoll.boilerplate.customer.model.CustomerUserPo;
import io.naccoll.boilerplate.customer.model.CustomerUserWxMiniappPo;
import io.naccoll.boilerplate.customer.service.CustomerUserBindService;
import io.naccoll.boilerplate.customer.service.CustomerUserBindThirdQueryService;
import io.naccoll.boilerplate.customer.service.CustomerUserQueryService;
import io.naccoll.boilerplate.customer.service.CustomerUserService;
import io.naccoll.boilerplate.customer.service.CustomerUserWxMiniappQueryService;
import io.naccoll.boilerplate.customer.service.CustomerUserWxMiniappService;
import io.naccoll.boilerplate.customer.utils.WxMiniappServiceFactory;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.SneakyThrows;

import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 微信小程序用户服务实现
 *
 * @author NaccOll
 */
@Service
@Validation
public class CustomerUserWxMiniappServiceImpl
		implements CustomerUserWxMiniappService, RealmUserDetailsAuthCodeAuthorizationService {

	@Resource
	private CustomerUserWxMiniappDao customerUserWxMiniappDao;

	@Resource
	private CustomerUserWxMiniappQueryService customerUserWxMiniappQueryService;

	@Resource
	private CustomerUserQueryService customerUserQueryService;

	@Resource
	private CustomerUserService customerUserService;

	@Resource
	private CustomerUserBindThirdQueryService customerUserBindThirdQueryService;

	@Resource
	private CustomerUserBindService customerUserBindService;

	@Resource
	private WxMiniappServiceFactory wxMiniappServiceFactory;

	@Resource
	private IdService idService;

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public CustomerUserWxMiniappPo create(@Valid CustomerUserWxMiniappCreateCommand command) {
		CustomerUserWxMiniappPo customerUserMpMiniappPo = new CustomerUserWxMiniappPo();
		BeanUtils.copyProperties(command, customerUserMpMiniappPo);
		customerUserMpMiniappPo.setId(idService.getId());
		return customerUserWxMiniappDao.save(customerUserMpMiniappPo);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public CustomerUserWxMiniappPo update(@Valid CustomerUserWxMiniappUpdateCommand command) {
		CustomerUserWxMiniappPo customerUserMpMiniappPo = customerUserWxMiniappQueryService
			.findByIdNotNull(command.getId());
		BeanUtils.copyProperties(command, customerUserMpMiniappPo);
		return customerUserWxMiniappDao.save(customerUserMpMiniappPo);
	}

	@Override
	public void deleteById(Long id) {
		customerUserWxMiniappDao.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deleteByIds(Collection<Long> ids) {
		ids.forEach(id -> {
			deleteById(id);
		});
	}

	@Override
	public Realm getRealm() {
		return Realm.CUSTOMER;
	}

	@Override
	public ClientId getClientId() {
		return ClientId.MP_MINIAPP;
	}

	@SneakyThrows
	@Override
	public UserDetailsImpl loadUserByUsername(String code, AuthCodeLoginAuthenticationToken authentication)
			throws UsernameNotFoundException {
		Long thirdId = authentication.getThirdId();
		CustomerUserBindThirdPo thirdApp = customerUserBindThirdQueryService.findById(thirdId);
		if (thirdApp == null) {
			throw new BadCredentialsException("thirdId错误");
		}
		String appId = thirdApp.getThirdAppId();
		WxMaService wxMaService = wxMiniappServiceFactory.create(appId);
		if (wxMaService == null) {
			throw new BadCredentialsException("未配置appId的密钥");
		}
		WxMaJscode2SessionResult accessToken = wxMaService.getUserService().getSessionInfo(code);
		String openid = accessToken.getOpenid();
		CustomerUserWxMiniappPo customerUserMpMiniappPo = customerUserWxMiniappQueryService
			.findByThirdIdAndOpenid(thirdId, openid);
		CustomerUserPo customerUserPo = null;
		if (customerUserMpMiniappPo == null) {
			CustomerUserCreateCommand command = new CustomerUserCreateCommand();
			command.setRegisterChannel(authentication.getClientId().getId());
			customerUserPo = customerUserService.create(command);

			CustomerUserWxMiniappCreateCommand mpCreateCommand = new CustomerUserWxMiniappCreateCommand();
			mpCreateCommand.setCustomerUserId(customerUserPo.getId());
			mpCreateCommand.setThirdId(thirdId);
			mpCreateCommand.setOpenid(openid);
			mpCreateCommand.setUnionId(StringUtils.hasText(accessToken.getUnionid()) ? accessToken.getUnionid() : "");
			mpCreateCommand.setLastAuthDate(new Date());
			customerUserMpMiniappPo = create(mpCreateCommand);

			customerUserBindService.bind(customerUserMpMiniappPo.getCustomerUserId(), thirdId,
					customerUserMpMiniappPo.getOpenid());

		}
		customerUserPo = customerUserQueryService.findById(customerUserMpMiniappPo.getCustomerUserId());
		if (customerUserPo == null) {
			CustomerUserCreateCommand command = new CustomerUserCreateCommand();
			command.setRegisterChannel(authentication.getClientId().getId());
			customerUserPo = customerUserService.create(command);
			customerUserBindService.bindSync(customerUserPo.getId(), thirdId, customerUserMpMiniappPo.getOpenid());
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
	public CustomerUserWxMiniappPo bind(Long userId, Long thirdId, String openid) {
		CustomerUserWxMiniappPo customerUserWxMiniappByUserId = customerUserWxMiniappQueryService
			.findByThirdIdAndUserId(thirdId, userId);
		if (customerUserWxMiniappByUserId != null) {
			throw new ClientParamException("用户已绑定小程序，无法再次绑定");
		}
		CustomerUserWxMiniappPo customerUserWxMiniapp = customerUserWxMiniappQueryService
			.findByThirdIdAndOpenid(thirdId, openid);
		if (customerUserWxMiniapp != null) {
			customerUserWxMiniapp.setCustomerUserId(userId);
			return customerUserWxMiniappDao.save(customerUserWxMiniapp);
		}
		CustomerUserWxMiniappCreateCommand mpCreateCommand = new CustomerUserWxMiniappCreateCommand();
		mpCreateCommand.setCustomerUserId(userId);
		mpCreateCommand.setThirdId(thirdId);
		mpCreateCommand.setOpenid(openid);
		mpCreateCommand.setUnionId("");
		mpCreateCommand.setLastAuthDate(new Date());
		return create(mpCreateCommand);
	}

	@Override
	public CustomerUserWxMiniappPo unbind(Long userId, Long thirdId, String openid) {
		CustomerUserWxMiniappPo customerUserWxMiniappByOpenid = customerUserWxMiniappQueryService
			.findByThirdIdAndOpenid(thirdId, openid);
		if (customerUserWxMiniappByOpenid == null) {
			return null;
		}
		customerUserWxMiniappByOpenid.setCustomerUserId(0L);
		return customerUserWxMiniappDao.save(customerUserWxMiniappByOpenid);
	}

}
