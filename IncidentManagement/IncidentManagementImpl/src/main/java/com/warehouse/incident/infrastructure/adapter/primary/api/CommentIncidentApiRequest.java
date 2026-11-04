package com.warehouse.incident.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentIncidentApiRequest(@NotBlank @Size(max = 2000) String text) {
}
