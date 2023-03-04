package com.udacity.pricing.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Represents the price of a given vehicle, including currency.
 */
@Entity
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "vehicleid")
    private Long vehicleId;
    @Column(name = "currency")
    private String currency;
    @Column(name = "totalprice")
    private BigDecimal totalPrice;

    public Price() {
    }

    public Price(String currency, BigDecimal totalPrice, Long vehicleId) {
        this.currency = currency;
        this.totalPrice = totalPrice;
        this.vehicleId = vehicleId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }
}
