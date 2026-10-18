package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.cache;

import com.warehouse.organisationstructure.OperatorTestFixtures;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.CommunicationConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.CurrencyCode;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.CustomerLanguage;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DeliveryAreaMode;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentCapacityPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentDeliveryAreaPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DuplicateScanPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.FleetConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.LengthUnit;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointCollectionPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.RecipientVerificationMethod;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.RegionalConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.RegionalDateFormat;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ScanErrorFeedback;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.SortingConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.UncoveredPostalCodePolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.UnresolvedDestinationPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.WeightUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedisOperatorConfigurationCacheTest {

    private static final String KEY = RedisOperatorConfigurationCache.key(OperatorTestFixtures.OPERATOR_ID);

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private RedisOperatorConfigurationCache cache;

    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        cache = new RedisOperatorConfigurationCache(redisTemplate);
    }

    @Test
    void shouldRoundTripConfigurationThroughRedis() {
        final OperatorConfiguration configuration = OperatorTestFixtures.configuration()
                .withDepartmentConfiguration(new DepartmentConfiguration(true, false, true, false,
                        new DepartmentDeliveryAreaPolicy(DeliveryAreaMode.RADIUS, 40, false, true,
                                UncoveredPostalCodePolicy.BLOCK),
                        null, null, new DepartmentCapacityPolicy(1500, 90, true, 25)))
                .withFleetConfiguration(new FleetConfiguration(14, 21, 8, 30, false, 15, 33))
                .withCommunicationConfiguration(new CommunicationConfiguration("Manager", "kontakt@example.com",
                        "Stopka", "https://example.com/logo.png", "#123ABC", CustomerLanguage.DE, 90, false, 5))
                .withRegionalConfiguration(new RegionalConfiguration("Europe/Berlin", RegionalDateFormat.YYYY_MM_DD,
                        CurrencyCode.EUR, WeightUnit.LB, LengthUnit.IN, "07:30", "15:30", true, false))
                .withSortingConfiguration(new SortingConfiguration(true, false, true, DuplicateScanPolicy.BLOCK,
                        UnresolvedDestinationPolicy.BLOCK, "R-99", false, ScanErrorFeedback.VIBRATION, 12))
                .withPickupPointConfiguration(new PickupPointConfiguration(null,
                        new PickupPointCollectionPolicy(RecipientVerificationMethod.SMS_CODE, 8, 48, 3, false, 1,
                                false, 1),
                        null, null, null, null));
        final ArgumentCaptor<String> json = ArgumentCaptor.forClass(String.class);

        cache.put(OperatorTestFixtures.OPERATOR_ID, configuration);
        verify(valueOperations).set(eq(KEY), json.capture(), eq(RedisOperatorConfigurationCache.CONFIGURATION_TTL));
        when(valueOperations.get(KEY)).thenReturn(json.getValue());

        final Optional<OperatorConfiguration> cached = cache.find(OperatorTestFixtures.OPERATOR_ID);

        assertThat(cached).isPresent();
        assertThat(RedisOperatorConfigurationCache.JSON_MAPPER.writeValueAsString(cached.get()))
                .isEqualTo(json.getValue());
        assertThat(cached.get().getDepartmentConfiguration().isAutoAssignDeliveryAreas()).isTrue();
        assertThat(cached.get().getDepartmentConfiguration().isPreventDepartmentDeletion()).isTrue();
        assertThat(cached.get().getDepartmentConfiguration().getDeliveryAreaPolicy().getDeliveryAreaMode())
                .isEqualTo(DeliveryAreaMode.RADIUS);
        assertThat(cached.get().getDepartmentConfiguration().getCapacityPolicy().getDefaultDailyCapacity())
                .isEqualTo(1500);
        assertThat(cached.get().getDepartmentConfiguration().getConnectionPolicy().getSortingCenterConnectionCount())
                .isEqualTo(1);
        assertThat(cached.get().getShippingCapabilities().isSupportsInternationalShipping())
                .isEqualTo(configuration.getShippingCapabilities().isSupportsInternationalShipping());
        assertThat(cached.get().getShipmentConfiguration().getTrackingNumberRule().getPrefixMode())
                .isEqualTo(configuration.getShipmentConfiguration().getTrackingNumberRule().getPrefixMode());        assertThat(cached.get().getFleetConfiguration().getDefaultVehiclePalletCapacity()).isEqualTo(33);
        assertThat(cached.get().getCommunicationConfiguration().getDefaultCustomerLanguage())
                .isEqualTo(CustomerLanguage.DE);
        assertThat(cached.get().getRegionalConfiguration().getTimeZone()).isEqualTo("Europe/Berlin");
        assertThat(cached.get().getRegionalConfiguration().getCurrency()).isEqualTo(CurrencyCode.EUR);
        assertThat(cached.get().getSortingConfiguration().getDuplicateScanPolicy())
                .isEqualTo(DuplicateScanPolicy.BLOCK);
        assertThat(cached.get().getSortingConfiguration().getExceptionRampCode()).isEqualTo("R-99");
        assertThat(cached.get().getPickupPointConfiguration().getCollectionPolicy().getVerificationMethod())
                .isEqualTo(RecipientVerificationMethod.SMS_CODE);
        assertThat(cached.get().getPickupPointConfiguration().getDefaultsPolicy().getWeekdayOpeningTime())
                .isEqualTo("08:00");
    }

    @Test
    void shouldEvictEntryThatCannotBeRead() {
        when(valueOperations.get(KEY)).thenReturn("{not json");

        assertThat(cache.find(OperatorTestFixtures.OPERATOR_ID)).isEmpty();
        verify(redisTemplate).delete(KEY);
    }

    @Test
    void shouldFallBackToMissWhenRedisIsUnavailable() {
        when(valueOperations.get(anyString())).thenThrow(new RedisConnectionFailureException("down"));

        assertThat(cache.find(OperatorTestFixtures.OPERATOR_ID)).isEmpty();
    }

    @Test
    void shouldIgnoreEvictionFailure() {
        when(redisTemplate.delete(KEY)).thenThrow(new RedisConnectionFailureException("down"));

        cache.evict(OperatorTestFixtures.OPERATOR_ID);
    }

    @Test
    void shouldUseDedicatedKeyWithTtl() {
        assertThat(KEY).isEqualTo("organisation:operator:" + OperatorTestFixtures.OPERATOR_ID.value()
                + ":configuration:v1");
        assertThat(RedisOperatorConfigurationCache.CONFIGURATION_TTL).isEqualTo(Duration.ofMinutes(10));
    }
}
