package io.naccoll.boilerplate.pay.dao;

import java.util.Date;
import java.util.Optional;

import io.naccoll.boilerplate.pay.model.PayAlipayJournalPo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Pay alipay journal dao.
 */
public interface PayAlipayJournalDao
		extends JpaRepository<PayAlipayJournalPo, Long>, JpaSpecificationExecutor<PayAlipayJournalPo> {

	/**
	 * Find first by pay journal id order by id desc pay alipay journal po.
	 * @param payJournalId the pay journal id
	 * @return the pay alipay journal po
	 */
	PayAlipayJournalPo findFirstByPayJournalIdOrderByIdDesc(Long payJournalId);

	/**
	 * Find by pay merchant id and out trade no optional.
	 * @param payMerchantId the pay merchant id
	 * @param outTradeNo the out trade no
	 * @return the optional
	 */
	Optional<PayAlipayJournalPo> findByPayMerchantIdAndOutTradeNo(Long payMerchantId, String outTradeNo);

	/**
	 * Update time end int.
	 * @param id the id
	 * @param successTime the success time
	 * @return the int
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Modifying
	@Query("UPDATE PayAlipayJournalPo j set j.successTime = ?2 where j.id = ?1")
	int updateTimeEnd(Long id, Date successTime);

	/**
	 * Update status int.
	 * @param id the id
	 * @param status the status
	 * @param successTime the success time
	 * @return the int
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Modifying
	@Query("UPDATE PayAlipayJournalPo j set j.status = ?2,j.successTime = ?3 where j.id = ?1")
	int updateStatus(Long id, Integer status, Date successTime);

}
