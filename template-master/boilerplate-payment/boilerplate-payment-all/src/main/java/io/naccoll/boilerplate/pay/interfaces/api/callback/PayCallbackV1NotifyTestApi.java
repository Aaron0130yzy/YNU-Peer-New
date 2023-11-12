package io.naccoll.boilerplate.pay.interfaces.api.callback;

import io.naccoll.boilerplate.pay.constant.PayApiConstant;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Pay callback v 1 notify test api.
 */
@Slf4j
@Hidden
@RestController
@RequestMapping(PayApiConstant.CallbackV1.NOTIFY_TEST)
public class PayCallbackV1NotifyTestApi {

	/**
	 * Wechat jspi response entity.
	 * @return the response entity
	 */
	@PostMapping("/wechat-jsapi")
	public ResponseEntity<Void> wechatJspi() {
		return ResponseEntity.ok().build();
	}

}
