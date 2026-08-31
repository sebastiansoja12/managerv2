package com.warehouse.auth.domain.model;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrganizationUserAnonymizationTest {

    @Test
    void shouldRemovePersonalDataAndDisableAccount() {
        final OrganizationUser user = OrganizationUser.of(new UserId(5L), "jkowalski", "hash", "Jan", "Kowalski",
                "jan@acme.pl", OrganizationUser.Role.ADMIN, new DepartmentId(10L), "api-key");
        user.setDeleted(false);

        user.anonymize("{erased}");

        assertThat(user.getUsername()).isEqualTo("deleted-5");
        assertThat(user.getFirstName()).isEqualTo("Deleted");
        assertThat(user.getLastName()).isEqualTo("User");
        assertThat(user.getEmail()).isEqualTo("deleted-5@erased.invalid");
        assertThat(user.getPassword()).isEqualTo("{erased}");
        assertThat(user.hasApiKey()).isFalse();
        assertThat(user.isDeleted()).isTrue();
    }

    @Test
    void shouldReportApiKeyPresence() {
        final OrganizationUser withKey = OrganizationUser.of(new UserId(5L), "a", "hash", "A", "B", "a@b.pl",
                OrganizationUser.Role.USER, new DepartmentId(10L), "api-key");
        final OrganizationUser withoutKey = OrganizationUser.of(new UserId(6L), "c", "hash", "C", "D", "c@d.pl",
                OrganizationUser.Role.USER, new DepartmentId(10L), " ");

        assertThat(withKey.hasApiKey()).isTrue();
        assertThat(withoutKey.hasApiKey()).isFalse();
    }
}
