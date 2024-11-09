package com.warehouse.terminal.domain.port.primary;

public interface TerminalPairPort {
    boolean isConnected();
    boolean isValid();
    boolean isUserValid();
    boolean isVersionValid();
    boolean updateRequired();

    void pair();
    void unpair();
    void update();

}
