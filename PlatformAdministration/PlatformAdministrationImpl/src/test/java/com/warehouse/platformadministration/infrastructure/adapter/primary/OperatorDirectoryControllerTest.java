package com.warehouse.platformadministration.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.platformadministration.application.port.primary.OperatorDirectoryPort;
import com.warehouse.platformadministration.domain.exception.OperatorNotFoundException;
import com.warehouse.platformadministration.domain.vo.OperatorUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class OperatorDirectoryControllerTest {

    private final OperatorDirectoryPort operatorDirectoryPort = mock(OperatorDirectoryPort.class);

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(new OperatorDirectoryController(operatorDirectoryPort)).build();
    }

    @Test
    void shouldReturnOperatorUsersInGuiContract() throws Exception {
        when(operatorDirectoryPort.findUsers(OperatorId.of(1L))).thenReturn(List.of(new OperatorUser(new UserId(5L),
                "jkowalski", "Jan", "Kowalski", "jan@test.pl", "ADMIN", new DepartmentCode("KT1"), false)));

        mockMvc.perform(get("/operators/1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId.value").value(5))
                .andExpect(jsonPath("$[0].role").value("ADMIN"))
                .andExpect(jsonPath("$[0].departmentCode").value("KT1"));
    }

    @Test
    void shouldCreateDepartmentAndReturnItsCode() throws Exception {
        when(operatorDirectoryPort.createDepartment(eq(OperatorId.of(1L)), any())).thenReturn(new DepartmentCode("KT2"));

        mockMvc.perform(post("/operators/1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"departmentCode":"KT2","city":"Katowice","street":"Testowa 1","postalCode":"40-001",
                                 "countryCode":"PL","taxId":"1234567890","telephoneNumber":"+48123456789",
                                 "openingHours":"08:00-16:00","email":"kt2@test.pl","departmentType":"BRANCH"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.value").value("KT2"));
    }

    @Test
    void shouldRejectIncompleteDepartment() throws Exception {
        mockMvc.perform(post("/operators/1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"departmentCode\":\"KT2\"}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(operatorDirectoryPort);
    }

    @Test
    void shouldRejectUnknownUserRole() throws Exception {
        mockMvc.perform(post("/operators/1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName":"Jan","lastName":"Kowalski","username":"jkowalski","password":"secret",
                                 "email":"jan@test.pl","role":"OWNER","departmentCode":"KT1","language":"pl"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundForUnknownOperator() throws Exception {
        when(operatorDirectoryPort.findCouriers(OperatorId.of(99L))).thenThrow(new OperatorNotFoundException(OperatorId.of(99L)));

        mockMvc.perform(get("/operators/99/couriers"))
                .andExpect(status().isNotFound());
    }
}
