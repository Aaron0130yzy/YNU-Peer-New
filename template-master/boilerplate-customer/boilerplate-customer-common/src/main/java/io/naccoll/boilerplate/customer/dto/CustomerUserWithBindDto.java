package io.naccoll.boilerplate.customer.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author NaccOll
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerUserWithBindDto extends CustomerUserDto {

	@Schema(description = "绑定关系")
	private List<CustomerUserBindDto> binds;

}
