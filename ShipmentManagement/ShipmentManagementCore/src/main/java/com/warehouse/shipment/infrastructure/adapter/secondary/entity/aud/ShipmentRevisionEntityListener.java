package com.warehouse.shipment.infrastructure.adapter.secondary.entity.aud;

import com.warehouse.shipment.infrastructure.adapter.secondary.entity.AuditEnversInfo;
import org.hibernate.envers.RevisionListener;

public class ShipmentRevisionEntityListener implements RevisionListener {
    @Override
    public void newRevision(final Object revisionEntity) {
        final AuditEnversInfo auditEnversInfo = (AuditEnversInfo) revisionEntity;
        // TODO
        auditEnversInfo.setUsername(null);
    }
}