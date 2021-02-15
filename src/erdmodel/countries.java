package erdmodel;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
public class countries {
    private int countryID;
    private String country;
    private Date createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;

    public countries() {

    }
    public countries(int countryID, String country, Date createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {
        this.countryID = countryID;
        this.country = country;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public Timestamp getLastUpdate() {
        this.lastUpdate = Timestamp.valueOf(LocalDateTime.of(lastUpdate.toLocalDateTime().toLocalDate(), lastUpdate.toLocalDateTime().toLocalTime()));
        return this.lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }


}
