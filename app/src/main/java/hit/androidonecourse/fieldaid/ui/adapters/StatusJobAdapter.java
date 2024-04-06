package hit.androidonecourse.fieldaid.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.databinding.JobStatusListItemBinding;
import hit.androidonecourse.fieldaid.domain.RepositoryMediator;
import hit.androidonecourse.fieldaid.domain.models.Job;
import hit.androidonecourse.fieldaid.domain.models.Project;
import hit.androidonecourse.fieldaid.domain.models.Site;
import hit.androidonecourse.fieldaid.ui.handlers.StatusJobHandler;

public class StatusJobAdapter extends RecyclerView.Adapter<StatusJobAdapter.StatusTaskViewHolder>  {
    private ArrayList<Project> projectArrayList;
    private ArrayList<Site> siteArrayList;
    private ArrayList<Job> jobArrayList;
    private Context context;

    private RepositoryMediator repositoryMediator;

    private RecyclerViewClickListener listener;

    public StatusJobAdapter(ArrayList<Project> projectArrayList, ArrayList<Site> siteArrayList, ArrayList<Job> jobArrayList, Context context, RecyclerViewClickListener listener) {
        this.projectArrayList = projectArrayList;
        this.siteArrayList = siteArrayList;
        this.jobArrayList = jobArrayList;
        this.context = context;
        this.listener = listener;
        repositoryMediator = RepositoryMediator.getInstance(context);
    }

    @NonNull
    @Override
    public StatusTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        JobStatusListItemBinding jobStatusListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.job_status_list_item,
                parent,
                false
        );
        return new StatusTaskViewHolder(jobStatusListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusTaskViewHolder holder, int position) {
        Job currentJob = jobArrayList.get(position);
        Optional<Site> jobsSite = siteArrayList.stream().filter(s -> s.getId() == currentJob.getSiteId()).findFirst();
        if(jobsSite.isPresent()){
            Site site = jobsSite.get();
            holder.jobStatusListItemBinding.setSiteName(site.getName());
            Optional<Project> sitesProject = projectArrayList.stream().filter(p -> p.getId() == site.getProjectId()).findFirst();
            if(sitesProject.isPresent()){
                Project project = sitesProject.get();
                holder.jobStatusListItemBinding.setProjectName(project.getName());
            }
        }
        holder.jobStatusListItemBinding.setJob(currentJob);
        holder.jobStatusListItemBinding.setTaskCount(String.valueOf(currentJob.getTaskIds().size() - 1));
        holder.jobStatusListItemBinding.setHandler(new StatusJobHandler(context));

        int statusIcon = 0;
        switch (currentJob.getStatus()) {
            case "Late":
                statusIcon = R.drawable.ic_status_late_48;
                break;
            case "Pending":
                statusIcon = R.drawable.ic_status_pending_48;
                break;
            case "Completed":
                statusIcon = R.drawable.ic_status_completed_48;
                break;
        }
        holder.jobStatusListItemBinding.setStatusIcon(statusIcon);
        holder.bind(position);
        boolean isSelected = repositoryMediator.getCurrentStatusJob() == currentJob;
        holder.jobStatusListItemBinding.setIsSelected(isSelected);
        Log.d("FieldAid", "onBindViewHolder: Job" + currentJob.getName() + "is selected: " + isSelected);

    }

    @Override
    public int getItemCount() {
        if(jobArrayList != null){
            return jobArrayList.size();
        }
        else {
            return 0;
        }
    }

    public void setStatusJobs(ArrayList<Job> jobs){
        this.jobArrayList = jobs;
        notifyDataSetChanged();
    }

    public class StatusTaskViewHolder extends RecyclerView.ViewHolder {
        private final JobStatusListItemBinding jobStatusListItemBinding;
        public StatusTaskViewHolder(@NonNull JobStatusListItemBinding jobStatusListItemBinding) {
            super(jobStatusListItemBinding.getRoot());
            this.jobStatusListItemBinding = jobStatusListItemBinding;
        }
        private void bind(int position){
            jobStatusListItemBinding.setListener(listener);
            jobStatusListItemBinding.setPosition(position);
        }
    }
}
