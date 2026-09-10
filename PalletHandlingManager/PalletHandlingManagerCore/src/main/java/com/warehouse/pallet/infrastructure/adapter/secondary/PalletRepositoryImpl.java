package com.warehouse.pallet.infrastructure.adapter.secondary;

import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.configuration.identificator.ShipmentId;
import com.warehouse.pallet.domain.enumeration.PalletStatus;
import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.domain.model.PalletPage;
import com.warehouse.pallet.domain.model.PalletSearchCriteria;
import com.warehouse.pallet.domain.port.secondary.PalletRepository;
import com.warehouse.pallet.domain.vo.SealNumber;
import com.warehouse.pallet.domain.vo.TrackingNumber;
import com.warehouse.pallet.infrastructure.adapter.secondary.document.PalletDocument;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PalletRepositoryImpl implements PalletRepository {

    private static final String PALLET_ID = "palletId.value";
    private static final String TRACKING_NUMBER = "trackingNumber.value";
    private static final String OPERATOR_ID = "operatorId.value";
    private static final String SHIPMENT_IDS = "shipmentIds";
    private static final String PALLET_STATUS = "palletStatus";
    private static final String MODIFIED = "modified";

    private static final List<String> FINISHED_STATUSES = List.of(
            PalletStatus.CANCELLED.name(), PalletStatus.DELIVERED.name());
    private static final List<String> NOT_EMPTY_CANDIDATE_STATUSES = List.of(
            PalletStatus.CANCELLED.name(), PalletStatus.DELIVERED.name(),
            PalletStatus.LOADED.name(), PalletStatus.IN_TRANSIT.name());

    private final PalletDocumentReadRepository repository;
    private final MongoTemplate mongoTemplate;

    public PalletRepositoryImpl(final PalletDocumentReadRepository repository, final MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void createOrUpdate(final Pallet pallet) {
        final PalletDocument palletDocument = PalletDocument.from(pallet);
        final Query query = Query.query(Criteria.where(PALLET_ID).is(pallet.getPalletId().value()));
        this.mongoTemplate.findAndReplace(query, palletDocument, FindAndReplaceOptions.options().upsert());
    }

    @Override
    public Pallet findById(final PalletId palletId) {
        return this.repository
                .findByPalletId(palletId)
                .map(Pallet::from)
                .orElse(null);
    }

    @Override
    public Optional<Pallet> findByTrackingNumber(final TrackingNumber trackingNumber) {
        final Query query = Query.query(Criteria.where(TRACKING_NUMBER).is(trackingNumber.value()));
        return Optional.ofNullable(this.mongoTemplate.findOne(query, PalletDocument.class)).map(Pallet::from);
    }

    @Override
    public boolean existsByTrackingNumber(final TrackingNumber trackingNumber) {
        final Query query = Query.query(Criteria.where(TRACKING_NUMBER).is(trackingNumber.value()));
        return this.mongoTemplate.exists(query, PalletDocument.class);
    }

    @Override
    public boolean existsBySealNumber(final SealNumber sealNumber) {
        return repository.existsBySealNumber(sealNumber);
    }

    @Override
    public Optional<PalletId> findActivePalletIdContaining(final ShipmentId shipmentId) {
        final Query query = Query.query(Criteria.where(SHIPMENT_IDS + ".value").is(shipmentId.value())
                .and(PALLET_STATUS).nin(FINISHED_STATUSES));
        return Optional.ofNullable(this.mongoTemplate.findOne(query, PalletDocument.class))
                .map(PalletDocument::getPalletId);
    }

    @Override
    public PalletPage search(final PalletSearchCriteria criteria) {
        final List<Criteria> conditions = new ArrayList<>();
        if (criteria.operatorId() != null) {
            conditions.add(Criteria.where(OPERATOR_ID).is(criteria.operatorId().value()));
        }
        if (criteria.palletStatus() != null) {
            conditions.add(Criteria.where(PALLET_STATUS).is(criteria.palletStatus().name()));
        }
        if (criteria.onlyEmpty()) {
            conditions.add(Criteria.where(PALLET_STATUS).nin(NOT_EMPTY_CANDIDATE_STATUSES));
            conditions.add(new Criteria().orOperator(
                    Criteria.where(SHIPMENT_IDS).size(0),
                    Criteria.where(SHIPMENT_IDS).exists(false)));
        }
        final Query query = conditions.isEmpty()
                ? new Query()
                : Query.query(new Criteria().andOperator(conditions.toArray(new Criteria[0])));
        final long totalElements = this.mongoTemplate.count(query, PalletDocument.class);
        query.with(Sort.by(Sort.Direction.DESC, MODIFIED))
                .skip((long) criteria.page() * criteria.size())
                .limit(criteria.size());
        final List<Pallet> pallets = this.mongoTemplate.find(query, PalletDocument.class).stream()
                .map(Pallet::from)
                .toList();
        return new PalletPage(pallets, criteria.page(), criteria.size(), totalElements);
    }
}
