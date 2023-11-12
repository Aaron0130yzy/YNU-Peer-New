package io.naccoll.boilerplate.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BindResultDto {

	@Schema(description = "绑定是否成功")
	private boolean success;

	@Schema(description = "关联的其他用户")
	private CustomerUserWithBindDto another;

	@Schema(description = "换绑所需的密钥")
	private String security;

	@Schema(description = "新令牌")
	private String accessToken;

}
