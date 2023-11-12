package io.naccoll.boilerplate.pay.service;

import cn.hutool.core.util.RandomUtil;
import io.naccoll.boilerplate.core.id.IdService;
import io.naccoll.boilerplate.core.security.enums.SecurityDataType;
import io.naccoll.boilerplate.core.security.service.DataSecurityService;
import io.naccoll.boilerplate.core.validate.Validation;
import io.naccoll.boilerplate.pay.dao.PayMerchantDao;
import io.naccoll.boilerplate.pay.dto.PayMerchantCreateCommand;
import io.naccoll.boilerplate.pay.model.PayMerchantPo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

/**
 * The type Pay merchant service.
 */
@Service
@Validation
public class PayMerchantService {

	/**
	 * The Pay merchant dao.
	 */
	@Resource
	public PayMerchantDao payMerchantDao;

	@Resource
	private PayMerchantQueryService payMerchantQueryService;

	@Resource
	private DataSecurityService dataSecurityService;

	@Resource
	private IdService idService;

	/**
	 * Create pay merchant po.
	 * @param command the command
	 * @return the pay merchant po
	 */
	public PayMerchantPo create(@Valid PayMerchantCreateCommand command) {
		PayMerchantPo payMerchantPo = new PayMerchantPo();
		payMerchantPo.setId(idService.getId());
		payMerchantPo.setName(command.getName());
		payMerchantPo
			.setSecret(dataSecurityService.encryptStr(RandomUtil.randomString(32), SecurityDataType.PAYMENT_CONFIG));
		payMerchantPo.setOrganizationId(command.getOrganizationId());
		payMerchantPo.setDepartId(command.getDepartId());
		payMerchantPo.setSignAlgorithm(command.getSignAlgorithm());
		return payMerchantDao.save(payMerchantPo);
	}

	/**
	 * Delete by id.
	 * @param id the id
	 */
	public void deleteById(Long id) {
		PayMerchantPo payMerchantPo = payMerchantQueryService.findByIdNotNull(id);
		payMerchantDao.delete(payMerchantPo);
	}

}
