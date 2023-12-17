package com.warehouse.returntoken.domain.service;

import com.warehouse.returntoken.domain.model.Parcel;
import com.warehouse.returntoken.domain.vo.Token;

public interface ReturnTokenService {

    Token determineToken(Parcel parcel);
}
