package com.schizoscrypt.storage.enums;

import org.springframework.security.core.GrantedAuthority;

public enum AppRole implements GrantedAuthority {
    ADMIN_ROLE,
    WORKER_ROLE,
    EMPLOYER_ROLE;

    @Override
    public String getAuthority() {
        return name();
    }
}
