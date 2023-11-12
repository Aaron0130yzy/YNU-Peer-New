package io.naccoll.boilerplate.pay.dto;

import java.math.BigDecimal;

import io.naccoll.boilerplate.core.validate.IsIdableEnum;
import io.naccoll.boilerplate.pay.enums.PayChannel;
import io.naccoll.boilerplate.pay.enums.PayWay;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * The type Create order command.
 */
@Data
public class OrderCreateCommand {

	@Schema(description = "支付订单号, 可以不传，由支付中心生成")
	@Size(max = 32)
	private String orderNo;

	@Schema(description = "商户id", hidden = true)
	private Long payMerchantId;

	@Schema(description = "应用id", hidden = true)
	private Long payAppId = 0L;

	@Schema(description = "组织id", hidden = true)
	private Long organizationId;

	@Schema(description = "科室Id", hidden = true)
	private Long departId;

	@Schema(description = "支付中心门店编码")
	private Long payStoreId = 0L;

	@Schema(description = "商户业务订单号")
	private String merchantBusinessNo = "";

	@Schema(description = "支付订单号前缀，当不传递orderNo时，建议传递，最长10位，若不传递则支付中心规则为准")
	@Size(max = 10)
	private String orderNoPrefix = "";

	/**
	 * 支付金额
	 */
	@Schema(description = "支付金额")
	@NotNull
	private BigDecimal payPrice;

	/**
	 * 支付渠道
	 */
	@Schema(description = "支付渠道 1: 微信JSAPI支付,2:微信NATIVE支付,3:微信支付码支付")
	@NotNull
	@IsIdableEnum(support = PayChannel.class, message = "不支持该支付模式")
	private Integer channel;

	@Schema(description = "支付配置id", hidden = true)
	private Long payWayConfigId;

	@Schema
	private String notifyType;

	@Schema(description = "终端设备号")
	private String deviceInfo = "";

	@Schema(description = "付款码支付 授权码")
	private String authCode;

	@Schema(description = "微信支付参数")
	private Wechat wechat;

	@Schema(description = "支付宝支付参数")
	private Alipay alipay;

	/**
	 * Gets pay way.
	 * @return the pay way
	 */
	public PayWay getPayWay() {
		return PayChannel.fromId(channel).getPayWay();
	}

	/**
	 * The type Wechat.
	 */
	@Data
	public static class Wechat {

		@Schema(description = "支付用户openid，JSAPI必须携带, 当使用subAppId时会作为subopenid使用")
		private String openid;

		@Schema(description = "商品id, NATIVE支付时最好传递，若不传递，则使用订单号作为商品id")
		private String productId;

		@Schema(description = "子商户")
		private String subMchId;

		@Schema(description = "子应用")
		private String subAppId;

		@Schema(description = "微信配置编码")
		private String wechatConfigCode;

		@Schema(description = "客户端ip")
		private String clientIp;

		@Schema(description = "商品描述, 不填则默认为商户名订单号")
		private String body;

		@Schema(description = "附加信息")
		private String attach = "";

		@Schema(description = "订单优惠标记")
		private String goodsTag = "";

	}

	/**
	 * The type Alipay.
	 */
	@Data
	public static class Alipay {

		@Schema(description = "订单标题")
		private String subject;

		@Schema(description = "订单描述")
		private String body;

		@Schema(description = "用户唯一标识")
		private String openid;

		@Schema(description = "支付宝配置编码")
		private String alipayConfigCode;

	}

}
