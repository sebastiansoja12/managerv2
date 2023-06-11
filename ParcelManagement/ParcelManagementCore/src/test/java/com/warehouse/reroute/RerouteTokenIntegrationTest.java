package com.warehouse.reroute;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.reroute.configuration.RerouteTokenTestConfiguration;
import com.warehouse.reroute.domain.enumeration.ParcelType;
import com.warehouse.reroute.domain.enumeration.Size;
import com.warehouse.reroute.domain.enumeration.Status;
import com.warehouse.reroute.domain.exception.EmailNotFoundException;
import com.warehouse.reroute.domain.model.Parcel;
import com.warehouse.reroute.domain.model.RerouteRequest;
import com.warehouse.reroute.domain.model.UpdateParcelRequest;
import com.warehouse.reroute.domain.port.primary.RerouteServicePort;
import com.warehouse.reroute.domain.vo.Recipient;
import com.warehouse.reroute.domain.vo.Sender;
import com.warehouse.reroute.infrastructure.adapter.secondary.exception.RerouteTokenNotFoundException;
import com.warehouse.reroute.infrastructure.api.RerouteService;
import com.warehouse.reroute.infrastructure.api.dto.EmailDto;
import com.warehouse.reroute.infrastructure.api.dto.ParcelId;
import com.warehouse.reroute.infrastructure.api.dto.RerouteRequestDto;
import com.warehouse.reroute.infrastructure.api.dto.RerouteResponseDto;
import com.warehouse.shipment.infrastructure.api.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = RerouteTokenTestConfiguration.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RerouteTokenIntegrationTest {

    @Autowired
    private RerouteService rerouteService;

    @Autowired
    private RerouteServicePort rerouteServicePort;

    private final static Long PARCEL_ID = 100001L;

    private final static Long PARCEL_ID_2 = 100002L;

    private final static Long PARCEL_ID_3 = 100003L;


    private final static Long INVALID_PARCEL_ID = 10L;


    private final static Integer VALID_TOKEN = 12345;


    private final static Integer INVALID_TOKEN = 0;


    private final static String EMAIL = "test@test.pl";

    @Test
    void shouldSendRequest() {
        shouldSendRerouteRequest();
    }

    @Test
    void shouldNotSendRequest() {
        shouldThrowExceptionWhenEmailIsNull();
    }

    @Test
    @DatabaseSetup("/dataset/rerouteToken.xml")
    void shouldThrowRerouteTokenNotFoundException() {
        shouldNotUpdateParcelRequestWhenTokenDoesntExistInDb();
    }

    @Test
    @DatabaseSetup("/dataset/rerouteToken.xml")
    void shouldThrowExceptionWhenParcelStatusIsNotCreated() {
        shouldThrowExceptionWhenStatusIsNotCreated();
    }

    @Test
    @DatabaseSetup("/dataset/rerouteToken.xml")
    void shouldThrowExceptionWhenParcelTypeIsChild() {
        shouldThrowExceptionWhileTryingToUpdateParcelWithTypeChild();
    }


    void shouldNotUpdateParcelRequestWhenTokenDoesntExistInDb() {
        // given
        final UpdateParcelRequest updateParcelRequest = UpdateParcelRequest.builder()
                .parcel(createParcel())
                .id(PARCEL_ID)
                .token(INVALID_TOKEN)
                .build();

        // when
        final Executable executable = () -> rerouteServicePort.update(updateParcelRequest);

        final RerouteTokenNotFoundException rerouteTokenNotFoundException =
                assertThrows(RerouteTokenNotFoundException.class, executable);
        // then
        assertAll(
                () -> assertThat(rerouteTokenNotFoundException.getMessage())
                        .isEqualTo("Reroute token was not found")
        );
    }

    void shouldThrowExceptionWhenStatusIsNotCreated() {
        // given
        final Parcel parcel = Parcel.builder()
                .recipient(createRecipient())
                .parcelSize(Size.TEST)
                .sender(createSender())
                .parcelType(ParcelType.PARENT)
                .status(Status.SENT)
                .build();

        final UpdateParcelRequest updateParcelRequest = UpdateParcelRequest.builder()
                .parcel(parcel)
                .id(PARCEL_ID_2)
                .token(VALID_TOKEN)
                .build();

        // when
        final Executable executable = () -> rerouteServicePort.update(updateParcelRequest);


        // then
        final IllegalArgumentException illegalArgumentException =
                      assertThrows(IllegalArgumentException.class, executable);
        assertAll(
                () -> assertThat(illegalArgumentException.getMessage())
                        .isEqualTo("Parcel cannot be rerouted because it was already sent")
        );
    }

    void shouldThrowExceptionWhileTryingToUpdateParcelWithTypeChild() {
        // given
        final Parcel parcel = Parcel.builder()
                .recipient(createRecipient())
                .parcelSize(Size.TEST)
                .sender(createSender())
                .parcelType(ParcelType.CHILD)
                .status(Status.CREATED)
                .build();

        final UpdateParcelRequest updateParcelRequest = UpdateParcelRequest.builder()
                .parcel(parcel)
                .id(PARCEL_ID_3)
                .token(VALID_TOKEN)
                .build();

        // when
        final Executable executable = () -> rerouteServicePort.update(updateParcelRequest);


        // then
        final IllegalArgumentException illegalArgumentException =
                assertThrows(IllegalArgumentException.class, executable);
        assertAll(
                () -> assertThat(illegalArgumentException.getMessage())
                        .isEqualTo("Parcel cannot be rerouted after redirection")
        );
    }

    void shouldSendRerouteRequest() {
        // given
        final EmailDto emailDto = new EmailDto(EMAIL);
        final ParcelId parcelId = new ParcelId(PARCEL_ID);

        final RerouteRequestDto requestDto = new RerouteRequestDto();
        requestDto.setEmail(emailDto);
        requestDto.setParcelId(parcelId);

        // when
        final RerouteResponseDto response = rerouteService.sendReroutingInformation(requestDto);
        //then
        assertThat(response.getToken().intValue()).isNotNull();

    }

    void shouldThrowExceptionWhenEmailIsNull() {
        // given
        final RerouteRequest request = new RerouteRequest();
        request.setParcelId(PARCEL_ID);
        request.setEmail(null);

        final String exceptionMessage = "E-Mail cannot be null";

        // when
        final Executable executable = () -> rerouteServicePort.sendReroutingInformation(request);

        //then
        final EmailNotFoundException exception = assertThrows(EmailNotFoundException.class, executable);
        assertThat(exception.getMessage()).isEqualTo(exceptionMessage);
    }


    private Parcel createParcel() {
        return Parcel.builder()
                .recipient(createRecipient())
                .parcelSize(Size.TEST)
                .sender(createSender())
                .parcelType(ParcelType.PARENT)
                .status(Status.CREATED)
                .build();
    }

    private ParcelDto createParcelDto() {
        final ParcelIdDto parcelId = new ParcelIdDto();
        parcelId.setValue(PARCEL_ID);

        final ParcelDto parcel = new ParcelDto();
        parcel.setRecipient(createShipmentApiRecipient());
        parcel.setSender(createShipmentApiSender());
        parcel.setParcelSize(ParcelSizeDto.TEST);
        parcel.setParcelId(parcelId);
        return parcel;
    }

    private Recipient createRecipient() {
        return Recipient.builder()
                .firstName("test")
                .lastName("test")
                .city("test")
                .street("test")
                .postalCode("00-000")
                .telephoneNumber("123")
                .email("test@test.pl")
                .build();
    }

    private Sender createSender() {
        return Sender.builder()
                .firstName("updatedTest")
                .lastName("test")
                .city("test")
                .street("test")
                .postalCode("00-000")
                .telephoneNumber("123")
                .email("test@test.pl")
                .build();
    }

    private SenderDto createShipmentApiSender() {
        return SenderDto.builder()
                .firstName("updatedTest")
                .lastName("test")
                .city("test")
                .street("test")
                .postalCode("00-000")
                .telephoneNumber("123")
                .email("test@test.pl")
                .build();
    }

    private RecipientDto createShipmentApiRecipient() {
        return RecipientDto.builder()
                .firstName("test")
                .lastName("test")
                .city("test")
                .street("test")
                .postalCode("00-000")
                .telephoneNumber("123")
                .email("test@test.pl")
                .build();
    }

}
