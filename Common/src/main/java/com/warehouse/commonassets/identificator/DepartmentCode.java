package com.warehouse.commonassets.identificator;

import java.util.Objects;

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
    public void setValue(final String value) { this.value = value; }

    @Override
    public String toString() { return value; }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof DepartmentCode that)) {
            return false;
        }
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
