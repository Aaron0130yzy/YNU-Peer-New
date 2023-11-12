package io.naccoll.boilerplate.customer.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.naccoll.boilerplate.customer.dto.CustomerUserQueryCondition;
import io.naccoll.boilerplate.customer.model.CustomerUserPo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author NaccOll
 **/
public interface CustomerUserQueryService {

	/**
	 * 查询数据分页
	 * @param condition 条件
	 * @param pageable 分页参数
	 * @return Page<CustomerUserPo>
	 */
	Page<CustomerUserPo> page(CustomerUserQueryCondition condition, Pageable pageable);

	/**
	 * 查询所有数据不分页
	 * @param condition 条件参数
	 * @return List<CustomerUserPo>
	 */
	List<CustomerUserPo> findAll(CustomerUserQueryCondition condition);

	/**
	 * 根据ID查询
	 * @param id CustomerUserID
	 * @return CustomerUserPo
	 */
	CustomerUserPo findById(Long id);

	/**
	 * 根据ID查询
	 * @param id CustomerUserID
	 * @return CustomerUserPo
	 */
	CustomerUserPo findByIdNotNull(Long id);

	/**
	 * 根据ID集合查询列表
	 * @param ids CustomerUserID集合
	 * @return List<CustomerUserPo>
	 */
	List<CustomerUserPo> findByIds(Collection<Long> ids);

	/**
	 * 根据ID集合查询列表
	 * @param ids CustomerUserID集合
	 * @return List<CustomerUserPo>
	 */
	Map<Long, CustomerUserPo> findMapByIds(Collection<Long> ids);

	/**
	 * 通过手机号查询用户
	 * @param tel 手机号
	 * @return 用户
	 */
	CustomerUserPo findByTel(String tel);

}
