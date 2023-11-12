
package io.naccoll.boilerplate.customer.service;

import io.naccoll.boilerplate.customer.dto.CustomerUserBindCreateCommand;
import io.naccoll.boilerplate.customer.dto.CustomerUserBindUpdateCommand;
import io.naccoll.boilerplate.customer.model.CustomerUserBindPo;

/**
 * @author NaccOll
 */
public interface CustomerUserBindService {

	/**
	 * 绑定
	 * @param customerUserId 用户id
	 * @param thirdId 第三方应用id
	 * @param thirdUserId 第三方用户id
	 * @return 绑定关系
	 */
	CustomerUserBindPo bind(Long customerUserId, Long thirdId, String thirdUserId);

	/**
	 * 绑定并同步
	 * @param customerUserId
	 * @param thirdId
	 * @param thirdUserId
	 * @return
	 */
	CustomerUserBindPo bindSync(Long customerUserId, Long thirdId, String thirdUserId);

	/**
	 * 解绑
	 * @param customerUserId 用户id
	 * @param thirdId 第三方应用id
	 * @return 绑定关系
	 */
	void unbind(Long customerUserId, Long thirdId);

	/**
	 * 创建用户绑定关系
	 * @param command 创建参数
	 * @return CustomerUserBindPo
	 */
	CustomerUserBindPo create(CustomerUserBindCreateCommand command);

	/**
	 * 更新用户绑定关系
	 * @param command 更新参数
	 * @return CustomerUserBindPo
	 */
	CustomerUserBindPo update(CustomerUserBindUpdateCommand command);

	/**
	 * 删除用户绑定关系
	 * @param id 用户绑定关系Id
	 */
	void deleteById(Long id);

}
