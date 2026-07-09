package com.warehouse.logistics.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.ProcessId;

public final class LogisticsProcessContext {

    private static final ThreadLocal<ProcessId> CURRENT_PROCESS_ID = new ThreadLocal<>();

    private LogisticsProcessContext() {
    }

    public static void setProcessId(final ProcessId processId) {
        CURRENT_PROCESS_ID.set(processId);
    }

    public static ProcessId getProcessId() {
        return CURRENT_PROCESS_ID.get();
    }

    public static void clear() {
        CURRENT_PROCESS_ID.remove();
    }
}
