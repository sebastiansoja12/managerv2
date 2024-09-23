package com.warehouse.commonassets.response;

import java.util.List;

import com.warehouse.commonassets.vo.ProcessReturn;
import com.warehouse.commonassets.vo.RouteProcess;


public record Response(Long zebraId, String version, String username, List<ProcessReturn> processReturns,
                       List<RouteProcess> routeProcesses) {

}
