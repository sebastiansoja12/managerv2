package com.warehouse.qrcode.infrastructure.adapter.secondary;

import com.warehouse.qrcode.domain.model.Parcel;
import com.warehouse.qrcode.domain.port.secondary.ParcelRepository;
import com.warehouse.qrcode.infrastructure.adapter.secondary.exception.ParcelNotFoundException;
import com.warehouse.qrcode.infrastructure.adapter.secondary.mapper.ParcelMapper;
import lombok.AllArgsConstructor;

import static org.mapstruct.factory.Mappers.getMapper;

@AllArgsConstructor
public class ParcelRepositoryImpl implements ParcelRepository {

    private final ParcelReadRepository parcelReadRepository;

    private final ParcelMapper mapper = getMapper(ParcelMapper.class);

    private final String exceptionMessage = "Parcel %s was not found";

    @Override
    public Parcel find(Long id) {
        return parcelReadRepository.findById(id).map(mapper::map).orElseThrow(
                () -> new ParcelNotFoundException(String.format(exceptionMessage, id))
        );
    }
}
