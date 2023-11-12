package io.naccoll.boilerplate.pay.service;

import io.naccoll.boilerplate.pay.enums.PayWay;

/**
 * The interface Third pay sub serivce.
 */
public interface ThirdPaySubSerivce extends ThirdPaySerivce {

	/**
	 * Gets pay way.
	 * @return the pay way
	 */
	PayWay getPayWay();

}
