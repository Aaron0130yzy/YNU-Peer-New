package io.naccoll.boilerplate.pay.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.naccoll.boilerplate.core.exception.ResourceNotFoundException;
import io.naccoll.boilerplate.pay.dao.PayAlipayJournalDao;
import io.naccoll.boilerplate.pay.dao.PayAlipayRefundJournalDao;
import io.naccoll.boilerplate.pay.model.PayAlipayJournalPo;
import io.naccoll.boilerplate.pay.model.PayAlipayRefundJournalPo;
import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * The type Pay alipay journal query service.
 */
@Service
public class PayAlipayJournalQueryService {

	@Resource
	private PayAlipayJournalDao payAlipayJournalDao;

	@Resource
	private PayAlipayRefundJournalDao payAlipayRefundJournalDao;

	/**
	 * Find by id not null pay alipay journal po.
	 * @param id the id
	 * @return the pay alipay journal po
	 */
	public PayAlipayJournalPo findByIdNotNull(long id) {
		return payAlipayJournalDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("不存在该支付宝流水"));
	}

	/**
	 * 根据支付流水ID找到最新一条匹配的支付宝支付流水
	 * @param journalId 支付流水ID
	 * @return 最新一条匹配的支付宝支付流水
	 */
	public PayAlipayJournalPo findLastByJournalId(long journalId) {
		return payAlipayJournalDao.findFirstByPayJournalIdOrderByIdDesc(journalId);
	}

	/**
	 * 根据退款流水id查询最新的一条支付宝退款流水
	 * @param refundJournalId 退款流水id
	 * @return 支付宝退款流水
	 */
	public PayAlipayRefundJournalPo findLastRefundByRefundJournalId(Long refundJournalId) {
		return payAlipayRefundJournalDao.findFirstByRefundJournalIdOrderByIdDesc(refundJournalId);
	}

	/**
	 * 查询退款流水id集合对应的支付宝退款流水列表
	 * @param refundJournalIds 退款流水Id集合
	 * @return 支付宝退款流水列表
	 */
	private List<PayAlipayRefundJournalPo> findRefundListByRefundJournalIds(Collection<Long> refundJournalIds) {
		return payAlipayRefundJournalDao.findByRefundJournalIdIn(refundJournalIds);
	}

	/**
	 * 查询退款流水id集合对应的最新一条匹配的支付宝退款流水
	 * @param refundJournalIds 退款流水Id集合
	 * @return 最新一条匹配的支付宝退款流水的Map
	 */
	public Map<Long, PayAlipayRefundJournalPo> findRefundLastMapByRefundJournalIds(Collection<Long> refundJournalIds) {
		return findRefundListByRefundJournalIds(refundJournalIds).stream()
			.collect(Collectors.toMap(PayAlipayRefundJournalPo::getRefundJournalId, i -> i, (t, t2) -> {
				if (t.getId() <= t2.getId()) {
					return t2;
				}
				return t;
			}));
	}

}
