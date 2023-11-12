package io.naccoll.boilerplate.pay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * The type Create order dto.
 */
@Data
public class CreateOrderDto {

	@Schema(description = "支付订单号")
	private String orderNo;

	@Schema(description = "二维码地址")
	private String url;

	@Schema(description = "页面")
	private String page;

	@Schema(description = "交易流水号")
	@Deprecated
	private String tradeNo;

	@Schema(description = "支付机构交易流水号")
	private String thirdPayNo;

	private WechatJsApi wechat;

	/**
	 * Create from wx native create order dto.
	 * @param url the url
	 * @param orderNo the order no
	 * @return the create order dto
	 */
	public static CreateOrderDto createFromWxNative(String url, String orderNo) {
		CreateOrderDto createOrderDto = new CreateOrderDto();
		createOrderDto.setUrl(url);
		createOrderDto.setOrderNo(orderNo);
		return createOrderDto;
	}

	/**
	 * Create order no create order dto.
	 * @param orderNo the order no
	 * @return the create order dto
	 */
	public static CreateOrderDto createOrderNo(String orderNo) {
		CreateOrderDto createOrderDto = new CreateOrderDto();
		createOrderDto.setOrderNo(orderNo);
		return createOrderDto;
	}

	/**
	 * The type Wechat js api.
	 */
	@Data
	public static class WechatJsApi {

		private String appId;

		private String timeStamp;

		private String nonceStr;

		/**
		 * 由于package为java保留关键字，因此改为packageValue. 前端使用时记得要更改为package
		 */
		@JsonProperty("package")
		private String packageValue;

		private String signType;

		private String paySign;

	}

}
