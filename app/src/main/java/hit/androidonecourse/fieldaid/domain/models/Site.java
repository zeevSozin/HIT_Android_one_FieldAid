package hit.androidonecourse.fieldaid.domain.models;

import java.util.List;

public class Site extends EntityBase{
    private long projectId;
    private List<Long> contactIds;

    private CustomLatLng latLongMapString;

    public Site() {
    }

    public Site(long id, String name, String description, String creationDateTime, String updateDateTime, long projectId, List<Long> contacts,CustomLatLng latLongMapString) {
        super(id, name, description, creationDateTime, updateDateTime);
        this.projectId = projectId;
        this.contactIds = contacts;
        this.latLongMapString = latLongMapString;
    }

    public List<Long> getContactIds() {
        return contactIds;
    }

    public void setContactIds(List<Long> contactIds) {
        this.contactIds = contactIds;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public CustomLatLng getLatLongMapString() {
        return latLongMapString;
    }

    public void setLatLongMapString(CustomLatLng latLongMapString) {
        this.latLongMapString = latLongMapString;
    }
}
