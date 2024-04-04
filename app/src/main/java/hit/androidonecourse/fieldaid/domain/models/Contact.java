package hit.androidonecourse.fieldaid.domain.models;

public class Contact extends EntityBase{
    private String phoneNumber;

    public Contact() {
    }

    public Contact(long id, String name, String description, String creationDateTime, String updateDateTime, String phoneNumber) {
        super(id, name, description, creationDateTime, updateDateTime);
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        //TODO: add dat validation
        this.phoneNumber = phoneNumber;
    }
}
