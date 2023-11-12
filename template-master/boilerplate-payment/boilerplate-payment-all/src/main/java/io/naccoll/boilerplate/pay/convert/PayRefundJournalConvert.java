package io.naccoll.boilerplate.pay.convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.naccoll.boilerplate.pay.dto.PayAlipayRefundJournalDto;
import io.naccoll.boilerplate.pay.dto.PayRefundJournalDetailDto;
import io.naccoll.boilerplate.pay.dto.PayRefundJournalDto;
import io.naccoll.boilerplate.pay.dto.PayWechatRefundJournalDto;
import io.naccoll.boilerplate.pay.enums.PayWay;
import io.naccoll.boilerplate.pay.model.PayAlipayRefundJournalPo;
import io.naccoll.boilerplate.pay.model.PayRefundJournalPo;
import io.naccoll.boilerplate.pay.model.PayWechatRefundJournalPo;
import io.naccoll.boilerplate.pay.service.PayAlipayJournalQueryService;
import io.naccoll.boilerplate.pay.service.PayWechatJournalQueryService;
import jakarta.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

/**
 * The type Pay refund journal convert.
 */
@Component
public class PayRefundJournalConvert {

	@Resource
	private PayAlipayJournalQueryService payAlipayJournalQueryService;

	@Resource
	private PayWechatJournalQueryService payWechatJournalQueryService;

	public PayRefundJournalDetailDto convertPayRefundJournalDetailDto(PayRefundJournalPo po) {
		PayRefundJournalDto dto = convertPayRefundJournalDtoList(List.of(po)).get(0);
		PayRefundJournalDetailDto result = new PayRefundJournalDetailDto();
		BeanUtils.copyProperties(dto, result);
		if (Objects.equals(dto.getPayWay(), PayWay.ALIPAY.getId())) {
			PayAlipayRefundJournalPo alipay = payAlipayJournalQueryService.findLastRefundByRefundJournalId(dto.getId());
			PayAlipayRefundJournalDto alipayDto = new PayAlipayRefundJournalDto();
			BeanUtils.copyProperties(alipay, alipayDto);
			result.setAlipay(alipayDto);
		}
		if (Objects.equals(dto.getPayWay(), PayWay.WECHAT.getId())) {
			PayWechatRefundJournalPo wechat = payWechatJournalQueryService.findLastRefundByRefundJournalId(dto.getId());
			PayWechatRefundJournalDto wechatDto = new PayWechatRefundJournalDto();
			BeanUtils.copyProperties(wechat, wechatDto);
			result.setWechat(wechatDto);
		}
		return result;
	}

	public PayRefundJournalDto convertPayRefundJournalDto(PayRefundJournalPo po) {
		return convertPayRefundJournalDtoList(List.of(po)).get(0);
	}

	public List<PayRefundJournalDto> convertPayRefundJournalDtoList(List<PayRefundJournalPo> list) {
		if (list == null) {
			return null;
		}
		if (list.isEmpty()) {
			return new ArrayList<>();
		}

		return list.parallelStream().map(payRefundJournalPo -> {
			if (payRefundJournalPo == null) {
				return null;
			}
			PayRefundJournalDto dto = new PayRefundJournalDto();
			BeanUtils.copyProperties(payRefundJournalPo, dto);
			return dto;
		}).toList();
	}

	public Page<PayRefundJournalDto> convertPayRefundJournalDtoPage(Page<PayRefundJournalPo> page) {
		List<PayRefundJournalDto> list = convertPayRefundJournalDtoList(page.getContent());
		return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
	}

}
