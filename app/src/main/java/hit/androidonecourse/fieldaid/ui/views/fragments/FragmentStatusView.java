package hit.androidonecourse.fieldaid.ui.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.security.cert.PKIXRevocationChecker;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.domain.JobStatusManager;
import hit.androidonecourse.fieldaid.domain.RepositoryMediator;
import hit.androidonecourse.fieldaid.domain.models.Job;
import hit.androidonecourse.fieldaid.domain.models.Project;
import hit.androidonecourse.fieldaid.domain.models.Site;
import hit.androidonecourse.fieldaid.domain.models.Task;
import hit.androidonecourse.fieldaid.ui.adapters.RecyclerViewClickListener;
import hit.androidonecourse.fieldaid.ui.adapters.StatusJobAdapter;

public class FragmentStatusView extends Fragment implements RecyclerViewClickListener {
    private RepositoryMediator repositoryMediator;
    private JobStatusManager jobStatusManager;
    private ArrayList<Project> projects = new ArrayList<>();
    private ArrayList<Site> sites = new ArrayList<>();
    private ArrayList<Job> jobs = new ArrayList<>();
    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<Job> orderedJobs = new ArrayList<>();

    private RecyclerView statusJobRecyclerView;
    private StatusJobAdapter statusJobAdapter;
    private ImageButton imageButtonFilterAll;
    private ImageButton imageButtonFilterCompleted;
    private ImageButton imageButtonFilterPending;
    private ImageButton imageButtonFilterLate;


    private String statusJobFilter = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status_view, container, false);


        repositoryMediator =RepositoryMediator.getInstance(this.getContext());

        imageButtonFilterLate = view.findViewById(R.id.imageBtn_filter_status_late);
        imageButtonFilterPending = view.findViewById(R.id.imageBtn_filter_status_pending);
        imageButtonFilterCompleted = view.findViewById(R.id.imageBtn_filter_status_completed);
        imageButtonFilterAll = view.findViewById(R.id.imageBtn_filter_status_all);



        statusJobRecyclerView = view.findViewById(R.id.status_jobs_recyclerView);
        statusJobRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        statusJobRecyclerView.setHasFixedSize(true);


        jobStatusManager = new JobStatusManager(jobs, tasks, repositoryMediator);

        repositoryMediator.getProjectLiveData().observe(this.getViewLifecycleOwner(), collection ->{
            projects.clear();
            for(Project project: collection){
                if(!project.isDeleted()){
                    projects.add(project);
                }
            }
        });

        repositoryMediator.getSiteLiveData().observe(this.getViewLifecycleOwner(), collection ->{
            sites.clear();
            for(Site site: collection){
                if(!site.isDeleted()){
                    sites.add(site);
                }
            }
        });

        repositoryMediator.getJobLiveData().observe(this.getViewLifecycleOwner(), collection ->{
            jobs.clear();
            for (Job job : collection){
                if(!job.isDeleted()){
                    jobs.add(job);
                }
            }
            jobStatusManager.setJobs(this.jobs);
        });

        repositoryMediator.getTaskLiveData().observe(this.getViewLifecycleOwner(), collection ->{
            tasks.clear();
            tasks.addAll(collection);
            jobStatusManager.setTasks(this.tasks);
        });

        orderedJobs = jobStatusManager.getOrderedJobs();


        imageButtonFilterAll.setOnClickListener(v ->{
            statusJobFilter = null;
            statusJobAdapter.setStatusJobs(filterJobs(jobs,null));
        });
        imageButtonFilterPending.setOnClickListener(v ->{
            statusJobFilter = "Pending";
            statusJobAdapter.setStatusJobs(filterJobs(jobs,"Pending"));
        });
        imageButtonFilterCompleted.setOnClickListener(v ->{
            statusJobFilter = "Completed";
            statusJobAdapter.setStatusJobs(filterJobs(jobs,"Completed"));
        });
        imageButtonFilterLate.setOnClickListener(v ->{
            statusJobFilter = "Late";
            statusJobAdapter.setStatusJobs(filterJobs(jobs,"Late"));
        });

        repositoryMediator.getIsAllCollectionsLoaded().observe(getViewLifecycleOwner(), isReady -> {
            if (isReady) {
                statusJobAdapter = new StatusJobAdapter(projects,sites, filterJobs(orderedJobs, statusJobFilter),getContext(),this);
                statusJobRecyclerView.setAdapter(statusJobAdapter);

                Log.d("FieldAid", "onCreateView: Status the collection is ready");
            }
        });

        return view;
    }

    private ArrayList<Job> filterJobs(ArrayList<Job> jobs, String filter){
        ArrayList<Job> filteredJobs = new ArrayList<>();
        filteredJobs.addAll(jobs);
        if(filter != null){
            filteredJobs.clear();
            switch (filter){
                case "Late":
                    filteredJobs.addAll(jobs.stream().filter(j -> Objects.equals(j.getStatus(), "Late")).collect(Collectors.toList()));
                    break;
                case "Pending":
                    filteredJobs.addAll(jobs.stream().filter(j -> Objects.equals(j.getStatus(), "Pending")).collect(Collectors.toList()));
                    break;
                case "Completed":
                    filteredJobs.addAll(jobs.stream().filter(j -> Objects.equals(j.getStatus(), "Completed")).collect(Collectors.toList()));
                    break;
            }
        }

        return filteredJobs;
    }

    @Override
    public void recyclerViewClickListener(int position) {
        Job selectedJob = orderedJobs.get(position);
        repositoryMediator.setCurrentStatusJob(selectedJob);
        repositoryMediator.setTaskFilterByJob(selectedJob);
        statusJobRecyclerView.setAdapter(statusJobAdapter);
        Site jobsSite = getSiteFromJob(selectedJob);
        if(jobsSite != null){
            repositoryMediator.setCurrentSite(jobsSite);
        }
        Log.d("FieldAid", "recyclerViewClickListener: Status view current job is: " +
                repositoryMediator.getCurrentStatusJob().getName());


    }
    private Site getSiteFromJob(Job job){
        Optional<Site> optionalSite = sites.stream().filter(s -> s.getId() == job.getSiteId()).findFirst();
        return optionalSite.orElse(null);
    }
}