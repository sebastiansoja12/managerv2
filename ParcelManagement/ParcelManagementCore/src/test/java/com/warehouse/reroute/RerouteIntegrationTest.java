package com.warehouse.reroute;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.warehouse.reroute.domain.exception.RerouteTokenExpiredException;
import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.vo.RerouteParcelResponse;
import org.junit.jupiter.api.Disabled;
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

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.reroute.configuration.RerouteTokenTestConfiguration;
import com.warehouse.reroute.domain.enumeration.ParcelType;
import com.warehouse.reroute.domain.enumeration.Size;
import com.warehouse.reroute.domain.enumeration.Status;
import com.warehouse.reroute.domain.exception.EmailNotFoundException;
import com.warehouse.reroute.domain.exception.RerouteException;
import com.warehouse.reroute.domain.port.primary.RerouteTokenPort;
import com.warehouse.reroute.domain.vo.Recipient;
import com.warehouse.reroute.domain.vo.Sender;
import com.warehouse.reroute.infrastructure.adapter.secondary.exception.RerouteTokenNotFoundException;
import com.warehouse.shipment.infrastructure.api.dto.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = RerouteTokenTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RerouteIntegrationTest {

    @Autowired
    private RerouteTokenPort rerouteTokenPort;

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
    @DatabaseSetup("/dataset/rerouteToken.xml")
    @Disabled
    void shouldRerouteParcel() {
        shouldSendRequestToRerouteParcel();
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


    private void shouldSendRequestToRerouteParcel() {
        // TODO analyse and fix bug - secondary adapter not activated
        // given
        final RerouteParcelRequest request = RerouteParcelRequest.builder()
                .id(100001L)
                .parcel(createParcel())
                .token(12345)
                .build();
        // when
        final RerouteParcelResponse response = rerouteTokenPort.update(request);
        // then
        assertEquals("updatedTest", response.getSender().getFirstName());
    }


    void shouldNotUpdateParcelRequestWhenTokenDoesntExistInDb() {
        // given
        final RerouteParcelRequest updateParcelRequest = RerouteParcelRequest.builder()
                .parcel(createParcel())
                .id(PARCEL_ID)
                .token(INVALID_TOKEN)
                .build();

        // when
        final Executable executable = () -> rerouteTokenPort.update(updateParcelRequest);

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
        final RerouteParcel parcel = RerouteParcel.builder()
                .recipient(createRecipient())
                .parcelSize(Size.TEST)
                .sender(createSender())
                .parcelType(ParcelType.PARENT)
                .status(Status.SENT)
                .build();

        final RerouteParcelRequest updateParcelRequest = RerouteParcelRequest.builder()
                .parcel(parcel)
                .id(PARCEL_ID_2)
                .token(VALID_TOKEN)
                .build();

        // when
        final Executable executable = () -> rerouteTokenPort.update(updateParcelRequest);


        // then
        final RerouteException illegalArgumentException =
                      assertThrows(RerouteException.class, executable);
        assertAll(
                () -> assertThat(illegalArgumentException.getMessage())
                        .isEqualTo("Parcel cannot be rerouted")
        );
    }

    void shouldThrowExceptionWhileTryingToUpdateParcelWithTypeChild() {
        // given
        final RerouteParcel parcel = RerouteParcel.builder()
                .recipient(createRecipient())
                .parcelSize(Size.TEST)
                .sender(createSender())
                .parcelType(ParcelType.CHILD)
                .status(Status.CREATED)
                .build();

        final RerouteParcelRequest updateParcelRequest = RerouteParcelRequest.builder()
                .parcel(parcel)
                .id(PARCEL_ID_3)
                .token(VALID_TOKEN)
                .build();

        // when
        final Executable executable = () -> rerouteTokenPort.update(updateParcelRequest);


        // then
        final RerouteException illegalArgumentException =
                assertThrows(RerouteException.class, executable);
        assertAll(
                () -> assertThat(illegalArgumentException.getMessage())
                        .isEqualTo("Parcel cannot be rerouted")
        );
    }

    void shouldSendRerouteRequest() {
        // given
        final RerouteRequest request = new RerouteRequest();
        request.setEmail(EMAIL);
        request.setParcelId(PARCEL_ID);

        // when
        final RerouteResponse response = rerouteTokenPort.sendReroutingInformation(request);
        //then
        assertThat(response.getToken()).isNotNull();
    }

    void shouldThrowExceptionWhenEmailIsNull() {
        // given
        final RerouteRequest request = new RerouteRequest();
        request.setParcelId(PARCEL_ID);
        request.setEmail(null);

        final String exceptionMessage = "E-mail cannot be null";

        // when
        final Executable executable = () -> rerouteTokenPort.sendReroutingInformation(request);

        //then
        final EmailNotFoundException exception = assertThrows(EmailNotFoundException.class, executable);
        assertThat(exception.getMessage()).isEqualTo(exceptionMessage);
    }


    private RerouteParcel createParcel() {
        return RerouteParcel.builder()
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
