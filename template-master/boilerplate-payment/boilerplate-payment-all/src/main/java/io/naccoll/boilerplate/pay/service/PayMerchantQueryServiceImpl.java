package io.naccoll.boilerplate.pay.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.naccoll.boilerplate.core.exception.ResourceNotFoundException;
import io.naccoll.boilerplate.core.persistence.dao.DaoUtils;
import io.naccoll.boilerplate.pay.dao.PayMerchantDao;
import io.naccoll.boilerplate.pay.dto.PayMerchantQueryCondition;
import io.naccoll.boilerplate.pay.model.PayMerchantPo;
import jakarta.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * The type Pay merchant query service.
 */
@Service
public class PayMerchantQueryServiceImpl implements PayMerchantQueryService {

	@Resource
	private PayMerchantDao payMerchantDao;

	/**
	 * Find by id not null pay merchant po.
	 * @param id the id
	 * @return the pay merchant po
	 */
	public PayMerchantPo findByIdNotNull(Long id) {
		PayMerchantPo payMerchantPo = payMerchantDao.findById(id).orElse(null);
		if (payMerchantPo == null) {
			throw new ResourceNotFoundException("不存在该支付商户");
		}
		return payMerchantPo;
	}

	/**
	 * Find map by ids map.
	 * @param ids the ids
	 * @return the map
	 */
	public Map<Long, PayMerchantPo> findMapByIds(Collection<Long> ids) {
		return DaoUtils.findMapByPrimaryKeyIn(payMerchantDao, ids);
	}

	/**
	 * Find by ids list.
	 * @param ids the ids
	 * @return the list
	 */
	public List<PayMerchantPo> findByIds(Collection<Long> ids) {
		return DaoUtils.findByPrimaryKeyIn(payMerchantDao, ids);
	}

	/**
	 * Page page.
	 * @param pageable the pageable
	 * @param condition the condition
	 * @return the page
	 */
	public Page<PayMerchantPo> page(Pageable pageable, PayMerchantQueryCondition condition) {
		return payMerchantDao.page(pageable, condition);
	}

}
