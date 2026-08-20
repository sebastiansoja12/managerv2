package com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.validator;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.organisationstructure.api.dto.AnnouncementRequestDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnnouncementRequestValidator {

    public Result<Void, List<String>> validate(final AnnouncementRequestDto request) {
        final List<String> errors = new ArrayList<>();
        if (request == null) {
            errors.add("Announcement request must not be null");
        } else if (request.message() == null || request.message().isBlank()) {
            errors.add("Announcement message must not be blank");
        }
        return errors.isEmpty() ? Result.success() : Result.failure(errors);
    }
}
