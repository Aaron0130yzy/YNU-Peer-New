
package io.naccoll.boilerplate.customer.service;

import java.util.Collection;

import io.naccoll.boilerplate.core.security.entity.UserDetailsImpl;
import io.naccoll.boilerplate.customer.dto.BindResultDto;
import io.naccoll.boilerplate.customer.dto.CustomerUserCreateCommand;
import io.naccoll.boilerplate.customer.dto.CustomerUserUpdateCommand;
import io.naccoll.boilerplate.customer.dto.RebindCommand;
import io.naccoll.boilerplate.customer.dto.RebindDto;
import io.naccoll.boilerplate.customer.model.CustomerUserPo;

/**
 * @author NaccOll
 */
public interface CustomerUserService {

	/**
	 * 创建用户
	 * @param command 创建参数
	 * @return CustomerUserPo
	 */
	CustomerUserPo create(CustomerUserCreateCommand command);

	/**
	 * 更新用户
	 * @param command 更新参数
	 * @return CustomerUserPo
	 */
	CustomerUserPo update(CustomerUserUpdateCommand command);

	/**
	 * 变更用户状态
	 * @param id 用户id
	 * @param status 状态
	 * @return
	 */
	CustomerUserPo updateStatus(Long id, boolean status);

	/**
	 * 解绑手机号
	 * @param id 用户id
	 * @param force 是否强制，不管用户之后是否能够登录
	 * @return 用户
	 */
	CustomerUserPo unbindTel(Long id, boolean force);

	/**
	 * 删除用户
	 * @param id 用户Id
	 */
	void deleteById(Long id);

	/**
	 * 批量删除用户
	 * @param ids 用户Id集合
	 */
	void deleteByIds(Collection<Long> ids);

	/**
	 * 绑定手机号
	 * @param tel 手机号
	 * @param userDetails 当前用户信息
	 * @return
	 */
	BindResultDto bindTel(String tel, UserDetailsImpl userDetails);

	/**
	 * 绑定冲突时进行的在绑定操作
	 * @param command
	 * @return
	 */
	RebindDto rebind(RebindCommand command);

	/**
	 * 绑定手机号
	 * @param tel 手机号
	 * @param userDetails 当前用户信息
	 * @return
	 */
	void rebindTel(String tel, UserDetailsImpl userDetails);

	/**
	 * 更新用户头像
	 * @param userId 用户id
	 * @param avatar 头像路径
	 * @return 用户信息
	 */
	CustomerUserPo updateAvatar(Long userId, String avatar);

}
