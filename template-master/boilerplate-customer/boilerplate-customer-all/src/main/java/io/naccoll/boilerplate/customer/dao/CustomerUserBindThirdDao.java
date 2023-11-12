package io.naccoll.boilerplate.customer.dao;

import java.util.List;
import java.util.Optional;

import io.naccoll.boilerplate.core.persistence.dao.BaseDao;
import io.naccoll.boilerplate.core.persistence.specification.SpecificationFactory;
import io.naccoll.boilerplate.customer.constant.CustomerCacheName;
import io.naccoll.boilerplate.customer.dto.CustomerUserBindThirdQueryCondition;
import io.naccoll.boilerplate.customer.model.CustomerUserBindThirdPo;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author NaccOll
 */
public interface CustomerUserBindThirdDao extends BaseDao<CustomerUserBindThirdPo, Long> {

	@Override
	@Caching(put = { @CachePut(value = CustomerCacheName.USER_BIND_THIRD_ID, key = "#result.id"), })
	<S extends CustomerUserBindThirdPo> S save(S entity);

	@Override
	@Caching(put = { @CachePut(value = CustomerCacheName.USER_BIND_THIRD_ID, key = "#result.id"), })
	<S extends CustomerUserBindThirdPo> S saveAndFlush(S entity);

	@Override
	@Caching(evict = { @CacheEvict(value = CustomerCacheName.USER_BIND_THIRD_ID, allEntries = true) })
	<S extends CustomerUserBindThirdPo> List<S> saveAll(Iterable<S> entities);

	@Override
	@Caching(evict = { @CacheEvict(value = CustomerCacheName.USER_BIND_THIRD_ID, allEntries = true) })
	<S extends CustomerUserBindThirdPo> List<S> saveAllAndFlush(Iterable<S> entities);

	@Override
	@CacheEvict(value = CustomerCacheName.USER_BIND_THIRD_ID, key = "#p0.id")
	void delete(CustomerUserBindThirdPo entity);

	@Override
	@CacheEvict(value = CustomerCacheName.USER_BIND_THIRD_ID, allEntries = true)
	void deleteAll();

	@Override
	@CacheEvict(value = CustomerCacheName.USER_BIND_THIRD_ID, allEntries = true)
	void deleteAll(Iterable<? extends CustomerUserBindThirdPo> entities);

	@Override
	@CacheEvict(value = CustomerCacheName.USER_BIND_THIRD_ID, allEntries = true)
	void deleteAllById(Iterable<? extends Long> longs);

	@Override
	@CacheEvict(value = CustomerCacheName.USER_BIND_THIRD_ID, allEntries = true)
	void deleteAllByIdInBatch(Iterable<Long> longs);

	@Override
	@CacheEvict(value = CustomerCacheName.USER_BIND_THIRD_ID, allEntries = true)
	void deleteAllInBatch();

	@Override
	@CacheEvict(value = CustomerCacheName.USER_BIND_THIRD_ID, allEntries = true)
	void deleteAllInBatch(Iterable<CustomerUserBindThirdPo> entities);

	@Override
	@CacheEvict(value = CustomerCacheName.USER_BIND_THIRD_ID, key = "#p0")
	void deleteById(Long id);

	@Override
	@Cacheable(value = CustomerCacheName.USER_BIND_THIRD_ID, key = "#p0")
	Optional<CustomerUserBindThirdPo> findById(Long id);

	/**
	 * 查询CustomerUserBindThird列表
	 * @param condition CustomerUserBindThird查询条件
	 * @return CustomerUserBindThird列表
	 */
	default List<CustomerUserBindThirdPo> findAll(CustomerUserBindThirdQueryCondition condition) {
		Specification<CustomerUserBindThirdPo> spec = buildSpecification(condition);
		return findAll(spec);
	}

	/**
	 * 查询CustomerUserBindThird分页
	 * @param condition CustomerUserBindThird查询条件
	 * @param pageable
	 * @return CustomerUserBindThird分页
	 */
	default Page<CustomerUserBindThirdPo> page(CustomerUserBindThirdQueryCondition condition, Pageable pageable) {
		Specification<CustomerUserBindThirdPo> spec = buildSpecification(condition);
		return findAll(spec, pageable);
	}

	default Specification<CustomerUserBindThirdPo> buildSpecification(CustomerUserBindThirdQueryCondition condition) {
		Specification<CustomerUserBindThirdPo> spec = SpecificationFactory.builder();
		return spec;
	}

}
