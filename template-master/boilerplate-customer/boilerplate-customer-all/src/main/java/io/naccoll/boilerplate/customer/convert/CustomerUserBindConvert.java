package io.naccoll.boilerplate.customer.convert;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.naccoll.boilerplate.customer.dto.CustomerUserBindDto;
import io.naccoll.boilerplate.customer.model.CustomerUserBindPo;
import io.naccoll.boilerplate.customer.model.CustomerUserBindThirdPo;
import io.naccoll.boilerplate.customer.service.CustomerUserBindThirdQueryService;
import jakarta.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

/**
 * @author NaccOll
 */
@Component
public class CustomerUserBindConvert {

	@Resource
	private CustomerUserBindThirdQueryService customerUserBindThirdQueryService;

	public CustomerUserBindDto convertCustomerUserBindDto(CustomerUserBindPo po) {
		return convertCustomerUserBindDtoList(List.of(po)).get(0);
	}

	public List<CustomerUserBindDto> convertCustomerUserBindDtoList(List<CustomerUserBindPo> list) {
		Set<Long> thirdIds = list.stream().map(i -> i.getThirdId()).collect(Collectors.toSet());
		;
		Map<Long, CustomerUserBindThirdPo> thirdMap = customerUserBindThirdQueryService.findMapByIds(thirdIds);
		List<CustomerUserBindDto> dtos = list.stream().map(po -> {
			CustomerUserBindDto dto = new CustomerUserBindDto();
			BeanUtils.copyProperties(po, dto);
			CustomerUserBindThirdPo third = thirdMap.get(po.getThirdId());
			if (third != null) {
				dto.setThirdName(third.getThirdName());
				dto.setThirdTypeName(third.getThirdTypeName());
			}
			return dto;
		}).toList();
		return dtos;
	}

	public Page<CustomerUserBindDto> convertCustomerUserBindDtoPage(Page<CustomerUserBindPo> page) {
		List<CustomerUserBindDto> list = convertCustomerUserBindDtoList(page.getContent());
		return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
	}

}
