package io.naccoll.boilerplate.customer.convert;

import java.util.List;

import io.naccoll.boilerplate.customer.dto.CustomerUserWxMpDto;
import io.naccoll.boilerplate.customer.model.CustomerUserWxMpPo;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

/**
 * @author NaccOll
 */
@Component
public class CustomerUserWxMpConvert {

	public CustomerUserWxMpDto convertCustomerUserWxMpDto(CustomerUserWxMpPo po) {
		return convertCustomerUserWxMpDtoList(List.of(po)).get(0);
	}

	public List<CustomerUserWxMpDto> convertCustomerUserWxMpDtoList(List<CustomerUserWxMpPo> list) {
		List<CustomerUserWxMpDto> customerUserWxMpDtos = list.stream().map(po -> {
			CustomerUserWxMpDto dto = new CustomerUserWxMpDto();
			BeanUtils.copyProperties(po, dto);
			return dto;
		}).toList();
		return customerUserWxMpDtos;
	}

	public Page<CustomerUserWxMpDto> convertCustomerUserWxMpDtoPage(Page<CustomerUserWxMpPo> page) {
		List<CustomerUserWxMpDto> customerUserWxMpDtos = convertCustomerUserWxMpDtoList(page.getContent());
		return new PageImpl<>(customerUserWxMpDtos, page.getPageable(), page.getTotalElements());
	}

}
