package model;

public class Client {
    private String cusId;
    private String name;
    private String address;
    private String type;
    private String mobileNo;
    private String landNo;

    public Client() {
    }

    public Client(String cusId, String name, String address, String type, String mobileNo, String landNo) {
        this.cusId = cusId;
        this.name = name;
        this.address = address;
        this.type = type;
        this.mobileNo = mobileNo;
        this.landNo = landNo;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getLandNo() {
        return landNo;
    }

    public void setLandNo(String landNo) {
        this.landNo = landNo;
    }

    @Override
    public String toString() {
        return "Client{" +
                "cId='" + cusId + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", type='" + type + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", landNo='" + landNo + '\'' +
                '}';
    }
}
