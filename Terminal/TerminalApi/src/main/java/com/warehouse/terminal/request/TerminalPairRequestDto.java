package com.warehouse.terminal.request;


import com.warehouse.terminal.dto.TerminalIdDto;
import com.warehouse.terminal.dto.UserIdDto;

public record TerminalPairRequestDto(TerminalIdDto terminalId, UserIdDto userId) {

}
