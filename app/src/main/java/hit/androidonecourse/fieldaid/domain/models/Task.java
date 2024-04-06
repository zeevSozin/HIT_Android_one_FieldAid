package hit.androidonecourse.fieldaid.domain.models;

import java.util.List;

public class Task extends EntityBase{
    private long jobId;
    private List<Long> formIds;

    private String status;
    private String dueDateTime;

    public Task() {
    }

    public Task(long id, String name, String description, String creationDateTime, String updateDateTime, long jobId, List<Long> formIds, String status, String dueDateTime) {
        super(id, name, description, creationDateTime, updateDateTime);
        this.jobId = jobId;
        this.formIds = formIds;
        this.status = status;
        this.dueDateTime = dueDateTime;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public List<Long> getFormIds() {
        return formIds;
    }

    public void setFormIds(List<Long> formIds) {
        this.formIds = formIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(String dueDateTime) {
        this.dueDateTime = dueDateTime;
    }
}
