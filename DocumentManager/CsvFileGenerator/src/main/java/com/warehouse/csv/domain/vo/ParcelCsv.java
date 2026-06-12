package com.warehouse.csv.domain.vo;

import java.math.BigDecimal;

import lombok.*;

@Value
@Builder
public class ParcelCsv {
    Long shipmentId;
    String firstName;
    String lastName;
    String senderEmail;
    String senderTelephoneNumber;
    String senderCity;
    String senderPostalCode;
    String senderStreet;
    String recipientFirstName;
    String recipientLastName;
    String recipientEmail;
    String recipientTelephoneNumber;
    String recipientCity;
    String recipientPostalCode;
    String recipientStreet;
    String dangerousGoodUnNumber;
    String dangerousGoodProperShippingName;
    String dangerousGoodHazardClass;
    String dangerousGoodPackingGroup;
    BigDecimal dangerousGoodQuantity;
    String dangerousGoodQuantityUnit;
    Integer dangerousGoodPackageCount;
    String dangerousGoodPackagingType;
    String dangerousGoodRegulationType;
    String dangerousGoodTransportMode;
    String dangerousGoodEmergencyContact24h;
    Boolean dangerousGoodLimitedQuantity;
    Boolean dangerousGoodMarinePollutant;
    Boolean dangerousGoodCorrosive;
}
