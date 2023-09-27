package com.warehouse.routetracker.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    String username;

    String firstName;

    String email;
}
