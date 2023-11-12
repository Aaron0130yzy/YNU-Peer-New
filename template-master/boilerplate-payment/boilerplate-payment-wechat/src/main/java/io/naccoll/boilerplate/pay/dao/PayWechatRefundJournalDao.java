package io.naccoll.boilerplate.pay.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import io.naccoll.boilerplate.core.persistence.dao.BaseDao;
import io.naccoll.boilerplate.pay.model.PayWechatRefundJournalPo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Pay wechat refund journal dao.
 */
public interface PayWechatRefundJournalDao extends BaseDao<PayWechatRefundJournalPo, Long> {

	/**
	 * Find first by refund journal id order by id desc pay wechat refund journal po.
	 * @param refundOrderId the refund order id
	 * @return the pay wechat refund journal po
	 */
	PayWechatRefundJournalPo findFirstByRefundJournalIdOrderByIdDesc(Long refundOrderId);

	/**
	 * Find by refund journal id in list.
	 * @param refundOrderIds the refund order ids
	 * @return the list
	 */
	List<PayWechatRefundJournalPo> findByRefundJournalIdIn(Collection<Long> refundOrderIds);

	/**
	 * Find by status list.
	 * @param status the status
	 * @return the list
	 */
	List<PayWechatRefundJournalPo> findByStatus(String status);

	/**
	 * Find by status and success time is null list.
	 * @param status the status
	 * @return the list
	 */
	List<PayWechatRefundJournalPo> findByStatusAndSuccessTimeIsNull(String status);

	/**
	 * Update success time int.
	 * @param id the id
	 * @param successTime the success time
	 * @return the int
	 */
	@Query("update PayWechatRefundJournalPo rj set rj.successTime = ?2 where rj.id = ?1")
	@Transactional(rollbackFor = Throwable.class)
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	int updateSuccessTime(Long id, Date successTime);

}
