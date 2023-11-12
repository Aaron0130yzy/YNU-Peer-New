package io.naccoll.boilerplate.pay.service;

import java.util.List;

import io.naccoll.boilerplate.pay.dto.PayRefundJournalQueryCondition;
import io.naccoll.boilerplate.pay.model.PayRefundJournalPo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PayRefundJournalQueryService {

	PayRefundJournalPo findByMerchantIdAndRefundOrderNo(Long merchantId, String refundOrderNo);

	PayRefundJournalPo findById(Long id);

	List<PayRefundJournalPo> findByPayJournalId(Long payId);

	Page<PayRefundJournalPo> queryPage(PayRefundJournalQueryCondition condition, Pageable pageable);

}
