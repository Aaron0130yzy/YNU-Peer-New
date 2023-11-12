package io.naccoll.boilerplate.pay.convert;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.naccoll.boilerplate.pay.dto.PayAlipayJournalDto;
import io.naccoll.boilerplate.pay.dto.PayJournalDetailDto;
import io.naccoll.boilerplate.pay.dto.PayJournalDto;
import io.naccoll.boilerplate.pay.dto.PayJournalSimpleDto;
import io.naccoll.boilerplate.pay.dto.PayRefundJournalDto;
import io.naccoll.boilerplate.pay.dto.PayWechatJournalDto;
import io.naccoll.boilerplate.pay.enums.PayRefundStatus;
import io.naccoll.boilerplate.pay.enums.PayStatus;
import io.naccoll.boilerplate.pay.enums.PayWay;
import io.naccoll.boilerplate.pay.enums.RefundStatus;
import io.naccoll.boilerplate.pay.model.PayAlipayConfigPo;
import io.naccoll.boilerplate.pay.model.PayAlipayJournalPo;
import io.naccoll.boilerplate.pay.model.PayJournalPo;
import io.naccoll.boilerplate.pay.model.PayMerchantPo;
import io.naccoll.boilerplate.pay.model.PayRefundJournalPo;
import io.naccoll.boilerplate.pay.model.PayWechatConfigPo;
import io.naccoll.boilerplate.pay.model.PayWechatJournalPo;
import io.naccoll.boilerplate.pay.service.PayAlipayConfigQueryService;
import io.naccoll.boilerplate.pay.service.PayAlipayJournalQueryService;
import io.naccoll.boilerplate.pay.service.PayJournalQueryService;
import io.naccoll.boilerplate.pay.service.PayMerchantQueryService;
import io.naccoll.boilerplate.pay.service.PayRefundJournalQueryService;
import io.naccoll.boilerplate.pay.service.PayWechatConfigQueryService;
import io.naccoll.boilerplate.pay.service.PayWechatJournalQueryService;
import io.naccoll.boilerplate.pay.service.ThirdPaySerivce;
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
 * The type Pay journal convert.
 */
@Component
public class PayJournalConvert {

	@Resource
	private PayRefundJournalQueryService payRefundJournalQueryService;

	@Resource
	private PayRefundJournalConvert payRefundJournalConvert;

	@Resource
	private PayJournalQueryService payJournalQueryService;

	@Resource
	private PayWechatJournalQueryService payWechatJournalQueryService;

	@Resource
	private PayAlipayJournalQueryService payAlipayJournalQueryService;

	@Resource
	private PayMerchantQueryService payMerchantQueryService;

	@Resource
	private PayAlipayConfigQueryService payAlipayConfigQueryService;

	@Resource
	private PayWechatConfigQueryService payWechatConfigQueryService;

	@Resource
	private ThirdPaySerivce thirdPaySerivce;

	@Resource
	private SysOrganizationQueryService sysOrganizationQueryService;

	@Resource
	private SysDepartQueryService sysDepartQueryService;

	/**
	 * Convert pay journal dto page page.
	 * @param page the page
	 * @return the page
	 */
	public Page<PayJournalSimpleDto> convertPayJournalDtoPage(Page<PayJournalPo> page) {
		List<PayJournalSimpleDto> list = convertPayJournalDtoList(page.getContent());
		return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
	}

	/**
	 * Convert pay journal dto list list.
	 * @param list the list
	 * @return the list
	 */
	public List<PayJournalSimpleDto> convertPayJournalDtoList(List<PayJournalPo> list) {
		List<Long> orgIds = list.stream().map(PayJournalPo::getOrganizationId).toList();
		List<Long> departIds = list.stream().map(PayJournalPo::getDepartId).toList();
		List<Long> payMerchantIds = list.stream().map(PayJournalPo::getPayMerchantId).toList();
		List<Long> alipayConfigIds = list.stream()
			.filter(i -> Objects.equals(i.getPayWay(), PayWay.ALIPAY.getId()))
			.map(PayJournalPo::getPayWayConfigId)
			.toList();
		List<Long> wechatConfigIds = list.stream()
			.filter(i -> Objects.equals(i.getPayWay(), PayWay.WECHAT.getId()))
			.map(i -> i.getPayWayConfigId())
			.toList();
		Map<Long, SysOrganizationPo> orgMap = sysOrganizationQueryService.findMapByIds(orgIds);
		Map<Long, SysDepartPo> departMap = sysDepartQueryService.findMapByIds(departIds);
		Map<Long, PayMerchantPo> payMerchantMap = payMerchantQueryService.findMapByIds(payMerchantIds);
		Map<Long, PayAlipayConfigPo> alipayConfigMap = payAlipayConfigQueryService.findMapByIds(alipayConfigIds);
		Map<Long, PayWechatConfigPo> wechatConfigMap = payWechatConfigQueryService.findMapByIds(wechatConfigIds);
		return list.stream().map(i -> {
			PayJournalSimpleDto payJournalDto = new PayJournalSimpleDto();
			BeanUtils.copyProperties(i, payJournalDto);
			SysOrganizationPo org = orgMap.get(payJournalDto.getOrganizationId());
			SysDepartPo depart = departMap.get(payJournalDto.getDepartId());
			PayMerchantPo payMerchant = payMerchantMap.get(payJournalDto.getPayMerchantId());
			if (org != null) {
				payJournalDto.setOrganizationName(org.getName());
			}
			if (depart != null) {
				payJournalDto.setDepartName(depart.getName());
			}
			if (payMerchant != null) {
				payJournalDto.setPayMerchantName(payMerchant.getName());
			}
			if (Objects.equals(payJournalDto.getPayWay(), PayWay.WECHAT.getId())) {
				PayWechatConfigPo alipayConfig = wechatConfigMap.get(payJournalDto.getPayWayConfigId());
				if (alipayConfig != null) {
					payJournalDto.setPayWayConfigName(alipayConfig.getName());
				}
			}
			if (Objects.equals(payJournalDto.getPayWay(), PayWay.ALIPAY.getId())) {
				PayAlipayConfigPo wechatConfig = alipayConfigMap.get(payJournalDto.getPayWayConfigId());
				if (wechatConfig != null) {
					payJournalDto.setPayWayConfigName(wechatConfig.getName());
				}
			}
			return payJournalDto;
		}).toList();
	}

	/**
	 * Convert pay journal dto pay journal dto.
	 * @param payJournalPo the pay journal po
	 * @return the pay journal dto
	 */
	public PayJournalDto convertPayJournalDto(PayJournalPo payJournalPo) {
		if (payJournalPo == null) {
			return null;
		}
		PayJournalDto payJournalDto = new PayJournalDto();
		BeanUtils.copyProperties(payJournalPo, payJournalDto);
		if (Objects.equals(payJournalPo.getPayStatus(), PayStatus.WAIT_PAY.getId())) {
			boolean success = thirdPaySerivce.syncPayOrderStatus(payJournalDto);
			if (success) {
				payJournalPo = payJournalQueryService.findByIdNotNull(payJournalDto.getId());
				BeanUtils.copyProperties(payJournalPo, payJournalDto);
			}
		}
		if (!payJournalPo.getRefundStatus().equals(PayRefundStatus.NO.getId())) {
			List<PayRefundJournalPo> refundJournalPos = payRefundJournalQueryService
				.findByPayJournalId(payJournalPo.getId())
				.stream()
				.filter(i -> !Objects.equals(i.getRefundStatus(), RefundStatus.ERROR.getId()))
				.toList();
			List<PayRefundJournalDto> refundJournalDtos = payRefundJournalConvert
				.convertPayRefundJournalDtoList(refundJournalPos);
			payJournalDto.setRefundJournals(refundJournalDtos);
		}
		return payJournalDto;
	}

	/**
	 * Convert to pay journal detail dto pay journal detail dto.
	 * @param payJournalPo the pay journal po
	 * @return the pay journal detail dto
	 */
	public PayJournalDetailDto convertToPayJournalDetailDto(PayJournalPo payJournalPo) {
		if (payJournalPo == null) {
			return null;
		}
		PayJournalSimpleDto simpleDto = convertPayJournalDtoList(List.of(payJournalPo)).get(0);
		PayJournalDetailDto payJournalDto = new PayJournalDetailDto();
		BeanUtils.copyProperties(simpleDto, payJournalDto);
		if (Objects.equals(payJournalPo.getPayWay(), PayWay.WECHAT.getId())) {
			PayWechatJournalPo wechatJournalPo = payWechatJournalQueryService
				.findLastByPayJournalId(payJournalPo.getId());
			PayWechatJournalDto journalDto = new PayWechatJournalDto();
			BeanUtils.copyProperties(wechatJournalPo, journalDto);
			payJournalDto.setWechatJournal(journalDto);
		}
		if (Objects.equals(payJournalPo.getPayWay(), PayWay.ALIPAY.getId())) {
			PayAlipayJournalPo alipay = payAlipayJournalQueryService.findLastByJournalId(payJournalPo.getId());
			PayAlipayJournalDto alipayDto = new PayAlipayJournalDto();
			BeanUtils.copyProperties(alipay, alipayDto);
			payJournalDto.setAlipayJournal(alipayDto);
		}

		if (!payJournalPo.getRefundStatus().equals(PayRefundStatus.NO.getId())) {
			List<PayRefundJournalPo> refundJournalPos = payRefundJournalQueryService
				.findByPayJournalId(payJournalPo.getId());
			payJournalDto.setRefundJournals(payRefundJournalConvert.convertPayRefundJournalDtoList(refundJournalPos));
		}
		return payJournalDto;
	}

}
