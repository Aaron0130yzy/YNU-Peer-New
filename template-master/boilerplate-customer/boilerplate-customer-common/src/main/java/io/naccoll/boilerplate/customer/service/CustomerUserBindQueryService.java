package io.naccoll.boilerplate.customer.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.naccoll.boilerplate.customer.dto.CustomerUserBindQueryCondition;
import io.naccoll.boilerplate.customer.model.CustomerUserBindPo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author NaccOll
 **/
public interface CustomerUserBindQueryService {

	/**
	 * 查询数据分页
	 * @param condition 条件
	 * @param pageable 分页参数
	 * @return Page<CustomerUserBindPo>
	 */
	Page<CustomerUserBindPo> page(CustomerUserBindQueryCondition condition, Pageable pageable);

	/**
	 * 查询所有数据不分页
	 * @param condition 条件参数
	 * @return List<CustomerUserBindPo>
	 */
	List<CustomerUserBindPo> findAll(CustomerUserBindQueryCondition condition);

	/**
	 * 根据ID查询
	 * @param id CustomerUserBindID
	 * @return CustomerUserBindPo
	 */
	CustomerUserBindPo findByIdNotNull(Long id);

	/**
	 * 根据ID集合查询列表
	 * @param ids CustomerUserBindID集合
	 * @return List<CustomerUserBindPo>
	 */
	List<CustomerUserBindPo> findByIds(Collection<Long> ids);

	/**
	 * 根据ID集合查询列表
	 * @param ids CustomerUserBindID集合
	 * @return List<CustomerUserBindPo>
	 */
	Map<Long, CustomerUserBindPo> findMapByIds(Collection<Long> ids);

	/**
	 * 查询用户绑定关系的数量
	 * @param userId 用户id
	 * @return 用户绑定关系的数量
	 */
	long countByCustomerUserId(Long userId);

	/**
	 * 查询第三方绑定关系
	 * @param thirdId 第三方Id
	 * @param userId 第三方用户id
	 * @return
	 */
	CustomerUserBindPo findByThirdIdAndThirdUserId(Long thirdId, String userId);

	/**
	 * 查询第三方绑定关系
	 * @param thirdId 第三方Id
	 * @param userId 用户id
	 * @return
	 */
	CustomerUserBindPo findByThirdIdAndUserId(Long thirdId, Long userId);

}
