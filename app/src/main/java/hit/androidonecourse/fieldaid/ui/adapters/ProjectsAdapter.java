package hit.androidonecourse.fieldaid.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import hit.androidonecourse.fieldaid.BR;
import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.databinding.ProjectListItemBinding;
import hit.androidonecourse.fieldaid.domain.RepositoryMediator;
import hit.androidonecourse.fieldaid.domain.models.Project;
import hit.androidonecourse.fieldaid.ui.handlers.ProjectListItemHandler;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder>  {
    private ArrayList<Project> projects;
    private Context context;
    private RepositoryMediator repositoryMediator;
    private RecyclerViewClickListener listener;


    public ProjectsAdapter(ArrayList<Project> projects, Context context, RecyclerViewClickListener itemListener) {
        this.projects = projects;
        this.context = context;
        listener = itemListener;
        repositoryMediator = RepositoryMediator.getInstance(context);
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ProjectListItemBinding projectListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.project_list_item,
                parent,
                false
        );
        return new ProjectViewHolder(projectListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project currentProject = projects.get(position);
        Glide.with(context).load(currentProject.getPictureUri()).
                error(R.drawable.ic_add_phot).placeholder(R.drawable.ic_add_phot).
                into(holder.projectListItemBinding.imageView);

        holder.projectListItemBinding.setProject(currentProject);
        holder.projectListItemBinding.setHandler(new ProjectListItemHandler(context));
        holder.bind(position);
        boolean isSelected = repositoryMediator.getCurrentProject() == currentProject;
        holder.projectListItemBinding.setIsSelected(isSelected);

    }

    @Override
    public int getItemCount() {
        if(projects != null){
            return  projects.size();
        }
        else {
            return 0;

        }
    }



    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;

        notifyDataSetChanged();
    }







    class ProjectViewHolder extends RecyclerView.ViewHolder {
        private ProjectListItemBinding projectListItemBinding;

        public ProjectViewHolder(@NonNull  ProjectListItemBinding projectListItemBinding) {
            super(projectListItemBinding.getRoot());
            this.projectListItemBinding = projectListItemBinding;

        }
        private void bind(int position){
            projectListItemBinding.setListener(listener);
            projectListItemBinding.setPosition(position);
        }



    }
}
