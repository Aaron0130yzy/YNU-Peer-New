package io.naccoll.boilerplate.customer.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.naccoll.boilerplate.core.exception.ResourceNotFoundException;
import io.naccoll.boilerplate.core.persistence.dao.DaoUtils;
import io.naccoll.boilerplate.customer.dao.CustomerUserBindHistoryDao;
import io.naccoll.boilerplate.customer.dto.CustomerUserBindHistoryQueryCondition;
import io.naccoll.boilerplate.customer.model.CustomerUserBindHistoryPo;
import io.naccoll.boilerplate.customer.service.CustomerUserBindHistoryQueryService;
import jakarta.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author NaccOll
 */
@Service
public class CustomerUserBindHistoryQueryServiceImpl implements CustomerUserBindHistoryQueryService {

	@Resource
	private CustomerUserBindHistoryDao customerUserBindHistoryDao;

	@Override
	public Page<CustomerUserBindHistoryPo> page(CustomerUserBindHistoryQueryCondition condition, Pageable pageable) {
		Page<CustomerUserBindHistoryPo> page = customerUserBindHistoryDao.page(condition, pageable);
		return page;
	}

	@Override
	public List<CustomerUserBindHistoryPo> findAll(CustomerUserBindHistoryQueryCondition condition) {
		return customerUserBindHistoryDao.findAll(condition);
	}

	@Override
	public CustomerUserBindHistoryPo findByIdNotNull(Long id) {
		CustomerUserBindHistoryPo customerUserBindHistory = customerUserBindHistoryDao.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("用户绑定历史不存在"));
		return customerUserBindHistory;
	}

	@Override
	public List<CustomerUserBindHistoryPo> findByIds(Collection<Long> ids) {
		return DaoUtils.findByPrimaryKeyIn(customerUserBindHistoryDao, ids);
	}

	@Override
	public Map<Long, CustomerUserBindHistoryPo> findMapByIds(Collection<Long> ids) {
		return DaoUtils.findMapByPrimaryKeyIn(customerUserBindHistoryDao, ids);
	}

}
