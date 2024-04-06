package hit.androidonecourse.fieldaid.domain.models;

import java.util.List;
import java.util.Map;

public class Site extends EntityBase{
    private long projectId;
    private List<Contact> contacts;

    private Map<String, String> latLongMapString;

    public Site() {
    }

    public Site(long id, String name, String description, String creationDateTime, String updateDateTime, long projectId, List<Contact> contacts,Map<String, String> latLongMapString) {
        super(id, name, description, creationDateTime, updateDateTime);
        this.projectId = projectId;
        this.contacts = contacts;
        this.latLongMapString = latLongMapString;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public Map<String, String> getLatLongMapString() {
        return latLongMapString;
    }

    public void setLatLongMapString(Map<String, String> latLongMapString) {
        this.latLongMapString = latLongMapString;
    }
}
