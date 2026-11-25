package com.warehouse.billing.customer.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateBillingCustomerApiRequest(@NotBlank @Size(max = 255) String companyName,
                                              @NotBlank @Pattern(regexp = "[A-Za-z0-9]{5,20}") String taxId,
                                              @Size(max = 255) String street,
                                              @Size(max = 20) String postalCode,
                                              @Size(max = 255) String city,
                                              @Pattern(regexp = "[A-Z]{2}") String countryCode,
                                              @NotBlank @Email @Size(max = 255) String invoiceEmail) {
}
