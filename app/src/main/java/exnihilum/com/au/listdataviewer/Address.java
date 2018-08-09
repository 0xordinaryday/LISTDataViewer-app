package exnihilum.com.au.listdataviewer;

public class Address {

    private String unitNumber = "";
    private String streetNumber = "";
    private String streetName = "";
    private String streetType = "";
    private String locality = "";

    public Address() {
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetType() {
        return streetType;
    }

    public void setStreetType(String streetType) {
        this.streetType = streetType;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    @Override
    public String toString() {
        return "Address{" +
                "Unit Number='" + unitNumber + '\'' +
                ", Street Number='" + streetNumber + '\'' +
                ", Street Name='" + streetName + '\'' +
                ", Street Type='" + streetType + '\'' +
                ", Locality='" + locality + '\'' +
                '}';
    }
}