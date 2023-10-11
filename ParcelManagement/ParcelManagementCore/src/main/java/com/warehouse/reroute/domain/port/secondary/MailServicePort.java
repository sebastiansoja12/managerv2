package com.warehouse.reroute.domain.port.secondary;

import com.warehouse.reroute.domain.model.RerouteToken;

public interface MailServicePort {

    void sendReroutingInformation(RerouteToken token);
}
