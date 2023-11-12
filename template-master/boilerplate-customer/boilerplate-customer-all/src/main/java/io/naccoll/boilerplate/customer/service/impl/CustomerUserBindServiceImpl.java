package io.naccoll.boilerplate.customer.service.impl;

import io.naccoll.boilerplate.core.audit.operate.OperateLog;
import io.naccoll.boilerplate.core.id.IdService;
import io.naccoll.boilerplate.core.validate.Validation;
import io.naccoll.boilerplate.customer.dao.CustomerUserBindDao;
import io.naccoll.boilerplate.customer.dto.CustomerUserBindCreateCommand;
import io.naccoll.boilerplate.customer.dto.CustomerUserBindUpdateCommand;
import io.naccoll.boilerplate.customer.event.ThirdBindEvent;
import io.naccoll.boilerplate.customer.event.ThirdUnbindEvent;
import io.naccoll.boilerplate.customer.model.CustomerUserBindPo;
import io.naccoll.boilerplate.customer.model.CustomerUserBindThirdPo;
import io.naccoll.boilerplate.customer.service.CustomerUserBindHistoryService;
import io.naccoll.boilerplate.customer.service.CustomerUserBindQueryService;
import io.naccoll.boilerplate.customer.service.CustomerUserBindService;
import io.naccoll.boilerplate.customer.service.CustomerUserBindThirdQueryService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author NaccOll
 */
@Service
@Validation
public class CustomerUserBindServiceImpl implements CustomerUserBindService {

	@Resource
	private CustomerUserBindDao customerUserBindDao;

	@Resource
	private CustomerUserBindQueryService customerUserBindQueryService;

	@Resource
	private IdService idService;

	@Resource
	private CustomerUserBindHistoryService customerUserBindHistoryService;

	@Resource
	private ApplicationEventPublisher applicationEventPublisher;

	@Resource
	private CustomerUserBindThirdQueryService customerUserBindThirdQueryService;

	@Transactional(rollbackFor = Throwable.class)
	public CustomerUserBindPo bind(Long customerUserId, Long thirdId, String thirdUserId) {
		CustomerUserBindPo customerUserBindPo = new CustomerUserBindPo();
		customerUserBindPo.setId(idService.getId());
		customerUserBindPo.setCustomerUserId(customerUserId);
		customerUserBindPo.setThirdId(thirdId);
		customerUserBindPo.setThirdUserId(thirdUserId);
		customerUserBindPo = customerUserBindDao.save(customerUserBindPo);
		customerUserBindHistoryService.bind(customerUserId, thirdId, thirdUserId);
		return customerUserBindPo;
	}

	@Override
	public CustomerUserBindPo bindSync(Long customerUserId, Long thirdId, String thirdUserId) {
		CustomerUserBindThirdPo third = customerUserBindThirdQueryService.findByIdNotNull(thirdId);
		CustomerUserBindPo customerUserBindPo = bind(customerUserId, thirdId, thirdUserId);
		applicationEventPublisher.publishEvent(new ThirdBindEvent(third, customerUserBindPo));
		return customerUserBindPo;
	}

	@Override
	@Transactional
	public void unbind(Long customerUserId, Long thirdId) {
		CustomerUserBindThirdPo third = customerUserBindThirdQueryService.findByIdNotNull(thirdId);
		CustomerUserBindPo bind = customerUserBindQueryService.findByThirdIdAndUserId(thirdId, customerUserId);
		customerUserBindHistoryService.unbind(bind.getCustomerUserId(), bind.getThirdId(), bind.getThirdUserId());
		customerUserBindDao.deleteById(bind.getId());
		applicationEventPublisher.publishEvent(new ThirdUnbindEvent(third, bind));
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public CustomerUserBindPo create(@Valid CustomerUserBindCreateCommand command) {
		CustomerUserBindPo po = new CustomerUserBindPo();
		BeanUtils.copyProperties(command, po);
		po.setId(idService.getId());
		return customerUserBindDao.save(po);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public CustomerUserBindPo update(@Valid CustomerUserBindUpdateCommand command) {
		CustomerUserBindPo po = customerUserBindQueryService.findByIdNotNull(command.getId());
		BeanUtils.copyProperties(command, po);
		return customerUserBindDao.save(po);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	@OperateLog(value = "删除用户绑定关系", id = "#id")
	public void deleteById(Long id) {
		CustomerUserBindPo bind = customerUserBindQueryService.findByIdNotNull(id);
		CustomerUserBindThirdPo third = customerUserBindThirdQueryService.findByIdNotNull(bind.getThirdId());
		customerUserBindHistoryService.unbind(bind.getCustomerUserId(), bind.getThirdId(), bind.getThirdUserId());
		customerUserBindDao.deleteById(id);
		applicationEventPublisher.publishEvent(new ThirdUnbindEvent(third, bind));
	}

}
