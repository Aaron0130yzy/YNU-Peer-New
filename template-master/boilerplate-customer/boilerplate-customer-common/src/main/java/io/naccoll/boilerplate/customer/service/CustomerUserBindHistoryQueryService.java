package io.naccoll.boilerplate.customer.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.naccoll.boilerplate.customer.dto.CustomerUserBindHistoryQueryCondition;
import io.naccoll.boilerplate.customer.model.CustomerUserBindHistoryPo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author NaccOll
 **/
public interface CustomerUserBindHistoryQueryService {

	/**
	 * 查询数据分页
	 * @param condition 条件
	 * @param pageable 分页参数
	 * @return Page<CustomerUserBindHistoryPo>
	 */
	Page<CustomerUserBindHistoryPo> page(CustomerUserBindHistoryQueryCondition condition, Pageable pageable);

	/**
	 * 查询所有数据不分页
	 * @param condition 条件参数
	 * @return List<CustomerUserBindHistoryPo>
	 */
	List<CustomerUserBindHistoryPo> findAll(CustomerUserBindHistoryQueryCondition condition);

	/**
	 * 根据ID查询
	 * @param id CustomerUserBindHistoryID
	 * @return CustomerUserBindHistoryPo
	 */
	CustomerUserBindHistoryPo findByIdNotNull(Long id);

	/**
	 * 根据ID集合查询列表
	 * @param ids CustomerUserBindHistoryID集合
	 * @return List<CustomerUserBindHistoryPo>
	 */
	List<CustomerUserBindHistoryPo> findByIds(Collection<Long> ids);

	/**
	 * 根据ID集合查询列表
	 * @param ids CustomerUserBindHistoryID集合
	 * @return List<CustomerUserBindHistoryPo>
	 */
	Map<Long, CustomerUserBindHistoryPo> findMapByIds(Collection<Long> ids);

}
