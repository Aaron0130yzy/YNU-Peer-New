
package io.naccoll.boilerplate.customer.service;

import java.util.Collection;

import io.naccoll.boilerplate.customer.dto.CustomerUserWxMiniappCreateCommand;
import io.naccoll.boilerplate.customer.dto.CustomerUserWxMiniappUpdateCommand;
import io.naccoll.boilerplate.customer.model.CustomerUserWxMiniappPo;

/**
 * 微信小程序用户服务
 *
 * @author NaccOll
 */
public interface CustomerUserWxMiniappService {

	/**
	 * 创建微信小程序用户
	 * @param command 创建参数
	 * @return CustomerUserMpMiniappPo
	 */
	CustomerUserWxMiniappPo create(CustomerUserWxMiniappCreateCommand command);

	/**
	 * 更新微信小程序用户
	 * @param command 更新参数
	 * @return CustomerUserMpMiniappPo
	 */
	CustomerUserWxMiniappPo update(CustomerUserWxMiniappUpdateCommand command);

	/**
	 * 删除微信小程序用户
	 * @param id 微信小程序用户Id
	 */
	void deleteById(Long id);

	/**
	 * 批量删除微信小程序用户
	 * @param ids 微信小程序用户Id集合
	 */
	void deleteByIds(Collection<Long> ids);

	/**
	 * 绑定
	 * @param userId 用户id
	 * @param thirdId 第三方id
	 * @param openid 第三方用户标识
	 * @return 绑定结果
	 */
	CustomerUserWxMiniappPo bind(Long userId, Long thirdId, String openid);

	/**
	 * 解绑
	 * @param userId 用户id
	 * @param thirdId 第三方id
	 * @param openid 第三方用户标识
	 * @return 解绑结果
	 */
	CustomerUserWxMiniappPo unbind(Long userId, Long thirdId, String openid);

}
