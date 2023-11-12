package io.naccoll.boilerplate.customer.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.naccoll.boilerplate.core.exception.ResourceNotFoundException;
import io.naccoll.boilerplate.core.persistence.dao.DaoUtils;
import io.naccoll.boilerplate.customer.dao.CustomerUserBindThirdDao;
import io.naccoll.boilerplate.customer.dto.CustomerUserBindThirdQueryCondition;
import io.naccoll.boilerplate.customer.model.CustomerUserBindThirdPo;
import io.naccoll.boilerplate.customer.service.CustomerUserBindThirdQueryService;
import jakarta.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author NaccOll
 */
@Service
public class CustomerUserBindThirdQueryServiceImpl implements CustomerUserBindThirdQueryService {

	@Resource
	private CustomerUserBindThirdDao customerUserBindThirdDao;

	@Override
	public Page<CustomerUserBindThirdPo> page(CustomerUserBindThirdQueryCondition condition, Pageable pageable) {
		Page<CustomerUserBindThirdPo> customerUserBindThirdPage = customerUserBindThirdDao.page(condition, pageable);
		return customerUserBindThirdPage;
	}

	@Override
	public List<CustomerUserBindThirdPo> findAll(CustomerUserBindThirdQueryCondition condition) {
		return customerUserBindThirdDao.findAll(condition);
	}

	@Override
	public CustomerUserBindThirdPo findById(Long id) {
		CustomerUserBindThirdPo customerUserBindThird = customerUserBindThirdDao.findById(id).orElse(null);
		return customerUserBindThird;
	}

	@Override
	public CustomerUserBindThirdPo findByIdNotNull(Long id) {
		CustomerUserBindThirdPo customerUserBindThird = customerUserBindThirdDao.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("用户绑定的第三方应用接口不存在"));
		return customerUserBindThird;
	}

	@Override
	public List<CustomerUserBindThirdPo> findByIds(Collection<Long> ids) {
		return DaoUtils.findByPrimaryKeyIn(customerUserBindThirdDao, ids);
	}

	@Override
	public Map<Long, CustomerUserBindThirdPo> findMapByIds(Collection<Long> ids) {
		return DaoUtils.findMapByPrimaryKeyIn(customerUserBindThirdDao, ids);
	}

}
