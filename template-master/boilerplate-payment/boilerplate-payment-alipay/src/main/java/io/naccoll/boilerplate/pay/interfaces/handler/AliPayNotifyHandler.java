package io.naccoll.boilerplate.pay.interfaces.handler;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Lock;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.egzosn.pay.ali.api.AliPayService;
import com.egzosn.pay.common.api.PayMessageHandler;
import com.egzosn.pay.common.bean.PayOutMessage;
import com.egzosn.pay.common.exception.PayErrorException;
import io.naccoll.boilerplate.core.cache.CacheTemplate;
import io.naccoll.boilerplate.pay.constant.PayLockConstant;
import io.naccoll.boilerplate.pay.dao.PayAlipayJournalDao;
import io.naccoll.boilerplate.pay.dto.AlipayNotify;
import io.naccoll.boilerplate.pay.dto.PayBackMessage;
import io.naccoll.boilerplate.pay.enums.PayStatus;
import io.naccoll.boilerplate.pay.enums.PaymentStatus;
import io.naccoll.boilerplate.pay.model.PayAlipayJournalPo;
import io.naccoll.boilerplate.pay.model.PayJournalPo;
import io.naccoll.boilerplate.pay.service.PayJournalQueryService;
import io.naccoll.boilerplate.pay.service.PayJournalService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

/**
 * The type Ali pay notify handler.
 */
@Slf4j
@Component
public class AliPayNotifyHandler implements PayMessageHandler<PayBackMessage, AliPayService> {

	@Resource
	private PayAlipayJournalDao alipayJournalDao;

	@Resource
	private PayJournalQueryService payJournalQueryService;

	@Resource
	private PayJournalService payJournalService;

	@Resource
	private CacheTemplate cacheTemplate;

	@Override
	public PayOutMessage handle(PayBackMessage payBackMessage, Map<String, Object> map, AliPayService aliPayService)
			throws PayErrorException {
		boolean valid = aliPayService.verify(map);
		if (!valid) {
			return aliPayService.getPayOutMessage("FAIL", "失败");
		}
		AlipayNotify notify = new AlipayNotify();
		map = MapUtil.toCamelCaseMap(map);
		BeanUtil.fillBeanWithMap(map, notify, true);

		Optional<PayAlipayJournalPo> journalDoOptional = alipayJournalDao
			.findByPayMerchantIdAndOutTradeNo(payBackMessage.getPayMerchantId(), notify.getOutTradeNo());
		if (!journalDoOptional.isPresent()) {
			return aliPayService.getPayOutMessage("FAIL", "失败");
		}
		String key = String.format(PayLockConstant.PAY_ORDER_PREFIX, payBackMessage.getPayMerchantId(),
				notify.getOutTradeNo());
		Lock lock = cacheTemplate.getLock(key);
		lock.lock();
		try {
			PayJournalPo journalPo = payJournalQueryService.findMerchantJournal(payBackMessage.getPayMerchantId(),
					notify.getOutTradeNo());
			PayAlipayJournalPo alipayJournalDo = journalDoOptional.get();
			if (!"TRADE_SUCCESS".equals(notify.getTradeStatus())) {

				alipayJournalDo.setStatus(PaymentStatus.CANCEL.getId());
				alipayJournalDo.setErrMsg(notify.getTradeStatus());
				payJournalService.updatePayStatus(journalPo.getId(), PayStatus.OTHER_ERROR.getId());
				alipayJournalDao.save(alipayJournalDo);
			}
			else {
				journalPo.setPayStatus(PayStatus.PAY.getId());
				alipayJournalDo.setStatus(PayStatus.PAY.getId());
				alipayJournalDo.setBuyerLogonId(notify.getBuyerLogonId());
				alipayJournalDo.setBuyerUserId(notify.getBuyerId());
				alipayJournalDo.setBuyerPayAmount(notify.getBuyerPayAmount());
				alipayJournalDo.setInvoiceAmount(notify.getInvoiceAmount());
				alipayJournalDo.setReceiptAmount(notify.getReceiptAmount());
				alipayJournalDo.setStoreId(notify.getSellerId());
				alipayJournalDo.setTradeNo(notify.getTradeNo());
				alipayJournalDo.setTradeStatus(notify.getTradeStatus());
				alipayJournalDo.setStatus(PaymentStatus.PAY.getId());
				payJournalService.updatePayStatus(journalPo.getId(), PayStatus.PAY.getId());
				alipayJournalDao.save(alipayJournalDo);
			}

		}
		catch (Exception e) {
			return aliPayService.getPayOutMessage("FAIL", "失败");
		}
		finally {
			lock.unlock();
		}
		return aliPayService.getPayOutMessage("SUCCESS", "OK");
	}

}
