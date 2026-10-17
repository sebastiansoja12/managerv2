package com.warehouse.platformadministration.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.platformadministration.application.port.primary.PlatformOwnerAccountPort;
import com.warehouse.platformadministration.application.port.primary.command.CreatePlatformOwnerAccountCommand;
import com.warehouse.platformadministration.domain.exception.PlatformActorRequiredException;
import com.warehouse.platformadministration.domain.vo.PlatformOwnerAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class PlatformOwnerAccountControllerTest {

    private final PlatformOwnerAccountPort platformOwnerAccountPort = mock(PlatformOwnerAccountPort.class);

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(new PlatformOwnerAccountController(platformOwnerAccountPort)).build();
    }

    @Test
    void shouldListPlatformOwnerAccounts() throws Exception {
        when(platformOwnerAccountPort.findAll()).thenReturn(List.of(new PlatformOwnerAccount(new PlatformOwnerId(1L),
                "owner", "Platform", "Owner", "owner@test.pl", true, null)));

        mockMvc.perform(get("/platform/owners"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].platformOwnerId").value(1))
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    void shouldCreatePlatformOwnerAccount() throws Exception {
        final CreatePlatformOwnerAccountCommand command = new CreatePlatformOwnerAccountCommand("owner2", "secret",
                "Anna", "Nowak", "anna@test.pl");
        when(platformOwnerAccountPort.create(command)).thenReturn(new PlatformOwnerId(2L));

        mockMvc.perform(post("/platform/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"owner2","password":"secret","firstName":"Anna","lastName":"Nowak",
                                 "email":"anna@test.pl"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.value").value(2));
    }

    @Test
    void shouldReturnConflictWhenBlockingIsNotAllowed() throws Exception {
        doThrow(new IllegalStateException("At least one active platform owner is required"))
                .when(platformOwnerAccountPort).block(new PlatformOwnerId(2L));

        mockMvc.perform(put("/platform/owners/2/block"))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnForbiddenWithoutPlatformActor() throws Exception {
        doThrow(new PlatformActorRequiredException()).when(platformOwnerAccountPort).block(new PlatformOwnerId(2L));

        mockMvc.perform(put("/platform/owners/2/block"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnBadRequestForDuplicatedAccount() throws Exception {
        when(platformOwnerAccountPort.create(new CreatePlatformOwnerAccountCommand("owner", "secret", "Anna", "Nowak",
                "anna@test.pl"))).thenThrow(new IllegalArgumentException("Platform owner already exists"));

        mockMvc.perform(post("/platform/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"owner","password":"secret","firstName":"Anna","lastName":"Nowak",
                                 "email":"anna@test.pl"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldChangePassword() throws Exception {
        mockMvc.perform(put("/platform/owners/2/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"new-secret\"}"))
                .andExpect(status().isNoContent());

        verify(platformOwnerAccountPort).changePassword(new PlatformOwnerId(2L), "new-secret");
    }
}
