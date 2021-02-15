package erdmodel;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

public class appointments {
    private int appointmentID;
    private int customerID;
    private int userID;
    private int contactID;
    private String title;
    private String description;
    private String location;
    private String type;
    private String createdBy;
    private String updatedBy;
    private String start;
    private String end;
    private LocalDateTime created;
    private LocalDateTime updated;

    public appointments() {

    }

    public appointments(int appointmentID, int customerID, int userID, int contactID, String title, String description, String location, String type, String createdBy, String updatedBy, String start, String end, LocalDateTime created, LocalDateTime updated) {
        this.appointmentID = appointmentID;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.start = start;
        this.end = end;
        this.created = created;
        this.updated = updated;
    }

    public appointments(Integer appointmentID, String title, String description, String location, String type, Integer customerID, Integer contactID, String localStartDT, String localEndDT) {
    }
    

    public appointments(int appointmentID, String title, String description, String location, String type, int customerID, int contactID, LocalTime start, LocalTime end) {
    }

    public appointments(int apptID, String title, String description, String location, String type, int customerid, int contactid, LocalDateTime startOut, LocalDateTime endOut) {
    }

    public appointments(String type) {
    }

    public appointments(int aptId, String title, String description, String location, String type, LocalDateTime utcStartDT, LocalDateTime utcEndDT, int customerId, int userId, int contactId, String contact) {
    }

    public appointments(int aptId, String title, String description, String location, String type, LocalDateTime utcStartDTTwo, LocalDateTime utcEndDTTwo, int customerId, int userId, int contact) {
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end =  end;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }


}
