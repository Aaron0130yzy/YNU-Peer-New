package io.naccoll.boilerplate.pay.service;

import io.naccoll.boilerplate.pay.dto.CreateOrderDto;
import io.naccoll.boilerplate.pay.dto.OrderCreateCommand;
import io.naccoll.boilerplate.pay.dto.RefundOrderCreateCommand;
import io.naccoll.boilerplate.pay.model.PayJournalPo;
import io.naccoll.boilerplate.pay.model.PayMerchantPo;
import io.naccoll.boilerplate.pay.model.PayRefundJournalPo;

/**
 * The interface Third pay serivce.
 */
public interface ThirdPaySerivce {

	/**
	 * Pay create order dto.
	 * @param payJournal the pay journal
	 * @param command the command
	 * @param payMerchant the pay merchant
	 * @return the create order dto
	 */
	CreateOrderDto pay(PayJournalPo payJournal, OrderCreateCommand command, PayMerchantPo payMerchant);

	/**
	 * Refund.
	 * @param payJournal the pay journal
	 * @param refundJournal the refund journal
	 * @param refundOrderCommand the refund order command
	 * @param payMerchant the pay merchant
	 */
	void refund(PayJournalPo payJournal, PayRefundJournalPo refundJournal, RefundOrderCreateCommand refundOrderCommand,
			PayMerchantPo payMerchant);

	/**
	 * Cancel order boolean.
	 * @param payJournal the pay journal
	 * @return the boolean
	 */
	boolean cancelOrder(PayJournalPo payJournal);

	/**
	 * Sync pay order status boolean.
	 * @param payJournal the pay journal
	 * @return the boolean
	 */
	boolean syncPayOrderStatus(PayJournalPo payJournal);

}
