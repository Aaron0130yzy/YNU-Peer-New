package io.naccoll.boilerplate.pay.service;

import java.util.List;

import io.naccoll.boilerplate.core.exception.BusinessException;
import io.naccoll.boilerplate.pay.dao.PayRefundJournalDao;
import io.naccoll.boilerplate.pay.dto.PayRefundJournalQueryCondition;
import io.naccoll.boilerplate.pay.model.PayRefundJournalPo;
import jakarta.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * The type Pay refund journal query service.
 */
@Service
public class PayRefundJournalQueryServiceImpl implements PayRefundJournalQueryService {

	@Resource
	private PayRefundJournalDao payRefundJournalDao;

	/**
	 * Find by merchant id and refund order no pay refund journal po.
	 * @param merchantId the merchant id
	 * @param refundOrderNo the refund order no
	 * @return the pay refund journal po
	 */
	public PayRefundJournalPo findByMerchantIdAndRefundOrderNo(Long merchantId, String refundOrderNo) {
		return payRefundJournalDao.findFirstByPayMerchantIdAndRefundOrderNo(merchantId, refundOrderNo);
	}

	/**
	 * Find by id pay refund journal po.
	 * @param id the id
	 * @return the pay refund journal po
	 */
	public PayRefundJournalPo findById(Long id) {
		return payRefundJournalDao.findById(id).orElseThrow(() -> new BusinessException("退款流水不存在"));
	}

	/**
	 * 通过支付流水id查询退款流水
	 * @param payId 支付流水id
	 * @return 退款流水记录列表
	 */
	public List<PayRefundJournalPo> findByPayJournalId(Long payId) {
		return payRefundJournalDao.findByPayJournalId(payId);
	}

	/**
	 * Query page page.
	 * @param condition the condition
	 * @param pageable the pageable
	 * @return the page
	 */
	public Page<PayRefundJournalPo> queryPage(PayRefundJournalQueryCondition condition, Pageable pageable) {
		return payRefundJournalDao.page(condition, pageable);
	}

}
