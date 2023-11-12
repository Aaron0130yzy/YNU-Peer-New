package io.naccoll.boilerplate.pay.interfaces.handler;

import java.util.List;
import java.util.Objects;

import io.naccoll.boilerplate.pay.dto.TradeStatusPollingMessage;
import io.naccoll.boilerplate.pay.enums.PayChannel;
import io.naccoll.boilerplate.pay.enums.PayStatus;
import io.naccoll.boilerplate.pay.enums.PayWay;
import io.naccoll.boilerplate.pay.enums.PaymentStatus;
import io.naccoll.boilerplate.pay.model.PayAlipayJournalPo;
import io.naccoll.boilerplate.pay.model.PayJournalPo;
import io.naccoll.boilerplate.pay.service.PayAlipayJournalQueryService;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

/**
 * The type Alipay trade status polling handler.
 */
@Slf4j
@Component
public class AlipayTradeStatusPollingHandler extends AbstractTradeStatusPollingHandler {

	@Resource
	private PayAlipayJournalQueryService payAlipayJournalQueryService;

	@SneakyThrows
	public void handleMessage(PayJournalPo payJournal, TradeStatusPollingMessage pollingMessage)
			throws MessagingException {
		long alipayJournalId = pollingMessage.getThirdPayJournalId();
		PayAlipayJournalPo payAlipayJournal = payAlipayJournalQueryService.findByIdNotNull(alipayJournalId);
		if (!Objects.equals(payAlipayJournal.getStatus(), PaymentStatus.WAIT_PAY.getId())) {
			return;
		}
		int count = pollingMessage.getCount();
		boolean success = thirdPaySerivce.syncPayOrderStatus(payJournal);
		if (success) {
			return;
		}

		PayChannel payChannel = PayChannel.fromId(payJournal.getChannel());
		if (!pollingMessage.hasNext() && List.of(PayChannel.ALIPAY_MICRO).contains(payChannel)) {
			log.info("轮询支付宝交易状态次数超过{}次，进行撤单，orderNo：{}", count, payJournal.getOrderNo());
			thirdPaySerivce.cancelOrder(payJournal);
			payJournalService.updatePayStatus(payJournal.getId(), PayStatus.CANCEL.getId());
			log.info("轮询支付宝交易状态次数超过{}次，撤单成功，orderNo：{}", count, payJournal.getOrderNo());
		}
	}

	@Override
	public PayWay getPayWay() {
		return PayWay.ALIPAY;
	}

}
