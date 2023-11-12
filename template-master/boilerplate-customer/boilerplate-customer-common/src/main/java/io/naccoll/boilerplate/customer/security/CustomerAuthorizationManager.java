package io.naccoll.boilerplate.customer.security;

import java.util.HashSet;
import java.util.function.Supplier;

import io.naccoll.boilerplate.core.security.entity.UserDetailsImpl;
import io.naccoll.boilerplate.core.security.enums.Realm;

import org.springframework.security.authorization.AuthorityAuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

@Component
public class CustomerAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.get().getDetails();
		boolean isGrant = userDetails.getRealm().name().equalsIgnoreCase(Realm.CUSTOMER.name());
		return new AuthorityAuthorizationDecision(isGrant, new HashSet<>());
	}

}
