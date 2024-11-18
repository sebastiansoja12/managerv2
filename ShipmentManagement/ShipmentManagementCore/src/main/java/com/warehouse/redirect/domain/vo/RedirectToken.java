package com.warehouse.redirect.domain.vo;

import java.time.LocalDateTime;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.redirect.infrastructure.adapter.secondary.entity.RedirectTokenEntity;


public class RedirectToken {
    
    private Long id;

    private String token;

    private LocalDateTime createdDate;

    private LocalDateTime expiryDate;

    private ShipmentId shipmentId;

    private String email;

	public RedirectToken(final Long id, 
                         final String token, 
                         final LocalDateTime createdDate,
                         final LocalDateTime expiryDate, 
                         final ShipmentId shipmentId, 
                         final String email) {
        this.id = id;
        this.token = token;
        this.createdDate = createdDate;
        this.expiryDate = expiryDate;
        this.shipmentId = shipmentId;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public String getEmail() {
        return email;
    }
    
    public void invalidate() {
        this.expiryDate = LocalDateTime.now();
    }
    
	public static RedirectToken from(final RedirectTokenEntity entity) {
		return new RedirectToken(entity.getId(), entity.getToken(), entity.getCreatedDate(), entity.getExpiryDate(),
				new ShipmentId(entity.getId()), entity.getEmail());
	}
}
