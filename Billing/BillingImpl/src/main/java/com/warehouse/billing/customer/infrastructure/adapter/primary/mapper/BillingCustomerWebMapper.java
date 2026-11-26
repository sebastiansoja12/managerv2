package com.warehouse.billing.customer.infrastructure.adapter.primary.mapper;

import com.warehouse.billing.api.dto.BillingCustomerDto;
import com.warehouse.billing.api.dto.BillingCustomerStatusDto;
import com.warehouse.billing.customer.application.port.primary.command.CreateBillingCustomerCommand;
import com.warehouse.billing.customer.application.port.primary.command.UpdateBillingCustomerCommand;
import com.warehouse.billing.customer.domain.model.BillingCustomer;
import com.warehouse.billing.customer.domain.vo.BillingCustomerAddress;
import com.warehouse.billing.customer.domain.vo.BillingCustomerDetails;
import com.warehouse.billing.customer.infrastructure.adapter.primary.api.BillingCustomerApiKeyResponse;
import com.warehouse.billing.customer.infrastructure.adapter.primary.api.BillingCustomerApiResponse;
import com.warehouse.billing.customer.infrastructure.adapter.primary.api.BillingCustomerIdDto;
import com.warehouse.billing.customer.infrastructure.adapter.primary.api.CreateBillingCustomerApiRequest;
import com.warehouse.billing.customer.infrastructure.adapter.primary.api.UpdateBillingCustomerApiRequest;
import com.warehouse.billing.customer.infrastructure.adapter.primary.api.UserIdDto;
import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.identificator.TaxId;

public class BillingCustomerWebMapper {

    public CreateBillingCustomerCommand toCommand(final CreateBillingCustomerApiRequest request) {
        return new CreateBillingCustomerCommand(new TaxId(request.taxId()), new BillingCustomerDetails(
                request.companyName(), new BillingCustomerAddress(request.street(), request.postalCode(),
                request.city(), request.countryCode()), request.invoiceEmail()));
    }

    public UpdateBillingCustomerCommand toCommand(final BillingCustomerId billingCustomerId,
                                                  final UpdateBillingCustomerApiRequest request) {
        return new UpdateBillingCustomerCommand(billingCustomerId, new BillingCustomerDetails(request.companyName(),
                new BillingCustomerAddress(request.street(), request.postalCode(), request.city(),
                        request.countryCode()), request.invoiceEmail()));
    }

    public BillingCustomerApiResponse toResponse(final BillingCustomer billingCustomer) {
        final BillingCustomerAddress address = billingCustomer.details().address();
        return new BillingCustomerApiResponse(new BillingCustomerIdDto(billingCustomer.id().value()),
                billingCustomer.details().companyName(), billingCustomer.taxId().value(), address.street(),
                address.postalCode(), address.city(), address.countryCode(), billingCustomer.details().invoiceEmail(),
                billingCustomer.status(), new UserIdDto(billingCustomer.userId().value()),
                billingCustomer.createdAt(), billingCustomer.updatedAt());
    }

    public BillingCustomerApiKeyResponse toApiKeyResponse(final String apiKey) {
        return new BillingCustomerApiKeyResponse(apiKey);
    }

    public BillingCustomerDto toDto(final BillingCustomer billingCustomer) {
        final BillingCustomerAddress address = billingCustomer.details().address();
        return new BillingCustomerDto(billingCustomer.id(), billingCustomer.operatorId(),
                billingCustomer.details().companyName(), billingCustomer.taxId(), address.street(),
                address.postalCode(), address.city(), address.countryCode(), billingCustomer.details().invoiceEmail(),
                BillingCustomerStatusDto.valueOf(billingCustomer.status().name()), billingCustomer.userId());
    }
}
