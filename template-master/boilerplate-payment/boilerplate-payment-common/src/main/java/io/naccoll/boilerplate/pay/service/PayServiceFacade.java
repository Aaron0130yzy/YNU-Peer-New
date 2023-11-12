package io.naccoll.boilerplate.pay.service;

import io.naccoll.boilerplate.pay.dto.CreateOrderDto;
import io.naccoll.boilerplate.pay.dto.OrderCancelCommand;
import io.naccoll.boilerplate.pay.dto.OrderCreateCommand;
import io.naccoll.boilerplate.pay.dto.RefundCommand;
import io.naccoll.boilerplate.pay.dto.RefundOrderCreateCommand;
import io.naccoll.boilerplate.pay.model.PayRefundJournalPo;
import jakarta.validation.Valid;

public interface PayServiceFacade {

	/**
	 * 获取支付参数
	 * @param command
	 * @return
	 */
	CreateOrderDto pay(@Valid OrderCreateCommand command);

	/**
	 * 通过id后台退款
	 * @param command
	 */
	void refund(RefundCommand command);

	/**
	 * 接入方退款
	 * @param command
	 * @return
	 */
	PayRefundJournalPo refund(@Valid RefundOrderCreateCommand command);

	/**
	 * 取消订单
	 * @param command
	 */
	void cancel(OrderCancelCommand command);

}
