package com.warehouse.onboarding.application.port.primary.command;

import com.warehouse.onboarding.domain.enumeration.ConsentType;
import com.warehouse.onboarding.domain.vo.ConsentVersion;

import java.util.Set;

public record AcceptConsentsCommand(Set<ConsentType> acceptedConsents, ConsentVersion version) {
}
