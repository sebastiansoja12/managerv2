package com.warehouse.redirect.infrastructure.adapter.primary;

import com.warehouse.redirect.domain.model.RedirectParcelRequest;
import com.warehouse.redirect.domain.model.RedirectParcelResponse;
import com.warehouse.redirect.domain.model.RedirectRequest;
import com.warehouse.redirect.domain.model.RedirectResponse;
import com.warehouse.redirect.domain.port.primary.RedirectTokenPort;
import com.warehouse.redirect.infrastructure.adapter.primary.mapper.RedirectRequestMapper;
import com.warehouse.redirect.infrastructure.adapter.primary.mapper.RedirectResponseMapper;
import com.warehouse.redirect.infrastructure.api.dto.RedirectParcelRequestDto;
import com.warehouse.redirect.infrastructure.api.dto.RedirectRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/redirections")
public class RedirectController {

    private final RedirectTokenPort redirectTokenPort;

    private final RedirectRequestMapper requestMapper;

    private final RedirectResponseMapper responseMapper;


    @PostMapping("/information")
    public ResponseEntity<?> sendInformation(@RequestBody RedirectRequestDto requestDto) {
        final RedirectRequest request = requestMapper.map(requestDto);
        final RedirectResponse response = redirectTokenPort.sendRedirectInformation(request);
        return new ResponseEntity<>(responseMapper.map(response), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> redirect(@RequestBody RedirectParcelRequestDto requestDto) {
        final RedirectParcelRequest request = requestMapper.map(requestDto);
        final RedirectParcelResponse response = redirectTokenPort.redirect(request);
        return new ResponseEntity<>(responseMapper.map(response), HttpStatus.OK);
    }

}
