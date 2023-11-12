package io.naccoll.boilerplate.pay.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import io.naccoll.boilerplate.core.exception.BusinessException;
import io.naccoll.boilerplate.core.persistence.dao.DaoUtils;
import io.naccoll.boilerplate.pay.dao.PayJournalDao;
import io.naccoll.boilerplate.pay.dto.PayJournalQueryCondition;
import io.naccoll.boilerplate.pay.model.PayJournalPo;
import io.naccoll.boilerplate.pay.model.PayJournalPo_;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * The type Pay journal query service.
 */
@Service
public class PayJournalQueryServiceImpl implements PayJournalQueryService {

	@Resource
	private PayJournalDao payJournalDao;

	public PayJournalPo findMerchantJournal(Long merchantId, String orderNo) {
		return payJournalDao.findFirstByPayMerchantIdAndOrderNo(merchantId, orderNo);
	}

	public List<PayJournalPo> findByOrderNo(String orderNo) {
		return payJournalDao.findByOrderNo(orderNo);
	}

	/**
	 * Find by id not null pay journal po.
	 * @param id the id
	 * @return the pay journal po
	 */
	public PayJournalPo findByIdNotNull(Long id) {
		return payJournalDao.findById(id).orElseThrow(() -> new BusinessException("支付流水不存在"));
	}

	/**
	 * Query map by ids map.
	 * @param ids the ids
	 * @return the map
	 */
	public Map<Long, PayJournalPo> queryMapByIds(Set<Long> ids) {
		return DaoUtils.findMapByPrimaryKeyIn(payJournalDao, ids);
	}

	public Page<PayJournalPo> queryPage(PayJournalQueryCondition condition, Pageable pageable) {
		Specification<PayJournalPo> specification = (root, criteriaQuery, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();
			List<Expression<Boolean>> expressions = predicate.getExpressions();
			if (condition.getChannel() != null) {
				expressions.add(criteriaBuilder.equal(root.get(PayJournalPo_.CHANNEL), condition.getChannel()));
			}
			if (condition.getPayStatus() != null) {
				expressions.add(criteriaBuilder.equal(root.get(PayJournalPo_.PAY_STATUS), condition.getPayStatus()));
			}
			if (condition.getPayMerchantId() != null) {
				expressions
					.add(criteriaBuilder.equal(root.get(PayJournalPo_.PAY_MERCHANT_ID), condition.getPayMerchantId()));
			}
			if (condition.getRefundStatus() != null) {
				expressions
					.add(criteriaBuilder.equal(root.get(PayJournalPo_.REFUND_STATUS), condition.getRefundStatus()));
			}
			if (condition.getOrganizationId() != null) {
				expressions
					.add(criteriaBuilder.equal(root.get(PayJournalPo_.ORGANIZATION_ID), condition.getOrganizationId()));
			}
			if (condition.getOrderNo() != null) {
				expressions.add(criteriaBuilder.like(root.get(PayJournalPo_.ORDER_NO), condition.getOrderNo() + "%"));
			}
			return predicate;
		};
		return payJournalDao.findAll(specification, pageable);
	}

}
