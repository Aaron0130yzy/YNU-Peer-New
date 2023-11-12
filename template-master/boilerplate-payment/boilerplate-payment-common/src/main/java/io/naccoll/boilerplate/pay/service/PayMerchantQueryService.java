package io.naccoll.boilerplate.pay.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.naccoll.boilerplate.pay.dto.PayMerchantQueryCondition;
import io.naccoll.boilerplate.pay.model.PayMerchantPo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PayMerchantQueryService {

	PayMerchantPo findByIdNotNull(Long id);

	Map<Long, PayMerchantPo> findMapByIds(Collection<Long> ids);

	List<PayMerchantPo> findByIds(Collection<Long> ids);

	Page<PayMerchantPo> page(Pageable pageable, PayMerchantQueryCondition condition);

}
