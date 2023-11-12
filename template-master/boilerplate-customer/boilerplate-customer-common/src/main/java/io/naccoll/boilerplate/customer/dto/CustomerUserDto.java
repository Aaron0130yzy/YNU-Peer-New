package io.naccoll.boilerplate.customer.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.naccoll.boilerplate.customer.model.CustomerUserPo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

/**
 * @author NaccOll
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerUserDto extends CustomerUserPo {

	@JsonIgnore
	public String password;

	@Schema(description = "是否已经设置密码")
	public boolean getHasPassword() {
		return StringUtils.hasText(getPassword());
	}

}
