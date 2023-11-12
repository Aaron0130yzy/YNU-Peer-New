package io.naccoll.boilerplate.pay.service;

import java.util.Date;

import io.naccoll.boilerplate.pay.dto.RefundOrderCreateCommand;
import io.naccoll.boilerplate.pay.model.PayJournalPo;
import io.naccoll.boilerplate.pay.model.PayRefundJournalPo;

public interface PayRefundJournalService {

	PayRefundJournalPo create(PayJournalPo journal, RefundOrderCreateCommand command);

	void updateFailRefundStatus(long id, String errorRemark);

	void updateRefundDeviceInfo(long id, String deviceInfo);

	void updateSuccessRefundStatus(long id, String thirdRefundNo, Date successDate);

	void updateThirdRefundNo(Long refundJournalId, String thirdRefundNo);

}
