package io.naccoll.boilerplate.customer.dao;

import java.util.List;

import io.naccoll.boilerplate.core.persistence.dao.BaseDao;
import io.naccoll.boilerplate.core.persistence.specification.SpecificationFactory;
import io.naccoll.boilerplate.customer.dto.CustomerUserBindQueryCondition;
import io.naccoll.boilerplate.customer.model.CustomerUserBindPo;
import io.naccoll.boilerplate.customer.model.CustomerUserBindPo_;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author NaccOll
 */
public interface CustomerUserBindDao extends BaseDao<CustomerUserBindPo, Long> {

	/**
	 * 统计用户绑定第三方的数量
	 * @param userId 用户id
	 * @return 用户绑定第三方的数量
	 */
	long countByCustomerUserId(Long userId);

	/**
	 * 根据第三方标识查询用户
	 * @param thirdId 第三方id
	 * @param thirdUserId 第三方用户标识
	 * @return
	 */
	CustomerUserBindPo findFirstByThirdIdAndThirdUserId(Long thirdId, String thirdUserId);

	/**
	 * 根据第三方标识查询用户
	 * @param thirdId 第三方id
	 * @param userId 第三方用户标识
	 * @return
	 */
	CustomerUserBindPo findFirstByThirdIdAndCustomerUserId(Long thirdId, Long userId);

	/**
	 * 查询CustomerUserBind列表
	 * @param condition CustomerUserBind查询条件
	 * @return CustomerUserBind列表
	 */
	default List<CustomerUserBindPo> findAll(CustomerUserBindQueryCondition condition) {
		Specification<CustomerUserBindPo> spec = buildSpecification(condition);
		return findAll(spec);
	}

	/**
	 * 查询CustomerUserBind分页
	 * @param condition CustomerUserBind查询条件
	 * @param pageable
	 * @return CustomerUserBind分页
	 */
	default Page<CustomerUserBindPo> page(CustomerUserBindQueryCondition condition, Pageable pageable) {
		Specification<CustomerUserBindPo> spec = buildSpecification(condition);
		return findAll(spec, pageable);
	}

	default Specification<CustomerUserBindPo> buildSpecification(CustomerUserBindQueryCondition condition) {
		Specification<CustomerUserBindPo> spec = SpecificationFactory.builder();
		if (condition.getCustomerUserId() != null) {
			spec = spec
				.and(SpecificationFactory.equal(CustomerUserBindPo_.CUSTOMER_USER_ID, condition.getCustomerUserId()));
		}
		return spec;
	}

}
