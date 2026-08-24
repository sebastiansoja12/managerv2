package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class DeliveryTimeConfiguration {
    private int minDeliveryDays;
    private int maxDeliveryDays;
    private int expressDeliveryDays;
    private int sameDayDeliveryHours;
    private int internationalDeliveryDays;

    public DeliveryTimeConfiguration() {
    }

    public DeliveryTimeConfiguration(final int minDeliveryDays,
                                     final int maxDeliveryDays,
                                     final int expressDeliveryDays,
                                     final int sameDayDeliveryHours,
                                     final int internationalDeliveryDays) {
        this.minDeliveryDays = minDeliveryDays;
        this.maxDeliveryDays = maxDeliveryDays;
        this.expressDeliveryDays = expressDeliveryDays;
        this.sameDayDeliveryHours = sameDayDeliveryHours;
        this.internationalDeliveryDays = internationalDeliveryDays;
    }

    public int getMinDeliveryDays() { return minDeliveryDays; }
    public int getMaxDeliveryDays() { return maxDeliveryDays; }
    public int getExpressDeliveryDays() { return expressDeliveryDays; }
    public int getSameDayDeliveryHours() { return sameDayDeliveryHours; }
    public int getInternationalDeliveryDays() { return internationalDeliveryDays; }
}
