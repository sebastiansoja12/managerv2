package com.warehouse.organisationstructure.announcement.domain.exception;

import com.warehouse.commonassets.identificator.AnnouncementId;

public class AnnouncementNotFoundException extends RuntimeException {

    public AnnouncementNotFoundException(final AnnouncementId announcementId) {
        super("Announcement " + announcementId.getValue() + " was not found");
    }
}
