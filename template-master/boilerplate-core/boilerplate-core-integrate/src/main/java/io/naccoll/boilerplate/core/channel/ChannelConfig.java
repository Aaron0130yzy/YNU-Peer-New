package io.naccoll.boilerplate.core.channel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.PriorityChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.store.ChannelMessageStore;
import org.springframework.integration.store.MessageGroupQueue;
import org.springframework.integration.store.MessageGroupStore;
import org.springframework.integration.store.SimpleMessageStore;

/**
 * The type Channel config.
 */
@AutoConfiguration
public class ChannelConfig {

	@Value("${spring.application.name}")
	private String applicationName;

	/**
	 * Simple message store simple message store.
	 * @return the simple message store
	 */
	@ConditionalOnMissingBean({ MessageGroupStore.class, ChannelMessageStore.class })
	@Bean
	public SimpleMessageStore simpleMessageStore() {
		return new SimpleMessageStore();
	}

	/**
	 * General queue input queue channel.
	 * @param channelMessageStore the channel message store
	 * @return the queue channel
	 */
	@Bean("generalQueueInput")
	public QueueChannel generalQueueInput(ChannelMessageStore channelMessageStore) {
		return new PriorityChannel(new MessageGroupQueue(channelMessageStore, applicationName + ":general:input"));
	}

}
