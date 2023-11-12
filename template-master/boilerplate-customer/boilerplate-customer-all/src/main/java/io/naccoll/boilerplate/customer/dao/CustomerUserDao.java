package io.naccoll.boilerplate.customer.dao;

import java.util.List;
import java.util.Optional;

import io.naccoll.boilerplate.core.persistence.dao.BaseDao;
import io.naccoll.boilerplate.core.persistence.specification.SpecificationFactory;
import io.naccoll.boilerplate.customer.constant.CustomerCacheName;
import io.naccoll.boilerplate.customer.dto.CustomerUserQueryCondition;
import io.naccoll.boilerplate.customer.model.CustomerUserPo;
import io.naccoll.boilerplate.customer.model.CustomerUserPo_;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * @author NaccOll
 */
public interface CustomerUserDao extends BaseDao<CustomerUserPo, Long> {

	@Override
	@Caching(put = { @CachePut(value = CustomerCacheName.USER_ID, key = "#result.id"), })
	<S extends CustomerUserPo> S save(S entity);

	@Override
	@Caching(put = { @CachePut(value = CustomerCacheName.USER_ID, key = "#result.id"), })
	<S extends CustomerUserPo> S saveAndFlush(S entity);

	@Override
	@Caching(evict = { @CacheEvict(value = CustomerCacheName.USER_ID, allEntries = true) })
	<S extends CustomerUserPo> List<S> saveAll(Iterable<S> entities);

	@Override
	@Caching(evict = { @CacheEvict(value = CustomerCacheName.USER_ID, allEntries = true) })
	<S extends CustomerUserPo> List<S> saveAllAndFlush(Iterable<S> entities);

	@Override
	@CacheEvict(value = CustomerCacheName.USER_ID, key = "#p0.id")
	void delete(CustomerUserPo entity);

	@Override
	@CacheEvict(value = CustomerCacheName.USER_ID, allEntries = true)
	void deleteAll();

	@Override
	@CacheEvict(value = CustomerCacheName.USER_ID, allEntries = true)
	void deleteAll(Iterable<? extends CustomerUserPo> entities);

	@Override
	@CacheEvict(value = CustomerCacheName.USER_ID, allEntries = true)
	void deleteAllById(Iterable<? extends Long> longs);

	@Override
	@CacheEvict(value = CustomerCacheName.USER_ID, allEntries = true)
	void deleteAllByIdInBatch(Iterable<Long> longs);

	@Override
	@CacheEvict(value = CustomerCacheName.USER_ID, allEntries = true)
	void deleteAllInBatch();

	@Override
	@CacheEvict(value = CustomerCacheName.USER_ID, allEntries = true)
	void deleteAllInBatch(Iterable<CustomerUserPo> entities);

	@Override
	@CacheEvict(value = CustomerCacheName.USER_ID, key = "#p0")
	void deleteById(Long id);

	@Override
	@Cacheable(value = CustomerCacheName.USER_ID, key = "#p0")
	Optional<CustomerUserPo> findById(Long id);

	/**
	 * 根据 Username 查询
	 * @param username /
	 * @return /
	 */
	CustomerUserPo findFirstByUsername(String username);

	/**
	 * 根据手机号查询
	 * @param tel 手机号
	 * @return C端用户
	 */
	CustomerUserPo findFirstByTel(String tel);

	/**
	 * 查询CustomerUser列表
	 * @param condition CustomerUser查询条件
	 * @return CustomerUser列表
	 */
	default List<CustomerUserPo> findAll(CustomerUserQueryCondition condition) {
		Specification<CustomerUserPo> spec = buildSpecification(condition);
		return findAll(spec);
	}

	/**
	 * 查询CustomerUser分页
	 * @param condition CustomerUser查询条件
	 * @param pageable
	 * @return CustomerUser分页
	 */
	default Page<CustomerUserPo> page(CustomerUserQueryCondition condition, Pageable pageable) {
		Specification<CustomerUserPo> spec = buildSpecification(condition);
		return findAll(spec, pageable);
	}

	default Specification<CustomerUserPo> buildSpecification(CustomerUserQueryCondition condition) {
		Specification<CustomerUserPo> spec = SpecificationFactory.builder();
		if (StringUtils.hasText(condition.getUsername())) {
			spec = spec.and(SpecificationFactory.equal(CustomerUserPo_.USERNAME, condition.getUsername()));
		}
		if (StringUtils.hasText(condition.getTel())) {
			spec = spec.and(SpecificationFactory.equal(CustomerUserPo_.TEL, condition.getTel()));
		}
		if (StringUtils.hasText(condition.getEmail())) {
			spec = spec.and(SpecificationFactory.equal(CustomerUserPo_.EMAIL, condition.getEmail()));
		}
		return spec;
	}

}
