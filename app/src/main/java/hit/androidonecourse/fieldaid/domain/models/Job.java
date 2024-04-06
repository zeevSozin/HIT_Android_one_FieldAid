package hit.androidonecourse.fieldaid.domain.models;

import java.util.List;

public class Job extends EntityBase{
    private long siteId;

    //TODO: Create enum of statuses
    private String status;
    private List<Long> taskIds;
    private String dueDateTime;

    public Job() {
    }

    public Job(long id, String name, String description, String creationDateTime,
               String updateDateTime, long siteId, String status,
               List<Long> taskIds, String dueDateTime) {

        super(id, name, description, creationDateTime, updateDateTime);
        this.siteId = siteId;
        this.status = status;
        this.taskIds = taskIds;
        this.dueDateTime = dueDateTime;
    }

    public long getSiteId() {
        return siteId;
    }

    public void setSitetId(long siteId) {
        this.siteId = siteId;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Long> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(List<Long> taskIds) {
        this.taskIds = taskIds;
    }

    public String getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(String dueDateTime) {
        this.dueDateTime = dueDateTime;
    }
}
