package hit.androidonecourse.fieldaid.domain;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.InstantSource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import hit.androidonecourse.fieldaid.domain.models.Job;
import hit.androidonecourse.fieldaid.domain.models.Task;

public class JobStatusManager {

    private List<Job> jobs;
    private List<Task> tasks;
    private List<Job> orderedJobs = new ArrayList<>();

    private List<Job> lateJobs = new ArrayList<>();
    private List<Job> pendingJobs = new ArrayList<>();
    private List<Job> completedJobs = new ArrayList<>();



    private int lateJobsCount;
    private int completedLobCount;
    private int pendingJobCount;
    private RepositoryMediator repositoryMediator;



    public JobStatusManager(List<Job> jobs, List<Task> tasks, RepositoryMediator repositoryMediator){
        this.jobs = jobs;
        this.tasks = tasks;
        this.repositoryMediator = repositoryMediator;
        sortJobsStatus();
        orderJobsByStatusAndDate();
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
        sortJobsStatus();
        orderJobsByStatusAndDate();
    }
    public void setTasks(List<Task> tasks){
        this.tasks = tasks;
        sortJobsStatus();
        orderJobsByStatusAndDate();
    }

    public ArrayList<Job> getOrderedJobs() {
        return (ArrayList<Job>) orderedJobs;
    }

    public int getLateJobsCount() {
        return lateJobsCount;
    }

    public int getCompletedLobCount() {
        return completedLobCount;
    }

    public int getPendingJobCount() {
        return pendingJobCount;
    }

    private void sortJobsStatus(){
        lateJobsCount = 0;
        completedLobCount = 0;
        pendingJobCount = 0;
        lateJobs.clear();
        pendingJobs.clear();
        completedJobs.clear();
        for (Job job: jobs) {
            String status = getJobStatus(job);
            if(status.equals("Late")){
                lateJobs.add(job);
                lateJobsCount ++;
            } else if (status.equals("Pending")) {
                pendingJobs.add(job);
                pendingJobCount ++;
            } else if (status.equals("Completed")) {
                completedJobs.add(job);
                completedLobCount ++;
            }
            if(!job.getStatus().equals(status)){
                job.setStatus(status);
                repositoryMediator.updateJob(job);
            }
        }
    }

    private String getJobStatus(Job job)  {
        String status = "Late";
        Job examinedJob = job;
        List<Task> examinedJobsTasks = tasks.stream().filter(t -> t.getJobId() == examinedJob.getId()).collect(Collectors.toList());
        if(examinedJobsTasks.size()> 0){
            String examinedJobDueDateSrt = job.getDueDateTime();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                try{
                    Date dueDate = dateFormat.parse(examinedJobDueDateSrt);
                    if(dueDate.after(Date.from(Instant.now()))){
                        status = "Pending";
                    }

                }
                catch (Exception  e){
                    Log.d("FieldAid", "getJobStatus: bad format ");
                }
//                Instant dueDateTimeStamp = Instant.parse(examinedJobDueDateSrt);
//                if(dueDateTimeStamp.isAfter(Instant.now())){
//                    status = "Pending";
//                }
            }
        } else if (examinedJobsTasks.isEmpty() || examinedJobsTasks == null) {
            status = "Completed";
        }
        return status;
    }


    private void orderJobsByStatusAndDate(){
        orderedJobs.clear();
        orderedJobs.addAll(getOrderedListOfJobsFromTreeMap(getJobDueDateToIdTreeMap(lateJobs)));
        orderedJobs.addAll(getOrderedListOfJobsFromTreeMap(getJobDueDateToIdTreeMap(pendingJobs)));
        orderedJobs.addAll(getOrderedListOfJobsFromTreeMap(getJobDueDateToIdTreeMap(completedJobs)));
    }
    private Date extractJobsDueDate(Job job){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try{
            Date dueDate = dateFormat.parse(job.getDueDateTime());
            return dueDate;



        }
        catch (Exception  e){
            Log.d("FieldAid", "getJobStatus: bad format ");
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            return Instant.parse(job.getDueDateTime());
//        }
        return null;
    }

    private Map<Date, Long> getJobDueDateToIdTreeMap(List<Job> jobs){
        Map<Date, Long> resultMap = new TreeMap<>();
        for (Job job: jobs) {
            resultMap.put(extractJobsDueDate(job), job.getId());
        }
        return  resultMap;
    }
    private List<Job> getOrderedListOfJobsFromTreeMap(Map<Date, Long> treeMap){
        List<Job> resultList = new ArrayList<>();
        for (Long id: treeMap.values()) {
            Optional<Job> optionalJob = jobs.stream().filter(j -> j.getId() == id).findFirst();
            if(optionalJob.isPresent()){
                Job job = optionalJob.get();
                resultList.add(job);
            }
        }
        return resultList;
    }


}
