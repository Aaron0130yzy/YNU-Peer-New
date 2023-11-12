package io.naccoll.boilerplate.pay.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.naccoll.boilerplate.pay.interfaces.handler.AbstractNotifyHandler;

/**
 * The type Pay success notify message.
 */
public class PaySuccessNotifyMessage implements Serializable {

	@Serial
	private static final long serialVersionUID = 281295483964314543L;

	private long payJournalId;

	private int count = 0;

	private Duration[] delays;

	private long createdTime;

	/**
	 * Instantiates a new Pay success notify message.
	 */
	PaySuccessNotifyMessage() {

	}

	/**
	 * Instantiates a new Pay success notify message.
	 * @param payJournalId the pay journal id
	 */
	public PaySuccessNotifyMessage(long payJournalId) {
		this(payJournalId, AbstractNotifyHandler.generatePollingDelays());
	}

	/**
	 * Instantiates a new Pay success notify message.
	 * @param payJournalId the pay journal id
	 * @param delays the delays
	 */
	public PaySuccessNotifyMessage(long payJournalId, Duration[] delays) {
		this.payJournalId = payJournalId;
		this.delays = delays;
		this.createdTime = System.currentTimeMillis();
	}

	/**
	 * Gets pay journal id.
	 * @return the pay journal id
	 */
	public long getPayJournalId() {
		return payJournalId;
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
