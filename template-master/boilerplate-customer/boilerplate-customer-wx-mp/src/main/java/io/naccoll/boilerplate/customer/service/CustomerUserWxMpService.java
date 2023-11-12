
package io.naccoll.boilerplate.customer.service;

import java.util.Collection;

import io.naccoll.boilerplate.customer.dto.CustomerUserWxMpCreateCommand;
import io.naccoll.boilerplate.customer.dto.CustomerUserWxMpUpdateCommand;
import io.naccoll.boilerplate.customer.model.CustomerUserWxMpPo;

/**
 * @author NaccOll
 */
public interface CustomerUserWxMpService {

	/**
	 * 创建微信公众号用户接口
	 * @param command 创建参数
	 * @return CustomerUserMpPo
	 */
	CustomerUserWxMpPo create(CustomerUserWxMpCreateCommand command);

	/**
	 * 更新微信公众号用户接口
	 * @param command 更新参数
	 * @return CustomerUserMpPo
	 */
	CustomerUserWxMpPo update(CustomerUserWxMpUpdateCommand command);

	/**
	 * 删除微信公众号用户接口
	 * @param id 微信公众号用户接口Id
	 */
	void deleteById(Long id);

	/**
	 * 批量删除微信公众号用户接口
	 * @param ids 微信公众号用户接口Id集合
	 */
	void deleteByIds(Collection<Long> ids);

	/**
	 * 绑定
	 * @param userId 用户id
	 * @param thirdId 第三方id
	 * @param openid 第三方用户标识
	 * @return 绑定结果
	 */
	CustomerUserWxMpPo bind(Long userId, Long thirdId, String openid);

	/**
	 * 解绑
	 * @param userId 用户id
	 * @param thirdId 第三方id
	 * @param openid 第三方用户标识
	 * @return 解绑结果
	 */
	CustomerUserWxMpPo unbind(Long userId, Long thirdId, String openid);

}
