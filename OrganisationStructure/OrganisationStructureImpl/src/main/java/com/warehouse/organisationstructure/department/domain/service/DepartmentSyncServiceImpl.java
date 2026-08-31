package com.warehouse.organisationstructure.department.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.organisationstructure.department.domain.model.Department;
import com.warehouse.organisationstructure.department.domain.vo.DepartmentSnapshot;
import com.warehouse.organisationstructure.department.infrastructure.adapter.secondary.DepartmentReadRepositoryImpl;
import com.warehouse.organisationstructure.department.infrastructure.adapter.secondary.entity.DepartmentEntity;
import com.warehouse.organisationstructure.department.infrastructure.adapter.secondary.mapper.DepartmentToModelMapper;

@Service
public class DepartmentSyncServiceImpl implements DepartmentSyncService {

    private final DepartmentReadRepositoryImpl departmentReadRepositoryImpl;

    private final OperatorFilteredRepository<DepartmentEntity> departmentRepository;

    public DepartmentSyncServiceImpl(final DepartmentReadRepositoryImpl departmentReadRepositoryImpl,
                                     final OperatorFilteredRepository<DepartmentEntity> departmentRepository) {
        this.departmentReadRepositoryImpl = departmentReadRepositoryImpl;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void syncReadModel(final DepartmentSnapshot snapshot) {
        departmentReadRepositoryImpl.sync(snapshot);
    }

    @Override
    @Transactional
    public int syncReadModels() {
        final List<DepartmentEntity> departments = departmentRepository.createCriteria(DepartmentEntity.class)
                .list();

        departments.stream()
                .map(DepartmentToModelMapper::map)
                .map(Department::snapshot)
                .forEach(departmentReadRepositoryImpl::sync);

        return departments.size();
    }
}
