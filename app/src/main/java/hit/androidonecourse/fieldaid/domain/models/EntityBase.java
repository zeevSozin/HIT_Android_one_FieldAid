package hit.androidonecourse.fieldaid.domain.models;

public class EntityBase {
    private long id;
    private String name;
    private String description;
    private String creationDateTime;
    private String updateDateTime;
    private boolean isDeleted;

    public EntityBase() {
    }

    public EntityBase(long id, String name, String description, String creationDateTime, String updateDateTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creationDateTime = creationDateTime;
        this.updateDateTime = updateDateTime;
        this.isDeleted = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        this.updateDateTime = updateDateTime;
    }
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
