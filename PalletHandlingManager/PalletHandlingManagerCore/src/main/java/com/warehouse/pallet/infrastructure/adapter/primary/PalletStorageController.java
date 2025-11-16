package com.warehouse.pallet.infrastructure.adapter.primary;

import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.infrastructure.adapter.primary.dto.PalletIdApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.pallet.domain.port.primary.PalletPort;

@RestController
@RequestMapping("/pallets")
public class PalletStorageController {

    private final PalletPort palletPort;

    public PalletStorageController(final PalletPort palletPort) {
        this.palletPort = palletPort;
    }

    @PostMapping
    public ResponseEntity<?> createPallet() {
        final PalletId palletId = this.palletPort.createEmptyPallet();
        return ResponseEntity.ok(new PalletIdApi(palletId.value()));
    }

    @GetMapping("/{palletId}")
    public ResponseEntity<?> getPallet(@PathVariable final String palletId) {
        final Pallet pallet = this.palletPort.getPallet(new PalletId(palletId));
        return ResponseEntity.ok(pallet);
    }
}
