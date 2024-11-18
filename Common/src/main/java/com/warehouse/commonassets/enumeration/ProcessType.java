package com.warehouse.commonassets.enumeration;

public enum ProcessType {
    CREATED, RETURN, ROUTE, REJECT, REROUTE, REDIRECT, MISS;

    public boolean isUpdateProcessType() {
        return this.equals(REROUTE) || this.equals(REDIRECT);
    }

    public boolean isCreateProcessType() {
        return this.equals(CREATED) || this.equals(REDIRECT);
    }
}
