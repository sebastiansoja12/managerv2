package com.warehouse.redirect.domain.service;


public interface RedirectTokenGenerator {
    String generateToken(Long parcelId, String email);
}
