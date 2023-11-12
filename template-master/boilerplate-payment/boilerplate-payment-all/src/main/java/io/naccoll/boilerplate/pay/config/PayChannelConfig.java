package io.naccoll.boilerplate.pay.config;

import java.util.List;

import io.naccoll.boilerplate.pay.dto.PaySuccessNotifyMessage;
import io.naccoll.boilerplate.pay.dto.RefundSuccessNotifyMessage;
import io.naccoll.boilerplate.pay.dto.TradeStatusPollingMessage;
import io.naccoll.boilerplate.pay.interfaces.handler.AbstractPaySuccessNotifyHandler;
import io.naccoll.boilerplate.pay.interfaces.handler.AbstractRefundSuccessNotifyHandler;
import io.naccoll.boilerplate.pay.interfaces.handler.AbstractTradeStatusPollingHandler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PriorityChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.store.ChannelMessageStore;
import org.springframework.integration.store.MessageGroupQueue;
import org.springframework.integration.store.MessageGroupStore;
import org.springframework.messaging.SubscribableChannel;

/**
 * The type Pay channel config.
 */
@Configuration
public class PayChannelConfig {

	private final String prefix = ":pay:";

	@Value("${spring.application.name}")
	private String applicationName;

	/**
	 * Pay success notify output channel subscribable channel.
	 * @param handlers the handlers
	 * @return the subscribable channel
	 */
	@Bean("paySuccessNotifyOutputChannel")
	public SubscribableChannel paySuccessNotifyOutputChannel(List<AbstractPaySuccessNotifyHandler> handlers) {
		SubscribableChannel subscribableChannel = new PublishSubscribeChannel();
		handlers.stream().forEach(h -> subscribableChannel.subscribe(h));
		return subscribableChannel;
	}

	/**
	 * Refund success notify output channel subscribable channel.
	 * @param handlers the handlers
	 * @return the subscribable channel
	 */
	@Bean("refundSuccessNotifyOutputChannel")
	public SubscribableChannel refundSuccessNotifyOutputChannel(List<AbstractRefundSuccessNotifyHandler> handlers) {
		SubscribableChannel subscribableChannel = new PublishSubscribeChannel();
		handlers.stream().forEach(h -> subscribableChannel.subscribe(h));
		return subscribableChannel;
	}

	/**
	 * Trade status polling output channel subscribable channel.
	 * @param handlers the handlers
	 * @return the subscribable channel
	 */
	@Bean("tradeStatusPollingOutputChannel")
	public SubscribableChannel tradeStatusPollingOutputChannel(List<AbstractTradeStatusPollingHandler> handlers) {
		SubscribableChannel subscribableChannel = new PublishSubscribeChannel();
		handlers.stream().forEach(h -> subscribableChannel.subscribe(h));
		return subscribableChannel;
	}

	/**
	 * Async notify input channel queue channel.
	 * @param channelMessageStore the channel message store
	 * @return the queue channel
	 */
	@Bean("asyncNotifyInputChannel")
	public QueueChannel asyncNotifyInputChannel(ChannelMessageStore channelMessageStore) {
		return new PriorityChannel(
				new MessageGroupQueue(channelMessageStore, applicationName + prefix + "async-notify-input"));
	}

	/**
	 * Delayed async notify input channel queue channel.
	 * @param channelMessageStore the channel message store
	 * @return the queue channel
	 */
	@Bean("delayedAsyncNotifyInputChannel")
	public QueueChannel delayedAsyncNotifyInputChannel(ChannelMessageStore channelMessageStore) {
		return new PriorityChannel(
				new MessageGroupQueue(channelMessageStore, applicationName + prefix + "delayed-async-notify-input"));
	}

	/**
	 * Trade status polling input channel queue channel.
	 * @param channelMessageStore the channel message store
	 * @return the queue channel
	 */
	@Bean("tradeStatusPollingInputChannel")
	public QueueChannel tradeStatusPollingInputChannel(ChannelMessageStore channelMessageStore) {
		return new PriorityChannel(
				new MessageGroupQueue(channelMessageStore, applicationName + prefix + "trade-status-polling-input"));
	}

	/**
	 * Asyn notify flow integration flow.
	 * @return the integration flow
	 */
	@Bean
	public IntegrationFlow asynNotifyFlow() {
		return IntegrationFlow.from("asyncNotifyInputChannel")
			.bridge(e -> e.poller(pollerFactory -> pollerFactory.fixedDelay(0)))
			.<Object, Class<?>>route(Object::getClass, m -> {
				m.channelMapping(PaySuccessNotifyMessage.class, "paySuccessNotifyOutputChannel");
				m.channelMapping(RefundSuccessNotifyMessage.class, "refundSuccessNotifyOutputChannel");
			})
			.get();
	}

	/**
	 * Delayed async notify flow integration flow.
	 * @param messageGroupStore the message group store
	 * @return the integration flow
	 */
	@Bean
	public IntegrationFlow delayedAsyncNotifyFlow(MessageGroupStore messageGroupStore) {
		return IntegrationFlow.from("delayedAsyncNotifyInputChannel")
			.bridge(e -> e.poller(pollerFactory -> pollerFactory.fixedDelay(0)))
			.delay(prefix + "async-notify-delay-3m",
					d -> d.defaultDelay(3 * 60 * 1000L)
						.delayExpression("headers['delay']")
						.messageStore(messageGroupStore))
			.<Object, Class<?>>route(Object::getClass, m -> {
				m.channelMapping(PaySuccessNotifyMessage.class, "paySuccessNotifyOutputChannel");
				m.channelMapping(RefundSuccessNotifyMessage.class, "refundSuccessNotifyOutputChannel");
			})
			.get();
	}

	/**
	 * Trade status polling flow integration flow.
	 * @param messageGroupStore the message group store
	 * @return the integration flow
	 */
	@Bean
	public IntegrationFlow tradeStatusPollingFlow(MessageGroupStore messageGroupStore) {
		return IntegrationFlow.from("tradeStatusPollingInputChannel")
			.bridge(e -> e.poller(pollerFactory -> pollerFactory.fixedDelay(0)))
			.delay(prefix + "trade-status-polling-delay-5s",
					d -> d.defaultDelay(5 * 1000L).delayExpression("headers['delay']").messageStore(messageGroupStore))
			.<Object, Class<?>>route(Object::getClass, m -> {
				m.channelMapping(TradeStatusPollingMessage.class, "tradeStatusPollingOutputChannel");
			})
			.get();
	}

}
