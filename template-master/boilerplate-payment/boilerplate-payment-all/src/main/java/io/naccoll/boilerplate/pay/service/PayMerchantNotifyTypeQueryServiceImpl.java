package io.naccoll.boilerplate.pay.service;

import io.naccoll.boilerplate.pay.dao.PayMerchantNotifyTypeDao;
import io.naccoll.boilerplate.pay.model.PayMerchantNotifyTypePo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * The type Pay merchant notify type query service.
 */
@Service
public class PayMerchantNotifyTypeQueryServiceImpl implements PayMerchantNotifyTypeQueryService {

	@Resource
	private PayMerchantNotifyTypeDao payMerchantNotifyTypeDao;

	/**
	 * Find by merchant id and notify pay merchant notify type po.
	 * @param merchantId the merchant id
	 * @param notifyType the notify type
	 * @return the pay merchant notify type po
	 */
	public PayMerchantNotifyTypePo findByMerchantIdAndNotify(Long merchantId, String notifyType) {
		return payMerchantNotifyTypeDao.findFirstByPayMerchantIdAndNotifyType(merchantId, notifyType);
	}

}
