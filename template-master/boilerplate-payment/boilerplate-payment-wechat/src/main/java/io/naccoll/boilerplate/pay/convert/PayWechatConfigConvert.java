package io.naccoll.boilerplate.pay.convert;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.naccoll.boilerplate.pay.dto.PayWechatConfigDto;
import io.naccoll.boilerplate.pay.model.PayWechatConfigPo;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

/**
 * The type Pay wechat config convert.
 */
@Component
public class PayWechatConfigConvert {

	/**
	 * Convert pay wechat config dto list list.
	 * @param list the list
	 * @return the list
	 */
	public List<PayWechatConfigDto> convertPayWechatConfigDtoList(List<PayWechatConfigPo> list) {
		return list.stream().map(i -> {
			PayWechatConfigDto dto = new PayWechatConfigDto();
			BeanUtils.copyProperties(i, dto);
			return dto;
		}).collect(Collectors.toList());
	}

	/**
	 * Convert pay wechat config dto page page.
	 * @param page the page
	 * @return the page
	 */
	public Page<PayWechatConfigDto> convertPayWechatConfigDtoPage(Page<PayWechatConfigPo> page) {
		List<PayWechatConfigDto> list = convertPayWechatConfigDtoList(page.getContent());
		return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
	}

	/**
	 * Convert pay wechat config dto pay wechat config dto.
	 * @param po the po
	 * @return the pay wechat config dto
	 */
	public PayWechatConfigDto convertPayWechatConfigDto(PayWechatConfigPo po) {
		return convertPayWechatConfigDtoList(Collections.singletonList(po)).get(0);
	}

}
