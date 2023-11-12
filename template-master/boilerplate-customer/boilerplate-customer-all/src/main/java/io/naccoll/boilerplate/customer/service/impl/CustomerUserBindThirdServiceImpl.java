package io.naccoll.boilerplate.customer.service.impl;

import java.util.Collection;

import io.naccoll.boilerplate.core.id.IdService;
import io.naccoll.boilerplate.core.validate.Validation;
import io.naccoll.boilerplate.customer.dao.CustomerUserBindThirdDao;
import io.naccoll.boilerplate.customer.dto.CustomerUserBindThirdCreateCommand;
import io.naccoll.boilerplate.customer.dto.CustomerUserBindThirdUpdateCommand;
import io.naccoll.boilerplate.customer.model.CustomerUserBindThirdPo;
import io.naccoll.boilerplate.customer.service.CustomerUserBindThirdQueryService;
import io.naccoll.boilerplate.customer.service.CustomerUserBindThirdService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author NaccOll
 */
@Service
@Validation
public class CustomerUserBindThirdServiceImpl implements CustomerUserBindThirdService {

	@Resource
	private CustomerUserBindThirdDao customerUserBindThirdDao;

	@Resource
	private CustomerUserBindThirdQueryService customerUserBindThirdQueryService;

	@Resource
	private IdService idService;

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public CustomerUserBindThirdPo create(@Valid CustomerUserBindThirdCreateCommand command) {
		CustomerUserBindThirdPo customerUserBindThirdPo = new CustomerUserBindThirdPo();
		BeanUtils.copyProperties(command, customerUserBindThirdPo);
		customerUserBindThirdPo.setId(idService.getId());
		return customerUserBindThirdDao.save(customerUserBindThirdPo);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public CustomerUserBindThirdPo update(@Valid CustomerUserBindThirdUpdateCommand command) {
		CustomerUserBindThirdPo customerUserBindThirdPo = customerUserBindThirdQueryService
			.findByIdNotNull(command.getId());
		BeanUtils.copyProperties(command, customerUserBindThirdPo);
		return customerUserBindThirdDao.save(customerUserBindThirdPo);
	}

	@Override
	public void deleteById(Long id) {
		customerUserBindThirdDao.deleteById(id);
	}

	@Override
	public void deleteByIds(Collection<Long> ids) {
		ids.forEach(id -> {
			customerUserBindThirdDao.deleteById(id);
		});
	}

}
