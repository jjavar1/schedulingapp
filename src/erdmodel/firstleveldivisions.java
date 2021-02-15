package erdmodel;

import java.sql.Date;

public class firstleveldivisions {
    private int divisionID;
    private int countryID;
    private String division;
    private String createdBy;
    private String updatedBY;
    private Date created;
    private Date updated;

    public firstleveldivisions() {

    }

    public firstleveldivisions(int divisionID, int countryID, String division, String createdBy, String updatedBY, Date created, Date updated) {
        this.divisionID = divisionID;
        this.countryID = countryID;
        this.division = division;
        this.createdBy = createdBy;
        this.updatedBY = updatedBY;
        this.created = created;
        this.updated = updated;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBY() {
        return updatedBY;
    }

    public void setUpdatedBY(String updatedBY) {
        this.updatedBY = updatedBY;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

}
