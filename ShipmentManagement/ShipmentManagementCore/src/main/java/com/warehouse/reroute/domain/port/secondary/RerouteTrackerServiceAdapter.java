package com.warehouse.reroute.domain.port.secondary;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RerouteTrackerServiceAdapter implements RerouteTrackerServicePort {

    @Override
    public void sendRerouteRequest() {
        log.info("Sending message to Route Tracker Service");
    }
}
