package io.naccoll.boilerplate.customer.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.naccoll.boilerplate.core.exception.ResourceNotFoundException;
import io.naccoll.boilerplate.core.persistence.dao.DaoUtils;
import io.naccoll.boilerplate.customer.dao.CustomerUserWxMiniappDao;
import io.naccoll.boilerplate.customer.dto.CustomerUserWxMiniappQueryCondition;
import io.naccoll.boilerplate.customer.model.CustomerUserWxMiniappPo;
import io.naccoll.boilerplate.customer.service.CustomerUserWxMiniappQueryService;
import jakarta.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 微信小程序用户查询服务实现
 *
 * @author NaccOll
 */
@Service
public class CustomerUserWxMiniappQueryServiceImpl implements CustomerUserWxMiniappQueryService {

	@Resource
	private CustomerUserWxMiniappDao customerUserWxMiniappDao;

	@Override
	public Page<CustomerUserWxMiniappPo> page(CustomerUserWxMiniappQueryCondition condition, Pageable pageable) {
		Page<CustomerUserWxMiniappPo> customerUserMpMiniappPage = customerUserWxMiniappDao.page(condition, pageable);
		return customerUserMpMiniappPage;
	}

	@Override
	public List<CustomerUserWxMiniappPo> findAll(CustomerUserWxMiniappQueryCondition condition) {
		return customerUserWxMiniappDao.findAll(condition);
	}

	@Override
	public CustomerUserWxMiniappPo findById(Long id) {
		CustomerUserWxMiniappPo customerUserMpMiniappPo = customerUserWxMiniappDao.findById(id).orElse(null);
		return customerUserMpMiniappPo;
	}

	@Override
	public CustomerUserWxMiniappPo findByIdNotNull(Long id) {
		CustomerUserWxMiniappPo customerUserMpMiniappPo = customerUserWxMiniappDao.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("微信小程序用户不存在"));
		return customerUserMpMiniappPo;
	}

	@Override
	public List<CustomerUserWxMiniappPo> findByIds(Collection<Long> ids) {
		return DaoUtils.findByPrimaryKeyIn(customerUserWxMiniappDao, ids);
	}

	@Override
	public Map<Long, CustomerUserWxMiniappPo> findMapByIds(Collection<Long> ids) {
		return DaoUtils.findMapByPrimaryKeyIn(customerUserWxMiniappDao, ids);
	}

	@Override
	public CustomerUserWxMiniappPo findByThirdIdAndOpenid(Long thirdId, String openId) {
		return customerUserWxMiniappDao.findFirstByThirdIdAndOpenid(thirdId, openId);
	}

	@Override
	public CustomerUserWxMiniappPo findByThirdIdAndUserId(Long thirdId, Long userId) {
		return customerUserWxMiniappDao.findFirstByThirdIdAndCustomerUserId(thirdId, userId);
	}

}
