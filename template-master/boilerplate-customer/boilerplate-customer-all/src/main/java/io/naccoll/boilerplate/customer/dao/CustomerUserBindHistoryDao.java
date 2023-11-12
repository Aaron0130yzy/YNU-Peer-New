package io.naccoll.boilerplate.customer.dao;

import java.util.List;

import io.naccoll.boilerplate.core.persistence.dao.BaseDao;
import io.naccoll.boilerplate.core.persistence.specification.SpecificationFactory;
import io.naccoll.boilerplate.customer.dto.CustomerUserBindHistoryQueryCondition;
import io.naccoll.boilerplate.customer.model.CustomerUserBindHistoryPo;
import io.naccoll.boilerplate.customer.model.CustomerUserBindHistoryPo_;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author NaccOll
 */
public interface CustomerUserBindHistoryDao extends BaseDao<CustomerUserBindHistoryPo, Long> {

	/**
	 * 查询CustomerUserBindHistory列表
	 * @param condition CustomerUserBindHistory查询条件
	 * @return CustomerUserBindHistory列表
	 */
	default List<CustomerUserBindHistoryPo> findAll(CustomerUserBindHistoryQueryCondition condition) {
		Specification<CustomerUserBindHistoryPo> spec = buildSpecification(condition);
		return findAll(spec);
	}

	/**
	 * 查询CustomerUserBindHistory分页
	 * @param condition CustomerUserBindHistory查询条件
	 * @param pageable
	 * @return CustomerUserBindHistory分页
	 */
	default Page<CustomerUserBindHistoryPo> page(CustomerUserBindHistoryQueryCondition condition, Pageable pageable) {
		Specification<CustomerUserBindHistoryPo> spec = buildSpecification(condition);
		return findAll(spec, pageable);
	}

	default Specification<CustomerUserBindHistoryPo> buildSpecification(
			CustomerUserBindHistoryQueryCondition condition) {
		Specification<CustomerUserBindHistoryPo> spec = SpecificationFactory.builder();
		if (condition.getCustomerUserId() != null) {
			spec = spec.and(SpecificationFactory.equal(CustomerUserBindHistoryPo_.CUSTOMER_USER_ID,
					condition.getCustomerUserId()));
		}
		return spec;
	}

}
