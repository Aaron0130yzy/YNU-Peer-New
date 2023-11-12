package io.naccoll.boilerplate.pay.service;

import java.util.Date;

import io.naccoll.boilerplate.core.id.IdService;
import io.naccoll.boilerplate.pay.dao.PayRefundJournalDao;
import io.naccoll.boilerplate.pay.dto.RefundOrderCreateCommand;
import io.naccoll.boilerplate.pay.enums.RefundStatus;
import io.naccoll.boilerplate.pay.model.PayJournalPo;
import io.naccoll.boilerplate.pay.model.PayRefundJournalPo;
import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * The type Pay refund journal service.
 */
@Service
public class PayRefundJournalServiceImpl implements PayRefundJournalService {

	@Resource
	private PayRefundJournalDao payRefundJournalDao;

	@Resource
	private IdService idService;

	/**
	 * Create pay refund journal po.
	 * @param journal the journal
	 * @param command the command
	 * @return the pay refund journal po
	 */
	public PayRefundJournalPo create(PayJournalPo journal, RefundOrderCreateCommand command) {
		PayRefundJournalPo refundJournal = new PayRefundJournalPo();
		refundJournal.setId(idService.getId());
		refundJournal.setOrderNo(journal.getOrderNo());
		refundJournal.setPayMerchantId(journal.getPayMerchantId());
		refundJournal.setOrganizationId(journal.getOrganizationId());
		refundJournal.setDepartId(journal.getDepartId());
		refundJournal.setNotifyType(command.getNotifyType());
		refundJournal.setRefundOrderNo(command.getRefundOrderNo());
		refundJournal.setRefundStatus(RefundStatus.REFUNDING.getId());
		refundJournal.setRefundReason(command.getRefundReason());
		refundJournal.setRefundOrderNo(command.getRefundOrderNo());
		refundJournal.setRefundPrice(command.getRefundPrice());
		refundJournal.setPayJournalId(journal.getId());
		refundJournal.setThirdPayNo(journal.getThirdPayNo());
		refundJournal.setThirdRefundNo("");
		refundJournal.setErrorRemark("");
		refundJournal.setDeviceInfo(command.getDeviceInfo());
		refundJournal.setPayWay(journal.getPayWay());
		refundJournal.setPayWayConfigId(journal.getPayWayConfigId());
		refundJournal.setMerchantBusinessNo(journal.getMerchantBusinessNo());
		refundJournal.setPayAppId(journal.getPayAppId());
		refundJournal.setPayStoreId(journal.getPayStoreId());
		return payRefundJournalDao.save(refundJournal);
	}

	/**
	 * Update fail refund status.
	 * @param id the id
	 * @param errorRemark the error remark
	 */
	public void updateFailRefundStatus(long id, String errorRemark) {
		int refundStatus = RefundStatus.ERROR.getId();
		payRefundJournalDao.updateRefundStatus(id, refundStatus, errorRemark, new Date());
	}

	/**
	 * Update refund device info.
	 * @param id the id
	 * @param deviceInfo the device info
	 */
	public void updateRefundDeviceInfo(long id, String deviceInfo) {
		int refundStatus = RefundStatus.REFUNDING.getId();
		payRefundJournalDao.updateRefundStatus(id, refundStatus, "", deviceInfo, new Date());
	}

	/**
	 * Update success refund status.
	 * @param id the id
	 * @param thirdRefundNo the third refund no
	 * @param successDate the success date
	 */
	public void updateSuccessRefundStatus(long id, String thirdRefundNo, Date successDate) {
		int refundStatus = RefundStatus.SUCCESS.getId();
		payRefundJournalDao.updateRefundStatus(id, refundStatus, "", thirdRefundNo, successDate, new Date());
	}

	/**
	 * Update third refund no.
	 * @param refundJournalId the refund journal id
	 * @param thirdRefundNo the third refund no
	 */
	public void updateThirdRefundNo(Long refundJournalId, String thirdRefundNo) {
		payRefundJournalDao.updateThirdRefundNo(refundJournalId, thirdRefundNo, new Date());
	}

}
