package io.naccoll.boilerplate.pay.interfaces.handler;

import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import io.naccoll.boilerplate.pay.dto.RefundSuccessNotifyMessage;
import io.naccoll.boilerplate.pay.model.PayMerchantNotifyTypePo;
import io.naccoll.boilerplate.pay.model.PayMerchantPo;
import io.naccoll.boilerplate.pay.model.PayRefundJournalPo;
import io.naccoll.boilerplate.pay.service.PayMerchantNotifyTypeQueryService;
import io.naccoll.boilerplate.pay.service.PayMerchantQueryService;
import io.naccoll.boilerplate.pay.service.PayRefundJournalQueryService;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.util.ObjectUtils;

/**
 * The type Abstract refund success notify handler.
 */
@Slf4j
public abstract class AbstractRefundSuccessNotifyHandler extends AbstractNotifyHandler {

	@Resource
	private PayMerchantNotifyTypeQueryService payMerchantNotifyTypeQueryService;

	@Resource
	private PayMerchantQueryService payMerchantQueryService;

	@Resource(name = "delayedAsyncNotifyInputChannel")
	private QueueChannel delayedAsyncNotifyInputChannel;

	@Resource
	private PayRefundJournalQueryService payRefundJournalQueryService;

	@SneakyThrows
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		if (!(message.getPayload() instanceof RefundSuccessNotifyMessage refundSuccessNotifyMessage)) {
			return;
		}
		handleMessage(refundSuccessNotifyMessage);
	}

	/**
	 * Handle message.
	 * @param message the message
	 */
	public void handleMessage(RefundSuccessNotifyMessage message) {

		long refundJournalId = message.getPayRefundJournalId();
		PayRefundJournalPo payRefundJournal = payRefundJournalQueryService.findById(refundJournalId);
		payRefundJournal = payRefundJournalQueryService.findById(payRefundJournal.getId());

		PayMerchantNotifyTypePo payMerchantNotifyTypePo = payMerchantNotifyTypeQueryService
			.findByMerchantIdAndNotify(payRefundJournal.getPayMerchantId(), payRefundJournal.getNotifyType());
		if (payMerchantNotifyTypePo == null || ObjectUtils.isEmpty(payMerchantNotifyTypePo.getRefundNotifyUrl())) {
			return;
		}
		PayMerchantPo merchant = payMerchantQueryService.findByIdNotNull(payRefundJournal.getPayMerchantId());
		Map<String, Object> map = new HashMap<>();
		map.put("orderNo", payRefundJournal.getOrderNo());
		map.put("merchantBusinessNo", payRefundJournal.getMerchantBusinessNo());
		map.put("refundOrderNo", payRefundJournal.getRefundOrderNo());
		map.put("refundStatus", payRefundJournal.getRefundStatus());
		map.put("payMode", getPayWay().name());
		map.put("thirdPayNo", payRefundJournal.getThirdPayNo());
		map.put("thirdRefundNo", payRefundJournal.getRefundOrderNo());
		map.put("refundSuccessTime", DateUtil.date(payRefundJournal.getSuccessTime()).getTime());

		generateBodySign(merchant, map);
		String body = JSONUtil.toJsonStr(map);
		int count = message.getCount();
		log.info("开始发送统一退款流水{}第{}次退款回调通知，单号：{}，通知内容：{}", payRefundJournal.getId(), count,
				payRefundJournal.getRefundOrderNo(), body);
		HttpResponse httpResponse = HttpUtil.createPost(payMerchantNotifyTypePo.getRefundNotifyUrl())
			.header("Content-Type", "application/json")
			.header(generateHeaderSign(merchant))
			.body(JSONUtil.toJsonStr(map))
			.execute();
		if (!httpResponse.isOk() && message.hasNext()) {
			message.next();
			Map<String, Object> headers = new HashMap<>();
			headers.put("delay", getDelay(message.getCreatedTime(), message.getDelaySecond()));
			delayedAsyncNotifyInputChannel.send(new GenericMessage<>(message, headers));
		}
	}

}
