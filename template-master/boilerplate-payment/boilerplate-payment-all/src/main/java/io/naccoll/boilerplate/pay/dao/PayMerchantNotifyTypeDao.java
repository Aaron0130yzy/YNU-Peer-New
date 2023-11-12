package io.naccoll.boilerplate.pay.dao;

import io.naccoll.boilerplate.core.persistence.dao.BaseDao;
import io.naccoll.boilerplate.pay.model.PayMerchantNotifyTypePo;

/**
 * The interface Pay merchant notify type dao.
 */
public interface PayMerchantNotifyTypeDao extends BaseDao<PayMerchantNotifyTypePo, Long> {

	/**
	 * Find first by pay merchant id and notify type pay merchant notify type po.
	 * @param merchantId the merchant id
	 * @param notifyType the notify type
	 * @return the pay merchant notify type po
	 */
	PayMerchantNotifyTypePo findFirstByPayMerchantIdAndNotifyType(Long merchantId, String notifyType);

}
