package com.warehouse.returning.application.service;

import com.warehouse.returning.application.port.primary.ReturnReadModelQueryPort;
import com.warehouse.returning.application.port.primary.ReturnReadModelSyncPort;
import com.warehouse.returning.application.port.secondary.ReturnReadModelRepository;
import com.warehouse.returning.domain.port.secondary.ReturnRepository;
import com.warehouse.returning.domain.vo.DepartmentId;
import com.warehouse.returning.domain.vo.OperatorId;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ReturnPage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReturnReadModelService implements ReturnReadModelSyncPort, ReturnReadModelQueryPort {

    private final ReturnRepository returnRepository;
    private final ReturnReadModelRepository returnReadModelRepository;

    public ReturnReadModelService(final ReturnRepository returnRepository,
                                  final ReturnReadModelRepository returnReadModelRepository) {
        this.returnRepository = returnRepository;
        this.returnReadModelRepository = returnReadModelRepository;
    }

    @Override
    @Transactional
    public void syncReadModel(final ReturnPackageId returnPackageId) {
        returnReadModelRepository.sync(returnRepository.findDetailsById(returnPackageId).toSnapshot());
    }

    @Override
    @Transactional(readOnly = true)
    public ReturnPage getReturns(final DepartmentId departmentId, final OperatorId operatorId, final int page,
                                 final int size) {
        return returnReadModelRepository.findByDepartmentIdAndOperatorId(departmentId, operatorId, page, size);
    }
}
