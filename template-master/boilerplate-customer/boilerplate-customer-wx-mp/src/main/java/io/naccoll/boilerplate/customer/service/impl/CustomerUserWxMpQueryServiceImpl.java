package io.naccoll.boilerplate.customer.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.naccoll.boilerplate.core.exception.ResourceNotFoundException;
import io.naccoll.boilerplate.core.persistence.dao.DaoUtils;
import io.naccoll.boilerplate.customer.dao.CustomerUserWxMpDao;
import io.naccoll.boilerplate.customer.dto.CustomerUserWxMpQueryCondition;
import io.naccoll.boilerplate.customer.model.CustomerUserWxMpPo;
import io.naccoll.boilerplate.customer.service.CustomerUserWxMpQueryService;
import jakarta.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author NaccOll
 */
@Service
public class CustomerUserWxMpQueryServiceImpl implements CustomerUserWxMpQueryService {

	@Resource
	private CustomerUserWxMpDao customerUserWxMpDao;

	@Override
	public Page<CustomerUserWxMpPo> page(CustomerUserWxMpQueryCondition condition, Pageable pageable) {
		Page<CustomerUserWxMpPo> customerUserMpPage = customerUserWxMpDao.page(condition, pageable);
		return customerUserMpPage;
	}

	@Override
	public List<CustomerUserWxMpPo> findAll(CustomerUserWxMpQueryCondition condition) {
		return customerUserWxMpDao.findAll(condition);
	}

	@Override
	public CustomerUserWxMpPo findByIdNotNull(Long id) {
		CustomerUserWxMpPo customerUserMpPo = customerUserWxMpDao.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("微信公众号用户接口不存在"));
		return customerUserMpPo;
	}

	@Override
	public List<CustomerUserWxMpPo> findByIds(Collection<Long> ids) {
		return DaoUtils.findByPrimaryKeyIn(customerUserWxMpDao, ids);
	}

	@Override
	public Map<Long, CustomerUserWxMpPo> findMapByIds(Collection<Long> ids) {
		return DaoUtils.findMapByPrimaryKeyIn(customerUserWxMpDao, ids);
	}

	public CustomerUserWxMpPo findByThirdIdAndOpenid(Long thirdId, String openid) {
		return customerUserWxMpDao.findFirstByThirdIdAndOpenid(thirdId, openid);
	}

	@Override
	public CustomerUserWxMpPo findByThirdIdAndUserId(Long thirdId, Long userId) {
		return customerUserWxMpDao.findFirstByThirdIdAndCustomerUserId(thirdId, userId);
	}

}
