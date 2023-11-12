package io.naccoll.boilerplate.pay.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.naccoll.boilerplate.core.exception.ResourceNotFoundException;
import io.naccoll.boilerplate.core.persistence.dao.DaoUtils;
import io.naccoll.boilerplate.pay.dao.PayAlipayConfigDao;
import io.naccoll.boilerplate.pay.model.PayAlipayConfigPo;
import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * The type Pay alipay config query service.
 */
@Service
public class PayAlipayConfigQueryService {

	@Resource
	private PayAlipayConfigDao payAlipayConfigDao;

	/**
	 * Find by id not null pay alipay config po.
	 * @param id the id
	 * @return the pay alipay config po
	 */
	public PayAlipayConfigPo findByIdNotNull(Long id) {
		PayAlipayConfigPo payAlipayConfigDo = payAlipayConfigDao.findById(id).orElse(null);
		if (payAlipayConfigDo == null) {
			throw new ResourceNotFoundException("微信商户配置不存在");
		}
		return payAlipayConfigDo;
	}

	/**
	 * Find by merchant id list.
	 * @param merchantId the merchant id
	 * @return the list
	 */
	public List<PayAlipayConfigPo> findByMerchantId(Long merchantId) {
		return payAlipayConfigDao.findByPayMerchantId(merchantId);
	}

	/**
	 * Find by merchant id and alipay pay alipay config po.
	 * @param merchantId the merchant id
	 * @param pid the pid
	 * @param appid the appid
	 * @return the pay alipay config po
	 */
	public PayAlipayConfigPo findByMerchantIdAndAlipay(Long merchantId, String pid, String appid) {
		return payAlipayConfigDao.findFirstByPayMerchantIdAndPartnerIdAndAppid(merchantId, pid, appid);
	}

	/**
	 * Find enable by merchant id and code pay alipay config po.
	 * @param merchantId the merchant id
	 * @param code the code
	 * @return the pay alipay config po
	 */
	public PayAlipayConfigPo findEnableByMerchantIdAndCode(Long merchantId, String code) {
		return payAlipayConfigDao.findFirstByPayMerchantIdAndCodeAndStatus(merchantId, code, 1);
	}

	/**
	 * 根据id集合查询支付宝支付配置Map
	 * @param ids id集合
	 * @return 支付宝支付配置
	 */
	public Map<Long, PayAlipayConfigPo> findMapByIds(Collection<Long> ids) {
		return DaoUtils.findMapByPrimaryKeyIn(payAlipayConfigDao, ids);
	}

}
