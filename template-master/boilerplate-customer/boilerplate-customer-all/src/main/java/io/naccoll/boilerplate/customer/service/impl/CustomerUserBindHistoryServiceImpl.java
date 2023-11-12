package io.naccoll.boilerplate.customer.service.impl;

import java.util.Collection;

import io.naccoll.boilerplate.core.id.IdService;
import io.naccoll.boilerplate.core.validate.Validation;
import io.naccoll.boilerplate.customer.dao.CustomerUserBindHistoryDao;
import io.naccoll.boilerplate.customer.dto.CustomerUserBindHistoryCreateCommand;
import io.naccoll.boilerplate.customer.dto.CustomerUserBindHistoryUpdateCommand;
import io.naccoll.boilerplate.customer.enums.BindStatus;
import io.naccoll.boilerplate.customer.model.CustomerUserBindHistoryPo;
import io.naccoll.boilerplate.customer.service.CustomerUserBindHistoryQueryService;
import io.naccoll.boilerplate.customer.service.CustomerUserBindHistoryService;
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
public class CustomerUserBindHistoryServiceImpl implements CustomerUserBindHistoryService {

	@Resource
	private CustomerUserBindHistoryDao customerUserBindHistoryDao;

	@Resource
	private CustomerUserBindHistoryQueryService customerUserBindHistoryQueryService;

	@Resource
	private IdService idService;

	@Override
	public CustomerUserBindHistoryPo bind(Long customerUserId, Long thirdId, String thirdUserId) {
		return create(CustomerUserBindHistoryCreateCommand.builder()
			.thirdId(thirdId)
			.thirdUserId(thirdUserId)
			.bindStatus(BindStatus.BIND.getId())
			.customerUserId(customerUserId)
			.build());
	}

	@Override
	public CustomerUserBindHistoryPo unbind(Long customerUserId, Long thirdId, String thirdUserId) {
		return create(CustomerUserBindHistoryCreateCommand.builder()
			.thirdId(thirdId)
			.thirdUserId(thirdUserId)
			.bindStatus(BindStatus.UNBIND.getId())
			.customerUserId(customerUserId)
			.build());
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public CustomerUserBindHistoryPo create(@Valid CustomerUserBindHistoryCreateCommand command) {
		CustomerUserBindHistoryPo customerUserBindHistoryPo = new CustomerUserBindHistoryPo();
		BeanUtils.copyProperties(command, customerUserBindHistoryPo);
		customerUserBindHistoryPo.setId(idService.getId());
		return customerUserBindHistoryDao.save(customerUserBindHistoryPo);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public CustomerUserBindHistoryPo update(@Valid CustomerUserBindHistoryUpdateCommand command) {
		CustomerUserBindHistoryPo customerUserBindHistoryPo = customerUserBindHistoryQueryService
			.findByIdNotNull(command.getId());
		BeanUtils.copyProperties(command, customerUserBindHistoryPo);
		return customerUserBindHistoryDao.save(customerUserBindHistoryPo);
	}

	@Override
	public void deleteById(Long id) {
		customerUserBindHistoryDao.deleteById(id);
	}

	@Override
	public void deleteByIds(Collection<Long> ids) {
		ids.forEach(id -> {
			customerUserBindHistoryDao.deleteById(id);
		});
	}

}
