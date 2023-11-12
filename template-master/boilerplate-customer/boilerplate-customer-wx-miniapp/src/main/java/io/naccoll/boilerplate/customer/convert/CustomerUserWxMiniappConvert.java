package io.naccoll.boilerplate.customer.convert;

import java.util.List;

import io.naccoll.boilerplate.customer.dto.CustomerUserWxMiniappDto;
import io.naccoll.boilerplate.customer.model.CustomerUserWxMiniappPo;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

/**
 * 微信小程序用户转换器
 *
 * @author NaccOll
 */
@Component
public class CustomerUserWxMiniappConvert {

	public CustomerUserWxMiniappDto convertCustomerUserWxMiniappDto(CustomerUserWxMiniappPo po) {
		return convertCustomerUserWxMiniappDtoList(List.of(po)).get(0);
	}

	public List<CustomerUserWxMiniappDto> convertCustomerUserWxMiniappDtoList(List<CustomerUserWxMiniappPo> list) {
		List<CustomerUserWxMiniappDto> customerUserWxMiniappDtos = list.stream().map(po -> {
			CustomerUserWxMiniappDto dto = new CustomerUserWxMiniappDto();
			BeanUtils.copyProperties(po, dto);
			return dto;
		}).toList();
		return customerUserWxMiniappDtos;
	}

	public Page<CustomerUserWxMiniappDto> convertCustomerUserWxMiniappDtoPage(Page<CustomerUserWxMiniappPo> page) {
		List<CustomerUserWxMiniappDto> customerUserWxMiniappDtos = convertCustomerUserWxMiniappDtoList(
				page.getContent());
		return new PageImpl<>(customerUserWxMiniappDtos, page.getPageable(), page.getTotalElements());
	}

}
