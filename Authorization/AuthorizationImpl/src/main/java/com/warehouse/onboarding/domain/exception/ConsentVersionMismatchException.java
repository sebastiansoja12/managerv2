package com.warehouse.onboarding.domain.exception;

import com.warehouse.onboarding.domain.vo.ConsentVersion;

public class ConsentVersionMismatchException extends RuntimeException {

    public ConsentVersionMismatchException(final ConsentVersion acceptedVersion, final ConsentVersion currentVersion) {
        super("Accepted consent version " + acceptedVersion.value() + " differs from current version "
                + currentVersion.value());
    }
}
