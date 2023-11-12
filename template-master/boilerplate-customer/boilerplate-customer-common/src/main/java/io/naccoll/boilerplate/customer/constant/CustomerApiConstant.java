package io.naccoll.boilerplate.customer.constant;

public class CustomerApiConstant {

	public final static String CUSTOMER_PREFIX = "/customer";

	public static class PlatformApiV1 {

		public final static String PREFIX = CUSTOMER_PREFIX + "/platform/api/v1";

		public final static String USER = PREFIX + "/user";

		public final static String USER_BIND_THIRD = PREFIX + "/user-bind-third";

		public final static String USER_BIND = PREFIX + "/user-bind";

		public final static String USER_BIND_HISTORY = PREFIX + "/user-bind-history";

		public final static String USER_WX_MP = PREFIX + "/user-wx-mp";

		public final static String USER_WX_MINIAPP = PREFIX + "/user-wx-miniapp";

	}

	public static class UserApiV1 {

		public final static String PREFIX = CUSTOMER_PREFIX + "/user/api/v1";

		public final static String USER = PREFIX + "/user";

		public final static String USER_BIND_THIRD = "/user-bind-third";

		public final static String USER_BIND = PREFIX + "/user-bind";

		public final static String USER_WX_MP = PREFIX + "/user-wx-mp";

		public final static String USER_WX_MINIAPP = PREFIX + "/user-wx-miniapp";

	}

	public static class PublicApiV1 {

		public final static String PREFIX = CUSTOMER_PREFIX + "/public/api/v1";

		public final static String USER = PREFIX + "/user";

		public final static String USER_WX_MP = PREFIX + "/user-wx-mp";

		public final static String USER_WX_MINIAPP = PREFIX + "/user-wx-miniapp";

	}

}
