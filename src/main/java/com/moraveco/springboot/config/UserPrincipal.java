package com.moraveco.springboot.config;

import java.security.Principal;

public class UserPrincipal implements Principal {
    private final String name;
    
    public UserPrincipal(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }
}

