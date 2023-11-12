package io.naccoll.boilerplate.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RebindDto {

	@Schema(description = "重置后的token")
	private String accessToken;

	@Schema(description = "新的用户信息")
	private CustomerUserWithBindDto info;

}
