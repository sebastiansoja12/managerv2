package com.warehouse.department.infrastructure.adapter.secondary;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.department.domain.port.secondary.DepartmentReadRepository;
import com.warehouse.department.domain.vo.DepartmentSnapshot;
import com.warehouse.department.infrastructure.adapter.secondary.entity.readmodel.DepartmentReadEntity;
import com.warehouse.department.infrastructure.adapter.secondary.mapper.DepartmentReadFactory;

@Repository
public class DepartmentReadRepositoryImpl implements DepartmentReadRepository<DepartmentReadEntity> {

    private static final List<DepartmentReadEntity.Status> EXCLUDED_STATUSES = List.of(
            DepartmentReadEntity.Status.ARCHIVED,
            DepartmentReadEntity.Status.DELETED
    );

    private final BaseRepository<DepartmentReadEntity> repository;

    private final DepartmentReadFactory factory;

    public DepartmentReadRepositoryImpl(final BaseRepository<DepartmentReadEntity> repository,
                                        final DepartmentReadFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    @Override
    public void sync(final DepartmentSnapshot snapshot) {
        final DepartmentReadEntity readEntity = factory.fromDepartmentSnapshot(snapshot);
        if (exists(snapshot.departmentCode())) {
            repository.update(readEntity);
        } else {
            repository.create(readEntity);
        }
    }

    @Override
    public List<DepartmentReadEntity> list() {
        return repository.createCriteria(DepartmentReadEntity.class)
                .notIn("status", EXCLUDED_STATUSES)
                .list();
    }

    @Override
    public DepartmentReadEntity findById(final DepartmentCode departmentCode) {
        return repository.createCriteria(DepartmentReadEntity.class)
                .eq("departmentCode.value", departmentCode)
                .notIn("status", EXCLUDED_STATUSES)
                .one()
                .orElse(null);
    }

    private boolean exists(final DepartmentCode departmentCode) {
        return repository.createCriteria(DepartmentReadEntity.class)
                .eq("departmentCode.value", departmentCode)
                .one()
                .isPresent();
    }
}
