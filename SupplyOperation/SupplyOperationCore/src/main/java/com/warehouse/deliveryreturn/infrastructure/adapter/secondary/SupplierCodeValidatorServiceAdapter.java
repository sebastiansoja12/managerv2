package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.exception.SupplierValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.warehouse.deliveryreturn.domain.port.secondary.SupplierCodeValidatorServicePort;
import com.warehouse.deliveryreturn.domain.vo.Supplier;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.SupplierDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper.SupplierValidateResponseMapper;
import com.warehouse.tools.supplier.SupplierValidatorProperties;

import lombok.Builder;

@Builder
public class SupplierCodeValidatorServiceAdapter implements SupplierCodeValidatorServicePort {

    private final RestClient restClient;
    
    private final SupplierValidatorProperties supplierValidatorProperties;
    
    private final SupplierValidateResponseMapper responseMapper = getMapper(SupplierValidateResponseMapper.class);

    private final Logger logger = LoggerFactory.getLogger("SupplierCodeValidator");
    
    private final String endpoint = "/v2/api/suppliers/%s/%s";

    @Override
    public void validateSupplier(String supplierCode) {
		final SupplierValidateConfiguration configuration = new SupplierValidateConfiguration(
				supplierValidatorProperties);
        validateSupplierByCode(configuration, supplierCode);
    }
    
    private void validateSupplierByCode(SupplierValidateConfiguration configuration, String supplierCode) {
        final ResponseEntity<? extends SupplierDto> responseEntity = restClient
                .get()
                .uri(String.format(endpoint, configuration.getEndpoint(), supplierCode))
                .retrieve()
                .toEntity(SupplierDto.class);
        
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new SupplierValidationException(400, "Supplier not valid");
        }
        
        final Supplier supplier = responseMapper.map(responseEntity.getBody());
        
        if (!supplier.isActive()) {
            throw new SupplierValidationException(406, "Supplier not valid");
        }

        logger.info("Supplier validated");
    }


	public record SupplierValidateConfiguration(SupplierValidatorProperties supplierValidatorProperties)
			implements Properties {

		@Override
		public String getUrl() {
			return supplierValidatorProperties.getUrl();
		}

		@Override
		public String getEndpoint() {
			return supplierValidatorProperties.getEndpoint();
		}
	}
}
