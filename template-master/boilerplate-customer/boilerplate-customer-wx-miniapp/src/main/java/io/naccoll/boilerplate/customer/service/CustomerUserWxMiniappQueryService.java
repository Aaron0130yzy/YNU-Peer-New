package io.naccoll.boilerplate.customer.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.naccoll.boilerplate.customer.dto.CustomerUserWxMiniappQueryCondition;
import io.naccoll.boilerplate.customer.model.CustomerUserWxMiniappPo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 微信小程序用户查询服务
 *
 * @author NaccOll
 **/
public interface CustomerUserWxMiniappQueryService {

	/**
	 * 查询数据分页
	 * @param condition 条件
	 * @param pageable 分页参数
	 * @return Page<CustomerUserMpMiniappPo>
	 */
	Page<CustomerUserWxMiniappPo> page(CustomerUserWxMiniappQueryCondition condition, Pageable pageable);

	/**
	 * 查询所有数据不分页
	 * @param condition 条件参数
	 * @return List<CustomerUserMpMiniappPo>
	 */
	List<CustomerUserWxMiniappPo> findAll(CustomerUserWxMiniappQueryCondition condition);

	/**
	 * 根据ID查询
	 * @param id CustomerUserMpMiniappID
	 * @return CustomerUserMpMiniappPo
	 */
	CustomerUserWxMiniappPo findById(Long id);

	/**
	 * 根据ID查询, 为空会抛出异常
	 * @param id CustomerUserMpMiniappID
	 * @return CustomerUserMpMiniappPo
	 */
	CustomerUserWxMiniappPo findByIdNotNull(Long id);

	/**
	 * 根据ID集合查询列表
	 * @param ids CustomerUserMpMiniappID集合
	 * @return List<CustomerUserMpMiniappPo>
	 */
	List<CustomerUserWxMiniappPo> findByIds(Collection<Long> ids);

	/**
	 * 根据ID集合查询列表
	 * @param ids CustomerUserMpMiniappID集合
	 * @return List<CustomerUserMpMiniappPo>
	 */
	Map<Long, CustomerUserWxMiniappPo> findMapByIds(Collection<Long> ids);

	/**
	 * 根据第三方Id与openid查询对应的微信小程序用户
	 * @param thirdId 第三方Id
	 * @param openId 微信小程序用户openid
	 * @return
	 */
	CustomerUserWxMiniappPo findByThirdIdAndOpenid(Long thirdId, String openId);

	/**
	 * 根据第三方id和用户id查询绑定了微信小程序的用户
	 * @param thirdId 第三方id
	 * @param userId 用户id
	 * @return
	 */
	CustomerUserWxMiniappPo findByThirdIdAndUserId(Long thirdId, Long userId);

}
