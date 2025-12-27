package com.warehouse.commonassets.model;

import com.warehouse.commonassets.identificator.DepartmentCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UsernameTenantPasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {
    
    private final DepartmentCode departmentCode;
    
	public UsernameTenantPasswordAuthenticationToken(final Object principal, final Object departmentCode,
			final Object credentials, final Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.departmentCode = (DepartmentCode) departmentCode;
    }
    
    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }
}
