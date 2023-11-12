package io.naccoll.boilerplate.pay.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.naccoll.boilerplate.core.exception.ResourceNotFoundException;
import io.naccoll.boilerplate.pay.dao.PayWechatJournalDao;
import io.naccoll.boilerplate.pay.dao.PayWechatRefundJournalDao;
import io.naccoll.boilerplate.pay.model.PayWechatJournalPo;
import io.naccoll.boilerplate.pay.model.PayWechatRefundJournalPo;
import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * The type Pay wechat journal query service.
 */
@Service
public class PayWechatJournalQueryService {

	@Resource
	private PayWechatJournalDao payWechatJournalDao;

	@Resource
	private PayWechatRefundJournalDao payWechatRefundJournalDao;

	/**
	 * Find by id not null pay wechat journal po.
	 * @param id the id
	 * @return the pay wechat journal po
	 */
	public PayWechatJournalPo findByIdNotNull(long id) {
		return payWechatJournalDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("不存在该微信流水"));
	}

	/**
	 * 查询支付流水id下最新的一笔微信流水记录
	 * @param payJournalId 支付流水id
	 * @return 最新的一笔微信流水记录
	 */
	public PayWechatJournalPo findLastByPayJournalId(long payJournalId) {
		return payWechatJournalDao.findFirstByPayJournalIdOrderByIdDesc(payJournalId);
	}

	/**
	 * 根据退款流水id查询最新的一条支付宝退款流水
	 * @param refundJournalId 退款流水id
	 * @return 支付宝退款流水
	 */
	public PayWechatRefundJournalPo findLastRefundByRefundJournalId(Long refundJournalId) {
		return payWechatRefundJournalDao.findFirstByRefundJournalIdOrderByIdDesc(refundJournalId);
	}

	/**
	 * 查询退款流水id集合对应的微信退款流水列表
	 * @param refundJournalIds 退款流水Id集合
	 * @return 微信退款流水列表
	 */
	private List<PayWechatRefundJournalPo> findListByRefundJournalIds(Collection<Long> refundJournalIds) {
		return payWechatRefundJournalDao.findByRefundJournalIdIn(refundJournalIds);
	}

	/**
	 * 查询退款流水id集合对应的最新一条匹配的微信退款流水
	 * @param refundJournalIds 退款流水Id集合
	 * @return 最新一条匹配的微信退款流水的Map
	 */
	public Map<Long, PayWechatRefundJournalPo> findLastMapByRefundJournalIds(Collection<Long> refundJournalIds) {
		return findListByRefundJournalIds(refundJournalIds).stream()
			.collect(Collectors.toMap(PayWechatRefundJournalPo::getRefundJournalId, i -> i, (t, t2) -> {
				if (t.getId() <= t2.getId()) {
					return t2;
				}
				return t;
			}));
	}

}
