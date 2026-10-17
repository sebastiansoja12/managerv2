package com.warehouse.platformadministration.domain.vo;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.commonassets.identificator.UserId;

public record PlatformActor(PlatformOwnerId platformOwnerId,
                            String username,
                            boolean impersonation,
                            UserId impersonatedUserId,
                            OperatorId operatorId) {
}
