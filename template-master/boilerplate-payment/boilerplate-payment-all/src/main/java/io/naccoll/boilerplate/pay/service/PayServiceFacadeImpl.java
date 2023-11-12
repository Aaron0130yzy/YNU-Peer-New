package io.naccoll.boilerplate.pay.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.Lock;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RadixUtil;
import cn.hutool.core.util.RandomUtil;
import io.naccoll.boilerplate.core.cache.CacheTemplate;
import io.naccoll.boilerplate.core.exception.BusinessException;
import io.naccoll.boilerplate.core.exception.ClientParamException;
import io.naccoll.boilerplate.core.id.IdService;
import io.naccoll.boilerplate.pay.constant.PayLockConstant;
import io.naccoll.boilerplate.pay.dto.CreateOrderDto;
import io.naccoll.boilerplate.pay.dto.OrderCancelCommand;
import io.naccoll.boilerplate.pay.dto.OrderCreateCommand;
import io.naccoll.boilerplate.pay.dto.RefundCommand;
import io.naccoll.boilerplate.pay.dto.RefundOrderCreateCommand;
import io.naccoll.boilerplate.pay.enums.PayChannel;
import io.naccoll.boilerplate.pay.enums.PayRefundStatus;
import io.naccoll.boilerplate.pay.enums.PayStatus;
import io.naccoll.boilerplate.pay.enums.RefundStatus;
import io.naccoll.boilerplate.pay.model.PayAlipayConfigPo;
import io.naccoll.boilerplate.pay.model.PayJournalPo;
import io.naccoll.boilerplate.pay.model.PayMerchantPo;
import io.naccoll.boilerplate.pay.model.PayRefundJournalPo;
import io.naccoll.boilerplate.pay.model.PayWechatConfigPo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * The type Pay service facade.
 */
@Slf4j
@Service
public class PayServiceFacadeImpl implements PayServiceFacade {

	@Resource
	private IdService idService;

	@Resource
	private PayJournalQueryService payJournalQueryService;

	@Resource
	private PayJournalService payJournalService;

	@Resource
	private PayMerchantQueryService payMerchantQueryService;

	@Resource
	private CacheTemplate cacheTemplate;

	@Resource
	private PayRefundJournalService payRefundJournalService;

	@Resource
	private PayRefundJournalQueryService payRefundJournalQueryService;

	@Resource
	private ThirdPaySerivce thirdPaySerivce;

	@Resource
	private PayAlipayConfigQueryService payAlipayConfigQueryService;

	@Resource
	private PayWechatConfigQueryService payWechatConfigQueryService;

	/**
	 * Pay create order dto.
	 * @param command the command
	 * @return the create order dto
	 */
	public CreateOrderDto pay(@Valid OrderCreateCommand command) {
		checkAndAssign(command);
		command.setPayPrice(command.getPayPrice().setScale(2, RoundingMode.HALF_UP));
		long merchantId = command.getPayMerchantId();
		String orderNo = command.getOrderNo();
		String deviceInfo = command.getDeviceInfo();
		PayChannel channel = PayChannel.fromId(command.getChannel());
		String key = String.format(PayLockConstant.PAY_ORDER_PREFIX, merchantId, orderNo);

		Lock lock = cacheTemplate.getLock(key);
		lock.lock();
		try {
			PayJournalPo payJournal = payJournalQueryService.findMerchantJournal(merchantId, orderNo);
			PayMerchantPo payMerchant = payMerchantQueryService.findByIdNotNull(merchantId);
			command.setOrganizationId(payMerchant.getOrganizationId());
			command.setDepartId(payMerchant.getDepartId());
			if (payJournal == null) {
				payJournal = payJournalService.create(command);
			}
			else {
				PayStatus journalPayStatus = PayStatus.fromId(payJournal.getPayStatus());
				if (Objects.equals(PayStatus.PAY, journalPayStatus)) {
					throw new ClientParamException("订单已支付，无法重新发起支付");
				}
				checkOrder(payJournal, command);
				payJournal = payJournalQueryService.findMerchantJournal(merchantId, orderNo);
				if (Arrays.asList(PayStatus.CANCEL, PayStatus.OTHER_ERROR).contains(journalPayStatus)) {
					payJournalService.updatePayStatus(payJournal.getId(), PayStatus.WAIT_PAY.getId(), deviceInfo);
				}
			}
			return thirdPaySerivce.pay(payJournal, command, payMerchant);
		}
		finally {
			lock.unlock();
		}
	}

	/**
	 * Refund.
	 * @param command1 the command 1
	 */
	public void refund(RefundCommand command1) {
		PayJournalPo journalPo = payJournalQueryService.findByIdNotNull(command1.getId());
		PayMerchantPo payMerchantPo = payMerchantQueryService.findByIdNotNull(journalPo.getPayMerchantId());
		String refundOrderNo = String.format("%s%04d%06d", DateUtil.format(new Date(), "yyyyMMddHHmmss"),
				payMerchantPo.getId() % 10000, RandomUtil.randomInt(6));
		RefundOrderCreateCommand command = new RefundOrderCreateCommand();
		command.setPayMerchantId(journalPo.getPayMerchantId());
		command.setPayAppId(journalPo.getPayAppId());
		command.setNotifyType(journalPo.getNotifyType());
		command.setOrderNo(journalPo.getOrderNo());
		command.setRefundOrderNo(refundOrderNo);
		command.setRefundPrice(command1.getRefundPrice());
		command.setRefundReason(command1.getReason());
		command.setDeviceInfo(command1.getDeviceInfo());
		this.refund(command);
	}

	/**
	 * Special refund.
	 * @param command the command
	 */
	public void specialRefund(@Valid RefundOrderCreateCommand command) {
		String orderNo = command.getOrderNo();
		List<PayJournalPo> journalPos = payJournalQueryService.findByOrderNo(orderNo);
		if (journalPos.isEmpty()) {
			throw new ClientParamException("交易流水不存在");
		}
		if (journalPos.size() > 1) {
			throw new BusinessException("该交易无法跨商户渠道退款，请联系支付中心系统管理员进行处理");
		}
		PayJournalPo journalPo = journalPos.get(0);

		command.setPayMerchantId(journalPo.getPayMerchantId());
		refund(command);
	}

	/**
	 * Refund pay refund journal po.
	 * @param command the command
	 * @return the pay refund journal po
	 */
	public PayRefundJournalPo refund(@Valid RefundOrderCreateCommand command) {
		long payMerchantId = command.getPayMerchantId();
		String orderNo = command.getOrderNo();
		BigDecimal willRefundPrice = command.getRefundPrice().setScale(2, RoundingMode.HALF_UP);
		command.setRefundPrice(willRefundPrice);
		if (willRefundPrice.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ClientParamException("退款金额应为正数");
		}
		String key = String.format(PayLockConstant.PAY_ORDER_PREFIX, payMerchantId, orderNo);
		Lock lock = cacheTemplate.getLock(key);
		lock.lock();
		try {
			PayJournalPo payJournal = payJournalQueryService.findMerchantJournal(payMerchantId, orderNo);
			if (payJournal == null) {
				throw new ClientParamException("无法查询到对应交易流水，请检查商户订单号是否正确");
			}

			String refundOrderNo = command.getRefundOrderNo();
			if (!StringUtils.hasText(refundOrderNo)) {
				String refundOrderPrefix = command.getRefundOrderNoPrefix();
				if (!StringUtils.hasText(refundOrderPrefix)) {
					refundOrderPrefix = String.format("R%02d%03d", payJournal.getPayWay(), payJournal.getChannel());
				}
				refundOrderNo = refundOrderPrefix + RadixUtil.encode(RadixUtil.RADIXS_59, idService.getId());
				command.setRefundOrderNo(refundOrderNo);
			}

			if (!Objects.equals(payJournal.getPayStatus(), PayStatus.PAY.getId())
					|| Objects.equals(payJournal.getRefundStatus(), PayRefundStatus.COMPLETE_REFUND.getId())) {
				throw new ClientParamException("订单状态不支持退款");
			}
			BigDecimal existRefundPrice = Optional.ofNullable(payJournal.getRefundPrice()).orElse(BigDecimal.ZERO);
			BigDecimal existRefundFreezePrice = Optional.ofNullable(payJournal.getRefundFreezePrice())
				.orElse(BigDecimal.ZERO);
			BigDecimal totalRefundPrice = willRefundPrice.add(existRefundFreezePrice).add(existRefundPrice);
			if (payJournal.getPayPrice().compareTo(totalRefundPrice) < 0) {
				throw new ClientParamException("退款金额不能超过支付金额");
			}
			PayMerchantPo payMerchantPo = payMerchantQueryService.findByIdNotNull(payMerchantId);
			boolean updateFlag = payJournalService.submitRefund(payJournal.getId(), willRefundPrice,
					existRefundFreezePrice, existRefundPrice);
			if (!updateFlag) {
				throw new ClientParamException("退款金额不能超过支付金额");
			}

			PayRefundJournalPo refundJournal;
			PayRefundJournalPo existRefund = payRefundJournalQueryService
				.findByMerchantIdAndRefundOrderNo(payMerchantId, command.getRefundOrderNo());
			if (existRefund != null) {
				if (Objects.equals(existRefund.getRefundStatus(), RefundStatus.REFUNDING.getId())) {
					throw new ClientParamException("正在退款中，请等待");
				}
				if (Objects.equals(existRefund.getRefundStatus(), RefundStatus.SUCCESS.getId())) {
					throw new ClientParamException("该退款单号已成功退款，请勿重复退款");
				}
				payRefundJournalService.updateRefundDeviceInfo(existRefund.getId(), command.getDeviceInfo());
				refundJournal = existRefund;
			}
			else {
				refundJournal = payRefundJournalService.create(payJournal, command);
			}

			payJournal = payJournalQueryService.findMerchantJournal(payMerchantId, orderNo);
			thirdPaySerivce.refund(payJournal, refundJournal, command, payMerchantPo);
			return refundJournal;
		}
		finally {
			lock.unlock();
		}
	}

	private void checkOrder(PayJournalPo payJournalPo, OrderCreateCommand command) {
		long journalId = payJournalPo.getId();
		String orderNo = payJournalPo.getOrderNo();

		int originChannel = payJournalPo.getChannel();
		int newChannel = command.getChannel();
		if (!Objects.equals(originChannel, newChannel)) {
			log.info("切换渠道支付，订单号：{}，原渠道：{}，现渠道：{}", orderNo, originChannel, newChannel);
			privateCacnelOrder(payJournalPo);
			payJournalService.updateChannel(journalId, newChannel);
		}
		BigDecimal originPrice = payJournalPo.getPayPrice();
		BigDecimal targetPrice = command.getPayPrice();
		if (originPrice.compareTo(targetPrice) != 0) {
			log.info("交易金额变化，订单号：{}，原支付金额：{}，现支付金额：{}", orderNo, originPrice, targetPrice);
			privateCacnelOrder(payJournalPo);
			payJournalService.updatePayPrice(journalId, targetPrice);
			command.setPayPrice(targetPrice);
		}
	}

	/**
	 * Cancel.
	 * @param command the command
	 */
	public void cancel(OrderCancelCommand command) {
		long payMerchantId = command.getPayMerchantId();
		String orderNo = command.getOrderNo();

		String key = String.format(PayLockConstant.PAY_ORDER_PREFIX, payMerchantId, orderNo);
		Lock lock = cacheTemplate.getLock(key);
		lock.lock();
		try {
			PayJournalPo payJournalPo = payJournalQueryService.findMerchantJournal(payMerchantId, orderNo);
			privateCacnelOrder(payJournalPo);
		}
		finally {
			lock.unlock();
		}
	}

	/**
	 * Private cacnel order.
	 * @param payJournalPo the pay journal po
	 */
	public void privateCacnelOrder(PayJournalPo payJournalPo) {
		thirdPaySerivce.cancelOrder(payJournalPo);
		payJournalService.updatePayStatus(payJournalPo.getId(), PayStatus.CANCEL.getId());
	}

	private void checkAndAssign(OrderCreateCommand command) {
		Long merchantId = command.getPayMerchantId();
		switch (command.getPayWay()) {
			case WECHAT:
				PayWechatConfigPo wechatConfig = payWechatConfigQueryService.findEnableByMerchantIdAndCode(merchantId,
						command.getWechat().getWechatConfigCode());
				if (wechatConfig == null) {
					throw new ClientParamException("请检查微信支付相关参数，若确认无误，可联系支付中心对接人员，配置微信支付相关信息");
				}
				command.setPayWayConfigId(wechatConfig.getId());
				break;
			case ALIPAY:
				PayAlipayConfigPo config = payAlipayConfigQueryService.findEnableByMerchantIdAndCode(merchantId,
						command.getAlipay().getAlipayConfigCode());
				if (config == null) {
					throw new ClientParamException("请检查支付宝支付相关参数，若确认无误，可联系支付中心对接人员，配置支付宝支付相关信息");
				}
				command.setPayWayConfigId(config.getId());
				break;
			default:
				throw new ClientParamException("暂不支持的支付模式");
		}
		if (!StringUtils.hasText(command.getOrderNo())) {
			String orderPrefix = command.getOrderNoPrefix();
			if (!StringUtils.hasText(orderPrefix)) {
				// 前两位是支付方式
				// 3-5位是支付渠道
				orderPrefix = String.format("%02d%03d", command.getPayWay().getId(), command.getChannel());
			}
			String orderNo = orderPrefix + RadixUtil.encode(RadixUtil.RADIXS_59, idService.getId());
			command.setOrderNo(orderNo);
		}
	}

}
