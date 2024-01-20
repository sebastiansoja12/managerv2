package com.warehouse.softwareconfiguration.infrastructure.adapter.primary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.softwareconfiguration.domain.model.Software;
import com.warehouse.softwareconfiguration.domain.port.primary.SoftwareConfigurationPort;
import com.warehouse.softwareconfiguration.domain.vo.SoftwareProperty;
import com.warehouse.softwareconfiguration.infrastructure.adapter.primary.dto.ErrorResponseDto;
import com.warehouse.softwareconfiguration.infrastructure.adapter.primary.dto.SoftwarePropertyDto;
import com.warehouse.softwareconfiguration.infrastructure.adapter.primary.exception.RestException;
import com.warehouse.softwareconfiguration.infrastructure.adapter.primary.mapper.SoftwareRequestMapper;
import com.warehouse.softwareconfiguration.infrastructure.adapter.primary.mapper.SoftwareResponseMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/software-configurations")
@RequiredArgsConstructor
public class SoftwareController {

    private final SoftwareConfigurationPort softwareConfigurationPort;

    private final SoftwareRequestMapper requestMapper = getMapper(SoftwareRequestMapper.class);
    private final SoftwareResponseMapper responseMapper = getMapper(SoftwareResponseMapper.class);

    @PostMapping
    public ResponseEntity<?> create(@RequestBody SoftwarePropertyDto property) {
        final SoftwareProperty softwareProperty = requestMapper.map(property);
        final Software software = softwareConfigurationPort.create(softwareProperty);
        return new ResponseEntity<>(responseMapper.map(software), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody SoftwarePropertyDto property) {
        final SoftwareProperty softwareProperty = requestMapper.map(property);
        final Software software = softwareConfigurationPort.update(id, softwareProperty);
        return new ResponseEntity<>(responseMapper.map(software), HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        final List<Software> software = softwareConfigurationPort.findAll();
        return new ResponseEntity<>(responseMapper.map(software), HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> get(@PathVariable String name) {
        final Software software = softwareConfigurationPort.get(name);
        return new ResponseEntity<>(responseMapper.map(software), HttpStatus.OK);
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<?> handleException(RestException ex) {
        final ErrorResponseDto error = new ErrorResponseDto(LocalDateTime.now(), ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatusCode.valueOf(error.getStatus()));
    }

}
