package io.naccoll.boilerplate.pay.constant;

/**
 * The type Pay api constant.
 */
public class PayApiConstant {

	/**
	 * The constant PAY_PREFIX.
	 */
	public static final String PAY_PREFIX = "/pay";

	/**
	 * The type Callback v 1.
	 */
	public static class CallbackV1 {

		/**
		 * The constant PREFIX.
		 */
		public static final String PREFIX = PAY_PREFIX + "/callback/v1";

		/**
		 * The constant ALIPAY.
		 */
		public static final String ALIPAY = PREFIX + "/alipay";

		/**
		 * The constant WECHAT.
		 */
		public static final String WECHAT = PREFIX + "/wechat";

		/**
		 * The constant NOTIFY_TEST.
		 */
		public static final String NOTIFY_TEST = PREFIX + "/notify-test";

	}

	/**
	 * The type Openapi api v 1.
	 */
	public static class OpenapiApiV1 {

		/**
		 * The constant PREFIX.
		 */
		public static final String PREFIX = PAY_PREFIX + "/openapi/api/v1";

		/**
		 * The constant MERCHANT.
		 */
		public static final String MERCHANT = PREFIX + "/merchant";

	}

	/**
	 * The type Platform api v 1.
	 */
	public static class PlatformApiV1 {

		/**
		 * The constant PREFIX.
		 */
		public static final String PREFIX = PAY_PREFIX + "/platform/api/v1";

		/**
		 * The constant MERCHANT.
		 */
		public static final String MERCHANT = PREFIX + "/merchant";

		/**
		 * The constant WECHAT_CONFIG.
		 */
		public static final String WECHAT_CONFIG = PREFIX + "/merchant/{merchantId}/wechat-config";

		/**
		 * The constant ALIPAY_CONFIG.
		 */
		public static final String ALIPAY_CONFIG = PREFIX + "/merchant/{merchantId}/alipay-config";

		/**
		 * The constant JOURNAL.
		 */
		public static final String JOURNAL = PREFIX + "/journal";

		/**
		 * The constant REFUND_JOURNAL.
		 */
		public static final String REFUND_JOURNAL = PREFIX + "/refund-journal";

	}

}
