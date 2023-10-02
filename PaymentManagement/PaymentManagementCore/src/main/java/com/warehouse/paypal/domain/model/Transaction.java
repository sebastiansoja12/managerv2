package com.warehouse.paypal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private String referenceId;
    private Amount amount;
    private Payee payee;
    private String description;
    private String noteToPayee;
    private String custom;
    private String invoiceNumber;
    private String softDescriptor;
    private String softDescriptorCity;
}
