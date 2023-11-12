package io.naccoll.boilerplate.pay.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import io.naccoll.boilerplate.pay.dto.PayJournalQueryCondition;
import io.naccoll.boilerplate.pay.model.PayJournalPo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PayJournalQueryService {

	PayJournalPo findMerchantJournal(Long merchantId, String orderNo);

	List<PayJournalPo> findByOrderNo(String orderNo);

	public PayJournalPo findByIdNotNull(Long id);

	Map<Long, PayJournalPo> queryMapByIds(Set<Long> ids);

	Page<PayJournalPo> queryPage(PayJournalQueryCondition condition, Pageable pageable);

}
