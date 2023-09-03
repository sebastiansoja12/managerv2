package com.warehouse.paypal.domain.model;



import lombok.Data;

@Data
public class CartBase {
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
