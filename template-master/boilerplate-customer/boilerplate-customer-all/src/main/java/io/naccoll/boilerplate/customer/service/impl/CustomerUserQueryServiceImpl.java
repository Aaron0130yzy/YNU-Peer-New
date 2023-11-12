package io.naccoll.boilerplate.customer.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.naccoll.boilerplate.core.exception.ResourceNotFoundException;
import io.naccoll.boilerplate.core.persistence.dao.DaoUtils;
import io.naccoll.boilerplate.core.security.entity.RequestAuthenticationToken;
import io.naccoll.boilerplate.core.security.entity.UserDetailsImpl;
import io.naccoll.boilerplate.core.security.enums.Realm;
import io.naccoll.boilerplate.core.security.enums.SecurityDataType;
import io.naccoll.boilerplate.core.security.service.DataSecurityService;
import io.naccoll.boilerplate.core.security.service.RealmUserDetailsAuthenticationService;
import io.naccoll.boilerplate.customer.dao.CustomerUserDao;
import io.naccoll.boilerplate.customer.dto.CustomerUserQueryCondition;
import io.naccoll.boilerplate.customer.model.CustomerUserPo;
import io.naccoll.boilerplate.customer.service.CustomerUserQueryService;
import jakarta.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author NaccOll
 */
@Service
public class CustomerUserQueryServiceImpl implements CustomerUserQueryService, RealmUserDetailsAuthenticationService {

	@Resource
	private CustomerUserDao customerUserDao;

	@Resource
	private DataSecurityService dataSecurityService;

	@Override
	public Page<CustomerUserPo> page(CustomerUserQueryCondition condition, Pageable pageable) {
		if (StringUtils.hasText(condition.getTel())) {
			condition.setTel(dataSecurityService.encryptStr(condition.getTel(), SecurityDataType.PHONE));
		}
		Page<CustomerUserPo> page = customerUserDao.page(condition, pageable);
		return page;
	}

	@Override
	public List<CustomerUserPo> findAll(CustomerUserQueryCondition condition) {
		if (StringUtils.hasText(condition.getTel())) {
			condition.setTel(dataSecurityService.encryptStr(condition.getTel(), SecurityDataType.PHONE));
		}
		return customerUserDao.findAll(condition);
	}

	@Override
	public CustomerUserPo findById(Long id) {
		CustomerUserPo customerUser = customerUserDao.findById(id).orElse(null);
		return customerUser;
	}

	@Override
	public CustomerUserPo findByIdNotNull(Long id) {
		CustomerUserPo customerUser = customerUserDao.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
		return customerUser;
	}

	@Override
	public List<CustomerUserPo> findByIds(Collection<Long> ids) {
		return DaoUtils.findByPrimaryKeyIn(customerUserDao, ids);
	}

	@Override
	public Map<Long, CustomerUserPo> findMapByIds(Collection<Long> ids) {
		return DaoUtils.findMapByPrimaryKeyIn(customerUserDao, ids);
	}

	public CustomerUserPo findByTel(String tel) {
		return customerUserDao.findFirstByTel(dataSecurityService.encryptStr(tel, SecurityDataType.PHONE));
	}

	@Override
	public Realm getRealm() {
		return Realm.CUSTOMER;
	}

	@Override
	public UserDetailsImpl loadUserByUsername(String username, RequestAuthenticationToken authentication)
			throws UsernameNotFoundException {
		UserDetailsImpl u = authentication.getDetails();
		CustomerUserPo customerUserPo = customerUserDao.findById(Long.parseLong(u.getId())).orElse(null);
		if (customerUserPo == null) {
			throw new UsernameNotFoundException("请重新登录");
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
			.build();
	}

}
