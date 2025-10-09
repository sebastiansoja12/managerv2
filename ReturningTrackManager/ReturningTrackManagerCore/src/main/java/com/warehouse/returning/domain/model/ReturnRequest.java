package com.warehouse.returning.domain.model;

import java.util.List;

import com.warehouse.returning.domain.vo.DepartmentCode;
import com.warehouse.returning.domain.vo.UserId;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ReturnRequestApi;


public class ReturnRequest {
    private List<ReturnPackageRequest> requests;
    private DepartmentCode issuerDepartmentCode;
    private UserId issuerUserId;

    public ReturnRequest() {
    }

    public ReturnRequest(final DepartmentCode issuerDepartmentCode, final UserId issuerUserId,
                         final List<ReturnPackageRequest> requests) {
        this.issuerDepartmentCode = issuerDepartmentCode;
        this.issuerUserId = issuerUserId;
        this.requests = requests;
    }

    public static ReturnRequest from(final ReturnRequestApi returnApiRequest) {
        final DepartmentCode departmentCode = DepartmentCode.of(returnApiRequest.departmentCode());
        final UserId userId = UserId.of(returnApiRequest.userId());
        final List<ReturnPackageRequest> returnPackageRequests = returnApiRequest.requests()
                .stream()
                .map(ReturnPackageRequest::from)
                .toList();
        return new ReturnRequest(departmentCode, userId, returnPackageRequests);
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

    public List<ReturnPackageRequest> getRequests() {
        return requests;
    }

    public void setRequests(final List<ReturnPackageRequest> requests) {
        this.requests = requests;
    }
}
