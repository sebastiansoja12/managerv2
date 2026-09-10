package com.warehouse.pallet.configuration;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;
import com.warehouse.pallet.domain.service.PalletTrackingNumberGenerator;
import com.warehouse.pallet.infrastructure.adapter.secondary.document.PalletDocument;
import com.warehouse.pallet.infrastructure.adapter.secondary.document.PalletHistoryDocument;
import org.bson.BsonType;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.PartialIndexFilter;
import org.springframework.data.mongodb.core.query.Criteria;

public class PalletMongoIndexInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(PalletMongoIndexInitializer.class);

    private final MongoTemplate mongoTemplate;
    private final PalletTrackingNumberGenerator trackingNumberGenerator;

    public PalletMongoIndexInitializer(final MongoTemplate mongoTemplate,
                                       final PalletTrackingNumberGenerator trackingNumberGenerator) {
        this.mongoTemplate = mongoTemplate;
        this.trackingNumberGenerator = trackingNumberGenerator;
    }

    @Override
    public void run(final ApplicationArguments args) {
        removeLegacyDepartmentCodes();
        assignMissingTrackingNumbers();
        final IndexOperations indexOperations = this.mongoTemplate.indexOps(PalletDocument.class);
        ensureIndex(indexOperations, new Index()
                .on("trackingNumber.value", Sort.Direction.ASC)
                .named("pallet_tracking_number_unique")
                .unique()
                .partial(PartialIndexFilter.of(Criteria.where("trackingNumber.value").exists(true))));
        ensureIndex(indexOperations, new Index()
                .on("palletId.value", Sort.Direction.ASC)
                .named("pallet_id_idx"));
        ensureIndex(this.mongoTemplate.indexOps(PalletHistoryDocument.class), new Index()
                .on("palletId.value", Sort.Direction.ASC)
                .on("occurredAt", Sort.Direction.DESC)
                .named("pallet_history_pallet_idx"));
        ensureIndex(indexOperations, new Index()
                .on("shipmentIds.value", Sort.Direction.ASC)
                .named("pallet_shipment_ids_idx"));
        ensureIndex(indexOperations, new Index()
                .on("operatorId.value", Sort.Direction.ASC)
                .on("palletStatus", Sort.Direction.ASC)
                .on("modified", Sort.Direction.DESC)
                .named("pallet_operator_status_modified_idx"));
    }

    private void removeLegacyDepartmentCodes() {
        final MongoCollection<Document> collection = this.mongoTemplate.getCollection(
                this.mongoTemplate.getCollectionName(PalletDocument.class));
        collection.updateMany(Filters.exists("originDepartment"), Updates.unset("originDepartment"));
        collection.updateMany(Filters.exists("destinationDepartment"), Updates.unset("destinationDepartment"));
        collection.updateMany(Filters.type("driver.assignedDepartments.value", BsonType.STRING),
                Updates.unset("driver.assignedDepartments"));
    }

    private void assignMissingTrackingNumbers() {
        final MongoCollection<Document> collection = this.mongoTemplate.getCollection(
                this.mongoTemplate.getCollectionName(PalletDocument.class));
        for (final Document document : collection.find(Filters.exists("trackingNumber.value", false))
                .projection(Projections.include("_id"))) {
            collection.updateOne(Filters.eq("_id", document.get("_id")),
                    Updates.set("trackingNumber", new Document("value",
                            this.trackingNumberGenerator.generate().value())));
        }
    }

    private void ensureIndex(final IndexOperations indexOperations, final Index index) {
        try {
            indexOperations.ensureIndex(index);
        } catch (final RuntimeException exception) {
            log.warn("Cannot create pallet index {}", index.getIndexKeys(), exception);
        }
    }
}
