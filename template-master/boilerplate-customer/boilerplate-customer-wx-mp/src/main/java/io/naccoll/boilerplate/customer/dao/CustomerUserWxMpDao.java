package io.naccoll.boilerplate.customer.dao;

import java.util.List;

import io.naccoll.boilerplate.core.persistence.dao.BaseDao;
import io.naccoll.boilerplate.core.persistence.specification.SpecificationFactory;
import io.naccoll.boilerplate.customer.dto.CustomerUserWxMpQueryCondition;
import io.naccoll.boilerplate.customer.model.CustomerUserWxMpPo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author NaccOll
 */
public interface CustomerUserWxMpDao extends BaseDao<CustomerUserWxMpPo, Long> {

	CustomerUserWxMpPo findFirstByThirdIdAndOpenid(Long thirdId, String openid);

	CustomerUserWxMpPo findFirstByThirdIdAndCustomerUserId(Long thirdId, Long userId);

	/**
	 * 查询CustomerUserMp列表
	 * @param condition CustomerUserMp查询条件
	 * @return CustomerUserMp列表
	 */
	default List<CustomerUserWxMpPo> findAll(CustomerUserWxMpQueryCondition condition) {
		Specification<CustomerUserWxMpPo> spec = buildSpecification(condition);
		return findAll(spec);
	}

	/**
	 * 查询CustomerUserMp分页
	 * @param condition CustomerUserMp查询条件
	 * @param pageable
	 * @return CustomerUserMp分页
	 */
	default Page<CustomerUserWxMpPo> page(CustomerUserWxMpQueryCondition condition, Pageable pageable) {
		Specification<CustomerUserWxMpPo> spec = buildSpecification(condition);
		return findAll(spec, pageable);
	}

	default Specification<CustomerUserWxMpPo> buildSpecification(CustomerUserWxMpQueryCondition condition) {
		Specification<CustomerUserWxMpPo> spec = SpecificationFactory.builder();
		return spec;
	}

}
