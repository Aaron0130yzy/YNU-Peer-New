package io.naccoll.boilerplate.customer.dao;

import java.util.List;

import io.naccoll.boilerplate.core.persistence.dao.BaseDao;
import io.naccoll.boilerplate.core.persistence.specification.SpecificationFactory;
import io.naccoll.boilerplate.customer.dto.CustomerUserWxMiniappQueryCondition;
import io.naccoll.boilerplate.customer.model.CustomerUserWxMiniappPo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * 微信小程序用户数据库访问层
 *
 * @author NaccOll
 */
public interface CustomerUserWxMiniappDao extends BaseDao<CustomerUserWxMiniappPo, Long> {

	CustomerUserWxMiniappPo findFirstByThirdIdAndOpenid(Long thirdId, String openid);

	CustomerUserWxMiniappPo findFirstByThirdIdAndCustomerUserId(Long thirdId, Long userId);

	/**
	 * 查询CustomerUserMpMiniapp列表
	 * @param condition CustomerUserMpMiniapp查询条件
	 * @return CustomerUserMpMiniapp列表
	 */
	default List<CustomerUserWxMiniappPo> findAll(CustomerUserWxMiniappQueryCondition condition) {
		Specification<CustomerUserWxMiniappPo> spec = buildSpecification(condition);
		return findAll(spec);
	}

	/**
	 * 查询CustomerUserMpMiniapp分页
	 * @param condition CustomerUserMpMiniapp查询条件
	 * @param pageable
	 * @return CustomerUserMpMiniapp分页
	 */
	default Page<CustomerUserWxMiniappPo> page(CustomerUserWxMiniappQueryCondition condition, Pageable pageable) {
		Specification<CustomerUserWxMiniappPo> spec = buildSpecification(condition);
		return findAll(spec, pageable);
	}

	default Specification<CustomerUserWxMiniappPo> buildSpecification(CustomerUserWxMiniappQueryCondition condition) {
		Specification<CustomerUserWxMiniappPo> spec = SpecificationFactory.builder();
		return spec;
	}

}
