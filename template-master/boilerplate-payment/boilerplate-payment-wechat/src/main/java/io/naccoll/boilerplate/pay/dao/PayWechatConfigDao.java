package io.naccoll.boilerplate.pay.dao;

import java.util.List;
import java.util.Optional;

import io.naccoll.boilerplate.core.persistence.dao.BaseDao;
import io.naccoll.boilerplate.core.persistence.specification.SpecificationFactory;
import io.naccoll.boilerplate.pay.constant.PayCacheName;
import io.naccoll.boilerplate.pay.dto.PayWechatConfigQueryCondition;
import io.naccoll.boilerplate.pay.model.PayWechatConfigPo;
import io.naccoll.boilerplate.pay.model.PayWechatConfigPo_;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * The interface Pay wechat config dao.
 */
public interface PayWechatConfigDao extends BaseDao<PayWechatConfigPo, Long> {

	@Override
	@Cacheable(value = PayCacheName.PAY_WX_CONFIG_ID, key = "#p0")
	Optional<PayWechatConfigPo> findById(Long id);

	@Override
	List<PayWechatConfigPo> findAll();

	@Override
	@Caching(put = { @CachePut(value = PayCacheName.PAY_WX_CONFIG_ID, key = "#result.id"), })
	<S extends PayWechatConfigPo> S save(S entity);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_WX_CONFIG_ID, allEntries = true) })
	<S extends PayWechatConfigPo> List<S> saveAll(Iterable<S> entities);

	@Override
	@Caching(put = { @CachePut(value = PayCacheName.PAY_WX_CONFIG_ID, key = "#result.id"), })
	<S extends PayWechatConfigPo> S saveAndFlush(S entity);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_WX_CONFIG_ID, allEntries = true) })
	<S extends PayWechatConfigPo> List<S> saveAllAndFlush(Iterable<S> entities);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_WX_CONFIG_ID, key = "#p0") })
	void deleteById(Long id);

	@Override
	@Caching(evict = {
			@CacheEvict(value = PayCacheName.PAY_WX_CONFIG_ID, key = "#p0.id", condition = "#p0.id != null") })
	void delete(PayWechatConfigPo entity);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_WX_CONFIG_ID, allEntries = true) })
	void deleteAllInBatch(Iterable<PayWechatConfigPo> entities);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_WX_CONFIG_ID, allEntries = true) })
	void deleteAllInBatch();

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_WX_CONFIG_ID, allEntries = true) })
	void deleteAll(Iterable<? extends PayWechatConfigPo> entities);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_WX_CONFIG_ID, allEntries = true) })
	void deleteAllById(Iterable<? extends Long> longs);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_WX_CONFIG_ID, allEntries = true) })
	void deleteAllByIdInBatch(Iterable<Long> longs);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_WX_CONFIG_ID, allEntries = true) })
	void deleteAll();

	/**
	 * Find first by pay merchant id and mchid and appid pay wechat config po.
	 * @param merchantId the merchant id
	 * @param mchid the mchid
	 * @param appid the appid
	 * @return the pay wechat config po
	 */
	PayWechatConfigPo findFirstByPayMerchantIdAndMchidAndAppid(Long merchantId, String mchid, String appid);

	/**
	 * Find by pay merchant id list.
	 * @param merchantId the merchant id
	 * @return the list
	 */
	List<PayWechatConfigPo> findByPayMerchantId(Long merchantId);

	/**
	 * Page page.
	 * @param pageable the pageable
	 * @param condition the condition
	 * @return the page
	 */
	default Page<PayWechatConfigPo> page(Pageable pageable, PayWechatConfigQueryCondition condition) {
		Specification<PayWechatConfigPo> spec = SpecificationFactory.builder();
		if (condition.getPayMerchantId() != null) {
			spec = spec
				.and(SpecificationFactory.equal(PayWechatConfigPo_.PAY_MERCHANT_ID, condition.getPayMerchantId()));
		}

		return findAll(spec, pageable);
	}

	/**
	 * Find first by pay merchant id and code and status pay wechat config po.
	 * @param merchantId the merchant id
	 * @param code the code
	 * @param status the status
	 * @return the pay wechat config po
	 */
	PayWechatConfigPo findFirstByPayMerchantIdAndCodeAndStatus(Long merchantId, String code, Integer status);

}
