package io.naccoll.boilerplate.pay.dao;

import java.util.List;
import java.util.Optional;

import io.naccoll.boilerplate.core.persistence.dao.BaseDao;
import io.naccoll.boilerplate.pay.constant.PayCacheName;
import io.naccoll.boilerplate.pay.model.PayAlipayConfigPo;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

/**
 * The interface Pay alipay config dao.
 */
public interface PayAlipayConfigDao extends BaseDao<PayAlipayConfigPo, Long> {

	@Override
	@Cacheable(value = PayCacheName.PAY_ALI_CONFIG_ID, key = "#p0")
	Optional<PayAlipayConfigPo> findById(Long id);

	@Override
	List<PayAlipayConfigPo> findAll();

	@Override
	@Caching(put = { @CachePut(value = PayCacheName.PAY_ALI_CONFIG_ID, key = "#result.id"), })
	<S extends PayAlipayConfigPo> S save(S entity);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_ALI_CONFIG_ID, allEntries = true) })
	<S extends PayAlipayConfigPo> List<S> saveAll(Iterable<S> entities);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_ALI_CONFIG_ID, allEntries = true) })
	<S extends PayAlipayConfigPo> List<S> saveAllAndFlush(Iterable<S> entities);

	@Override
	@Caching(put = { @CachePut(value = PayCacheName.PAY_ALI_CONFIG_ID, key = "#result.id"), })
	<S extends PayAlipayConfigPo> S saveAndFlush(S entity);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_ALI_CONFIG_ID, key = "#p0") })
	void deleteById(Long id);

	@Override
	@Caching(evict = {
			@CacheEvict(value = PayCacheName.PAY_ALI_CONFIG_ID, key = "#p0.id", condition = "#p0.id != null") })
	void delete(PayAlipayConfigPo entity);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_ALI_CONFIG_ID, allEntries = true) })
	void deleteAllInBatch(Iterable<PayAlipayConfigPo> entities);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_ALI_CONFIG_ID, allEntries = true) })
	void deleteAllInBatch();

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_ALI_CONFIG_ID, allEntries = true) })
	void deleteAll(Iterable<? extends PayAlipayConfigPo> entities);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_ALI_CONFIG_ID, allEntries = true) })
	void deleteAllById(Iterable<? extends Long> longs);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_ALI_CONFIG_ID, allEntries = true) })
	void deleteAllByIdInBatch(Iterable<Long> longs);

	@Override
	@Caching(evict = { @CacheEvict(value = PayCacheName.PAY_ALI_CONFIG_ID, allEntries = true) })
	void deleteAll();

	/**
	 * Find first by pay merchant id and partner id and appid pay alipay config po.
	 * @param payMerchantId the pay merchant id
	 * @param partnerId the partner id
	 * @param appid the appid
	 * @return the pay alipay config po
	 */
	PayAlipayConfigPo findFirstByPayMerchantIdAndPartnerIdAndAppid(Long payMerchantId, String partnerId, String appid);

	/**
	 * Find by pay merchant id list.
	 * @param payMerchantId the pay merchant id
	 * @return the list
	 */
	List<PayAlipayConfigPo> findByPayMerchantId(Long payMerchantId);

	/**
	 * Find first by pay merchant id and code and status pay alipay config po.
	 * @param payMerchantId the pay merchant id
	 * @param code the code
	 * @param status the status
	 * @return the pay alipay config po
	 */
	PayAlipayConfigPo findFirstByPayMerchantIdAndCodeAndStatus(Long payMerchantId, String code, Integer status);

}
