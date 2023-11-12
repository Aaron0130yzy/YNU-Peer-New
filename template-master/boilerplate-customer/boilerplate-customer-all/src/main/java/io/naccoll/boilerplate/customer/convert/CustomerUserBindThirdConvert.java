package io.naccoll.boilerplate.customer.convert;

import java.util.List;

import io.naccoll.boilerplate.customer.dto.CustomerUserBindThirdDto;
import io.naccoll.boilerplate.customer.model.CustomerUserBindThirdPo;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

/**
 * @author NaccOll
 */
@Component
public class CustomerUserBindThirdConvert {

	public CustomerUserBindThirdDto convertCustomerUserBindThirdDto(CustomerUserBindThirdPo po) {
		return convertCustomerUserBindThirdDtoList(List.of(po)).get(0);
	}

	public List<CustomerUserBindThirdDto> convertCustomerUserBindThirdDtoList(List<CustomerUserBindThirdPo> list) {
		List<CustomerUserBindThirdDto> customerUserBindThirdDtos = list.stream().map(po -> {
			CustomerUserBindThirdDto dto = new CustomerUserBindThirdDto();
			BeanUtils.copyProperties(po, dto);
			return dto;
		}).toList();
		return customerUserBindThirdDtos;
	}

	public Page<CustomerUserBindThirdDto> convertCustomerUserBindThirdDtoPage(Page<CustomerUserBindThirdPo> page) {
		List<CustomerUserBindThirdDto> customerUserBindThirdDtos = convertCustomerUserBindThirdDtoList(
				page.getContent());
		return new PageImpl<>(customerUserBindThirdDtos, page.getPageable(), page.getTotalElements());
	}

}
