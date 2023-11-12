package io.naccoll.boilerplate.pay.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.naccoll.boilerplate.core.persistence.model.JpaAuditable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * 微信支付流水
 */
@Schema(description = "微信支付流水")
@Getter
@Setter
@Table(name = "t_pay_wechat_journal")
@Entity
public class PayWechatJournalPo extends JpaAuditable implements Serializable, Comparable<PayWechatJournalPo> {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	@Schema(description = "")
	private Long id;

	/**
	 * 由微信生成的应用ID，全局唯一
	 */
	@Schema(description = "由微信生成的应用ID，全局唯一")
	private String appid;

	@Schema(description = "由微信生成的子应用ID")
	private String subAppid;

	/**
	 * 直连商户的商户号
	 */
	@Schema(description = "直连商户的商户号")
	private String mchid;

	/**
	 * 直连商户的商户号
	 */
	@Schema(description = "直连商户的子商户号")
	private String subMchid;

	@Schema(description = "发生交易使用的微信支付配置ID")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long wechatConfigId;

	/**
	 * 商品描述
	 */
	@Schema(description = "商品描述")
	private String description;

	/**
	 * 商户订单号
	 */
	@Schema(description = "商户订单号")
	private String outTradeNo;

	/**
	 * 交易结束时间, 订单失效时间
	 */
	@Schema(description = "交易结束时间, 订单失效时间")
	private String timeExpire;

	/**
	 * 附加数据
	 */
	@Schema(description = "附加数据")
	private String attach;

	/**
	 * 订单优惠标记
	 */
	@Schema(description = "订单优惠标记")
	private String goodsTag;

	/**
	 * 订单总金额
	 */
	@Schema(description = "订单总金额")
	private Integer amountTotal;

	/**
	 * 货币类型
	 */
	@Schema(description = "货币类型")
	private String amountCurrency;

	/**
	 * 用户标识
	 */
	@Schema(description = "用户标识")
	private String payerOpenid;

	/**
	 * 用户终端IP
	 */
	@Schema(description = "用户终端IP")
	private String sceneInfoPayerClientIp;

	/**
	 * 商户端设备号
	 */
	@Schema(description = "商户端设备号")
	private String sceneInfoDeviceId;

	/**
	 * 门店编号
	 */
	@Schema(description = "门店编号")
	private String sceneInfoStoreInfoId;

	/**
	 * 门店名称
	 */
	@Schema(description = "门店名称")
	private String sceneInfoStoreInfoName;

	/**
	 * 地区编码
	 */
	@Schema(description = "地区编码")
	private String sceneInfoStoreInfoAreaCode;

	/**
	 * 地址编码
	 */
	@Schema(description = "地址编码")
	private String sceneInfoStoreInfoAddress;

	@Schema(description = "错误描述")
	private String errCodeDes;

	@Schema(description = "支付中心支付商户Id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long payMerchantId;

	@Schema(description = "组织id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long organizationId;

	@Schema(description = "科室id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long departId;

	@Schema(description = "支付中心统一支付流水Id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long payJournalId;

	@Schema(description = "预支付id1")
	private String prepayId;

	@Schema(description = "微信流水号")
	private String transactionId;

	@Schema(description = "微信流水支付状态")
	private Integer status;

	@Schema(description = "支付完成时间")
	private Date timeEnd;

	@Schema(description = "交易类型")
	private String tradeType;

	@Schema(description = "支付连接信息，NATIVE的二维码或H5支付跳转链接")
	private String payUrl;

	@Schema(description = "支付中心门店id")
	private Long payStoreId = 0L;

	@Schema(description = "支付中心应用id")
	private Long payAppId = 0L;

	@Schema(description = "商户业务订单号")
	private String merchantBusinessNo = "";

	@Override
	public int compareTo(PayWechatJournalPo o) {
		if (this.getId() < o.getId()) {
			return -1;
		}
		else if (this.getId() > o.getId()) {
			return 1;
		}
		return 0;
	}

}
