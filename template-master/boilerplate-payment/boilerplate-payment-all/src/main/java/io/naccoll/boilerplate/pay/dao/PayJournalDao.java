package io.naccoll.boilerplate.pay.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.naccoll.boilerplate.core.persistence.dao.BaseDao;
import io.naccoll.boilerplate.pay.model.PayJournalPo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Pay journal dao.
 */
public interface PayJournalDao extends BaseDao<PayJournalPo, Long> {

	/**
	 * Find by channel and pay status and created date greater than list.
	 * @param channel the channel
	 * @param payStatus the pay status
	 * @param beginDate the begin date
	 * @return the list
	 */
	List<PayJournalPo> findByChannelAndPayStatusAndCreatedDateGreaterThan(Integer channel, Integer payStatus,
			Date beginDate);

	/**
	 * Find first by pay merchant id and order no pay journal po.
	 * @param merchantId the merchant id
	 * @param orderNo the order no
	 * @return the pay journal po
	 */
	PayJournalPo findFirstByPayMerchantIdAndOrderNo(Long merchantId, String orderNo);

	/**
	 * Find by order no list.
	 * @param orderNo the order no
	 * @return the list
	 */
	List<PayJournalPo> findByOrderNo(String orderNo);

	/**
	 * Update pay status int.
	 * @param id the id
	 * @param payStatus the pay status
	 * @param date the date
	 * @param thirdPayNo the third pay no
	 * @param successTime the success time
	 * @return the int
	 */
	@Query("""
			UPDATE PayJournalPo pp
			SET pp.payStatus=:payStatus,
				pp.lastModifiedDate=:date,
				pp.successTime = :successTime,
				pp.thirdPayNo = :thirdPayNo
			WHERE pp.id = :id
			""")
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Transactional(rollbackFor = Throwable.class)
	int updatePayStatus(@Param("id") Long id, @Param("payStatus") Integer payStatus, @Param("date") Date date,
			@Param("thirdPayNo") String thirdPayNo, @Param("successTime") Date successTime);

	/**
	 * Update pay status int.
	 * @param id the id
	 * @param payStatus the pay status
	 * @param deviceInfo the device info
	 * @param lastModifiedDate the last modified date
	 * @return the int
	 */
	@Query("""
			UPDATE PayJournalPo pp
			SET pp.payStatus=:payStatus,
				pp.deviceInfo=:deviceInfo,
				pp.lastModifiedDate=:lastModifiedDate
			WHERE pp.id = :id
			""")
	@Transactional(rollbackFor = Throwable.class)
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	int updatePayStatus(@Param("id") Long id, @Param("payStatus") Integer payStatus,
			@Param("deviceInfo") String deviceInfo, @Param("lastModifiedDate") Date lastModifiedDate);

	/**
	 * Update pay status int.
	 * @param id the id
	 * @param payStatus the pay status
	 * @param date the date
	 * @return the int
	 */
	@Query("""
			UPDATE PayJournalPo pp
			SET pp.payStatus=:payStatus,
				pp.lastModifiedDate=:date
			WHERE pp.id = :id
			""")
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Transactional(rollbackFor = Throwable.class)
	int updatePayStatus(@Param("id") Long id, @Param("payStatus") Integer payStatus, @Param("date") Date date);

	/**
	 * Update channel int.
	 * @param id the id
	 * @param payStatus the pay status
	 * @param date the date
	 * @return the int
	 */
	@Query("""
			UPDATE PayJournalPo pp
			SET pp.channel=:channel,
				pp.lastModifiedDate=:date
			WHERE pp.id = :id
			""")
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Transactional(rollbackFor = Throwable.class)
	int updateChannel(@Param("id") Long id, @Param("channel") Integer payStatus, @Param("date") Date date);

	/**
	 * Update pay price int.
	 * @param id the id
	 * @param payPrice the pay price
	 * @param date the date
	 * @return the int
	 */
	@Query("""
			UPDATE PayJournalPo pp
			SET pp.payPrice=:payPrice,
				pp.lastModifiedDate=:date
			WHERE pp.id = :id
			""")
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Transactional(rollbackFor = Throwable.class)
	int updatePayPrice(@Param("id") Long id, @Param("payPrice") BigDecimal payPrice, @Param("date") Date date);

	/**
	 * Submit refund int.
	 * @param id the id
	 * @param refundPrice the refund price
	 * @param existRefundFreezePrice the exist refund freeze price
	 * @param existRefundPrice the exist refund price
	 * @param date the date
	 * @return the int
	 */
	@Query("""
			UPDATE PayJournalPo pp
			SET pp.refundFreezePrice=pp.refundFreezePrice+:refundPrice,
				pp.lastModifiedDate=:date
			WHERE pp.id = :id
				AND pp.refundFreezePrice=:existRefundFreezePrice
				AND pp.refundPrice = :existRefundPrice
			""")
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Transactional(rollbackFor = Throwable.class)
	int submitRefund(@Param("id") Long id, @Param("refundPrice") BigDecimal refundPrice,
			@Param("existRefundFreezePrice") BigDecimal existRefundFreezePrice,
			@Param("existRefundPrice") BigDecimal existRefundPrice, @Param("date") Date date);

	/**
	 * Complete refund int.
	 * @param id the id
	 * @param refundPrice the refund price
	 * @param existRefundFreezePrice the exist refund freeze price
	 * @param existRefundPrice the exist refund price
	 * @param date the date
	 * @return the int
	 */
	@Query("""
			UPDATE PayJournalPo pp
			SET pp.refundFreezePrice=pp.refundFreezePrice-:refundPrice,
				pp.refundPrice=pp.refundPrice+:refundPrice,
				pp.lastModifiedDate=:date,
				pp.refundStatus=(CASE
					WHEN pp.refundPrice=pp.payPrice THEN 2
					WHEN pp.refundPrice<pp.payPrice AND pp.refundPrice>0 THEN 1
					ELSE 0 END
				)
			WHERE pp.id = :id
			AND pp.refundFreezePrice=:existRefundFreezePrice
			AND pp.refundPrice = :existRefundPrice
			""")
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Transactional(rollbackFor = Throwable.class)
	int completeRefund(@Param("id") Long id, @Param("refundPrice") BigDecimal refundPrice,
			@Param("existRefundFreezePrice") BigDecimal existRefundFreezePrice,
			@Param("existRefundPrice") BigDecimal existRefundPrice, @Param("date") Date date);

}
