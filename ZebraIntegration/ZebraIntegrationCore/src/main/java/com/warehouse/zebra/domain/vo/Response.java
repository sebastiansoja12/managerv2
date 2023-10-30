package com.warehouse.zebra.domain.vo;

import java.util.List;

import lombok.Builder;


@Builder
public record Response(Long zebraId, String version, String username, List<ProcessReturn> processReturns) {

}
