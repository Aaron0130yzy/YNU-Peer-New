package com.egzosn.pay.ali.bean;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.egzosn.pay.ali.utils.AntCertificationUtil;
import com.egzosn.pay.common.bean.result.PayException;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.common.util.str.StringUtils;

/**
 * 覆盖实现以避免多证书之间冲突
 */
public class CustomCertEnvironment extends CertEnvironment {

	/**
	 * 缓存的不同支付宝公钥证书序列号对应的支付宝公钥
	 */
	private final Map<String, String> CACHED_ALI_PAY_PUBLIC_KEY = new ConcurrentHashMap<String, String>();

	/**
	 * 支付宝根证书内容
	 */
	private String rootCertContent;

	/**
	 * 支付宝根证书序列号
	 */
	private String rootCertSN;

	/**
	 * 商户应用公钥证书序列号
	 */
	private String merchantCertSN;

	/**
	 * 默认的支付宝公钥证书序列号
	 */
	private String aliPayPublicKeySN;

	/**
	 * 构造证书运行环境
	 * @param merchantCert 商户公钥证书路径
	 * @param aliPayCert 支付宝公钥证书路径
	 * @param aliPayRootCert 支付宝根证书路径
	 */
	public CustomCertEnvironment(byte[] merchantCert, byte[] aliPayCert, byte[] aliPayRootCert) {
		super(new ByteArrayInputStream(merchantCert), new ByteArrayInputStream(aliPayCert),
				new ByteArrayInputStream(aliPayRootCert));
		if (null == merchantCert || null == aliPayCert || null == aliPayRootCert) {
			throw new PayErrorException(new PayException("", "证书参数merchantCert、aliPayCert或aliPayRootCert设置不完整。"));
		}

		this.rootCertContent = AntCertificationUtil.readFromInputStream(new ByteArrayInputStream(aliPayRootCert));
		this.rootCertSN = AntCertificationUtil.getRootCertSN(rootCertContent);
		this.merchantCertSN = AntCertificationUtil
			.getCertSN(AntCertificationUtil.readFromInputStream((new ByteArrayInputStream(merchantCert))));

		String aliPayPublicCertContent = AntCertificationUtil.readFromInputStream(new ByteArrayInputStream(aliPayCert));
		aliPayPublicKeySN = AntCertificationUtil.getCertSN(aliPayPublicCertContent);
		CACHED_ALI_PAY_PUBLIC_KEY.put(aliPayPublicKeySN,
				AntCertificationUtil.getCertPublicKey(aliPayPublicCertContent));

	}

	public String getRootCertSN() {
		return rootCertSN;
	}

	public String getMerchantCertSN() {
		return merchantCertSN;
	}

	public String getAliPayPublicKey(String sn) {
		// 如果没有指定sn，则默认取缓存中的第一个值
		if (StringUtils.isEmpty(sn)) {
			return CACHED_ALI_PAY_PUBLIC_KEY.values().iterator().next();
		}

		if (CACHED_ALI_PAY_PUBLIC_KEY.containsKey(sn)) {
			return CACHED_ALI_PAY_PUBLIC_KEY.get(sn);
		}
		else {
			// 网关在支付宝公钥证书变更前，一定会确认通知到商户并在商户做出反馈后，才会更新该商户的支付宝公钥证书
			// TODO: 后续可以考虑加入自动升级支付宝公钥证书逻辑，注意并发更新冲突问题
			throw new PayErrorException(new PayException("", "支付宝公钥证书[" + sn + "]已过期，请重新下载最新支付宝公钥证书并替换原证书文件"));
		}
	}

}
