package tn.esprit.entities;

public class DeliveryAgency {
    private int agencyId;
    private String agencyName;
    private String agencyAddress;
    private Integer nbDeliveries;

    public DeliveryAgency() {
    }

    public DeliveryAgency(int agencyId, String agencyName, String agencyAddress, Integer nbDeliveries) {
        this.agencyId = agencyId;
        this.agencyName = agencyName;
        this.agencyAddress = agencyAddress;
        this.nbDeliveries = nbDeliveries;
    }

    public int getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(int agencyId) {
        this.agencyId = agencyId;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getAgencyAddress() {
        return agencyAddress;
    }

    public void setAgencyAddress(String agencyAddress) {
        this.agencyAddress = agencyAddress;
    }

    public Integer getNbDeliveries() {
        return nbDeliveries;
    }

    public void setNbDeliveries(Integer nbDeliveries) {
        this.nbDeliveries = nbDeliveries;
    }

    @Override
    public String toString() {
        return "DeliveryAgency{" +
                "agencyId=" + agencyId +
                ", agencyName='" + agencyName + '\'' +
                ", agencyAddress='" + agencyAddress + '\'' +
                ", nbDeliveries=" + nbDeliveries +
                '}';
    }
}
