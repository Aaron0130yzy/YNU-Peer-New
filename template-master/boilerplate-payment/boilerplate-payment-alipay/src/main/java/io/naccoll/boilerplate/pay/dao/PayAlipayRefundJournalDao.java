package io.naccoll.boilerplate.pay.dao;

import java.util.Collection;
import java.util.List;

import io.naccoll.boilerplate.pay.model.PayAlipayRefundJournalPo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The interface Pay alipay refund journal dao.
 */
public interface PayAlipayRefundJournalDao
		extends JpaRepository<PayAlipayRefundJournalPo, Long>, JpaSpecificationExecutor<PayAlipayRefundJournalPo> {

	/**
	 * 根据退款流水id查询最新的一条支付宝退款流水
	 * @param refundJournalId 退款流水id
	 * @return 支付宝退款流水
	 */
	PayAlipayRefundJournalPo findFirstByRefundJournalIdOrderByIdDesc(Long refundJournalId);

	/**
	 * Find by refund journal id in list.
	 * @param refundJournalIds the refund journal ids
	 * @return the list
	 */
	List<PayAlipayRefundJournalPo> findByRefundJournalIdIn(Collection<Long> refundJournalIds);

}
