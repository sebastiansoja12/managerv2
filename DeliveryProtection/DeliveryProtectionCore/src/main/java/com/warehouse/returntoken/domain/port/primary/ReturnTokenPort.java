package com.warehouse.returntoken.domain.port.primary;

import com.warehouse.returntoken.domain.model.Parcel;
import com.warehouse.returntoken.domain.vo.Token;

public interface ReturnTokenPort {
    Token determine(Parcel parcel);
}
