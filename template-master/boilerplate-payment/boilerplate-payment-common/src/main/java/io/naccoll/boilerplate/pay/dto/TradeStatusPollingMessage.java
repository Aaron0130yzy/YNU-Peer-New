package io.naccoll.boilerplate.pay.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.naccoll.boilerplate.pay.interfaces.handler.AbstractTradeStatusPollingHandler;

/**
 * The type Trade status polling message.
 */
public class TradeStatusPollingMessage implements Serializable {

	@Serial
	private static final long serialVersionUID = 5752103066109054757L;

	private long payJournalId;

	private long thirdPayJournalId;

	private int count = 1;

	private Duration[] delays;

	private long createdTime;

	/**
	 * Instantiates a new Trade status polling message.
	 */
	TradeStatusPollingMessage() {

	}

	/**
	 * Instantiates a new Trade status polling message.
	 * @param payJournalId the pay journal id
	 * @param thirdPayJournalId the third pay journal id
	 */
	public TradeStatusPollingMessage(long payJournalId, long thirdPayJournalId) {
		this(payJournalId, thirdPayJournalId, AbstractTradeStatusPollingHandler.generatePollingDelays());
	}

	/**
	 * Instantiates a new Trade status polling message.
	 * @param payJournalId the pay journal id
	 * @param thirdPayJournalId the third pay journal id
	 * @param delays the delays
	 */
	public TradeStatusPollingMessage(long payJournalId, long thirdPayJournalId, Duration[] delays) {
		this.payJournalId = payJournalId;
		this.thirdPayJournalId = thirdPayJournalId;
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
	 * Gets third pay journal id.
	 * @return the third pay journal id
	 */
	public long getThirdPayJournalId() {
		return thirdPayJournalId;
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
