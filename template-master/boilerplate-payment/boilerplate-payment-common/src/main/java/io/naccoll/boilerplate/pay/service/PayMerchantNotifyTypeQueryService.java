package io.naccoll.boilerplate.pay.service;

import io.naccoll.boilerplate.pay.model.PayMerchantNotifyTypePo;

public interface PayMerchantNotifyTypeQueryService {

	PayMerchantNotifyTypePo findByMerchantIdAndNotify(Long merchantId, String notifyType);

}
