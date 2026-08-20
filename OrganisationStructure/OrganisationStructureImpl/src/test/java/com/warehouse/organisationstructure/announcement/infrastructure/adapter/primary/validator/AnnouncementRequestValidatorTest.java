package com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.validator;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.organisationstructure.api.dto.AnnouncementRequestDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnnouncementRequestValidatorTest {

    private final AnnouncementRequestValidator validator = new AnnouncementRequestValidator();

    @Test
    void shouldAcceptRequestWithMessage() {
        final Result<Void, List<String>> result = validator.validate(
                new AnnouncementRequestDto("Planned maintenance")
        );

        assertTrue(result.isSuccess());
    }

    @Test
    void shouldRejectNullRequest() {
        final Result<Void, List<String>> result = validator.validate(null);

        assertTrue(result.isFailure());
        assertEquals(List.of("Announcement request must not be null"), result.getFailure());
    }

    @Test
    void shouldRejectBlankMessage() {
        final Result<Void, List<String>> result = validator.validate(new AnnouncementRequestDto("   "));

        assertTrue(result.isFailure());
        assertEquals(List.of("Announcement message must not be blank"), result.getFailure());
    }
}
