package com.warehouse.commonassets.identificator;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DepartmentCode implements ObjectValue<String> {

    @Column(name = "department_code")
    private String value;

    public DepartmentCode() {}

    public DepartmentCode(final String value) {
        this.value = value;
    }

    public String value() {return value;}
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    @Override
    public String toString() { return value; }
}
