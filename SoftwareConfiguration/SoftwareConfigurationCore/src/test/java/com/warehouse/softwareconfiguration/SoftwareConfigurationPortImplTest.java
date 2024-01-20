package com.warehouse.softwareconfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.softwareconfiguration.domain.model.Software;
import com.warehouse.softwareconfiguration.domain.port.primary.SoftwareConfigurationPortImpl;
import com.warehouse.softwareconfiguration.domain.port.secondary.SoftwareConfigurationRepository;
import com.warehouse.softwareconfiguration.domain.vo.SoftwareProperty;
import com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.exception.SoftwarePropertyNotFoundException;

@ExtendWith(MockitoExtension.class)
public class SoftwareConfigurationPortImplTest {

    @Mock
    private SoftwareConfigurationRepository softwareConfigurationRepository;

    @InjectMocks
    private SoftwareConfigurationPortImpl softwareConfigurationPort;


    @Test
    void shouldCreateSoftwareProperty() {
        // given
        final SoftwareProperty softwareProperty = new SoftwareProperty("name", "category", "value");
        final Software software = Software
                .builder()
                .value("value")
                .name("name")
                .category("category")
                .build();
        when(softwareConfigurationRepository.create(softwareProperty)).thenReturn(software);
        // when
        final Software soft = softwareConfigurationPort.create(softwareProperty);
        // then
        assertNotNull(soft);
    }

    @Test
    void shouldGetSoftwarePropertyByName() {
        // given
        final Software software = Software
                .builder()
                .value("value")
                .name("name")
                .category("category")
                .build();
        when(softwareConfigurationRepository.get("name")).thenReturn(software);
        // when
        final Software soft = softwareConfigurationPort.get("name");
        // then
        assertNotNull(soft);
    }

    @Test
    void shouldFindAllSoftwareProperties() {
        // given
        final List<Software> softwares = Collections.emptyList();
        when(softwareConfigurationRepository.findAll()).thenReturn(softwares);
        // when
        final List<Software> soft = softwareConfigurationPort.findAll();
        // then
        assertNotNull(soft);
    }

    @Test
    void shouldUpdateSoftwareProperty() {
        // given
        final SoftwareProperty softwareProperty = new SoftwareProperty("name", "category", "value");
        final Software software = Software
                .builder()
                .value("value")
                .name("name")
                .category("category")
                .build();
        final String id = "1";
        when(softwareConfigurationRepository.update(id, softwareProperty)).thenReturn(software);
        // when
        final Software soft = softwareConfigurationPort.update(id, softwareProperty);
        // then
        assertNotNull(soft);
    }
    
    @Test
    void shouldNotFindSoftwareProperty() {
        // given && when
        doThrow(new SoftwarePropertyNotFoundException(404, "Software property not found"))
                .when(softwareConfigurationRepository)
                .get("name");
        final Executable executable = () -> softwareConfigurationPort.get("name");
        // then
		final SoftwarePropertyNotFoundException exception = assertThrows(SoftwarePropertyNotFoundException.class,
				executable);
        assertEquals("Software property not found", exception.getMessage());
    }
}
