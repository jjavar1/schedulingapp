package erdmodel;



import java.sql.Date;

public class customers {
    private int customerID;
    private int divisionID;
    private String customerName;
    private Date createDate;
    private String createdBy;
    private String address;
    private String postalCode;
    private String phone;
    private Date lastUpdate;
    private String lastUpdateBy;

    public customers() {

    }

    public customers(int customerID, int divisionID, String customerName, Date createDate, String createdBy, String address, String postalCode, String phone, Date lastUpdate, String lastUpdateBy) {
        this.customerID = customerID;
        this.divisionID = divisionID;
        this.customerName = customerName;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public customers(int customerId, String customerName, String address, String postalCode, String phone, int divisionId, String divisionName) {
    }

    public customers(int custID) {
    }

    public customers(int customerId, String customerName, String address, String postalCode, String phone, int divisionId) {
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }


}
