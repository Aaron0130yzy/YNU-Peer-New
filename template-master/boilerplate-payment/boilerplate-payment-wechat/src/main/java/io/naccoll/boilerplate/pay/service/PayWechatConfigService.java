package io.naccoll.boilerplate.pay.service;

import io.naccoll.boilerplate.core.exception.ClientParamException;
import io.naccoll.boilerplate.core.id.IdService;
import io.naccoll.boilerplate.core.security.enums.SecurityDataType;
import io.naccoll.boilerplate.core.security.service.DataSecurityService;
import io.naccoll.boilerplate.pay.dao.PayWechatConfigDao;
import io.naccoll.boilerplate.pay.dto.PayWechatConfigCreateCommand;
import io.naccoll.boilerplate.pay.dto.PayWechatConfigUpdateCommand;
import io.naccoll.boilerplate.pay.enums.wx.WechatConfigType;
import io.naccoll.boilerplate.pay.model.PayWechatConfigPo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * The type Pay wechat config service.
 */
@Service
public class PayWechatConfigService {

	@Resource
	private PayWechatConfigDao payWechatConfigDao;

	@Resource
	private PayWechatConfigQueryService payWechatConfigQueryService;

	@Resource
	private WechatPayService wechatPayService;

	@Resource
	private DataSecurityService dataSecurityService;

	@Resource
	private IdService idService;

	/**
	 * Create pay wechat config po.
	 * @param command the command
	 * @return the pay wechat config po
	 */
	public PayWechatConfigPo create(PayWechatConfigCreateCommand command) {
		PayWechatConfigPo payWechatConfig = new PayWechatConfigPo();
		payWechatConfig.setId(idService.getId());
		payWechatConfig.setName(command.getName());
		payWechatConfig.setCode(command.getCode());
		payWechatConfig.setPayMerchantId(command.getPayMerchantId());
		payWechatConfig.setMchid(command.getMchid());
		payWechatConfig.setMchKey(dataSecurityService.encryptStr(command.getMchKey(), SecurityDataType.PAYMENT_CONFIG));
		payWechatConfig.setAppid(command.getAppid());
		payWechatConfig
			.setKeyContent(dataSecurityService.encrypt(command.getKeyContent(), SecurityDataType.PAYMENT_CONFIG));
		payWechatConfig.setStatus(command.getStatus());
		WechatConfigType wechatConfigType = WechatConfigType.fromId(command.getConfigType());
		payWechatConfig.setSubAppid(command.getSubAppid());
		payWechatConfig.setSubMchid(command.getSubMchid());
		payWechatConfig.setConfigType(wechatConfigType.getId());
		switch (wechatConfigType) {
			case DIRECT:
			case PROVIDER:
				payWechatConfig.setSubMchid("");
				break;
			case SUB:
				if (!StringUtils.hasText(payWechatConfig.getSubMchid())) {
					throw new ClientParamException("特约商户子商户Id必须存在");
				}
				break;
			default:
		}

		return payWechatConfigDao.save(payWechatConfig);
	}

	/**
	 * Update pay wechat config po.
	 * @param command the command
	 * @return the pay wechat config po
	 */
	public PayWechatConfigPo update(PayWechatConfigUpdateCommand command) {
		PayWechatConfigPo payWechatConfigPo = payWechatConfigQueryService.findByIdNotNull(command.getId());
		payWechatConfigPo.setName(command.getName());
		if (StringUtils.hasText(command.getCode())) {
			payWechatConfigPo.setCode(command.getCode());
		}
		payWechatConfigPo.setMchid(command.getMchid());
		payWechatConfigPo.setAppid(command.getAppid());
		if (StringUtils.hasText(command.getMchKey())) {
			payWechatConfigPo
				.setMchKey(dataSecurityService.encryptStr(command.getMchKey(), SecurityDataType.PAYMENT_CONFIG));
		}
		if (command.getKeyContent() != null) {
			payWechatConfigPo
				.setKeyContent(dataSecurityService.encrypt(command.getKeyContent(), SecurityDataType.PAYMENT_CONFIG));
		}
		payWechatConfigPo.setStatus(command.getStatus());
		WechatConfigType wechatConfigType = WechatConfigType.fromId(command.getConfigType());
		payWechatConfigPo.setSubAppid(command.getSubAppid());
		payWechatConfigPo.setSubMchid(command.getSubMchid());
		payWechatConfigPo.setConfigType(wechatConfigType.getId());
		switch (wechatConfigType) {
			case DIRECT:
			case PROVIDER:
				payWechatConfigPo.setSubMchid("");
				break;
			case SUB:
				if (!StringUtils.hasText(payWechatConfigPo.getSubMchid())) {
					throw new ClientParamException("特约商户子商户Id必须存在");
				}
				break;
			default:
		}
		PayWechatConfigPo result = payWechatConfigDao.save(payWechatConfigPo);
		return result;
	}

}
