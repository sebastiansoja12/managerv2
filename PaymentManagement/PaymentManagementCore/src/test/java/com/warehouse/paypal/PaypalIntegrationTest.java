package com.warehouse.paypal;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.warehouse.paypal.configuration.PaypalTestConfiguration;
import com.warehouse.paypal.domain.model.LinkInformation;
import com.warehouse.paypal.domain.model.PaymentRequest;
import com.warehouse.paypal.domain.model.PaymentResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.xml.transform.StringSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PaypalTestConfiguration.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class,
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class PaypalIntegrationTest {

    private MockMvc mockMvc;

    private final static Long PARCEL_ID = 100001L;

    private final static double PRICE = 25;


    @Test
    @Disabled
    void shouldSendPaymentRequest() throws Exception {
        // given
        final PaymentRequest request = new PaymentRequest();
        request.setParcelId(PARCEL_ID);
        request.setPrice(PRICE);

        final StringSource request1 = getJsonPaymentRequest(request);

        final PaymentResponse response = PaymentResponse.builder()
                .link(link())
                .createTime("now")
                .paymentMethod("test")
                .failureReason("none")
                .build();

        final StringSource response1 = getJsonPaymentResponse(response);

        // when
        final MvcResult mvcResult = mockMvc.perform(post("/payment/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(request1)))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        // then
        final String responseJson = mvcResult.getResponse().getContentAsString();
        assertThat(responseJson).isNotNull();
        assertThat(responseJson).isEqualTo(response1.toString());
    }

    @Test
    void shouldUpdatePayment() {
        // given

        // when

        // then
    }

    @Test
    void shouldCreatePayment() {
        // given

        // when


        // then
    }

    @Test
    void shouldThrowException() {
        // given

        // when

        // then
    }


    private StringSource getJsonPaymentResponse(PaymentResponse response) {
        final String json = "{" +
                "    \"link\": {" +
                "        \"paymentUrl\": " + response.getLink().getPaymentUrl() +
                "    }," +
                "    \"createTime\": " + response.getCreateTime() +
                "    \"failureReason\": " + response.getFailureReason() +
                "    \"paymentMethod\": " + response.getPaymentMethod() +
                "}";
        return new StringSource(json);
    }

    private StringSource getJsonPaymentRequest(PaymentRequest request) {
        final String json =
                "{" +
                        "    \"parcelId\":" + request.getParcelId() + "," +
                        "    \"price\": " + request.getPrice() + "\n" +
                        "}";

        return new StringSource(json);
    }

    private LinkInformation link() {
        final LinkInformation linkInformation = new LinkInformation();
        linkInformation.setPaymentUrl("test.pl");
        return linkInformation;
    }
}
