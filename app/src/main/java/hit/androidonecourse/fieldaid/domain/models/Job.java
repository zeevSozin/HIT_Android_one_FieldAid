package hit.androidonecourse.fieldaid.domain.models;

import java.util.List;

public class Job extends EntityBase{
    private long projectId;

    private List<Long> siteIds;
    //TODO: Create enum of statuses
    private String status;
    private List<Long> taskIds;
    private String dueDateTime;

    public Job() {
    }

    public Job(long id, String name, String description, String creationDateTime,
               String updateDateTime, long projectId, List<Long> siteIds, String status,
               List<Long> taskIds, String dueDateTime) {

        super(id, name, description, creationDateTime, updateDateTime);
        this.projectId = projectId;
        this.siteIds = siteIds;
        this.status = status;
        this.taskIds = taskIds;
        this.dueDateTime = dueDateTime;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public List<Long> getSiteIds() {
        return siteIds;
    }

    public void setSiteIds(List<Long> siteIds) {
        this.siteIds = siteIds;
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
