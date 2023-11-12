package io.naccoll.boilerplate.pay.channel;

import io.naccoll.boilerplate.core.cache.redis.RedisCacheAutoConfiguration;
import io.naccoll.boilerplate.core.cache.simple.InMemoryCacheAutoConfig;
import io.naccoll.boilerplate.core.channel.ChannelConfig;
import io.naccoll.boilerplate.pay.config.PayChannelConfig;
import io.naccoll.boilerplate.pay.dto.PaySuccessNotifyMessage;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { PayChannelConfig.class })
@Import({ ChannelConfig.class, PayChannelConfig.class, RedisCacheAutoConfiguration.class, RedisAutoConfiguration.class,
		InMemoryCacheAutoConfig.class, JacksonProperties.class })
class PayChannelConfigManual {

	@Resource(name = "asyncNotifyInputChannel")
	private QueueChannel asyncNotifyInputChannel;

	@Resource
	private QueueChannel delayedAsyncNotifyInputChannel;

	@Test
	public void testA() throws InterruptedException {
		asyncNotifyInputChannel.send(new GenericMessage<>(new PaySuccessNotifyMessage(1740049387800887296L)));
		Thread.sleep(600_000L);
	}

	@Test
	public void testB() throws InterruptedException {
		delayedAsyncNotifyInputChannel.send(new GenericMessage<>(new PaySuccessNotifyMessage(1)));
		Thread.sleep(600_000L);
	}

}
