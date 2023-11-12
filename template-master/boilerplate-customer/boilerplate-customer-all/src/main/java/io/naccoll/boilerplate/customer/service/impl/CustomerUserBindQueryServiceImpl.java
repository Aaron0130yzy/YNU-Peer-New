package io.naccoll.boilerplate.customer.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.naccoll.boilerplate.core.exception.ResourceNotFoundException;
import io.naccoll.boilerplate.core.persistence.dao.DaoUtils;
import io.naccoll.boilerplate.customer.dao.CustomerUserBindDao;
import io.naccoll.boilerplate.customer.dto.CustomerUserBindQueryCondition;
import io.naccoll.boilerplate.customer.model.CustomerUserBindPo;
import io.naccoll.boilerplate.customer.service.CustomerUserBindQueryService;
import jakarta.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author NaccOll
 */
@Service
public class CustomerUserBindQueryServiceImpl implements CustomerUserBindQueryService {

	@Resource
	private CustomerUserBindDao customerUserBindDao;

	@Override
	public Page<CustomerUserBindPo> page(CustomerUserBindQueryCondition condition, Pageable pageable) {
		Page<CustomerUserBindPo> page = customerUserBindDao.page(condition, pageable);
		return page;
	}

	@Override
	public List<CustomerUserBindPo> findAll(CustomerUserBindQueryCondition condition) {
		return customerUserBindDao.findAll(condition);
	}

	@Override
	public CustomerUserBindPo findByIdNotNull(Long id) {
		CustomerUserBindPo customerUserBind = customerUserBindDao.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("用户绑定关系不存在"));
		return customerUserBind;
	}

	@Override
	public List<CustomerUserBindPo> findByIds(Collection<Long> ids) {
		return DaoUtils.findByPrimaryKeyIn(customerUserBindDao, ids);
	}

	@Override
	public Map<Long, CustomerUserBindPo> findMapByIds(Collection<Long> ids) {
		return DaoUtils.findMapByPrimaryKeyIn(customerUserBindDao, ids);
	}

	@Override
	public long countByCustomerUserId(Long userId) {
		return customerUserBindDao.countByCustomerUserId(userId);
	}

	@Override
	public CustomerUserBindPo findByThirdIdAndThirdUserId(Long thirdId, String userId) {
		return customerUserBindDao.findFirstByThirdIdAndThirdUserId(thirdId, userId);
	}

	@Override
	public CustomerUserBindPo findByThirdIdAndUserId(Long thirdId, Long userId) {
		return customerUserBindDao.findFirstByThirdIdAndCustomerUserId(thirdId, userId);
	}

}
