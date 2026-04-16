package com.warehouse.organisationstructure.operator.domain.port.primary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.OperatorTestFixtures;
import com.warehouse.organisationstructure.operator.domain.event.OperatorChangedEvent;
import com.warehouse.organisationstructure.operator.domain.event.OperatorCreatedEvent;
import com.warehouse.organisationstructure.operator.domain.model.Operator;
import com.warehouse.organisationstructure.operator.domain.model.UpdateOperatorCommand;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorRepository;
import com.warehouse.organisationstructure.operator.domain.registry.DomainContext;
import com.warehouse.organisationstructure.operator.domain.service.OperatorService;
import com.warehouse.organisationstructure.operator.domain.service.OperatorServiceImpl;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.secondary.OperatorConfigurationRepository;
import com.warehouse.organisationstructure.operatorconfiguration.domain.service.OperatorConfigurationService;
import com.warehouse.organisationstructure.operatorconfiguration.domain.service.OperatorConfigurationServiceImpl;

@ExtendWith(MockitoExtension.class)
class OperatorPortImplTest {

    @Mock
    private OperatorRepository operatorRepository;

    @Mock
    private OperatorConfigurationRepository operatorConfigurationRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private OperatorPort operatorPort;

    @BeforeEach
    void setUp() {
        final OperatorConfigurationService configurationService =
                new OperatorConfigurationServiceImpl(operatorConfigurationRepository);
        final OperatorService operatorService =
                new OperatorServiceImpl(operatorRepository, configurationService);
        operatorPort = new OperatorPortImpl(operatorService);
        setEventPublisher(eventPublisher);
    }

    @AfterEach
    void tearDown() {
        setEventPublisher(null);
    }

    @Test
    void shouldCreateOperatorUsingRealService() {
        when(operatorRepository.findMaxOperatorIdValue()).thenReturn(Optional.of(10004L));
        when(operatorRepository.existsById(OperatorId.of(10005L))).thenReturn(false);

        final OperatorId operatorId = operatorPort.create(OperatorTestFixtures.createCommand());

        assertEquals(OperatorId.of(10005L), operatorId);
        verify(operatorRepository).save(argThat(operator ->
                operator.getOperatorId().equals(operatorId)
                        && operator.getRegisteringUserId() == null
                        && operator.getProvisioningDetails() != null
                        && "WRO-1".equals(operator.getProvisioningDetails().firstDepartment().departmentCode())
        ));
        verify(eventPublisher).publishEvent(org.mockito.ArgumentMatchers.<Object>argThat(event ->
                event instanceof OperatorCreatedEvent createdEvent
                        && createdEvent.getSnapshot().operatorId().equals(operatorId)
        ));
    }

    @Test
    void shouldUpdateOperatorUsingRealServices() {
        final Operator existingOperator = OperatorTestFixtures.operator();
        final UpdateOperatorCommand command = OperatorTestFixtures.updateCommand();
        when(operatorRepository.findById(OperatorTestFixtures.OPERATOR_ID)).thenReturn(Optional.of(existingOperator));
        when(operatorConfigurationRepository.findByOperatorId(OperatorTestFixtures.OPERATOR_ID))
                .thenReturn(Optional.of(command.configuration()));
        when(operatorConfigurationRepository.save(OperatorTestFixtures.OPERATOR_ID, command.configuration()))
                .thenReturn(command.configuration());

        final Operator updatedOperator = operatorPort.update(OperatorTestFixtures.OPERATOR_ID, command);

        assertEquals("Updated Logistics", updatedOperator.getCompanyName());
        assertEquals(command.taxId(), updatedOperator.getTaxId());
        assertEquals(command.status(), updatedOperator.getStatus());
        verify(operatorRepository).save(existingOperator);
        verify(operatorConfigurationRepository).save(OperatorTestFixtures.OPERATOR_ID, command.configuration());
        verify(eventPublisher).publishEvent(org.mockito.ArgumentMatchers.<Object>argThat(event ->
                event instanceof OperatorChangedEvent changedEvent
                        && changedEvent.getSnapshot().operatorId().equals(OperatorTestFixtures.OPERATOR_ID)
        ));
    }

    @Test
    void shouldRejectCreateCommandWithoutFirstDepartment() {
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> operatorPort.create(new com.warehouse.organisationstructure.operator.domain.model.CreateOperatorCommand(
                        "Jan",
                        "Kowalski",
                        "j.kowalski",
                        "secret",
                        "pl",
                        "jan.kowalski@example.com",
                        OperatorTestFixtures.TAX_ID,
                        true,
                        true,
                        false,
                        "+48123123123",
                        "ops@example.com",
                        "Example Logistics",
                        OperatorTestFixtures.CONTRACT_START,
                        OperatorTestFixtures.CONTRACT_END,
                        OperatorTestFixtures.FOUNDED_DATE,
                        OperatorTestFixtures.configuration(),
                        null
                ))
        );

        assertEquals("First department data is required to create operator", exception.getMessage());
    }

    private void setEventPublisher(final ApplicationEventPublisher publisher) {
        try {
            final Field field = DomainContext.class.getDeclaredField("eventPublisher");
            field.setAccessible(true);
            field.set(null, publisher);
        } catch (final ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
