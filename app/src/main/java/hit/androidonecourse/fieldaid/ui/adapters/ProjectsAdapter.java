package hit.androidonecourse.fieldaid.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.databinding.ProjectListItemBinding;
import hit.androidonecourse.fieldaid.domain.models.Project;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder> {
    private ArrayList<Project> projects;

    public ProjectsAdapter(ArrayList<Project> projects) {
        this.projects = projects;
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

        holder.projectListItemBinding.setProject(currentProject);

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

    class ProjectViewHolder extends RecyclerView.ViewHolder{
        private ProjectListItemBinding projectListItemBinding;

        public ProjectViewHolder(@NonNull  ProjectListItemBinding projectListItemBinding) {
            super(projectListItemBinding.getRoot());
            this.projectListItemBinding = projectListItemBinding;
        }
    }
}
