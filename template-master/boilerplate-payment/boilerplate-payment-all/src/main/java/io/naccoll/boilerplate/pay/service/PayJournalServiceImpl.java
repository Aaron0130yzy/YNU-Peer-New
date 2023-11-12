package io.naccoll.boilerplate.pay.service;

import java.math.BigDecimal;
import java.util.Date;

import io.naccoll.boilerplate.core.id.IdService;
import io.naccoll.boilerplate.pay.dao.PayJournalDao;
import io.naccoll.boilerplate.pay.dto.OrderCreateCommand;
import io.naccoll.boilerplate.pay.enums.PayRefundStatus;
import io.naccoll.boilerplate.pay.enums.PayStatus;
import io.naccoll.boilerplate.pay.model.PayJournalPo;
import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Pay journal service.
 */
@Service
public class PayJournalServiceImpl implements PayJournalService {

	@Resource
	private PayJournalDao payJournalDao;

	@Resource
	private IdService idService;

	/**
	 * Create pay journal po.
	 * @param command the command
	 * @return the pay journal po
	 */
	public PayJournalPo create(OrderCreateCommand command) {
		PayJournalPo payJournalPo = new PayJournalPo();
		payJournalPo.setId(idService.getId());
		payJournalPo.setOrderNo(command.getOrderNo());
		payJournalPo.setPayMerchantId(command.getPayMerchantId());
		// TODO 验证PayAppId与PayStoreId
		payJournalPo.setPayAppId(command.getPayAppId());
		payJournalPo.setPayStoreId(command.getPayStoreId());
		payJournalPo.setChannel(command.getChannel());
		payJournalPo.setPayWay(command.getPayWay().getId());
		payJournalPo.setPayWayConfigId(command.getPayWayConfigId());
		payJournalPo.setOrganizationId(command.getOrganizationId());
		payJournalPo.setDepartId(command.getDepartId());
		payJournalPo.setPayPrice(command.getPayPrice());
		payJournalPo.setNotifyType(command.getNotifyType());
		payJournalPo.setRefundStatus(PayRefundStatus.NO.getId());
		payJournalPo.setPayStatus(PayStatus.WAIT_PAY.getId());
		payJournalPo.setRefundFreezePrice(BigDecimal.ZERO);
		payJournalPo.setRefundPrice(BigDecimal.ZERO);
		payJournalPo.setDeviceInfo(command.getDeviceInfo());
		payJournalPo.setMerchantBusinessNo(command.getMerchantBusinessNo());
		payJournalPo.setThirdPayNo("");
		return payJournalDao.save(payJournalPo);
	}

	/**
	 * Update pay status.
	 * @param id the id
	 * @param payStatus the pay status
	 */
	public void updatePayStatus(Long id, Integer payStatus) {
		payJournalDao.updatePayStatus(id, payStatus, new Date());
	}

	/**
	 * Update pay status.
	 * @param id the id
	 * @param payStatus the pay status
	 * @param deviceInfo the device info
	 */
	public void updatePayStatus(Long id, Integer payStatus, String deviceInfo) {
		payJournalDao.updatePayStatus(id, payStatus, deviceInfo, new Date());
	}

	/**
	 * Update success pay status.
	 * @param id the id
	 * @param payStatus the pay status
	 * @param thirdPayNo the third pay no
	 * @param successTime the success time
	 */
	public void updateSuccessPayStatus(Long id, Integer payStatus, String thirdPayNo, Date successTime) {
		payJournalDao.updatePayStatus(id, payStatus, new Date(), thirdPayNo, successTime);
	}

	/**
	 * Update channel.
	 * @param id the id
	 * @param channel the channel
	 */
	public void updateChannel(Long id, Integer channel) {
		payJournalDao.updateChannel(id, channel, new Date());
	}

	/**
	 * Update pay price.
	 * @param id the id
	 * @param payPrice the pay price
	 */
	public void updatePayPrice(Long id, BigDecimal payPrice) {
		payJournalDao.updatePayPrice(id, payPrice, new Date());
	}

	/**
	 * Submit refund boolean.
	 * @param id the id
	 * @param refundPrice the refund price
	 * @param existRefundFreezePrice the exist refund freeze price
	 * @param existRefundPrice the exist refund price
	 * @return the boolean
	 */
	public boolean submitRefund(Long id, BigDecimal refundPrice, BigDecimal existRefundFreezePrice,
			BigDecimal existRefundPrice) {
		return payJournalDao.submitRefund(id, refundPrice, existRefundFreezePrice, existRefundPrice, new Date()) == 1;
	}

	/**
	 * Complete refund boolean.
	 * @param id the id
	 * @param refundPrice the refund price
	 * @param existRefundFreezePrice the exist refund freeze price
	 * @param existRefundPrice the exist refund price
	 * @return the boolean
	 */
	@Transactional(rollbackFor = Throwable.class)
	public boolean completeRefund(Long id, BigDecimal refundPrice, BigDecimal existRefundFreezePrice,
			BigDecimal existRefundPrice) {
		return payJournalDao.completeRefund(id, refundPrice, existRefundFreezePrice, existRefundPrice, new Date()) == 1;
	}

}
