package com.warehouse.csv.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.csv.domain.port.secondary.ParcelRepository;
import com.warehouse.csv.domain.vo.ParcelCsv;
import com.warehouse.csv.infrastructure.adapter.secondary.exception.ParcelNotFoundException;
import com.warehouse.csv.infrastructure.adapter.secondary.mapper.ParcelMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ParcelRepositoryImpl implements ParcelRepository {

    private final ParcelReadRepository parcelReadRepository;

    private final ParcelMapper mapper = getMapper(ParcelMapper.class);

    private final String exceptionMessage = "Parcel %s was not found";

    @Override
    public ParcelCsv find(Long id) {
        return parcelReadRepository.findById(id).map(mapper::map).orElseThrow(
                () -> new ParcelNotFoundException(String.format(exceptionMessage, id))
        );
    }
}
