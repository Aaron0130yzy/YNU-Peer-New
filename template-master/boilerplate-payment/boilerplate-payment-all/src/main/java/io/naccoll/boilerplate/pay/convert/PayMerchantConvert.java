package io.naccoll.boilerplate.pay.convert;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.naccoll.boilerplate.pay.dto.PayMerchantSimpleDto;
import io.naccoll.boilerplate.pay.model.PayMerchantPo;
import io.naccoll.boilerplate.sys.model.SysDepartPo;
import io.naccoll.boilerplate.sys.model.SysOrganizationPo;
import io.naccoll.boilerplate.sys.service.SysDepartQueryService;
import io.naccoll.boilerplate.sys.service.SysOrganizationQueryService;
import jakarta.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

/**
 * The type Pay merchant convert.
 */
@Component
public class PayMerchantConvert {

	@Resource
	private SysDepartQueryService sysDepartQueryService;

	@Resource
	private SysOrganizationQueryService sysOrganizationQueryService;

	/**
	 * Convert pay merchant dto pay merchant simple dto.
	 * @param po the po
	 * @return the pay merchant simple dto
	 */
	public PayMerchantSimpleDto convertPayMerchantDto(PayMerchantPo po) {
		return convertPayMerchantDtoList(Collections.singletonList(po)).get(0);
	}

	/**
	 * Convert pay merchant dto list list.
	 * @param list the list
	 * @return the list
	 */
	public List<PayMerchantSimpleDto> convertPayMerchantDtoList(List<PayMerchantPo> list) {
		List<Long> organizationIds = list.stream().map(i -> i.getOrganizationId()).collect(Collectors.toList());
		List<Long> departIds = list.stream().map(i -> i.getDepartId()).collect(Collectors.toList());
		Map<Long, SysDepartPo> departMap = sysDepartQueryService.findMapByIds(departIds);
		Map<Long, SysOrganizationPo> orgMap = sysOrganizationQueryService.findMapByIds(organizationIds);
		return list.stream().map(i -> {
			PayMerchantSimpleDto dto = new PayMerchantSimpleDto();
			BeanUtils.copyProperties(i, dto);
			SysOrganizationPo org = orgMap.get(i.getOrganizationId());
			if (org != null) {
				dto.setOrganizationName(org.getName());
			}
			SysDepartPo depart = departMap.get(i.getDepartId());
			if (departMap.containsKey(i.getDepartId())) {
				dto.setDepartName(depart.getName());
			}
			return dto;
		}).collect(Collectors.toList());
	}

	/**
	 * Convert pay merchant dto page page.
	 * @param page the page
	 * @return the page
	 */
	public Page<PayMerchantSimpleDto> convertPayMerchantDtoPage(Page<PayMerchantPo> page) {
		List<PayMerchantSimpleDto> list = convertPayMerchantDtoList(page.getContent());
		return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
	}

}
