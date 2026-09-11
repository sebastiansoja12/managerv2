package com.warehouse.auth.infrastructure.adapter.primary;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.primary.CurrentUserAuthenticationPort;
import com.warehouse.auth.domain.port.primary.UserPort;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.ResponseMapper;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class CurrentUserControllerTest {

    private CurrentUserAuthenticationPort currentUserAuthenticationPort;

    private UserPort userPort;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        currentUserAuthenticationPort = mock(CurrentUserAuthenticationPort.class);
        userPort = mock(UserPort.class);
        final CurrentUserController controller = new CurrentUserController(
                currentUserAuthenticationPort,
                userPort,
                mock(PasswordEncoder.class),
                new ResponseMapper());
        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    void shouldReturnCurrentApiKeyWithoutCachingTheProfile() throws Exception {
        final UserId userId = new UserId(17L);
        final DepartmentId departmentId = new DepartmentId(11L);
        final User user = new User(userId, "profile-user", "password", "Profile", "User",
                "profile@example.com", User.Role.USER, departmentId, "mgr_existing-key", "pl", Set.of());
        user.assignOperator(new OperatorId(1L));
        when(currentUserAuthenticationPort.getCurrentUser()).thenReturn(user);
        when(userPort.getDepartmentCode(departmentId)).thenReturn(new DepartmentCode("KT1"));

        mockMvc.perform(get("/auth/me"))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(jsonPath("$.apiKey").value("mgr_existing-key"));
    }

    @Test
    void shouldRegenerateApiKeyForCurrentUserWithoutCachingTheSecret() throws Exception {
        final UserId userId = new UserId(17L);
        final User user = new User();
        user.setUserId(userId);
        when(currentUserAuthenticationPort.getCurrentUser()).thenReturn(user);
        when(userPort.regenerateApiKey(userId)).thenReturn("mgr_generated-key");

        mockMvc.perform(post("/auth/me/api-key"))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(jsonPath("$.apiKey").value("mgr_generated-key"));

        verify(userPort).regenerateApiKey(userId);
    }

    @Test
    void shouldDeleteApiKeyForCurrentUser() throws Exception {
        final UserId userId = new UserId(17L);
        final User user = new User();
        user.setUserId(userId);
        when(currentUserAuthenticationPort.getCurrentUser()).thenReturn(user);

        mockMvc.perform(delete("/auth/me/api-key"))
                .andExpect(status().isNoContent());

        verify(userPort).deleteApiKey(userId);
    }
}
