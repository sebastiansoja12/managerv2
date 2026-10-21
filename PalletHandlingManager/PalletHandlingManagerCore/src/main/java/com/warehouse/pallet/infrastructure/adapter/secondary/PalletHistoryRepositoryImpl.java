package com.warehouse.pallet.infrastructure.adapter.secondary;

import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.domain.model.PalletHistoryEntry;
import com.warehouse.pallet.domain.port.secondary.PalletHistoryRepository;
import com.warehouse.pallet.infrastructure.adapter.secondary.document.PalletHistoryDocument;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class PalletHistoryRepositoryImpl implements PalletHistoryRepository {

    private final MongoTemplate mongoTemplate;

    public PalletHistoryRepositoryImpl(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void append(final PalletHistoryEntry entry) {
        this.mongoTemplate.insert(PalletHistoryDocument.from(entry));
    }

    @Override
    public List<PalletHistoryEntry> findByPalletId(final PalletId palletId) {
        final Query query = Query.query(Criteria.where("palletId.value").is(palletId.value()))
                .with(Sort.by(Sort.Direction.DESC, "occurredAt"));
        return this.mongoTemplate.find(query, PalletHistoryDocument.class).stream()
                .map(PalletHistoryDocument::toEntry)
                .toList();
    }
}
