package com.warehouse.star.domain.port.secondary;

import java.util.List;

public interface StarServicePort {
    List<String> findFastestRoute(List<String> routes);
}
