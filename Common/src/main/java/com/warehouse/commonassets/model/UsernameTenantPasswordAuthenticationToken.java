package com.warehouse.commonassets.model;

import com.warehouse.commonassets.identificator.OperatorId;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UsernameTenantPasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {
    
    private final OperatorId operatorId;
    
	public UsernameTenantPasswordAuthenticationToken(final Object principal, final Object operatorId,
			final Object credentials, final Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.operatorId = (OperatorId) operatorId;
    }
    
    public OperatorId getOperatorId() {
        return operatorId;
    }
}
