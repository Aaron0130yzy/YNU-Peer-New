package io.naccoll.boilerplate.customer.convert;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.naccoll.boilerplate.customer.dto.CustomerUserBindHistoryDto;
import io.naccoll.boilerplate.customer.model.CustomerUserBindHistoryPo;
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
public class CustomerUserBindHistoryConvert {

	@Resource
	private CustomerUserBindThirdQueryService customerUserBindThirdQueryService;

	public CustomerUserBindHistoryDto convertCustomerUserBindHistoryDto(CustomerUserBindHistoryPo po) {
		return convertCustomerUserBindHistoryDtoList(List.of(po)).get(0);
	}

	public List<CustomerUserBindHistoryDto> convertCustomerUserBindHistoryDtoList(
			List<CustomerUserBindHistoryPo> list) {
		Set<Long> thirdIds = list.stream().map(CustomerUserBindHistoryPo::getThirdId).collect(Collectors.toSet());
		Map<Long, CustomerUserBindThirdPo> thirdMap = customerUserBindThirdQueryService.findMapByIds(thirdIds);
		List<CustomerUserBindHistoryDto> dtos = list.stream().map(po -> {
			CustomerUserBindHistoryDto dto = new CustomerUserBindHistoryDto();
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

	public Page<CustomerUserBindHistoryDto> convertCustomerUserBindHistoryDtoPage(
			Page<CustomerUserBindHistoryPo> page) {
		List<CustomerUserBindHistoryDto> list = convertCustomerUserBindHistoryDtoList(page.getContent());
		return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
	}

}
