package model;

public class PurchaseShop {
    private String phsId;
    private String name;
    private String farmName;
    private String address;
    private String meatType;
    private String contactNo;

    public PurchaseShop() {
    }

    public PurchaseShop(String phsId, String name, String farmName, String address, String meatType, String contactNo) {
        this.phsId = phsId;
        this.name = name;
        this.farmName = farmName;
        this.address = address;
        this.meatType = meatType;
        this.contactNo = contactNo;
    }

    public String getPhsId() {
        return phsId;
    }

    public void setPhsId(String phsId) {
        this.phsId = phsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMeatType() {
        return meatType;
    }

    public void setMeatType(String meatType) {
        this.meatType = meatType;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    @Override
    public String toString() {
        return "PurchaseShop{" +
                "phsId='" + phsId + '\'' +
                ", name='" + name + '\'' +
                ", farmName='" + farmName + '\'' +
                ", address='" + address + '\'' +
                ", meatType='" + meatType + '\'' +
                ", contactNo='" + contactNo + '\'' +
                '}';
    }
}
