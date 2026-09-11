package com.warehouse.supplierconfiguration.infrastructure.adapter.primary;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.enumeration.UserPermission;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.supplierconfiguration.application.exception.SupplierConfigurationAccessDeniedException;
import com.warehouse.supplierconfiguration.application.exception.SupplierConfigurationConflictException;
import com.warehouse.supplierconfiguration.application.exception.SupplierConfigurationNotFoundException;
import com.warehouse.supplierconfiguration.application.port.primary.SupplierConfigurationPort;
import com.warehouse.supplierconfiguration.application.port.primary.command.SupplierConfigurationTarget;
import com.warehouse.supplierconfiguration.domain.exception.InvalidSupplierConfigurationException;
import com.warehouse.supplierconfiguration.infrastructure.adapter.primary.dto.SubstituteSupplierApi;
import com.warehouse.supplierconfiguration.infrastructure.adapter.primary.dto.SupplierConfigurationApiResponse;
import com.warehouse.supplierconfiguration.infrastructure.adapter.primary.dto.SupplierConfigurationErrorApi;
import com.warehouse.supplierconfiguration.infrastructure.adapter.primary.dto.SupplierConfigurationPolicyApi;
import com.warehouse.supplierconfiguration.infrastructure.adapter.primary.dto.SupplierConfigurationPolicyUpdateApiRequest;
import com.warehouse.supplierconfiguration.infrastructure.adapter.primary.dto.SupplierConfigurationResetApiRequest;
import com.warehouse.supplierconfiguration.infrastructure.adapter.primary.dto.SupplierConfigurationRevisionApi;
import com.warehouse.supplierconfiguration.infrastructure.adapter.primary.dto.SupplierConfigurationUpdateApiRequest;
import com.warehouse.supplierconfiguration.infrastructure.adapter.primary.mapper.SupplierConfigurationWebMapper;
import jakarta.persistence.OptimisticLockException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AccessUserControl
public class SupplierConfigurationController {

    private final SupplierConfigurationPort supplierConfigurationPort;

    public SupplierConfigurationController(final SupplierConfigurationPort supplierConfigurationPort) {
        this.supplierConfigurationPort = supplierConfigurationPort;
    }

    @GetMapping("/suppliers/me/configuration")
    public ResponseEntity<SupplierConfigurationApiResponse> getCurrentSupplierConfiguration() {
        return ResponseEntity.ok(SupplierConfigurationWebMapper.map(
                this.supplierConfigurationPort.get(SupplierConfigurationTarget.currentSupplier())));
    }

    @PutMapping("/suppliers/me/configuration")
    public ResponseEntity<SupplierConfigurationApiResponse> updateCurrentSupplierConfiguration(
            @RequestBody final SupplierConfigurationUpdateApiRequest updateRequest) {
        return ResponseEntity.ok(SupplierConfigurationWebMapper.map(this.supplierConfigurationPort.update(
                SupplierConfigurationWebMapper.map(SupplierConfigurationTarget.currentSupplier(), updateRequest))));
    }

    @PostMapping("/suppliers/me/configuration/reset")
    public ResponseEntity<SupplierConfigurationApiResponse> resetCurrentSupplierConfiguration(
            @RequestBody final SupplierConfigurationResetApiRequest resetRequest) {
        return ResponseEntity.ok(SupplierConfigurationWebMapper.map(this.supplierConfigurationPort.resetToDefaults(
                SupplierConfigurationWebMapper.map(SupplierConfigurationTarget.currentSupplier(), resetRequest))));
    }

    @GetMapping("/suppliers/me/configuration/history")
    public ResponseEntity<List<SupplierConfigurationRevisionApi>> getCurrentSupplierConfigurationHistory() {
        return ResponseEntity.ok(this.supplierConfigurationPort.getHistory(SupplierConfigurationTarget.currentSupplier())
                .stream()
                .map(SupplierConfigurationWebMapper::map)
                .toList());
    }

    @GetMapping("/suppliers/me/configuration/substitute-candidates")
    public ResponseEntity<List<SubstituteSupplierApi>> getCurrentSupplierSubstituteCandidates() {
        return ResponseEntity.ok(this.supplierConfigurationPort
                .getSubstituteCandidates(SupplierConfigurationTarget.currentSupplier())
                .stream()
                .map(SupplierConfigurationWebMapper::map)
                .toList());
    }

    @GetMapping("/suppliers/{supplierCode}/configuration")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_READ, UserPermission.ROLE_MANAGER_READ,
            UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
    public ResponseEntity<SupplierConfigurationApiResponse> getSupplierConfiguration(
            @PathVariable final String supplierCode) {
        return ResponseEntity.ok(SupplierConfigurationWebMapper.map(
                this.supplierConfigurationPort.get(managedSupplier(supplierCode))));
    }

    @PutMapping("/suppliers/{supplierCode}/configuration")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
    public ResponseEntity<SupplierConfigurationApiResponse> updateSupplierConfiguration(
            @PathVariable final String supplierCode,
            @RequestBody final SupplierConfigurationUpdateApiRequest updateRequest) {
        return ResponseEntity.ok(SupplierConfigurationWebMapper.map(this.supplierConfigurationPort.update(
                SupplierConfigurationWebMapper.map(managedSupplier(supplierCode), updateRequest))));
    }

    @PostMapping("/suppliers/{supplierCode}/configuration/reset")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
    public ResponseEntity<SupplierConfigurationApiResponse> resetSupplierConfiguration(
            @PathVariable final String supplierCode,
            @RequestBody final SupplierConfigurationResetApiRequest resetRequest) {
        return ResponseEntity.ok(SupplierConfigurationWebMapper.map(this.supplierConfigurationPort.resetToDefaults(
                SupplierConfigurationWebMapper.map(managedSupplier(supplierCode), resetRequest))));
    }

    @GetMapping("/suppliers/{supplierCode}/configuration/history")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_READ, UserPermission.ROLE_MANAGER_READ,
            UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
    public ResponseEntity<List<SupplierConfigurationRevisionApi>> getSupplierConfigurationHistory(
            @PathVariable final String supplierCode) {
        return ResponseEntity.ok(this.supplierConfigurationPort.getHistory(managedSupplier(supplierCode))
                .stream()
                .map(SupplierConfigurationWebMapper::map)
                .toList());
    }

    @GetMapping("/suppliers/{supplierCode}/configuration/substitute-candidates")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_READ, UserPermission.ROLE_MANAGER_READ,
            UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
    public ResponseEntity<List<SubstituteSupplierApi>> getSupplierSubstituteCandidates(
            @PathVariable final String supplierCode) {
        return ResponseEntity.ok(this.supplierConfigurationPort.getSubstituteCandidates(managedSupplier(supplierCode))
                .stream()
                .map(SupplierConfigurationWebMapper::map)
                .toList());
    }

    @GetMapping("/supplier-configuration-policy")
    public ResponseEntity<SupplierConfigurationPolicyApi> getPolicy() {
        return ResponseEntity.ok(SupplierConfigurationWebMapper.map(this.supplierConfigurationPort.getPolicy()));
    }

    @PutMapping("/supplier-configuration-policy")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_UPDATE})
    public ResponseEntity<SupplierConfigurationPolicyApi> updatePolicy(
            @RequestBody final SupplierConfigurationPolicyUpdateApiRequest policyUpdateRequest) {
        return ResponseEntity.ok(SupplierConfigurationWebMapper.map(
                this.supplierConfigurationPort.updatePolicy(SupplierConfigurationWebMapper.map(policyUpdateRequest))));
    }

    private SupplierConfigurationTarget managedSupplier(final String supplierCode) {
        return SupplierConfigurationTarget.managedSupplier(new SupplierCode(supplierCode));
    }

    @ExceptionHandler(InvalidSupplierConfigurationException.class)
    public ResponseEntity<SupplierConfigurationErrorApi> handleInvalidConfiguration(
            final InvalidSupplierConfigurationException exception) {
        return ResponseEntity.badRequest().body(SupplierConfigurationWebMapper.map(
                "SUPPLIER_CONFIGURATION_INVALID", exception.getMessage(), exception.getViolations()));
    }

    @ExceptionHandler(SupplierConfigurationNotFoundException.class)
    public ResponseEntity<SupplierConfigurationErrorApi> handleNotFound(final SupplierConfigurationNotFoundException exception) {
        return error(HttpStatus.NOT_FOUND, "SUPPLIER_CONFIGURATION_NOT_FOUND", exception);
    }

    @ExceptionHandler(SupplierConfigurationAccessDeniedException.class)
    public ResponseEntity<SupplierConfigurationErrorApi> handleAccessDenied(
            final SupplierConfigurationAccessDeniedException exception) {
        return error(HttpStatus.FORBIDDEN, "SUPPLIER_CONFIGURATION_FORBIDDEN", exception);
    }

    @ExceptionHandler({
            SupplierConfigurationConflictException.class,
            OptimisticLockException.class,
            OptimisticLockingFailureException.class
    })
    public ResponseEntity<SupplierConfigurationErrorApi> handleConflict(final RuntimeException exception) {
        return error(HttpStatus.CONFLICT, "SUPPLIER_CONFIGURATION_CONFLICT", exception);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<SupplierConfigurationErrorApi> handleBadRequest(final IllegalArgumentException exception) {
        return error(HttpStatus.BAD_REQUEST, "SUPPLIER_CONFIGURATION_BAD_REQUEST", exception);
    }

    private ResponseEntity<SupplierConfigurationErrorApi> error(final HttpStatus status,
                                                                final String code,
                                                                final RuntimeException exception) {
        return ResponseEntity.status(status)
                .body(SupplierConfigurationWebMapper.map(code, exception.getMessage(), List.of()));
    }
}
