package hit.androidonecourse.fieldaid.ui.views.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.data.repositories.FirebaseCollectionCallback;
import hit.androidonecourse.fieldaid.data.repositories.ProjectRepo;
import hit.androidonecourse.fieldaid.data.repositories.SiteRepo;
import hit.androidonecourse.fieldaid.domain.models.Project;
import hit.androidonecourse.fieldaid.domain.models.Site;
import hit.androidonecourse.fieldaid.ui.adapters.ProjectsAdapter;
import hit.androidonecourse.fieldaid.ui.adapters.SitesAdapter;
import hit.androidonecourse.fieldaid.util.TimeStamp;

public class FragmentSitesView extends Fragment {
    private MutableLiveData<List<Site>> sitesLiveData;
    private MutableLiveData<List<Project>> projectsLiveData;

    private ArrayList<Site> siteArrayList = new ArrayList<>();
    private ArrayList<Project> projectArrayList = new ArrayList<>();

    private ProjectRepo projectRepo;
    private SiteRepo siteRepo;

    private RecyclerView sitesRecyclerView;
    private SitesAdapter sitesAdapter;
    private ProjectsAdapter projectsAdapter;
    private FloatingActionButton fabAddSite;

    // Dialog fields
    private Dialog addSiteDialog;

    private Spinner spinnerProjects;
    private EditText editTxtSiteName;
    private EditText editTxtSiteDescription;
    private Button btnAddSite;
    private Button btnCancel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSiteDialog = new Dialog(this.getContext());
        addSiteDialog.setContentView(R.layout.dialog_add_site);
        addSiteDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addSiteDialog.getWindow().setBackgroundDrawableResource(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark_normal_background);
        addSiteDialog.setCancelable(true);

        spinnerProjects = addSiteDialog.findViewById(R.id.dialog_addSite_spinner);
        editTxtSiteName = addSiteDialog.findViewById(R.id.dialog_addSite_editText_projectName);
        editTxtSiteDescription = addSiteDialog.findViewById(R.id.dialog_addSite_editText_projectDescription);
        btnAddSite = addSiteDialog.findViewById(R.id.dialog_addSite_btn_add);
        btnCancel = addSiteDialog.findViewById(R.id.dialog_addSite_btn_cancel);

        btnAddSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTxtSiteName.getText().toString().isEmpty()){
                    int projectListItemPos = spinnerProjects.getSelectedItemPosition();
                    long selectedProjectId = projectArrayList.get(projectListItemPos).getId();
                    addSite(selectedProjectId, editTxtSiteName.getText().toString(), editTxtSiteDescription.getText().toString());
                    addSiteDialog.dismiss();
                }

            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSiteDialog.dismiss();
            }
        });

        addSiteDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                List<String> projectNames = new ArrayList<>();
                for (Project p : projectArrayList) {
                    projectNames.add(p.getName());
                }

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        projectNames
                );

                spinnerProjects.setAdapter(spinnerAdapter);

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sites_view, container, false);
        sitesLiveData = new MutableLiveData<>();
        projectsLiveData = new MutableLiveData<>();

        projectRepo = new ProjectRepo("Project");
        projectsLiveData = projectRepo.getCollection();
        siteRepo = new SiteRepo("Site");
        sitesLiveData = siteRepo.getCollection();

        sitesRecyclerView = view.findViewById(R.id.sites_recyclerView);
        fabAddSite = view.findViewById(R.id.FAB_Sites_add);

        siteRepo.getAllObjects(new FirebaseCollectionCallback<MutableLiveData<List<Site>>>() {
            @Override
            public void onSuccess(MutableLiveData<List<Site>> result) {
                sitesLiveData = result;
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("FragmentSites", "onFailure: get all sites" + e.toString());

            }
        });
        projectRepo.getAllObjects(new FirebaseCollectionCallback<MutableLiveData<List<Project>>>() {
            @Override
            public void onSuccess(MutableLiveData<List<Project>> result) {
                projectsLiveData = result;
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("FragmentSites", "onFailure: get all projects" + e.toString());

            }
        });

        sitesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        sitesRecyclerView.setHasFixedSize(true);

        sitesLiveData.observe(getViewLifecycleOwner(), sites -> {
            siteArrayList.clear();
            siteArrayList.addAll(sites);
            sitesAdapter.notifyDataSetChanged();
        });

        projectsLiveData.observe(getViewLifecycleOwner(), projects -> {
            projectArrayList.clear();
            projectArrayList.addAll(projects);
            projectsAdapter.notifyDataSetChanged();

        });


        sitesAdapter = new SitesAdapter(siteArrayList, projectArrayList, getContext());
        sitesRecyclerView.setAdapter(sitesAdapter);

        projectsAdapter = new ProjectsAdapter(projectArrayList);

        fabAddSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSiteDialog.show();
            }
        });





        return view;
    }

    private void addSite(Long projectId, String name, String description){
        Site site = new Site(0,name, description, TimeStamp.getTimeStamp(), TimeStamp.getTimeStamp(),projectId,new ArrayList<>(),new HashMap<>());
        siteRepo.insert(site);
    }
}