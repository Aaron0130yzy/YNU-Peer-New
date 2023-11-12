package io.naccoll.boilerplate.pay.dao;

import java.util.Date;
import java.util.List;

import io.naccoll.boilerplate.core.persistence.dao.BaseDao;
import io.naccoll.boilerplate.pay.model.PayWechatJournalPo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Pay wechat journal dao.
 */
public interface PayWechatJournalDao extends BaseDao<PayWechatJournalPo, Long> {

	/**
	 * Find first by pay journal id pay wechat journal po.
	 * @param payJournalId the pay journal id
	 * @return the pay wechat journal po
	 */
	PayWechatJournalPo findFirstByPayJournalId(Long payJournalId);

	/**
	 * Find first by pay journal id order by id desc pay wechat journal po.
	 * @param payJournalId the pay journal id
	 * @return the pay wechat journal po
	 */
	PayWechatJournalPo findFirstByPayJournalIdOrderByIdDesc(Long payJournalId);

	/**
	 * Find by pay merchant id and status list.
	 * @param payMerchantId the pay merchant id
	 * @param status the status
	 * @return the list
	 */
	List<PayWechatJournalPo> findByPayMerchantIdAndStatus(Long payMerchantId, Integer status);

	/**
	 * Find by pay journal id in list.
	 * @param payJournalIds the pay journal ids
	 * @return the list
	 */
	List<PayWechatJournalPo> findByPayJournalIdIn(List<Long> payJournalIds);

	/**
	 * Find by status list.
	 * @param status the status
	 * @return the list
	 */
	List<PayWechatJournalPo> findByStatus(Integer status);

	/**
	 * Update time end int.
	 * @param id the id
	 * @param timeEnd the time end
	 * @return the int
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Modifying
	@Query("UPDATE PayWechatJournalPo j set j.timeEnd = ?2 where j.id = ?1")
	int updateTimeEnd(Long id, Date timeEnd);

}
