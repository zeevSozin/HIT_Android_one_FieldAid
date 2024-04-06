package hit.androidonecourse.fieldaid.domain.models;

import java.util.List;

public class Task extends EntityBase{
    private long jobId;


    public Task() {
    }

    public Task(long id, String name, String description, String creationDateTime, String updateDateTime, long jobId) {
        super(id, name, description, creationDateTime, updateDateTime);
        this.jobId = jobId;

    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

}
