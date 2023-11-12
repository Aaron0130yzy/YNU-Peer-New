
package io.naccoll.boilerplate.customer.service;

import java.util.Collection;

import io.naccoll.boilerplate.customer.dto.CustomerUserBindHistoryCreateCommand;
import io.naccoll.boilerplate.customer.dto.CustomerUserBindHistoryUpdateCommand;
import io.naccoll.boilerplate.customer.model.CustomerUserBindHistoryPo;

/**
 * @author NaccOll
 */
public interface CustomerUserBindHistoryService {

	/**
	 * 绑定
	 * @param customerUserId 用户id
	 * @param thirdId 第三方Id
	 * @param thirdUserId 第三方用户Id
	 * @return 绑定历史
	 */
	CustomerUserBindHistoryPo bind(Long customerUserId, Long thirdId, String thirdUserId);

	/**
	 * 解除绑定
	 * @param customerUserId 用户Id
	 * @param thirdId 第三方Id
	 * @param thirdUserId 第三方用户Id
	 * @return
	 */
	CustomerUserBindHistoryPo unbind(Long customerUserId, Long thirdId, String thirdUserId);

	/**
	 * 创建用户绑定历史
	 * @param command 创建参数
	 * @return CustomerUserBindHistoryPo
	 */
	CustomerUserBindHistoryPo create(CustomerUserBindHistoryCreateCommand command);

	/**
	 * 更新用户绑定历史
	 * @param command 更新参数
	 * @return CustomerUserBindHistoryPo
	 */
	CustomerUserBindHistoryPo update(CustomerUserBindHistoryUpdateCommand command);

	/**
	 * 删除用户绑定历史
	 * @param id 用户绑定历史Id
	 */
	void deleteById(Long id);

	/**
	 * 批量删除用户绑定历史
	 * @param ids 用户绑定历史Id集合
	 */
	void deleteByIds(Collection<Long> ids);

}
