package io.naccoll.boilerplate.pay.interfaces.handler;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import io.naccoll.boilerplate.core.enums.EnumHelper;
import io.naccoll.boilerplate.core.id.IdService;
import io.naccoll.boilerplate.core.security.service.DataSecurityService;
import io.naccoll.boilerplate.pay.enums.PayMerchantSignAlgorithm;
import io.naccoll.boilerplate.pay.enums.PayWay;
import io.naccoll.boilerplate.pay.model.PayMerchantPo;
import jakarta.annotation.Resource;

import org.springframework.messaging.MessageHandler;
import org.springframework.util.StringUtils;

/**
 * The type Abstract notify handler.
 */
public abstract class AbstractNotifyHandler implements MessageHandler {

	@Resource
	private DataSecurityService dataSecurityService;

	@Resource
	private IdService idService;

	/**
	 * Generate polling delays duration [ ].
	 * @return the duration [ ]
	 */
	public static Duration[] generatePollingDelays() {
		return new Duration[] { Duration.ofSeconds(15), Duration.ofSeconds(15), Duration.ofSeconds(30),
				Duration.ofMinutes(3), Duration.ofMinutes(10), Duration.ofMinutes(20), Duration.ofMinutes(30),
				Duration.ofMinutes(30), Duration.ofMinutes(30), Duration.ofMinutes(60), Duration.ofHours(3),
				Duration.ofHours(3), Duration.ofHours(3), Duration.ofHours(6), Duration.ofHours(6) };
	}

	/**
	 * Gets delay.
	 * @param createdTimestamp the created timestamp
	 * @param delaySecond the delay second
	 * @return the delay
	 */
	protected Date getDelay(long createdTimestamp, int delaySecond) {
		return DateUtil.offsetSecond(new Date(createdTimestamp), delaySecond);
	}

	/**
	 * Generate body sign.
	 * @param merchant the merchant
	 * @param map the map
	 */
	public void generateBodySign(PayMerchantPo merchant, Map<String, Object> map) {
		long notifyId = idService.getId();
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		map.put("notifyId", notifyId);
		map.put("notifyTime", timestamp);

		List<String> keys = map.keySet().stream().sorted().collect(Collectors.toList());
		List<String> originList = new LinkedList<>();
		for (String key : keys) {
			if (map.get(key) != null && StringUtils.hasText(map.get(key).toString())) {
				String item = key + "=";
				item = item + map.get(key).toString();
				originList.add(item);
			}
		}
		String originString = String.join("&", originList);
		HMac mac = new HMac(getAlgorithm(merchant), dataSecurityService.decryptStr(merchant.getSecret()).getBytes());
		String sign = mac.digestHex(originString);
		map.put("sign", sign);
	}

	/**
	 * Generate header sign map.
	 * @param merchant the merchant
	 * @return the map
	 */
	public Map<String, List<String>> generateHeaderSign(PayMerchantPo merchant) {
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		String nonceStr = UUID.randomUUID().toString();

		Map<String, List<String>> map = new HashMap<>();
		map.put("pay-merchant-id", Collections.singletonList(merchant.getId().toString()));
		map.put("pay-timestamp", Collections.singletonList(timestamp));
		map.put("pay-noncestr", Collections.singletonList(nonceStr));
		HMac mac = new HMac(getAlgorithm(merchant), dataSecurityService.decryptStr(merchant.getSecret()).getBytes());
		map.put("pay-signature", Collections.singletonList(mac.digestHex(merchant.getId() + nonceStr + timestamp)));
		return map;
	}

	private HmacAlgorithm getAlgorithm(PayMerchantPo merchant) {
		HmacAlgorithm algorithm = Optional.of(merchant.getSignAlgorithm())
			.filter(i -> EnumHelper.isValid(PayMerchantSignAlgorithm.class, i))
			.map(PayMerchantSignAlgorithm::fromId)
			.orElse(PayMerchantSignAlgorithm.HmacSHA256)
			.getAlgorithm();
		return algorithm;
	}

	/**
	 * Gets pay way.
	 * @return the pay way
	 */
	public abstract PayWay getPayWay();

}
