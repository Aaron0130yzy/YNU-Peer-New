package io.naccoll.boilerplate.customer.service.impl;

import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RadixUtil;
import io.naccoll.boilerplate.core.audit.login.LoginSuccessEvent;
import io.naccoll.boilerplate.core.audit.operate.OperateLog;
import io.naccoll.boilerplate.core.cache.CacheTemplate;
import io.naccoll.boilerplate.core.captch.CaptchaProvider;
import io.naccoll.boilerplate.core.exception.BusinessError;
import io.naccoll.boilerplate.core.exception.BusinessException;
import io.naccoll.boilerplate.core.exception.ClientParamException;
import io.naccoll.boilerplate.core.id.IdService;
import io.naccoll.boilerplate.core.security.entity.CaptchaCodeLoginAuthenticationToken;
import io.naccoll.boilerplate.core.security.entity.UserDetailsImpl;
import io.naccoll.boilerplate.core.security.enums.ClientId;
import io.naccoll.boilerplate.core.security.enums.Realm;
import io.naccoll.boilerplate.core.security.enums.SecurityDataType;
import io.naccoll.boilerplate.core.security.service.DataSecurityService;
import io.naccoll.boilerplate.core.security.service.RealmUserDetailsCaptchaCodeAuthorizationService;
import io.naccoll.boilerplate.core.security.service.SysPasswordService;
import io.naccoll.boilerplate.core.security.storage.AuthenticationManager;
import io.naccoll.boilerplate.core.security.utils.JwtUtils;
import io.naccoll.boilerplate.core.validate.Validation;
import io.naccoll.boilerplate.customer.constant.CustomerOperateConstant;
import io.naccoll.boilerplate.customer.convert.CustomerUserConvert;
import io.naccoll.boilerplate.customer.dao.CustomerUserDao;
import io.naccoll.boilerplate.customer.dto.BindResultDto;
import io.naccoll.boilerplate.customer.dto.CustomerUserCreateCommand;
import io.naccoll.boilerplate.customer.dto.CustomerUserUpdateCommand;
import io.naccoll.boilerplate.customer.dto.RebindCommand;
import io.naccoll.boilerplate.customer.dto.RebindDto;
import io.naccoll.boilerplate.customer.model.CustomerUserBindPo;
import io.naccoll.boilerplate.customer.model.CustomerUserPo;
import io.naccoll.boilerplate.customer.service.CustomerUserBindQueryService;
import io.naccoll.boilerplate.customer.service.CustomerUserBindService;
import io.naccoll.boilerplate.customer.service.CustomerUserQueryService;
import io.naccoll.boilerplate.customer.service.CustomerUserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * @author NaccOll
 */
@Service
@Validation
public class CustomerUserServiceImpl implements CustomerUserService, RealmUserDetailsCaptchaCodeAuthorizationService {

	@Resource
	private CustomerUserDao customerUserDao;

	@Resource
	private CustomerUserQueryService customerUserQueryService;

	@Resource
	private CustomerUserBindQueryService customerUserBindQueryService;

	@Resource
	private DataSecurityService dataSecurityService;

	@Resource
	private IdService idService;

	@Resource
	private SysPasswordService sysPasswordService;

	@Resource
	private CustomerUserBindService customerUserBindService;

	@Resource
	private CaptchaProvider captchaProvider;

	@Resource
	private CacheTemplate cacheTemplate;

	@Resource
	private JwtUtils jwtUtils;

	@Resource
	private AuthenticationManager authenticationManager;

	@Resource
	private ApplicationEventPublisher applicationEventPublisher;

	@Resource
	private CustomerUserConvert customerUserConvert;

	@Resource
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public CustomerUserPo create(@Valid CustomerUserCreateCommand command) {
		CustomerUserPo customerUserPo = new CustomerUserPo();
		BeanUtils.copyProperties(command, customerUserPo);
		Long id = idService.getId();
		customerUserPo.setId(id);
		Date date = DateUtil.date().offset(DateField.YEAR, 100);
		if (StringUtils.hasText(customerUserPo.getPassword())) {
			customerUserPo.setPassword(passwordEncoder.encode(customerUserPo.getPassword()));
		}
		if (StringUtils.hasText(command.getTel())) {
			customerUserPo.setTel(dataSecurityService.encryptStr(command.getTel(), SecurityDataType.PHONE));
		}
		if (customerUserPo.getCredentialsExpiredDate() != null) {
			customerUserPo.setCredentialsExpiredDate(date);
		}
		if (command.getAccountExpiredDate() == null) {
			customerUserPo.setAccountExpiredDate(date);
		}
		if (ObjectUtils.isEmpty(command.getUsername())) {
			String username = "u_" + RadixUtil.encode(RadixUtil.RADIXS_59, idService.getId());
			customerUserPo.setNickname(username);
		}
		if (ObjectUtils.isEmpty(command.getNickname())) {
			String username = "u_" + RadixUtil.encode(RadixUtil.RADIXS_59, idService.getId());
			customerUserPo.setUsername(username);
		}
		return customerUserDao.save(customerUserPo);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public CustomerUserPo update(@Valid CustomerUserUpdateCommand command) {
		CustomerUserPo customerUserPo = customerUserQueryService.findByIdNotNull(command.getId());
		String tel = command.getTel();
		String originTel = customerUserPo.getTel();
		BeanUtils.copyProperties(command, customerUserPo);
		if (StringUtils.hasText(tel) && !tel.contains("*")) {
			customerUserPo.setTel(dataSecurityService.encryptStr(tel, SecurityDataType.PHONE));
		}
		else {
			customerUserPo.setTel(originTel);
		}
		return customerUserDao.save(customerUserPo);
	}

	@Override
	public CustomerUserPo updateStatus(Long id, boolean status) {
		CustomerUserPo customerUserPo = customerUserQueryService.findByIdNotNull(id);
		customerUserPo.setEnabled(status);
		return customerUserDao.save(customerUserPo);
	}

	public CustomerUserPo bindTel(Long id, String tel) {
		CustomerUserPo customerUserPo = customerUserQueryService.findByIdNotNull(id);
		customerUserPo.setTel(dataSecurityService.encryptStr(tel, SecurityDataType.PHONE));
		return customerUserDao.save(customerUserPo);
	}

	@Override
	public CustomerUserPo unbindTel(Long id, boolean force) {
		CustomerUserPo customerUserPo = customerUserQueryService.findByIdNotNull(id);
		if (!StringUtils.hasText(customerUserPo.getTel())) {
			throw new ClientParamException("当前用户尚未绑定手机号");
		}
		long bindCount = customerUserBindQueryService.countByCustomerUserId(id);
		if (bindCount == 0 && !force) {
			throw new BusinessException("当前用户尚未绑定其他第三方，解绑手机号后用户无法再次登录", BusinessError.NEED_CONFIRM);
		}
		customerUserPo.setTel("");
		return customerUserDao.save(customerUserPo);
	}

	@Override
	@OperateLog(value = "删除用户", id = "#id")
	public void deleteById(Long id) {
		customerUserDao.deleteById(id);
	}

	@Override
	public void deleteByIds(Collection<Long> ids) {
		ids.forEach(id -> {
			customerUserDao.deleteById(id);
		});
	}

	@Override
	public Realm getRealm() {
		return Realm.CUSTOMER;
	}

	@Override
	public UserDetailsImpl loadUserByUsername(String receive, CaptchaCodeLoginAuthenticationToken authentication)
			throws UsernameNotFoundException {
		if (ObjectUtils.isEmpty(authentication.getCredentials())) {
			throw new BadCredentialsException("验证码错误");
		}
		if (captchaProvider.validateCode(CustomerOperateConstant.CUSTOMER_CODE_LOGIN, receive,
				authentication.getCredentials().toString()) != 0L) {
			throw new BadCredentialsException("验证码错误");
		}
		CustomerUserPo customerUserPo = customerUserQueryService.findByTel(receive);
		return getDefaultUser(customerUserPo, receive, authentication.getClientId());
	}

	private UserDetailsImpl getDefaultUser(CustomerUserPo customerUserPo, String tel, ClientId clientId) {
		if (customerUserPo == null) {
			CustomerUserCreateCommand command = new CustomerUserCreateCommand();
			command.setTel(tel);
			command.setRegisterChannel(clientId.getId());
			customerUserPo = create(command);
		}

		return UserDetailsImpl.builder()
			.id(customerUserPo.getId().toString())
			.username(customerUserPo.getUsername())
			.password(customerUserPo.getPassword())
			.realm(Realm.CUSTOMER)
			.clientId(clientId)
			.enabled(customerUserPo.getEnabled())
			.accountNonLocked(customerUserPo.getAccountNonLocked())
			.accountNonExpired(customerUserPo.getAccountNonExpired())
			.credentialsNonExpired(customerUserPo.getCredentialsNonExpired())
			.build();
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public BindResultDto bindTel(String tel, UserDetailsImpl userDetails) {
		Long userDetailsId = Long.parseLong(userDetails.getId());
		Long thirdId = userDetails.getThirdId();
		String thirdUserId = userDetails.getThirdUserId();
		String thirdRealId = userDetails.getThirdRealId();
		String ip = userDetails.getIp();
		CustomerUserPo telUser = customerUserQueryService.findByTel(tel);
		if (telUser != null) {
			if (Objects.equals(telUser.getId(), userDetailsId)) {
				throw new ClientParamException("要绑定的手机号与当前绑定手机相同");
			}
			else {
				if (thirdId != null) {
					CustomerUserBindPo bind = customerUserBindQueryService
						.findByThirdIdAndUserId(userDetails.getThirdId(), telUser.getId());
					if (bind == null) {
						String secret = UUID.randomUUID().toString();
						CustomerUserPo customerUser = customerUserQueryService.findByTel(tel);
						userDetails = getDefaultUser(customerUser, tel, userDetails.getClientId());
						userDetails.setThirdId(thirdId);
						userDetails.setThirdUserId(thirdUserId);
						userDetails.setThirdRealId(thirdRealId);
						userDetails.setIp(ip);
						customerUserBindService.unbind(userDetailsId, thirdId);
						customerUserBindService.bindSync(telUser.getId(), thirdId, userDetails.getThirdUserId());
						String jwtToken = jwtUtils.createUserToken(userDetails, secret);
						authenticationManager.login(userDetails);
						applicationEventPublisher.publishEvent(new LoginSuccessEvent(ip, userDetails));
						BindResultDto bindResultDto = new BindResultDto();
						bindResultDto.setSuccess(true);
						bindResultDto.setAnother(customerUserConvert.convertCustomerUserWithBindDto(telUser));
						bindResultDto.setAccessToken(jwtToken);
						return bindResultDto;
					}
				}
				String secure = UUID.randomUUID().toString().replaceAll("-", "");
				String key = String.format("%s:%s", CustomerOperateConstant.CUSTOMER_RESET_BIND, secure);
				cacheTemplate.set(key, telUser.getId(), Duration.ofMinutes(5));
				BindResultDto bindResultDto = new BindResultDto();
				bindResultDto.setSuccess(false);
				bindResultDto.setAnother(customerUserConvert.convertCustomerUserWithBindDto(telUser));
				bindResultDto.setSecurity(secure);
				return bindResultDto;
			}
		}
		this.bindTel(userDetailsId, tel);
		BindResultDto bindResultDto = new BindResultDto();
		bindResultDto.setSuccess(true);
		return bindResultDto;
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public RebindDto rebind(RebindCommand command) {
		String key = String.format("%s:%s", CustomerOperateConstant.CUSTOMER_RESET_BIND, command.getSecure());
		Object o = cacheTemplate.get(key);
		if (o == null) {
			throw new ClientParamException("凭据已过期，请重新操作");
		}
		cacheTemplate.delete(key);
		Long oldUserId = Long.parseLong(o.toString());
		UserDetailsImpl userDetails = command.getUserDetails();
		Long userDetailsId = Long.parseLong(userDetails.getId());
		String secret = UUID.randomUUID().toString();
		if (Objects.equals(oldUserId, command.getUserId())) {
			CustomerUserPo oldUser = customerUserQueryService.findByIdNotNull(oldUserId);
			UserDetailsImpl oldUserDetails = getDefaultUser(oldUser, null, userDetails.getClientId());
			oldUserDetails.setThirdId(userDetails.getThirdId());
			oldUserDetails.setThirdUserId(userDetails.getThirdUserId());
			oldUserDetails.setThirdRealId(userDetails.getThirdRealId());
			oldUserDetails.setIp(userDetails.getIp());
			customerUserBindService.unbind(oldUserId, oldUserDetails.getThirdId());
			customerUserBindService.unbind(userDetailsId, userDetails.getThirdId());
			customerUserBindService.bindSync(oldUser.getId(), oldUserDetails.getThirdId(),
					oldUserDetails.getThirdUserId());
			String jwtToken = jwtUtils.createUserToken(oldUserDetails, secret);
			authenticationManager.login(oldUserDetails);
			applicationEventPublisher.publishEvent(new LoginSuccessEvent(oldUserDetails.getIp(), oldUserDetails));
			RebindDto bindResultDto = new RebindDto();
			bindResultDto.setAccessToken(jwtToken);
			bindResultDto.setInfo(customerUserConvert.convertCustomerUserWithBindDto(oldUser));
			return bindResultDto;
		}
		else if (Objects.equals(userDetailsId, command.getUserId())) {
			CustomerUserPo user = customerUserQueryService.findByIdNotNull(oldUserId);
			String tel = dataSecurityService.decryptStr(user.getTel());
			unbindTel(oldUserId, true);
			bindTel(userDetailsId, tel);
			return new RebindDto();
		}
		else {
			throw new ClientParamException("不支持的换绑操作");
		}
	}

	@Override
	public void rebindTel(String tel, UserDetailsImpl userDetails) {
		Long userDetailsId = Long.parseLong(userDetails.getId());
		CustomerUserPo user = customerUserQueryService.findByTel(tel);
		if (user != null) {
			if (Objects.equals(user.getId(), userDetailsId)) {
				throw new ClientParamException("要绑定的手机号与当前绑定手机相同");
			}
			throw new ClientParamException("该手机号已经被注册");
		}
		this.bindTel(userDetailsId, tel);
	}

	@Override
	public CustomerUserPo updateAvatar(Long userId, String avatar) {
		CustomerUserPo user = customerUserQueryService.findByIdNotNull(userId);
		user.setAvatar(avatar);
		return customerUserDao.save(user);
	}

}
