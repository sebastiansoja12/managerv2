package com.warehouse.chat.infrastructure.adapter.secondary.api;

import java.util.List;

public record ChatPresenceNotificationDto(List<String> onlineUserIds) {
}
