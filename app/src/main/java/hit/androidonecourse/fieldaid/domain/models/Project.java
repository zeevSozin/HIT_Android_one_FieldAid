package hit.androidonecourse.fieldaid.domain.models;

import java.util.List;

public class Project extends EntityBase {
    private List<Long> contactIds;



    private String pictureUri = "";

    public Project() {
    }

    public Project(long id, String name, String description, String creationDateTime, String updateDateTime, List<Long> contactIds, String pictureUri) {
        super(id, name, description, creationDateTime, updateDateTime);
        this.contactIds = contactIds;
        this.pictureUri = pictureUri;
    }

    public List<Long> getContactIds() {
        return contactIds;
    }

    public void setContactIds(List<Long> contacts) {
        this.contactIds = contacts;
    }
    public String getPictureUri() {
        return pictureUri;
    }

    public void setPictureUri(String pictureUri) {
        this.pictureUri = pictureUri;
    }
}
