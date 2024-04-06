package hit.androidonecourse.fieldaid.domain.models;

import java.util.List;

public class Project extends EntityBase {
    private List<Long> contactIds;

    public Project() {
    }

    public Project(long id, String name, String description, String creationDateTime, String updateDateTime, List<Long> contactIds) {
        super(id, name, description, creationDateTime, updateDateTime);
        this.contactIds = contactIds;
    }

    public List<Long> getContactIds() {
        return contactIds;
    }

    public void setContactIds(List<Long> contacts) {
        this.contactIds = contacts;
    }
}
