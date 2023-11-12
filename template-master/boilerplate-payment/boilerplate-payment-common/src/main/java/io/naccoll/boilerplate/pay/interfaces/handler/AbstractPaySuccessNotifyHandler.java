package io.naccoll.boilerplate.pay.interfaces.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import io.naccoll.boilerplate.pay.dto.PaySuccessNotifyMessage;
import io.naccoll.boilerplate.pay.enums.PayRefundStatus;
import io.naccoll.boilerplate.pay.enums.PayStatus;
import io.naccoll.boilerplate.pay.model.PayJournalPo;
import io.naccoll.boilerplate.pay.model.PayMerchantNotifyTypePo;
import io.naccoll.boilerplate.pay.model.PayMerchantPo;
import io.naccoll.boilerplate.pay.service.PayJournalQueryService;
import io.naccoll.boilerplate.pay.service.PayMerchantNotifyTypeQueryService;
import io.naccoll.boilerplate.pay.service.PayMerchantQueryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.util.ObjectUtils;

/**
 * The type Abstract pay success notify handler.
 */
@Slf4j
@Resource
public abstract class AbstractPaySuccessNotifyHandler extends AbstractNotifyHandler {

	/**
	 * The Pay journal query service.
	 */
	@Resource
	protected PayJournalQueryService payJournalQueryService;

	/**
	 * The Pay merchant notify type query service.
	 */
	@Resource
	protected PayMerchantNotifyTypeQueryService payMerchantNotifyTypeQueryService;

	/**
	 * The Pay merchant query service.
	 */
	@Resource
	protected PayMerchantQueryService payMerchantQueryService;

	@Resource(name = "delayedAsyncNotifyInputChannel")
	private QueueChannel delayedAsyncNotifyInputChannel;

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		if (!(message.getPayload() instanceof PaySuccessNotifyMessage)) {
			return;
		}
		handleMessage((PaySuccessNotifyMessage) message.getPayload());
	}

	/**
	 * Handle message.
	 * @param message the message
	 */
	public void handleMessage(PaySuccessNotifyMessage message) {
		long payJournalId = message.getPayJournalId();
		PayJournalPo payJournal = payJournalQueryService.findByIdNotNull(payJournalId);
		if (!Objects.equals(payJournal.getPayStatus(), PayStatus.PAY.getId())) {
			return;
		}
		if (!Objects.equals(payJournal.getRefundStatus(), PayRefundStatus.NO.getId())) {
			return;
		}
		if (!Objects.equals(payJournal.getPayWay(), getPayWay().getId())) {
			return;
		}
		PayMerchantNotifyTypePo payMerchantNotifyTypePo = payMerchantNotifyTypeQueryService
			.findByMerchantIdAndNotify(payJournal.getPayMerchantId(), payJournal.getNotifyType());
		if (payMerchantNotifyTypePo == null || ObjectUtils.isEmpty(payMerchantNotifyTypePo.getPayNotifyUrl())) {
			return;
		}
		PayMerchantPo merchant = payMerchantQueryService.findByIdNotNull(payJournal.getPayMerchantId());

		Map<String, Object> map = new HashMap<>();
		map.put("orderNo", payJournal.getOrderNo());
		map.put("merchantBusinessNo", payJournal.getOrderNo());
		map.put("payStatus", payJournal.getPayStatus());
		map.put("payMode", getPayWay().name());
		map.put("thirdPayNo", payJournal.getThirdPayNo());
		map.put("paySuccessTime", DateUtil.date(payJournal.getSuccessTime()).getTime());

		generateBodySign(merchant, map);
		String body = JSONUtil.toJsonStr(map);
		int count = message.getCount();
		log.info("开始发送统一支付流水{}第{}次支付回调通知，单号：{}，通知内容：{}", payJournal.getId(), count, payJournal.getOrderNo(), body);
		HttpResponse httpResponse = HttpUtil.createPost(payMerchantNotifyTypePo.getPayNotifyUrl())
			.header("Content-Type", "application/json")
			.header(generateHeaderSign(merchant))
			.body(body)
			.execute();
		if (!httpResponse.isOk() && message.hasNext()) {
			message.next();
			Map<String, Object> headers = new HashMap<>();
			headers.put("delay", getDelay(message.getCreatedTime(), message.getDelaySecond()));
			delayedAsyncNotifyInputChannel.send(new GenericMessage<>(message, headers));
		}
	}

}
