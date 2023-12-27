package com.warehouse.auth.infrastructure.adapter.primary;


import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/csrf")
public class CsrfTokenController {

    @GetMapping
    public CsrfToken getCsrfToken(CsrfToken csrfToken) {
        return csrfToken;
    }
}

