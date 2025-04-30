package tn.esprit.entities;

import java.time.LocalDate;

public class Delivery {
    private int deliveryId;
    private int orderId;
    private LocalDate estimatedDate;
    private float deliveryFees;
    private String destination;
    private boolean state;
    private int agencyId;

    public Delivery() {
    }

    public Delivery(int deliveryId, int orderId, LocalDate estimatedDate, float deliveryFees, String destination, boolean state, int agencyId) {
        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.estimatedDate = estimatedDate;
        this.deliveryFees = deliveryFees;
        this.destination = destination;
        this.state = state;
        this.agencyId = agencyId;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public LocalDate getEstimatedDate() {
        return estimatedDate;
    }

    public void setEstimatedDate(LocalDate estimatedDate) {
        this.estimatedDate = estimatedDate;
    }

    public float getDeliveryFees() {
        return deliveryFees;
    }

    public void setDeliveryFees(float deliveryFees) {
        this.deliveryFees = deliveryFees;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(int agencyId) {
        this.agencyId = agencyId;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "deliveryId=" + deliveryId +
                ", orderId=" + orderId +
                ", estimatedDate=" + estimatedDate +
                ", deliveryFees=" + deliveryFees +
                ", destination='" + destination + '\'' +
                ", state=" + state +
                ", agencyId=" + agencyId +
                '}';
    }
}
