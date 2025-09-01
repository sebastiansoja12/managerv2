package com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import com.warehouse.softwareconfiguration.domain.vo.UserId;
import com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.document.enumeration.BarcodeType;
import com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.document.enumeration.EscalationRule;
import com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.document.enumeration.Printer;
import com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.document.enumeration.SupplierAccess;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "departmentSoftwareConfiguration")
public class DepartmentConfigurationDocument {

    private DepartmentId departmentId;
    private String name;
    private String location;

    private Set<String> pickupPointIds;
    private Set<TeamId> teamIds;
    private UserId managerUserId;

    private Printer defaultPrinter;
    private String labelFormat;
    private Set<DeviceId> defaultDeviceIds;
    private Set<BarcodeType> allowedBarcodeTypes;
    private boolean scanSoundEnabled;

    private int shipmentWeightLimit;
    private Set<String> allowedShipmentTypes;
    private boolean fragileHandlingEnabled;
    private boolean oversizeHandlingEnabled;
    private boolean returnProcessingEnabled;

    private String defaultSupplier;
    private Set<SupplierAccess> supplierAccessList;
    private String pickupSchedule;
    private int maxShipmentsPerDay;
    private boolean customsHandlingEnabled;

    private Set<ShiftTemplate> shiftTemplates;
    private Set<LocalDate> holidayCalendar;
    private int maxWorkHoursPerEmployee;
    private Set<String> trainingRequired;

    private boolean incidentAlertsEnabled;
    private EscalationRule escalationRules;
    private String taskAssignmentStrategy;

    private Set<String> allowedIpRanges;
    private Set<String> deviceWhitelist;
    private boolean twoFactorAuthRequired;

    private Map<String, String> integrations;
    private String exportFormat;
    private String reportFrequency;
    private List<String> dashboardWidgets;
    private Map<String, Boolean> columnsVisibility;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastAuditDate;
    private UserId createdBy;
    private UserId updatedBy;
}

