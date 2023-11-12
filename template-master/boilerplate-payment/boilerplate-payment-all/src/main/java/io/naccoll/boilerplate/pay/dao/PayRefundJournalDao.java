package io.naccoll.boilerplate.pay.dao;

import java.util.Date;
import java.util.List;

import io.naccoll.boilerplate.core.persistence.dao.BaseDao;
import io.naccoll.boilerplate.pay.dto.PayRefundJournalQueryCondition;
import io.naccoll.boilerplate.pay.model.PayRefundJournalPo;
import io.naccoll.boilerplate.pay.model.PayRefundJournalPo_;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Pay refund journal dao.
 */
public interface PayRefundJournalDao extends BaseDao<PayRefundJournalPo, Long> {

	/**
	 * Update refund status int.
	 * @param id the id
	 * @param payStatus the pay status
	 * @param errorRemark the error remark
	 * @param thirdRefundNo the third refund no
	 * @param successDate the success date
	 * @param date the date
	 * @return the int
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("""
			UPDATE PayRefundJournalPo pr
			SET pr.refundStatus=:refundStatus,
				pr.errorRemark=:errorRemark,
				pr.lastModifiedDate=:lastModifiedDate,
				pr.thirdRefundNo=:thirdRefundNo,
				pr.successTime = :successDate
			WHERE pr.id = :id
			""")
	int updateRefundStatus(@Param("id") Long id, @Param("refundStatus") Integer payStatus,
			@Param("errorRemark") String errorRemark, @Param("thirdRefundNo") String thirdRefundNo,
			@Param("successDate") Date successDate, @Param("lastModifiedDate") Date date);

	/**
	 * Update refund status int.
	 * @param id the id
	 * @param payStatus the pay status
	 * @param errorRemark the error remark
	 * @param deviceInfo the device info
	 * @param lastModifiedDate the last modified date
	 * @return the int
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("""
			UPDATE PayRefundJournalPo pr
			SET pr.refundStatus=:refundStatus,
				pr.errorRemark=:errorRemark,
				pr.deviceInfo=:deviceInfo,
				pr.lastModifiedDate=:lastModifiedDate
			WHERE pr.id = :id
			""")
	int updateRefundStatus(@Param("id") Long id, @Param("refundStatus") Integer payStatus,
			@Param("errorRemark") String errorRemark, @Param("deviceInfo") String deviceInfo,
			@Param("lastModifiedDate") Date lastModifiedDate);

	/**
	 * Update refund status int.
	 * @param id the id
	 * @param payStatus the pay status
	 * @param errorRemark the error remark
	 * @param date the date
	 * @return the int
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("""
			UPDATE PayRefundJournalPo pr
			SET pr.refundStatus=:refundStatus,
				pr.errorRemark=:errorRemark,
				pr.lastModifiedDate=:lastModifiedDate
			WHERE pr.id = :id
			""")
	int updateRefundStatus(@Param("id") Long id, @Param("refundStatus") Integer payStatus,
			@Param("errorRemark") String errorRemark, @Param("lastModifiedDate") Date date);

	/**
	 * Find first by pay merchant id and refund order no pay refund journal po.
	 * @param payMerchantId the pay merchant id
	 * @param refundOrderNo the refund order no
	 * @return the pay refund journal po
	 */
	PayRefundJournalPo findFirstByPayMerchantIdAndRefundOrderNo(Long payMerchantId, String refundOrderNo);

	/**
	 * Find by pay journal id list.
	 * @param payJournalId the pay journal id
	 * @return the list
	 */
	List<PayRefundJournalPo> findByPayJournalId(Long payJournalId);

	/**
	 * Update third refund no int.
	 * @param id the id
	 * @param thirdRefundNo the third refund no
	 * @param date the date
	 * @return the int
	 */
	@Query("""
			UPDATE PayRefundJournalPo pr
			SET pr.thirdRefundNo=:thirdRefundNo,
				pr.lastModifiedDate=:lastModifiedDate
			WHERE pr.id = :id
			""")
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Transactional(rollbackFor = Throwable.class)
	int updateThirdRefundNo(@Param("id") Long id, @Param("thirdRefundNo") String thirdRefundNo,
			@Param("lastModifiedDate") Date date);

	/**
	 * Page page.
	 * @param condition the condition
	 * @param pageable the pageable
	 * @return the page
	 */
	default Page<PayRefundJournalPo> page(PayRefundJournalQueryCondition condition, Pageable pageable) {
		Specification<PayRefundJournalPo> specification = (root, criteriaQuery, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();
			List<Expression<Boolean>> expressions = predicate.getExpressions();
			if (condition.getPayMerchantId() != null) {
				expressions.add(criteriaBuilder.equal(root.get(PayRefundJournalPo_.PAY_MERCHANT_ID),
						condition.getPayMerchantId()));
			}
			if (condition.getOrganizationId() != null) {
				expressions.add(criteriaBuilder.equal(root.get(PayRefundJournalPo_.ORGANIZATION_ID),
						condition.getOrganizationId()));
			}
			if (condition.getPayId() != null) {
				expressions
					.add(criteriaBuilder.equal(root.get(PayRefundJournalPo_.PAY_JOURNAL_ID), condition.getPayId()));
			}
			if (condition.getRefundStatus() != null) {
				expressions.add(criteriaBuilder.equal(root.get(PayRefundJournalPo_.REFUND_STATUS),
						condition.getRefundStatus()));
			}
			if (condition.getRefundOrderNo() != null) {
				expressions.add(criteriaBuilder.like(root.get(PayRefundJournalPo_.REFUND_ORDER_NO),
						"%" + condition.getRefundOrderNo() + "%"));
			}
			return predicate;
		};
		return findAll(specification, pageable);
	}

}
