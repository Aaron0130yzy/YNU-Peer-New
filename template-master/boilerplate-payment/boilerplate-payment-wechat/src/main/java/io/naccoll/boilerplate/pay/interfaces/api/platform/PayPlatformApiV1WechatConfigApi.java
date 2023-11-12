package io.naccoll.boilerplate.pay.interfaces.api.platform;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.naccoll.boilerplate.core.audit.operate.OperateLog;
import io.naccoll.boilerplate.core.exception.ClientParamException;
import io.naccoll.boilerplate.pay.constant.PayApiConstant;
import io.naccoll.boilerplate.pay.convert.PayWechatConfigConvert;
import io.naccoll.boilerplate.pay.dto.PayWechatConfigCreateCommand;
import io.naccoll.boilerplate.pay.dto.PayWechatConfigDto;
import io.naccoll.boilerplate.pay.dto.PayWechatConfigUpdateCommand;
import io.naccoll.boilerplate.pay.service.PayWechatConfigQueryService;
import io.naccoll.boilerplate.pay.service.PayWechatConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Pay platform api v 1 wechat config api.
 */
@RestController
@Tag(name = "微信支付配置")
@RequestMapping(PayApiConstant.PlatformApiV1.WECHAT_CONFIG)
public class PayPlatformApiV1WechatConfigApi {

	@Resource
	private PayWechatConfigConvert payWechatConfigConvert;

	@Resource
	private PayWechatConfigQueryService payWechatConfigQueryService;

	@Resource
	private PayWechatConfigService payWechatConfigService;

	/**
	 * Create pay wechat config dto.
	 * @param merchantId the merchant id
	 * @param command the command
	 * @param file the file
	 * @return the pay wechat config dto
	 */
	@PreAuthorize("hasPermission(0L,'GLOBAL','pay/wechat-config:add')")
	@Operation(summary = "创建商户微信支付配置")
	@OperateLog("创建商户微信支付配置")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public PayWechatConfigDto create(@PathVariable Long merchantId, PayWechatConfigCreateCommand command,
			@RequestPart("keyFile") MultipartFile file) {
		try (InputStream stream = file.getInputStream()) {
			command.setKeyContent(FileCopyUtils.copyToByteArray(stream));
		}
		catch (IOException e) {
			throw new ClientParamException("证书有误");
		}
		command.setPayMerchantId(merchantId);
		return payWechatConfigConvert.convertPayWechatConfigDto(payWechatConfigService.create(command));
	}

	/**
	 * Update pay wechat config dto.
	 * @param merchantId the merchant id
	 * @param command the command
	 * @param file the file
	 * @return the pay wechat config dto
	 */
	@PreAuthorize("hasPermission(0L,'GLOBAL','pay/wechat-config:edit')")
	@Operation(summary = "更新商户微信支付配置")
	@OperateLog(value = "更新商户微信支付配置", id = "#command.id")
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public PayWechatConfigDto update(@PathVariable Long merchantId, PayWechatConfigUpdateCommand command,
			@RequestPart(value = "keyFile", required = false) MultipartFile file) {
		if (file != null) {
			try (InputStream stream = file.getInputStream()) {
				command.setKeyContent(FileCopyUtils.copyToByteArray(stream));
			}
			catch (IOException e) {
				throw new ClientParamException("证书有误");
			}
		}
		command.setPayMerchantId(merchantId);
		return payWechatConfigConvert.convertPayWechatConfigDto(payWechatConfigService.update(command));
	}

	/**
	 * Get pay wechat config dto.
	 * @param merchantId the merchant id
	 * @param id the id
	 * @return the pay wechat config dto
	 */
	@PreAuthorize("hasPermission(0L,'GLOBAL','pay/wechat-config:read')")
	@Operation(summary = "获取商户配置")
	@GetMapping("/{id}")
	public PayWechatConfigDto get(@PathVariable Long merchantId, @PathVariable Long id) {
		return payWechatConfigConvert.convertPayWechatConfigDto(payWechatConfigQueryService.findByIdNotNull(id));
	}

	/**
	 * List list.
	 * @param merchantId the merchant id
	 * @return the list
	 */
	@PreAuthorize("hasPermission(0L,'GLOBAL','pay/refund-journal:read')")
	@Operation(summary = "列表查询")
	@GetMapping("/list")
	public List<PayWechatConfigDto> list(@PathVariable Long merchantId) {
		return payWechatConfigConvert
			.convertPayWechatConfigDtoList(payWechatConfigQueryService.findByMerchantId(merchantId));
	}

}
