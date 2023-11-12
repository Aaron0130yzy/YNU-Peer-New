package io.naccoll.boilerplate.pay.interfaces.api.platform;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.egzosn.pay.common.util.sign.SignUtils;
import io.naccoll.boilerplate.core.audit.operate.OperateLog;
import io.naccoll.boilerplate.core.exception.ClientParamException;
import io.naccoll.boilerplate.pay.constant.PayApiConstant;
import io.naccoll.boilerplate.pay.convert.PayAlipayConfigConvert;
import io.naccoll.boilerplate.pay.dto.PayAlipayConfigCreateCommand;
import io.naccoll.boilerplate.pay.dto.PayAlipayConfigDto;
import io.naccoll.boilerplate.pay.dto.PayAlipayConfigUpdateCommand;
import io.naccoll.boilerplate.pay.service.PayAlipayConfigQueryService;
import io.naccoll.boilerplate.pay.service.PayAlipayConfigService;
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
 * The type Pay platform api v 1 alipay config api.
 */
@RestController
@Tag(name = "支付宝配置")
@RequestMapping(PayApiConstant.PlatformApiV1.ALIPAY_CONFIG)
public class PayPlatformApiV1AlipayConfigApi {

	@Resource
	private PayAlipayConfigConvert payAlipayConfigConvert;

	@Resource
	private PayAlipayConfigQueryService payAlipayConfigQueryService;

	@Resource
	private PayAlipayConfigService payAlipayConfigService;

	/**
	 * Create pay alipay config dto.
	 * @param merchantId the merchant id
	 * @param command the command
	 * @param appKey the app key
	 * @param appCert the app cert
	 * @param alipayCert the alipay cert
	 * @param alipayRootCert the alipay root cert
	 * @return the pay alipay config dto
	 */
	@PreAuthorize("hasPermission(0L,'GLOBAL','pay/alipay-config:add')")
	@Operation(summary = "创建支付宝支付配置")
	@OperateLog(value = "创建支付宝支付配置")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public PayAlipayConfigDto create(@PathVariable Long merchantId, PayAlipayConfigCreateCommand command,
			@RequestPart(value = "appKey", required = false) MultipartFile appKey,
			@RequestPart(value = "appCert", required = false) MultipartFile appCert,
			@RequestPart(value = "alipayCert", required = false) MultipartFile alipayCert,
			@RequestPart(value = "alipayRootCert", required = false) MultipartFile alipayRootCert) {
		parseSecretFiles(command, appKey, appCert, alipayCert, alipayRootCert);
		command.setPayMerchantId(merchantId);
		command.setSignType(SignUtils.RSA2.getName());
		return payAlipayConfigConvert.convertPayAlipayConfigDto(payAlipayConfigService.create(command));
	}

	/**
	 * Update pay alipay config dto.
	 * @param merchantId the merchant id
	 * @param command the command
	 * @param appKey the app key
	 * @param appCert the app cert
	 * @param alipayCert the alipay cert
	 * @param alipayRootCert the alipay root cert
	 * @return the pay alipay config dto
	 */
	@PreAuthorize("hasPermission(0L,'GLOBAL','pay/alipay-config:edit')")
	@Operation(summary = "更新商户支付宝支付配置")
	@OperateLog(value = "更新商户支付宝支付配置", id = "#command.id")
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public PayAlipayConfigDto update(@PathVariable Long merchantId, PayAlipayConfigUpdateCommand command,
			@RequestPart(value = "appKey", required = false) MultipartFile appKey,
			@RequestPart(value = "appCert", required = false) MultipartFile appCert,
			@RequestPart(value = "alipayCert", required = false) MultipartFile alipayCert,
			@RequestPart(value = "alipayRootCert", required = false) MultipartFile alipayRootCert) {
		parseSecretFiles(command, appKey, appCert, alipayCert, alipayRootCert);
		command.setPayMerchantId(merchantId);
		command.setSignType(SignUtils.RSA2.getName());
		return payAlipayConfigConvert.convertPayAlipayConfigDto(payAlipayConfigService.update(command));
	}

	private void parseSecretFiles(PayAlipayConfigCreateCommand command, MultipartFile appKey, MultipartFile appCert,
			MultipartFile alipayCert, MultipartFile alipayRootCert) {
		if (appKey != null) {
			try (InputStream appKeyInputStream = appKey.getInputStream()) {
				command.setAppKeyByteArr(FileCopyUtils.copyToByteArray(appKeyInputStream));
			}
			catch (IOException e) {
				throw new ClientParamException("应用私钥文件有误");
			}
		}
		if (appCert != null) {
			try (InputStream appCertInputStream = appCert.getInputStream()) {
				command.setAppCertByteArr(FileCopyUtils.copyToByteArray(appCertInputStream));
			}
			catch (IOException e) {
				throw new ClientParamException("应用证书有误");
			}
		}
		if (alipayCert != null) {
			try (InputStream alipayCertInputStream = alipayCert.getInputStream()) {
				command.setAlipayCertByteArr(FileCopyUtils.copyToByteArray(alipayCertInputStream));
			}
			catch (IOException e) {
				throw new ClientParamException("支付宝证书有误");
			}
		}
		if (alipayRootCert != null) {
			try (InputStream alipayRootCertInputStream = alipayRootCert.getInputStream()) {
				command.setAlipayRootCertByteArr(FileCopyUtils.copyToByteArray(alipayRootCertInputStream));
			}
			catch (IOException e) {
				throw new ClientParamException("支付宝根证书有误");
			}
		}
	}

	/**
	 * Get pay alipay config dto.
	 * @param merchantId the merchant id
	 * @param id the id
	 * @return the pay alipay config dto
	 */
	@PreAuthorize("hasPermission(0L,'GLOBAL','pay/alipay-config:read')")
	@Operation(summary = "获取商户配置")
	@GetMapping("/{id}")
	public PayAlipayConfigDto get(@PathVariable Long merchantId, @PathVariable Long id) {
		return payAlipayConfigConvert.convertPayAlipayConfigDto(payAlipayConfigQueryService.findByIdNotNull(id));
	}

	/**
	 * List list.
	 * @param merchantId the merchant id
	 * @return the list
	 */
	@PreAuthorize("hasPermission(0L,'GLOBAL','pay/alipay-config:read')")
	@Operation(summary = "列表查询")
	@GetMapping("/list")
	public List<PayAlipayConfigDto> list(@PathVariable Long merchantId) {
		return payAlipayConfigConvert
			.convertPayAlipayConfigDtoList(payAlipayConfigQueryService.findByMerchantId(merchantId));
	}

}
