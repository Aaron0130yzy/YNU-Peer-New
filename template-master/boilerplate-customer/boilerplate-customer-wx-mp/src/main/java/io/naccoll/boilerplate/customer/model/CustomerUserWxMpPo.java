package io.naccoll.boilerplate.customer.model;

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
 * 微信公众号用户接口
 *
 * @author NaccOll
 */
@Getter
@Setter
@Entity
@Table(name = "t_customer_user_wx_mp")
public class CustomerUserWxMpPo extends JpaAuditable implements Serializable {

	/**
	 * id
	 */
	@Id
	@Schema(description = "id")
	private Long id;

	@Schema(description = "第三方应用Id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long thirdId;

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	@JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
	private Long customerUserId;

	/**
	 * 微信openid
	 */
	@Schema(description = "微信openid")
	private String openid;

	/**
	 * 昵称
	 */
	@Schema(description = "昵称")
	private String nickname;

	/**
	 * 头像
	 */
	@Schema(description = "头像")
	private String headImgUrl;

	/**
	 * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	 */
	@Schema(description = "用户的性别，值为1时是男性，值为2时是女性，值为0时是未知")
	private Integer sex;

	/**
	 * 国家
	 */
	@Schema(description = "国家")
	private String country;

	/**
	 * 城市
	 */
	@Schema(description = "城市")
	private String province;

	/**
	 * 省份
	 */
	@Schema(description = "省份")
	private String city;

	/**
	 * 是否关注
	 */
	@Schema(description = "是否关注")
	private Boolean subscribe;

	/**
	 * 关注时间
	 */
	@Schema(description = "关注时间")
	private Date subscribeTime;

	/**
	 * 微信UnionId
	 */
	@Schema(description = "微信UnionId")
	private String unionId;

	/**
	 * 用户关注的渠道来源
	 */
	@Schema(description = "用户关注的渠道来源")
	private String subscribeScene;

	/**
	 * 二维码扫码场景
	 */
	@Schema(description = "二维码扫码场景")
	private String qrScene;

	/**
	 * 二维码扫码场景描述
	 */
	@Schema(description = "二维码扫码场景描述")
	private String qrSceneStr;

	/**
	 * 公众号对粉丝的备注
	 */
	@Schema(description = "公众号对粉丝的备注")
	private String remark;

	/**
	 * 上次授权的时间
	 */
	@Schema(description = "上次授权的时间")
	private Date lastAuthDate;

}
