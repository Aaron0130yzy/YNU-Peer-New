package io.naccoll.boilerplate.customer.dto;

import io.naccoll.boilerplate.core.security.entity.UserDetailsImpl;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RebindCommand {

	@Schema(description = "密钥信息")
	private String secure;

	@Schema(description = "需绑定的用户id，当选择当前帐号时，老账号的手机号将绑定到当前账号，当选择老帐号的时候，当前渠道的第三方绑定到老帐号上，当前账号注销")
	private Long userId;

	@Schema(hidden = true)
	private UserDetailsImpl userDetails;

}
