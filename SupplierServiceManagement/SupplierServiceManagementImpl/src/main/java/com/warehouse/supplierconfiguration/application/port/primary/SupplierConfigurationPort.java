package com.warehouse.supplierconfiguration.application.port.primary;

import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplierconfiguration.application.port.primary.command.ResetSupplierConfigurationCommand;
import com.warehouse.supplierconfiguration.application.port.primary.command.SupplierConfigurationTarget;
import com.warehouse.supplierconfiguration.application.port.primary.command.UpdateSupplierConfigurationCommand;
import com.warehouse.supplierconfiguration.application.port.primary.command.UpdateSupplierConfigurationPolicyCommand;
import com.warehouse.supplierconfiguration.application.port.primary.result.SubstituteSupplierResult;
import com.warehouse.supplierconfiguration.application.port.primary.result.SupplierConfigurationResult;
import com.warehouse.supplierconfiguration.domain.model.SupplierConfigurationPolicy;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationRevision;

import java.util.List;
import java.util.Optional;

public interface SupplierConfigurationPort {
    SupplierConfigurationResult get(final SupplierConfigurationTarget target);
    SupplierConfigurationResult update(final UpdateSupplierConfigurationCommand command);
    SupplierConfigurationResult resetToDefaults(final ResetSupplierConfigurationCommand command);
    List<SupplierConfigurationRevision> getHistory(final SupplierConfigurationTarget target);
    List<SubstituteSupplierResult> getSubstituteCandidates(final SupplierConfigurationTarget target);
    SupplierConfigurationPolicy getPolicy();
    SupplierConfigurationPolicy updatePolicy(final UpdateSupplierConfigurationPolicyCommand command);
    Optional<SupplierConfigurationResult> findEffectiveBySupplierId(final SupplierId supplierId);
    Optional<SupplierConfigurationResult> findEffectiveByUserId(final UserId userId);
}
