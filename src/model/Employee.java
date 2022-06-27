package model;

public class Employee {
    private String empId;
    private String name;
    private String address;
    private String joinDate;
    private String resigningDate;
    private Double salary;
    private String contactNo;

    public Employee() {
    }

    public Employee(String empId, String name, String address, String joinDate, String resigningDate, Double salary, String contactNo) {
        this.empId = empId;
        this.name = name;
        this.address = address;
        this.joinDate = joinDate;
        this.resigningDate = resigningDate;
        this.salary = salary;
        this.contactNo = contactNo;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
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

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getResigningDate() {
        return resigningDate;
    }

    public void setResigningDate(String resigningDate) {
        this.resigningDate = resigningDate;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "eId='" + empId + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", joinDate='" + joinDate + '\'' +
                ", resigningDate='" + resigningDate + '\'' +
                ", salary=" + salary +
                ", contactNo='" + contactNo + '\'' +
                '}';
    }
}
