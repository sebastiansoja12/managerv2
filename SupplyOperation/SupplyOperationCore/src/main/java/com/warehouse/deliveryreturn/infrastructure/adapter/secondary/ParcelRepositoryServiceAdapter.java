package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.exception.BusinessException;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.exception.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.warehouse.deliveryreturn.domain.port.secondary.ParcelRepositoryServicePort;
import com.warehouse.deliveryreturn.domain.vo.Parcel;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ParcelDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper.ParcelMapper;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.property.ParcelProperty;


public class ParcelRepositoryServiceAdapter implements ParcelRepositoryServicePort {

    private final RestClient restClient;
    
    private final ParcelProperty parcelProperty;

    private final ParcelMapper parcelMapper = getMapper(ParcelMapper.class);
    
    private final Logger logger = LoggerFactory.getLogger(ParcelRepositoryServicePort.class);

    public ParcelRepositoryServiceAdapter(ParcelProperty parcelProperty) {
        this.parcelProperty = parcelProperty;
        this.restClient = RestClient.builder().baseUrl(parcelProperty.getUrl()).build();
    }

    @Override
    public Parcel downloadParcel(Long parcelId) {
        final ResponseEntity<ParcelDto> parcelResponse = restClient
                .get()
                .uri("/v2/api/shipments/{parcelId}", parcelId)
                .retrieve()
				.onStatus(HttpStatusCode::is5xxServerError,
						(req, res) -> {
							logger.error("Could not establish connection");
                            throw new TechnicalException(res.getStatusCode().value(), res.getStatusText());
						})
                .onStatus(HttpStatusCode::is4xxClientError,
                        (req, res) -> {
                            logger.error("Parcel {} was not found", parcelId);
                            throw new BusinessException(res.getStatusCode().value(), res.getStatusText());
                        })
                .toEntity(ParcelDto.class);
        if (parcelResponse.getStatusCode().is2xxSuccessful()) {
            if (parcelResponse.getBody() != null) {
                return parcelMapper.map(parcelResponse.getBody());
            }
        }
        return emptyResponseParcel();
    }

    private Parcel emptyResponseParcel() {
        return Parcel.builder().build();
    }
}
