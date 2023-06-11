package com.warehouse.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.warehouse.auth.configuration.AuthTestConfiguration;
import com.warehouse.auth.domain.model.LoginRequest;
import com.warehouse.auth.domain.model.RegisterRequest;
import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AuthTestConfiguration.class)
@TestPropertySource(locations = {"/application-hsqlmem.properties"})
@ActiveProfiles("hsqlmem")
@Disabled
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationPort authenticationPort;


    @Test
    @Transactional
    void shouldRegisterUser() throws Exception {
        // given
        final RegisterRequest registerRequest = createRegisterRequest();
        final ObjectMapper mapper = new ObjectMapper();
        // when
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        final ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        final String requestJson = ow.writeValueAsString(registerRequest);
        final MvcResult login = mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        // then
        final String token = login.getResponse().getContentAsString();
        assertThat(token).isNotNull();
    }

    @Test 
    void shouldLoginAndGetContent() throws Exception {
        // given
        final LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("s-soja");
        loginRequest.setPassword("test");
        final ObjectMapper mapper = new ObjectMapper();
        // when
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        final ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        final String requestJson = ow.writeValueAsString(loginRequest);

        // then
        final MvcResult login = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        final String token = login.getResponse().getContentAsString();
        assertThat(token).isNotNull();
    }

    @Test
    @Transactional
    void shouldLoginAndGetError() throws Exception {
        // given
        final LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(null);
        loginRequest.setPassword(null);
        final ObjectMapper mapper = new ObjectMapper();
        // when
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        final ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        final String requestJson = ow.writeValueAsString(loginRequest);

        // then
        final MvcResult login = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().is(406))
                .andReturn();
        final String errorMessage = login.getResponse().getErrorMessage();
        assertThat(errorMessage).isEqualTo(null);
    }

    private RegisterRequest createRegisterRequest() {
        final RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFirstName("test");
        registerRequest.setLastName("test");
        registerRequest.setUsername("test");
        registerRequest.setPassword("test");
        registerRequest.setDepotCode("KT1");
        registerRequest.setEmail("test@test.pl");

        return registerRequest;
    }
}
