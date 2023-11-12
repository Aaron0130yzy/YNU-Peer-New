package io.naccoll.boilerplate.pay.service;

import io.naccoll.boilerplate.core.id.IdService;
import io.naccoll.boilerplate.core.security.enums.SecurityDataType;
import io.naccoll.boilerplate.core.security.service.DataSecurityService;
import io.naccoll.boilerplate.pay.dao.PayAlipayConfigDao;
import io.naccoll.boilerplate.pay.dto.PayAlipayConfigCreateCommand;
import io.naccoll.boilerplate.pay.dto.PayAlipayConfigUpdateCommand;
import io.naccoll.boilerplate.pay.model.PayAlipayConfigPo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * The type Pay alipay config service.
 */
@Service
public class PayAlipayConfigService {

	@Resource
	private PayAlipayConfigDao payAlipayConfigDao;

	@Resource
	private PayAlipayConfigQueryService payAlipayConfigQueryService;

	@Resource
	private AlipayService alipayService;

	@Resource
	private DataSecurityService dataSecurityService;

	@Resource
	private IdService idService;

	/**
	 * Create pay alipay config po.
	 * @param command the command
	 * @return the pay alipay config po
	 */
	public PayAlipayConfigPo create(PayAlipayConfigCreateCommand command) {
		byte[] alipayCert = dataSecurityService.encrypt(command.getAlipayCertByteArr(),
				SecurityDataType.PAYMENT_CONFIG);
		byte[] alipayRootCert = dataSecurityService.encrypt(command.getAlipayRootCertByteArr(),
				SecurityDataType.PAYMENT_CONFIG);
		byte[] appCert = dataSecurityService.encrypt(command.getAppCertByteArr(), SecurityDataType.PAYMENT_CONFIG);
		byte[] appKey = dataSecurityService.encrypt(command.getAppKeyByteArr(), SecurityDataType.PAYMENT_CONFIG);
		PayAlipayConfigPo payAlipayConfig = new PayAlipayConfigPo();
		payAlipayConfig.setId(idService.getId());
		payAlipayConfig.setName(command.getName());
		payAlipayConfig.setCode(command.getCode());
		payAlipayConfig.setAppid(command.getAppid());
		payAlipayConfig.setPayMerchantId(command.getPayMerchantId());
		payAlipayConfig.setPartnerId(command.getPartnerId());
		payAlipayConfig.setAlipayCert(alipayCert);
		payAlipayConfig.setAlipayRootCert(alipayRootCert);
		payAlipayConfig.setAppCert(appCert);
		payAlipayConfig.setAppKey(appKey);
		payAlipayConfig.setSignType(command.getSignType());
		payAlipayConfig.setStatus(command.getStatus());
		return payAlipayConfigDao.save(payAlipayConfig);
	}

	/**
	 * Update pay alipay config po.
	 * @param command the command
	 * @return the pay alipay config po
	 */
	public PayAlipayConfigPo update(PayAlipayConfigUpdateCommand command) {
		PayAlipayConfigPo payAlipayConfig = payAlipayConfigQueryService.findByIdNotNull(command.getId());
		payAlipayConfig.setName(command.getName());
		payAlipayConfig.setCode(command.getCode());
		payAlipayConfig.setPayMerchantId(command.getPayMerchantId());
		payAlipayConfig.setAppid(command.getAppid());
		payAlipayConfig.setPartnerId(command.getPartnerId());
		payAlipayConfig.setSignType(command.getSignType());
		payAlipayConfig.setStatus(command.getStatus());
		if (command.getAppKeyByteArr() != null) {
			byte[] appKey = dataSecurityService.encrypt(command.getAppKeyByteArr(), SecurityDataType.PAYMENT_CONFIG);
			payAlipayConfig.setAppKey(appKey);
		}
		if (command.getAppCertByteArr() != null) {
			byte[] appCert = dataSecurityService.encrypt(command.getAppCertByteArr(), SecurityDataType.PAYMENT_CONFIG);
			payAlipayConfig.setAppCert(appCert);
		}
		if (command.getAlipayRootCertByteArr() != null) {
			byte[] alipayRootCert = dataSecurityService.encrypt(command.getAlipayRootCertByteArr(),
					SecurityDataType.PAYMENT_CONFIG);
			payAlipayConfig.setAlipayRootCert(alipayRootCert);
		}
		if (command.getAlipayCertByteArr() != null) {
			byte[] alipayCert = dataSecurityService.encrypt(command.getAlipayCertByteArr(),
					SecurityDataType.PAYMENT_CONFIG);
			payAlipayConfig.setAlipayCert(alipayCert);
		}
		PayAlipayConfigPo result = payAlipayConfigDao.save(payAlipayConfig);
		return result;
	}

}
