package io.naccoll.boilerplate.pay.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.naccoll.boilerplate.pay.interfaces.handler.AbstractNotifyHandler;

/**
 * The type Refund success notify message.
 */
public class RefundSuccessNotifyMessage implements Serializable {

	@Serial
	private static final long serialVersionUID = 281295483963215115L;

	private long payRefundJournalId;

	private int count = 0;

	private Duration[] delays;

	private long createdTime;

	/**
	 * Instantiates a new Refund success notify message.
	 */
	RefundSuccessNotifyMessage() {

	}

	/**
	 * Instantiates a new Refund success notify message.
	 * @param payRefundJournalId the pay refund journal id
	 */
	public RefundSuccessNotifyMessage(long payRefundJournalId) {
		this(payRefundJournalId, AbstractNotifyHandler.generatePollingDelays());
	}

	/**
	 * Instantiates a new Refund success notify message.
	 * @param payRefundJournalId the pay refund journal id
	 * @param delays the delays
	 */
	public RefundSuccessNotifyMessage(long payRefundJournalId, Duration[] delays) {
		this.payRefundJournalId = payRefundJournalId;
		this.delays = delays;
		this.createdTime = System.currentTimeMillis();
	}

	/**
	 * Gets pay refund journal id.
	 * @return the pay refund journal id
	 */
	public long getPayRefundJournalId() {
		return payRefundJournalId;
	}

	/**
	 * Gets count.
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Get delays duration [ ].
	 * @return the duration [ ]
	 */
	public Duration[] getDelays() {
		return this.delays;
	}

	/**
	 * Gets created time.
	 * @return the created time
	 */
	public long getCreatedTime() {
		return createdTime;
	}

	/**
	 * Has next boolean.
	 * @return the boolean
	 */
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public boolean hasNext() {
		return count < getRetryCount();
	}

	/**
	 * Next.
	 */
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public void next() {
		count++;
	}

	/**
	 * Gets retry count.
	 * @return the retry count
	 */
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public int getRetryCount() {
		return this.delays.length;
	}

	/**
	 * Gets delay second.
	 * @return the delay second
	 */
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public int getDelaySecond() {
		long delay = 0;
		for (int i = 0; i < count; i++) {
			delay = delay + delays[i].toSeconds();
		}
		return (int) delay;
	}

}
