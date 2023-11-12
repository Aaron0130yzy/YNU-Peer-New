package io.naccoll.boilerplate.customer.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.naccoll.boilerplate.customer.dto.CustomerUserWxMpQueryCondition;
import io.naccoll.boilerplate.customer.model.CustomerUserWxMpPo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author NaccOll
 **/
public interface CustomerUserWxMpQueryService {

	/**
	 * 查询数据分页
	 * @param condition 条件
	 * @param pageable 分页参数
	 * @return Page<CustomerUserMpPo>
	 */
	Page<CustomerUserWxMpPo> page(CustomerUserWxMpQueryCondition condition, Pageable pageable);

	/**
	 * 查询所有数据不分页
	 * @param condition 条件参数
	 * @return List<CustomerUserMpPo>
	 */
	List<CustomerUserWxMpPo> findAll(CustomerUserWxMpQueryCondition condition);

	/**
	 * 根据ID查询
	 * @param id CustomerUserMpID
	 * @return CustomerUserMpPo
	 */
	CustomerUserWxMpPo findByIdNotNull(Long id);

	/**
	 * 根据ID集合查询列表
	 * @param ids CustomerUserMpID集合
	 * @return List<CustomerUserMpPo>
	 */
	List<CustomerUserWxMpPo> findByIds(Collection<Long> ids);

	/**
	 * 根据ID集合查询列表
	 * @param ids CustomerUserMpID集合
	 * @return List<CustomerUserMpPo>
	 */
	Map<Long, CustomerUserWxMpPo> findMapByIds(Collection<Long> ids);

	/**
	 * 通过第三方应用Id与openid查询微信用户
	 * @param thirdId 第三方应用id
	 * @param openid 微信用户openid
	 * @return
	 */
	CustomerUserWxMpPo findByThirdIdAndOpenid(Long thirdId, String openid);

	/**
	 * 通过第三方应用Id与openid查询微信用户
	 * @param thirdId 第三方应用id
	 * @param userId 微信用户openid
	 * @return
	 */
	CustomerUserWxMpPo findByThirdIdAndUserId(Long thirdId, Long userId);

}
