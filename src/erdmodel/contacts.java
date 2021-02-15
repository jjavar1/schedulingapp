package erdmodel;

public class contacts {
    private int contactID;
    private String contactName;
    private String contactEmail;

    public contacts(int contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    public contacts() {

    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }


}
