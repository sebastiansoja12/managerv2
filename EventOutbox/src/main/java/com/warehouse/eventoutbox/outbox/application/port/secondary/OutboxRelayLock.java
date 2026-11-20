package com.warehouse.eventoutbox.outbox.application.port.secondary;

public interface OutboxRelayLock {

    boolean tryAcquire();
}
