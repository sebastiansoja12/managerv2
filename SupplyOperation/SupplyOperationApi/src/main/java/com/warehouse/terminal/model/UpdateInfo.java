package com.warehouse.terminal.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UpdateInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateInfo<T> {

    @XmlElement(name = "OldValue")
    private T oldValue;
    @XmlElement(name = "NewValue")
    private T newValue;
    @XmlElement(name = "Updated")
    private boolean updated;

    public UpdateInfo() {
    }

    public UpdateInfo(T oldValue, T newValue, boolean updated) {
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.updated = updated;
    }

    public T getOldValue() {
        return oldValue;
    }

    public void setOldValue(final T oldValue) {
        this.oldValue = oldValue;
    }

    public T getNewValue() {
        return newValue;
    }

    public void setNewValue(final T newValue) {
        this.newValue = newValue;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(final boolean updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "UpdateInfo{" +
                "oldValue=" + oldValue +
                ", newValue=" + newValue +
                ", updated=" + updated +
                '}';
    }
}

