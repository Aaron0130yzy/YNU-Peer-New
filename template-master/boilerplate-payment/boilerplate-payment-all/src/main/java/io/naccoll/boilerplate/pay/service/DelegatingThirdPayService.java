package io.naccoll.boilerplate.pay.service;

import java.util.HashMap;
import java.util.Map;

import io.naccoll.boilerplate.pay.dto.CreateOrderDto;
import io.naccoll.boilerplate.pay.dto.OrderCreateCommand;
import io.naccoll.boilerplate.pay.dto.RefundOrderCreateCommand;
import io.naccoll.boilerplate.pay.enums.PayWay;
import io.naccoll.boilerplate.pay.model.PayJournalPo;
import io.naccoll.boilerplate.pay.model.PayMerchantPo;
import io.naccoll.boilerplate.pay.model.PayRefundJournalPo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The type Delegating third pay service.
 */
@Service
@Primary
public class DelegatingThirdPayService implements ThirdPaySerivce {

	private final Map<PayWay, ThirdPaySubSerivce> paySerivceMap;

	/**
	 * Instantiates a new Delegating third pay service.
	 * @param applicationContext the application context
	 */
	public DelegatingThirdPayService(ApplicationContext applicationContext) {
		Map<String, ThirdPaySubSerivce> map = applicationContext.getBeansOfType(ThirdPaySubSerivce.class);
		paySerivceMap = new HashMap<>(map.size());
		for (ThirdPaySubSerivce value : map.values()) {
			paySerivceMap.put(value.getPayWay(), value);
		}
	}

	@Override
	public CreateOrderDto pay(PayJournalPo payJournal, OrderCreateCommand command, PayMerchantPo payMerchant) {
		PayWay payWay = PayWay.fromId(payJournal.getPayWay());
		return getDelegating(payWay).pay(payJournal, command, payMerchant);
	}

	@Override
	public void refund(PayJournalPo payJournal, PayRefundJournalPo refundJournal,
			RefundOrderCreateCommand refundOrderCommand, PayMerchantPo payMerchant) {
		PayWay payWay = PayWay.fromId(payJournal.getPayWay());
		getDelegating(payWay).refund(payJournal, refundJournal, refundOrderCommand, payMerchant);
	}

	@Override
	public boolean cancelOrder(PayJournalPo payJournal) {
		PayWay payWay = PayWay.fromId(payJournal.getPayWay());
		return getDelegating(payWay).cancelOrder(payJournal);
	}

	@Override
	public boolean syncPayOrderStatus(PayJournalPo payJournal) {
		PayWay payWay = PayWay.fromId(payJournal.getPayWay());
		return getDelegating(payWay).syncPayOrderStatus(payJournal);
	}

	/**
	 * Gets delegating.
	 * @param payWay the pay way
	 * @return the delegating
	 */
	public ThirdPaySerivce getDelegating(PayWay payWay) {
		return paySerivceMap.get(payWay);
	}

}
