package com.warehouse.pallet.domain.service;

import org.springframework.stereotype.Service;

import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.domain.port.secondary.PalletRepository;
import com.warehouse.pallet.domain.vo.SealNumber;

@Service
public class SealNumberServiceImpl implements SealNumberService {

    private final PalletRepository palletRepository;

    public SealNumberServiceImpl(final PalletRepository palletRepository) {
        this.palletRepository = palletRepository;
    }

    @Override
    public void validateSealNumber(final PalletId palletId, final SealNumber sealNumber) {
        if (palletRepository.existsBySealNumber(sealNumber)) {
            throw new IllegalArgumentException("Seal number already exists");
        }
    }
}
