package io.naccoll.boilerplate.pay.interfaces.handler;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import io.naccoll.boilerplate.pay.dto.TradeStatusPollingMessage;
import io.naccoll.boilerplate.pay.enums.PayChannel;
import io.naccoll.boilerplate.pay.enums.PayStatus;
import io.naccoll.boilerplate.pay.enums.PayWay;
import io.naccoll.boilerplate.pay.model.PayJournalPo;
import io.naccoll.boilerplate.pay.service.PayJournalQueryService;
import io.naccoll.boilerplate.pay.service.PayJournalService;
import io.naccoll.boilerplate.pay.service.ThirdPaySerivce;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.GenericMessage;

/**
 * The type Abstract trade status polling handler.
 */
@Slf4j
public abstract class AbstractTradeStatusPollingHandler implements MessageHandler {

	/**
	 * The Trade status polling input channel.
	 */
	@Resource(name = "tradeStatusPollingInputChannel")
	protected QueueChannel tradeStatusPollingInputChannel;

	/**
	 * The Third pay serivce.
	 */
	@Resource
	protected ThirdPaySerivce thirdPaySerivce;

	/**
	 * The Pay journal service.
	 */
	@Resource
	protected PayJournalService payJournalService;

	/**
	 * The Pay journal query service.
	 */
	@Resource
	protected PayJournalQueryService payJournalQueryService;

	/**
	 * Generate polling delays duration [ ].
	 * @return the duration [ ]
	 */
	public static Duration[] generatePollingDelays() {
		return new Duration[] { Duration.ofSeconds(5), Duration.ofSeconds(10), Duration.ofSeconds(10),
				Duration.ofSeconds(10), Duration.ofSeconds(10), Duration.ofSeconds(10), Duration.ofSeconds(10),
				Duration.ofSeconds(30), Duration.ofSeconds(30), Duration.ofSeconds(30), Duration.ofSeconds(60),
				Duration.ofSeconds(60), Duration.ofSeconds(60), Duration.ofMinutes(5), Duration.ofMinutes(5),
				Duration.ofMinutes(10), Duration.ofMinutes(10) };
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		Object payload = message.getPayload();
		if (!(payload instanceof TradeStatusPollingMessage pollingMessage)) {
			log.info("未知消息，丢弃。{}", JSONUtil.toJsonStr(payload));
			return;
		}
		PayJournalPo payJournal = payJournalQueryService.findByIdNotNull(pollingMessage.getPayJournalId());
		if (!Objects.equals(payJournal.getPayWay(), getPayWay().getId())) {
			return;
		}
		int count = Optional.of(pollingMessage.getCount()).orElse(1);
		long payJournalId = payJournal.getId();
		String orderNo = payJournal.getOrderNo();
		PayChannel channel = PayChannel.fromId(payJournal.getChannel());

		log.info("统一支付流水{}进行第{}次交易状态轮询查询，单号=[{}], 支付渠道=[{}]", payJournalId, count, orderNo, channel.getName());
		if (!Objects.equals(payJournal.getPayStatus(), PayStatus.WAIT_PAY.getId())) {
			return;
		}

		if (pollingMessage.hasNext()) {
			pollingMessage.next();
			Map<String, Object> headers = new HashMap<>();
			headers.put("delay", getDelay(pollingMessage));
			tradeStatusPollingInputChannel.send(new GenericMessage<>(pollingMessage, headers));
		}

		try {
			handleMessage(payJournal, pollingMessage);
		}
		catch (Exception e) {
			log.error("统一支付流水{}进行第{}次交易状态轮询查询失败，单号=[{}], 支付渠道=[{}],异常信息：{}", payJournalId, count, orderNo,
					channel.getName(), e.getMessage());
		}
	}

	/**
	 * Handle message.
	 * @param payJournal the pay journal
	 * @param message the message
	 */
	public abstract void handleMessage(PayJournalPo payJournal, TradeStatusPollingMessage message);

	/**
	 * Gets pay way.
	 * @return the pay way
	 */
	public abstract PayWay getPayWay();

	/**
	 * Gets delay.
	 * @param message the message
	 * @return the delay
	 */
	protected Date getDelay(TradeStatusPollingMessage message) {
		return DateUtil.offsetSecond(new Date(message.getCreatedTime()), message.getDelaySecond());
	}

}
