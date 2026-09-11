package com.warehouse.terminal.domain.model.command;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.terminal.domain.vo.ActivationCode;

public record DeviceActivationCommand(ActivationCode activationCode,
                                      String installationId,
                                      DeviceType deviceType,
                                      String appVersion,
                                      String manufacturer,
                                      String model,
                                      String osName,
                                      String osVersion) {
}
