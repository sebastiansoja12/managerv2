package com.warehouse.commonassets.searchobject;

import java.util.List;

public interface SpecificationRepository<T extends SearchCriteria, R> {

    List<R> list(T searchObject);
}
