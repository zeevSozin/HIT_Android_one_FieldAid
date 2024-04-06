package hit.androidonecourse.fieldaid.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Optional;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.databinding.JobListItemBinding;
import hit.androidonecourse.fieldaid.domain.RepositoryMediator;
import hit.androidonecourse.fieldaid.domain.models.Job;
import hit.androidonecourse.fieldaid.domain.models.Site;
import hit.androidonecourse.fieldaid.ui.handlers.JobListItemHandler;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.JobViewHolder> {
    private ArrayList<Job> jobArrayList;
    private ArrayList<Site> siteArrayList;
    private Context context;
    private RepositoryMediator repositoryMediator;
    private RecyclerViewClickListener listener;

    public JobsAdapter(ArrayList<Job> jobArrayList, ArrayList<Site> siteArrayList, Context context,  RecyclerViewClickListener listener) {
        repositoryMediator = RepositoryMediator.getInstance(context);
        this.jobArrayList = jobArrayList;
        this.siteArrayList = siteArrayList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        JobListItemBinding jobListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.job_list_item,
                parent,
                false
        );
        return new JobViewHolder(jobListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job currentJob = jobArrayList.get(position);
        Optional<Site> jobsSite =  siteArrayList.stream().filter(s -> s.getId() == currentJob.getSiteId()).findFirst();
        String siteName ="";
        holder.jobListItemBinding.setJob(currentJob);
        siteName = jobsSite.isPresent()? jobsSite.get().getName() : "Not assigned";
        holder.jobListItemBinding.setSiteName(siteName);
        holder.jobListItemBinding.setHandler(new JobListItemHandler(context));
        holder.bind(position);
        boolean isSelected = repositoryMediator.getCurrentJob() == currentJob;
        holder.jobListItemBinding.setIsSelected(isSelected);

    }

    @Override
    public int getItemCount() {
        if(jobArrayList != null){
            return jobArrayList.size();
        }
        return 0;
    }

    public void setJobs(ArrayList<Job> jobs){
        this.jobArrayList = jobs;
        notifyDataSetChanged();
    }


    public class JobViewHolder extends RecyclerView.ViewHolder{
        private final JobListItemBinding jobListItemBinding;
        public JobViewHolder(@NonNull JobListItemBinding jobListItemBinding) {
            super(jobListItemBinding.getRoot());
            this.jobListItemBinding = jobListItemBinding;
        }
        private void bind(int position){
            jobListItemBinding.setListener(listener);
            jobListItemBinding.setPosition(position);
        }
    }

}
