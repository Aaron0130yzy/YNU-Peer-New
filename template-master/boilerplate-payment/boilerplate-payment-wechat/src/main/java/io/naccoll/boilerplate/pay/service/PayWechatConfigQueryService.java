package io.naccoll.boilerplate.pay.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.naccoll.boilerplate.core.exception.ResourceNotFoundException;
import io.naccoll.boilerplate.core.persistence.dao.DaoUtils;
import io.naccoll.boilerplate.pay.dao.PayWechatConfigDao;
import io.naccoll.boilerplate.pay.model.PayWechatConfigPo;
import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * The type Pay wechat config query service.
 */
@Service
public class PayWechatConfigQueryService {

	@Resource
	private PayWechatConfigDao payWechatConfigDao;

	/**
	 * Find by id not null pay wechat config po.
	 * @param id the id
	 * @return the pay wechat config po
	 */
	public PayWechatConfigPo findByIdNotNull(Long id) {
		PayWechatConfigPo payWechatConfigPo = payWechatConfigDao.findById(id).orElse(null);
		if (payWechatConfigPo == null) {
			throw new ResourceNotFoundException("微信商户配置不存在");
		}
		return payWechatConfigPo;
	}

	/**
	 * Find by merchant id list.
	 * @param merchantId the merchant id
	 * @return the list
	 */
	public List<PayWechatConfigPo> findByMerchantId(Long merchantId) {
		return payWechatConfigDao.findByPayMerchantId(merchantId);
	}

	/**
	 * Find by merchant id and wx pay wechat config po.
	 * @param merchantId the merchant id
	 * @param wxMchId the wx mch id
	 * @param wxAppId the wx app id
	 * @return the pay wechat config po
	 */
	public PayWechatConfigPo findByMerchantIdAndWx(Long merchantId, String wxMchId, String wxAppId) {
		return payWechatConfigDao.findFirstByPayMerchantIdAndMchidAndAppid(merchantId, wxMchId, wxAppId);
	}

	/**
	 * Find enable by merchant id and code pay wechat config po.
	 * @param merchantId the merchant id
	 * @param code the code
	 * @return the pay wechat config po
	 */
	public PayWechatConfigPo findEnableByMerchantIdAndCode(Long merchantId, String code) {
		return payWechatConfigDao.findFirstByPayMerchantIdAndCodeAndStatus(merchantId, code, 1);
	}

	/**
	 * 根据id集合查询微信支付配置Map
	 * @param ids id集合
	 * @return 微信支付配置Map
	 */
	public Map<Long, PayWechatConfigPo> findMapByIds(Collection<Long> ids) {
		return DaoUtils.findMapByPrimaryKeyIn(payWechatConfigDao, ids);
	}

}
