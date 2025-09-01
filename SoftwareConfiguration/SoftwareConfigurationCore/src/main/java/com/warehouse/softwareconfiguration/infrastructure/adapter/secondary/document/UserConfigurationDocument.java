package com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import com.warehouse.softwareconfiguration.domain.vo.UserId;
import com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.document.enumeration.BarcodeType;
import com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.document.enumeration.EscalationRule;
import com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.document.enumeration.SupplierAccess;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "userConfigurationDocument")
public class UserConfigurationDocument {

    private UserId userId;
    private DepartmentId departmentId;
    private String pickupPointId;
    private String role;
    private ShiftId shiftId;
    private String employeeNumber;
    private TeamId teamId;

    private DeviceId deviceId;
    private String defaultZone;
    private RackId rackId;
    private String floor;

    private String defaultPrinter;
    private String labelFormat;
    private String scanMode;
    private boolean scanSoundEnabled;
    private Set<BarcodeType> barcodeTypes;

    private String defaultSupplier;
    private Set<SupplierAccess> supplierAccesses;
    private String pickupSchedule;

    private boolean taskNotificationsEnabled;
    private boolean shipmentStatusNotificationsEnabled;
    private boolean incidentAlertsEnabled;
    private EscalationRule escalationRule;

    private boolean twoFactorAuthEnabled;
    private String pinCode;
    private Set<String> allowedIpRanges;
    private Set<String> deviceWhitelist;

    private String language;
    private String timezone;
    private String dateFormat;
    private String timeFormat;
    private String theme;

    private List<String> dashboardWidgets;
    private Map<String, Boolean> columnsVisibility;
    private Map<String, String> defaultFilters;

    private Map<String, String> integrations;
    private String exportFormat;
    private String reportFrequency;

    private LocalDateTime lastLogin;
    private int failedLoginAttempts;
    private LocalDateTime lastPasswordChange;
}

