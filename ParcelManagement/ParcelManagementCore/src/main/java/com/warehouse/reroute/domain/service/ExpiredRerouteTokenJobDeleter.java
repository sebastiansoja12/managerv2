package com.warehouse.reroute.domain.service;

import java.time.Instant;

public interface ExpiredRerouteTokenJobDeleter {

    void deleteAllExpiredSince();

}
