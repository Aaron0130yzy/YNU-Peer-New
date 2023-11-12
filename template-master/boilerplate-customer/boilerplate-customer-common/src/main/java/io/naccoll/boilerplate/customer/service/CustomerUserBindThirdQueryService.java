package io.naccoll.boilerplate.customer.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.naccoll.boilerplate.customer.dto.CustomerUserBindThirdQueryCondition;
import io.naccoll.boilerplate.customer.model.CustomerUserBindThirdPo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author NaccOll
 **/
public interface CustomerUserBindThirdQueryService {

	/**
	 * 查询数据分页
	 * @param condition 条件
	 * @param pageable 分页参数
	 * @return Page<CustomerUserBindThirdPo>
	 */
	Page<CustomerUserBindThirdPo> page(CustomerUserBindThirdQueryCondition condition, Pageable pageable);

	/**
	 * 查询所有数据不分页
	 * @param condition 条件参数
	 * @return List<CustomerUserBindThirdPo>
	 */
	List<CustomerUserBindThirdPo> findAll(CustomerUserBindThirdQueryCondition condition);

	/**
	 * 根据ID查询
	 * @param id CustomerUserBindThirdID
	 * @return CustomerUserBindThirdPo
	 */
	CustomerUserBindThirdPo findById(Long id);

	/**
	 * 根据ID查询, 为空会抛出异常
	 * @param id CustomerUserBindThirdID
	 * @return CustomerUserBindThirdPo
	 */
	CustomerUserBindThirdPo findByIdNotNull(Long id);

	/**
	 * 根据ID集合查询列表
	 * @param ids CustomerUserBindThirdID集合
	 * @return List<CustomerUserBindThirdPo>
	 */
	List<CustomerUserBindThirdPo> findByIds(Collection<Long> ids);

	/**
	 * 根据ID集合查询列表
	 * @param ids CustomerUserBindThirdID集合
	 * @return List<CustomerUserBindThirdPo>
	 */
	Map<Long, CustomerUserBindThirdPo> findMapByIds(Collection<Long> ids);

}
