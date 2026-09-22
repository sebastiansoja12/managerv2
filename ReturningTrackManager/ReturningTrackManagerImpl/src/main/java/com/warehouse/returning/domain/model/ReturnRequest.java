package com.warehouse.returning.domain.model;

import java.util.List;

import com.warehouse.common.DepartmentId;
import com.warehouse.common.OperatorId;
import com.warehouse.returning.domain.vo.UserId;


public class ReturnRequest {
    private List<ReturnPackageRequest> requests;
    private DepartmentId issuerDepartmentId;
    private UserId issuerUserId;
    private OperatorId operatorId;

    public ReturnRequest() {
    }

    public ReturnRequest(final DepartmentId issuerDepartmentId, final UserId issuerUserId,
                         final List<ReturnPackageRequest> requests) {
        this(issuerDepartmentId, issuerUserId, null, requests);
    }

    public ReturnRequest(final DepartmentId issuerDepartmentId, final UserId issuerUserId,
                         final OperatorId operatorId, final List<ReturnPackageRequest> requests) {
        this.issuerDepartmentId = issuerDepartmentId;
        this.issuerUserId = issuerUserId;
        this.operatorId = operatorId;
        this.requests = requests;
    }

    public DepartmentId getIssuerDepartmentId() {
        return issuerDepartmentId;
    }

    public void setIssuerDepartmentId(final DepartmentId issuerDepartmentId) {
        this.issuerDepartmentId = issuerDepartmentId;
    }

    public UserId getIssuerUserId() {
        return issuerUserId;
    }

    public void setIssuerUserId(final UserId issuerUserId) {
        this.issuerUserId = issuerUserId;
    }

    public OperatorId getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(final OperatorId operatorId) {
        this.operatorId = operatorId;
    }

    public List<ReturnPackageRequest> getRequests() {
        return requests;
    }

    public void setRequests(final List<ReturnPackageRequest> requests) {
        this.requests = requests;
    }
}
