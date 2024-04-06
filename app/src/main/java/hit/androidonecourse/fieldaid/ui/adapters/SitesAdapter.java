package hit.androidonecourse.fieldaid.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Optional;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.databinding.SiteListItemBinding;
import hit.androidonecourse.fieldaid.domain.RepositoryMediator;
import hit.androidonecourse.fieldaid.domain.models.Project;
import hit.androidonecourse.fieldaid.domain.models.Site;
import hit.androidonecourse.fieldaid.ui.handlers.SiteListItemHandler;
import hit.androidonecourse.fieldaid.ui.views.activities.ActivityMainView;

public class SitesAdapter extends RecyclerView.Adapter<SitesAdapter.SiteViewHolder> {
    private ArrayList<Site> siteArrayList;
    private ArrayList<Project> projectArrayList;

    private Context context;

    private RepositoryMediator repositoryMediator;
    private RecyclerViewClickListener listener;

    public SitesAdapter(ArrayList<Site> sites, ArrayList<Project> projects, Context context,RecyclerViewClickListener itemListener){
        this.siteArrayList = sites;
        this.projectArrayList = projects;
        this.context = context;
        listener = itemListener;
        repositoryMediator = RepositoryMediator.getInstance(context);
    }


    @NonNull
    @Override
    public SiteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SiteListItemBinding siteListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.site_list_item,
                parent,
                false
        );
        return new SiteViewHolder(siteListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SiteViewHolder holder, int position) {
        Site currrentSite = siteArrayList.get(position);
        Optional<Project> sitesProject = projectArrayList.stream().filter(p -> p.getId() == currrentSite.getProjectId()).findFirst();
        String projectName = "";
        holder.siteListItemBinding.setSite(currrentSite);
        projectName = sitesProject.isPresent()?  sitesProject.get().getName() : "Not assigned";
        holder.siteListItemBinding.setProjectName(projectName);
        holder.siteListItemBinding.setHandler(new SiteListItemHandler(context));
        holder.bind(position);
        boolean isSelected = repositoryMediator.getCurrentSite() == currrentSite;
        holder.siteListItemBinding.setIsSelected(isSelected);
    }

    @Override
    public int getItemCount() {
        if(siteArrayList != null){
            return siteArrayList.size();
        }
        else {
            return 0;
        }
    }

    public void setProjects(ArrayList<Site> sites) {
        this.siteArrayList = sites;

        notifyDataSetChanged();
    }



    public class SiteViewHolder extends RecyclerView.ViewHolder{
        private final SiteListItemBinding siteListItemBinding;
        public SiteViewHolder(@NonNull SiteListItemBinding siteListItemBinding) {
            super(siteListItemBinding.getRoot());
            this.siteListItemBinding = siteListItemBinding;
        }

        private void bind(int position){
            siteListItemBinding.setListener(listener);
            siteListItemBinding.setPosition(position);
        }
    }
}
