package com.warehouse.terminal.domain.port.primary;

public class TerminalPairPortImpl implements TerminalPairPort {


    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public boolean isUserValid() {
        return false;
    }

    @Override
    public boolean isVersionValid() {
        return false;
    }

    @Override
    public boolean updateRequired() {
        return false;
    }

    @Override
    public void pair() {

    }

    @Override
    public void unpair() {

    }

    @Override
    public void update() {

    }
}
