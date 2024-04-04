package hit.androidonecourse.fieldaid.domain.models;

import java.util.List;

public class Project extends EntityBase {
    private List<Contact> contacts;

    public Project() {
    }

    public Project(long id, String name, String description, String creationDateTime, String updateDateTime, List<Contact> contacts) {
        super(id, name, description, creationDateTime, updateDateTime);
        this.contacts = contacts;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }
}
