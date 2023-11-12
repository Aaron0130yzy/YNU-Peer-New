
package io.naccoll.boilerplate.customer.service;

import java.util.Collection;

import io.naccoll.boilerplate.customer.dto.CustomerUserBindThirdCreateCommand;
import io.naccoll.boilerplate.customer.dto.CustomerUserBindThirdUpdateCommand;
import io.naccoll.boilerplate.customer.model.CustomerUserBindThirdPo;

/**
 * @author NaccOll
 */
public interface CustomerUserBindThirdService {

	/**
	 * 创建用户绑定的第三方应用接口
	 * @param command 创建参数
	 * @return CustomerUserBindThirdPo
	 */
	CustomerUserBindThirdPo create(CustomerUserBindThirdCreateCommand command);

	/**
	 * 更新用户绑定的第三方应用接口
	 * @param command 更新参数
	 * @return CustomerUserBindThirdPo
	 */
	CustomerUserBindThirdPo update(CustomerUserBindThirdUpdateCommand command);

	/**
	 * 删除用户绑定的第三方应用接口
	 * @param id 用户绑定的第三方应用接口Id
	 */
	void deleteById(Long id);

	/**
	 * 批量删除用户绑定的第三方应用接口
	 * @param ids 用户绑定的第三方应用接口Id集合
	 */
	void deleteByIds(Collection<Long> ids);

}
