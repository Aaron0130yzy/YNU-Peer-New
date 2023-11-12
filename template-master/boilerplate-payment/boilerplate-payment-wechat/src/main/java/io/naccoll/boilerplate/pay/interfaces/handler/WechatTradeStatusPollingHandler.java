package io.naccoll.boilerplate.pay.interfaces.handler;

import java.util.List;
import java.util.Objects;

import io.naccoll.boilerplate.pay.dto.TradeStatusPollingMessage;
import io.naccoll.boilerplate.pay.enums.PayChannel;
import io.naccoll.boilerplate.pay.enums.PayStatus;
import io.naccoll.boilerplate.pay.enums.PayWay;
import io.naccoll.boilerplate.pay.enums.PaymentStatus;
import io.naccoll.boilerplate.pay.model.PayJournalPo;
import io.naccoll.boilerplate.pay.model.PayWechatJournalPo;
import io.naccoll.boilerplate.pay.service.PayWechatJournalQueryService;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

/**
 * The type Wechat trade status polling handler.
 */
@Slf4j
@Component
public class WechatTradeStatusPollingHandler extends AbstractTradeStatusPollingHandler {

	@Resource
	private PayWechatJournalQueryService payWechatJournalQueryService;

	@SneakyThrows
	public void handleMessage(PayJournalPo payJournal, TradeStatusPollingMessage pollingMessage)
			throws MessagingException {
		long wechatJournalId = pollingMessage.getThirdPayJournalId();
		PayWechatJournalPo wechatJournal = payWechatJournalQueryService.findByIdNotNull(wechatJournalId);
		if (!Objects.equals(wechatJournal.getStatus(), PaymentStatus.WAIT_PAY.getId())) {
			return;
		}

		int count = pollingMessage.getCount();
		PayChannel channel = PayChannel.fromId(payJournal.getChannel());

		boolean success = thirdPaySerivce.syncPayOrderStatus(payJournal);
		if (success) {
			return;
		}

		if (!pollingMessage.hasNext() && List.of(PayChannel.WECHAT_MICRO).contains(channel)) {
			log.info("轮询微信交易状态次数超过{}次，进行撤单，orderNo：{}", count, payJournal.getOrderNo());
			thirdPaySerivce.cancelOrder(payJournal);
			payJournalService.updatePayStatus(payJournal.getId(), PayStatus.CANCEL.getId());
			log.info("轮询微信交易状态次数超过{}次，撤单成功，orderNo：{}", count, payJournal.getOrderNo());
		}
	}

	@Override
	public PayWay getPayWay() {
		return PayWay.WECHAT;
	}

}
