package com.warehouse.process;

import java.time.Instant;
import java.util.UUID;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.enumeration.DeviceUserType;
import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.process.domain.context.DomainContext;
import com.warehouse.process.domain.enumeration.ProcessStatus;
import com.warehouse.process.domain.model.DeviceInformation;
import com.warehouse.process.domain.model.InitializeProcessCommand;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.domain.vo.ShipmentRejected;
import com.warehouse.process.domain.vo.ShipmentUpdated;
import com.warehouse.process.domain.vo.ProcessCommunication;

public final class ProcessHubTestFixtures {

    public static final Instant CREATED_AT = Instant.parse("2026-06-30T10:15:30Z");
    public static final Instant MODIFIED_AT = Instant.parse("2026-06-30T10:20:30Z");

    private ProcessHubTestFixtures() {
    }

    public static void initializeDomainContext() {
        new DomainContext().setApplicationEventPublisher(new ApplicationEventPublisher() {
            @Override
            public void publishEvent(final ApplicationEvent event) {
            }

            @Override
            public void publishEvent(final Object event) {
            }
        });
    }

    public static ProcessId processId() {
        return new ProcessId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
    }

    public static ProcessId otherProcessId() {
        return new ProcessId(UUID.fromString("22222222-2222-2222-2222-222222222222"));
    }

    public static DepartmentCode departmentCode() {
        return new DepartmentCode("KT1");
    }

    public static DepartmentCode otherDepartmentCode() {
        return new DepartmentCode("KT2");
    }

    public static DeviceInformation deviceInformation() {
        return new DeviceInformation(
                new DeviceId("TL:test-device-01"),
                departmentCode(),
                new UserId(100L),
                DeviceType.TERMINAL,
                DeviceUserType.SUPPLIER,
                "1.0.0"
        );
    }

    public static InitializeProcessCommand initializeCommand() {
        return InitializeProcessCommand.builder()
                .request("<TerminalRequest/>")
                .deviceInformation(deviceInformation())
                .build();
    }

    public static ProcessLog processLog() {
        return ProcessLog.builder()
                .processId(processId())
                .request("initial-request")
                .response("initial-response")
                .createdAt(CREATED_AT)
                .modifiedAt(MODIFIED_AT)
                .deviceInformation(deviceInformation())
                .build();
    }

    public static ProcessLog finishedProcessLog() {
        final ProcessLog processLog = processLog();
        processLog.applyShipmentRejected(shipmentRejected("reject-request", "reject-response", null));
        processLog.applyShipmentUpdate(shipmentUpdated("update-request", "update-response"));
        processLog.changeStatus(ProcessStatus.SUCCESS);
        return processLog;
    }

    public static ShipmentRejected shipmentRejected(final String request, final String response, final String faultDescription) {
        return new ShipmentRejected(
                ServiceType.DELIVERY_OPERATION,
                ProcessType.REJECT,
                request,
                response,
                faultDescription
        );
    }

    public static ShipmentUpdated shipmentUpdated(final String request, final String response) {
        return new ShipmentUpdated(
                new ShipmentId(123L),
                new DeviceId("TL:test-device-01"),
                new UserId(100L),
                departmentCode(),
                ServiceType.SHIPMENT_MANAGEMENT,
                ProcessType.REJECT,
                "DELIVERY_OPERATION",
                "SHIPMENT_MANAGEMENT",
                request,
                response
        );
    }

    public static ProcessCommunication processCommunication(final ServiceType targetServiceType,
                                                            final String request,
                                                            final String response,
                                                            final String faultDescription) {
        return new ProcessCommunication(
                ServiceType.DELIVERY_OPERATION,
                targetServiceType,
                ProcessType.RETURN,
                request,
                response,
                faultDescription
        );
    }
}
