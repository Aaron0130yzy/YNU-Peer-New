package io.naccoll.boilerplate.pay.service;

import java.math.BigDecimal;
import java.util.Date;

import io.naccoll.boilerplate.pay.dto.OrderCreateCommand;
import io.naccoll.boilerplate.pay.model.PayJournalPo;

public interface PayJournalService {

	PayJournalPo create(OrderCreateCommand command);

	void updatePayStatus(Long id, Integer payStatus);

	void updatePayStatus(Long id, Integer payStatus, String deviceInfo);

	void updateSuccessPayStatus(Long id, Integer payStatus, String thirdPayNo, Date successTime);

	void updateChannel(Long id, Integer channel);

	void updatePayPrice(Long id, BigDecimal payPrice);

	boolean submitRefund(Long id, BigDecimal refundPrice, BigDecimal existRefundFreezePrice,
			BigDecimal existRefundPrice);

	boolean completeRefund(Long id, BigDecimal refundPrice, BigDecimal existRefundFreezePrice,
			BigDecimal existRefundPrice);

}
