package io.naccoll.boilerplate.pay.dao;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import io.naccoll.boilerplate.core.persistence.dao.BaseDao;
import io.naccoll.boilerplate.core.persistence.specification.SpecificationFactory;
import io.naccoll.boilerplate.pay.constant.PayCacheName;
import io.naccoll.boilerplate.pay.dto.PayMerchantQueryCondition;
import io.naccoll.boilerplate.pay.model.PayMerchantPo;
import io.naccoll.boilerplate.pay.model.PayMerchantPo_;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * The interface Pay merchant dao.
 */
public interface PayMerchantDao extends BaseDao<PayMerchantPo, Long> {

	@Override
	@Cacheable(value = PayCacheName.PAY_MERCHANT_ID, key = "#p0")
	Optional<PayMerchantPo> findById(Long id);

	@Override
	List<PayMerchantPo> findAll();

	@Override
	@Caching(put = { @CachePut(value = PayCacheName.PAY_MERCHANT_ID, key = "#result.id"), })
	<S extends PayMerchantPo> S save(S entity);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_MERCHANT_ID, allEntries = true) })
	<S extends PayMerchantPo> List<S> saveAll(Iterable<S> entities);

	@Override
	@Caching(put = { @CachePut(value = PayCacheName.PAY_MERCHANT_ID, key = "#result.id"), })
	<S extends PayMerchantPo> S saveAndFlush(S entity);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_MERCHANT_ID, allEntries = true) })
	<S extends PayMerchantPo> List<S> saveAllAndFlush(Iterable<S> entities);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_MERCHANT_ID, key = "#p0") })
	void deleteById(Long id);

	@Override
	@Caching(
			evict = { @CacheEvict(value = PayCacheName.PAY_MERCHANT_ID, key = "#p0.id", condition = "#p0.id != null") })
	void delete(PayMerchantPo entity);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_MERCHANT_ID, allEntries = true) })
	void deleteAllInBatch(Iterable<PayMerchantPo> entities);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_MERCHANT_ID, allEntries = true) })
	void deleteAllInBatch();

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_MERCHANT_ID, allEntries = true) })
	void deleteAll(Iterable<? extends PayMerchantPo> entities);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_MERCHANT_ID, allEntries = true) })
	void deleteAllById(Iterable<? extends Long> longs);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_MERCHANT_ID, allEntries = true) })
	void deleteAllByIdInBatch(Iterable<Long> longs);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_MERCHANT_ID, allEntries = true) })
	void deleteAll();

	/**
	 * Page page.
	 * @param pageable the pageable
	 * @param condition the condition
	 * @return the page
	 */
	default Page<PayMerchantPo> page(Pageable pageable, PayMerchantQueryCondition condition) {
		Specification<PayMerchantPo> spec = SpecificationFactory.builder();
		if (StringUtils.hasText(condition.getName())) {
			spec = spec.and(SpecificationFactory.equal(PayMerchantPo_.NAME, "%" + condition.getName() + "%"));
		}
		if (condition.getOrganizationId() != null && !Objects.equals(condition.getOrganizationId(), 0L)) {
			spec = spec.and(SpecificationFactory.equal(PayMerchantPo_.ORGANIZATION_ID, condition.getOrganizationId()));
		}
		return findAll(spec, pageable);
	}

}
