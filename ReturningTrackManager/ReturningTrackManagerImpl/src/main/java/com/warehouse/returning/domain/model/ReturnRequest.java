package com.warehouse.returning.domain.model;

import java.util.List;

import com.warehouse.returning.domain.vo.DepartmentCode;
import com.warehouse.returning.domain.vo.UserId;


public class ReturnRequest {
    private List<ReturnPackageRequest> requests;
    private DepartmentCode issuerDepartmentCode;
    private UserId issuerUserId;
    private Long operatorId;

    public ReturnRequest() {
    }

    public ReturnRequest(final DepartmentCode issuerDepartmentCode, final UserId issuerUserId,
                         final List<ReturnPackageRequest> requests) {
        this(issuerDepartmentCode, issuerUserId, null, requests);
    }

    public ReturnRequest(final DepartmentCode issuerDepartmentCode, final UserId issuerUserId,
                         final Long operatorId, final List<ReturnPackageRequest> requests) {
        this.issuerDepartmentCode = issuerDepartmentCode;
        this.issuerUserId = issuerUserId;
        this.operatorId = operatorId;
        this.requests = requests;
    }

    public DepartmentCode getIssuerDepartmentCode() {
        return issuerDepartmentCode;
    }

    public void setIssuerDepartmentCode(final DepartmentCode issuerDepartmentCode) {
        this.issuerDepartmentCode = issuerDepartmentCode;
    }

    public UserId getIssuerUserId() {
        return issuerUserId;
    }

    public void setIssuerUserId(final UserId issuerUserId) {
        this.issuerUserId = issuerUserId;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(final Long operatorId) {
        this.operatorId = operatorId;
    }

    public List<ReturnPackageRequest> getRequests() {
        return requests;
    }

    public void setRequests(final List<ReturnPackageRequest> requests) {
        this.requests = requests;
    }
}
