package com.warehouse.logisticsdelivery.infrastructure.adapter.primary;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logisticsdelivery.application.port.primary.DeliveryReadModelPort;
import com.warehouse.logisticsdelivery.application.port.primary.command.DeliveryReadFilter;
import com.warehouse.logisticsdelivery.application.port.primary.command.DeliveryReadSort;
import com.warehouse.logisticsdelivery.application.port.primary.command.DeliveryReadSortField;
import com.warehouse.logisticsdelivery.application.port.primary.command.SearchDeliveriesCommand;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryTargetType;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryType;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryReadPageResponseDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.mapper.DeliveryReadModelResponseMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/deliveries")
public class DeliveryQueryController {

    private final DeliveryReadModelPort deliveryReadModelPort;
    private final DeliveryReadModelResponseMapper responseMapper;

    public DeliveryQueryController(final DeliveryReadModelPort deliveryReadModelPort,
                                   final DeliveryReadModelResponseMapper responseMapper) {
        this.deliveryReadModelPort = deliveryReadModelPort;
        this.responseMapper = responseMapper;
    }

    @GetMapping("/query")
    public DeliveryReadPageResponseDto query(@RequestParam(defaultValue = "0") final int page,
                                             @RequestParam(defaultValue = "25") final int size,
                                             @RequestParam(required = false) final List<Long> shipmentIds,
                                             @RequestParam(required = false) final String text,
                                             @RequestParam(required = false) final List<String> lifecycleStatuses,
                                             @RequestParam(defaultValue = "false") final boolean hideCanceled,
                                             @RequestParam(defaultValue = "false") final boolean hideCompleted,
                                             @RequestParam(required = false) final List<String> deliveryStatuses,
                                             @RequestParam(required = false) final String type,
                                             @RequestParam(required = false) final String targetType,
                                             @RequestParam(required = false) final String method,
                                             @RequestParam(required = false) final Long departmentId,
                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                             final LocalDate createdFrom,
                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                             final LocalDate createdTo,
                                             @RequestParam(defaultValue = "CREATED") final String sort,
                                             @RequestParam(defaultValue = "desc") final String direction) {
        if (page < 0 || size < 1 || size > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid delivery page");
        }
        if (createdFrom != null && createdTo != null && createdFrom.isAfter(createdTo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid delivery date range");
        }
        try {
            final DeliveryReadFilter filter = new DeliveryReadFilter(
                    shipmentIds == null ? List.of() : shipmentIds.stream().map(ShipmentId::new).toList(),
                    text,
                    values(lifecycleStatuses, DeliveryLifecycleStatus::valueOf),
                    hideCanceled,
                    hideCompleted,
                    values(deliveryStatuses, DeliveryStatus::valueOf),
                    type == null ? null : DeliveryType.valueOf(type),
                    targetType == null ? null : DeliveryTargetType.valueOf(targetType),
                    method == null ? null : DeliveryMethod.valueOf(method),
                    departmentId == null ? null : new DepartmentId(departmentId),
                    createdFrom,
                    createdTo);
            final DeliveryReadSort readSort = new DeliveryReadSort(DeliveryReadSortField.valueOf(sort),
                    "asc".equalsIgnoreCase(direction));
            return responseMapper.map(deliveryReadModelPort.search(new SearchDeliveriesCommand(page, size, filter,
                    readSort)));
        } catch (final IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid delivery filter");
        }
    }

    private <T> List<T> values(final List<String> values, final Function<String, T> parser) {
        return values == null ? List.of() : values.stream().filter(value -> !value.isBlank()).map(parser).toList();
    }
}
