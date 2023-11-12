package io.naccoll.boilerplate.pay.convert;

import java.util.List;
import java.util.stream.Collectors;

import io.naccoll.boilerplate.pay.dto.PayAlipayConfigDto;
import io.naccoll.boilerplate.pay.model.PayAlipayConfigPo;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * The type Pay alipay config convert.
 */
@Component
public class PayAlipayConfigConvert {

	public PayAlipayConfigDto convertPayAlipayConfigDto(PayAlipayConfigPo po) {
		return convertPayAlipayConfigDtoList(List.of(po)).get(0);
	}

	public List<PayAlipayConfigDto> convertPayAlipayConfigDtoList(List<PayAlipayConfigPo> list) {
		return list.stream().map(i -> {
			PayAlipayConfigDto payAlipayConfigDto = new PayAlipayConfigDto();
			BeanUtils.copyProperties(i, payAlipayConfigDto);
			return payAlipayConfigDto;
		}).collect(Collectors.toList());
	}

}
