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

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.data.repositories.FirebaseCollectionCallback;
import hit.androidonecourse.fieldaid.data.repositories.ProjectRepo;
import hit.androidonecourse.fieldaid.data.repositories.SiteRepo;
import hit.androidonecourse.fieldaid.domain.RepositoryMediator;
import hit.androidonecourse.fieldaid.domain.models.CustomLatLng;
import hit.androidonecourse.fieldaid.domain.models.Project;
import hit.androidonecourse.fieldaid.domain.models.Site;
import hit.androidonecourse.fieldaid.ui.adapters.ProjectsAdapter;
import hit.androidonecourse.fieldaid.ui.adapters.RecyclerViewClickListener;
import hit.androidonecourse.fieldaid.ui.adapters.SitesAdapter;
import hit.androidonecourse.fieldaid.util.CustomLocationManager;
import hit.androidonecourse.fieldaid.util.TimeStamp;

public class FragmentSitesView extends Fragment implements RecyclerViewClickListener {
    private RepositoryMediator repositoryMediator;
    private ArrayList<Site> siteArrayList = new ArrayList<>();
    private ArrayList<Project> projectArrayList = new ArrayList<>();

    private RecyclerView sitesRecyclerView;
    private SitesAdapter sitesAdapter;
    private ProjectsAdapter projectsAdapter;
    private FloatingActionButton fabAddSite;

    private boolean isFiltered = false;
    private Project projectFilter;

    CustomLocationManager customLocationManager;

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
        Log.d("FieldAid", "onCreate: Fragment site creating");
        repositoryMediator = repositoryMediator.getInstance(this.getContext());

        if(repositoryMediator.getSitesFilterProject() != null){
            isFiltered = true;
            projectFilter = repositoryMediator.getSitesFilterProject();
        }



        addSiteDialog = new Dialog(this.getContext());
        addSiteDialog.setContentView(R.layout.dialog_add_site);
        addSiteDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addSiteDialog.getWindow().setBackgroundDrawableResource(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark_normal_background);
        addSiteDialog.setCancelable(true);

        spinnerProjects = addSiteDialog.findViewById(R.id.dialog_addSite_spinner);
        editTxtSiteName = addSiteDialog.findViewById(R.id.dialog_addSite_editText_SiteName);
        editTxtSiteDescription = addSiteDialog.findViewById(R.id.dialog_addSite_editText_SiteDescription);
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
                if(!isFiltered){
                    for (Project p : projectArrayList) {
                        projectNames.add(p.getName());
                    }
                }
                else {
                    projectNames.add(projectFilter.getName());
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
        Log.d("FieldAid", "onCreateView: Fragment sites creating");
        View view = inflater.inflate(R.layout.fragment_sites_view, container, false);

        sitesRecyclerView = view.findViewById(R.id.sites_recyclerView);
        fabAddSite = view.findViewById(R.id.FAB_Sites_add);
        customLocationManager = new CustomLocationManager(this.getContext());

        sitesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        sitesRecyclerView.setHasFixedSize(true);



        repositoryMediator.getSiteLiveData().observe(getViewLifecycleOwner(), sites -> {
            siteArrayList.clear();
            if(!isFiltered){
                siteArrayList.addAll(sites);
            }
            else {
                siteArrayList.addAll(sites.stream().filter(s -> s.getProjectId() == projectFilter.getId()).collect(Collectors.toList()));

            }
        });

        repositoryMediator.getProjectLiveData().observe(getViewLifecycleOwner(), projects -> {
            projectArrayList.clear();
            projectArrayList.addAll(projects);
        });


        sitesAdapter = new SitesAdapter(siteArrayList, projectArrayList, getContext(),this);
        sitesRecyclerView.setAdapter(sitesAdapter);

        projectsAdapter = new ProjectsAdapter(projectArrayList, getContext(), new RecyclerViewClickListener() {
            @Override
            public void recyclerViewClickListener(int position) {

            }
        });

        fabAddSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSiteDialog.show();
            }
        });

        return view;
    }

    private void addSite(Long projectId, String name, String description){
        CustomLatLng customLatLng = customLocationManager.getCustomLatLngCurrentLocation();
        Site site = new Site(0,name, description, TimeStamp.getTimeStamp(), TimeStamp.getTimeStamp(),projectId,new ArrayList<>(),customLatLng);
        repositoryMediator.insertSite(site);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("FieldAid", "onPause: Fragment Sites paused");
    }

    @Override
    public void recyclerViewClickListener(int position) {
        repositoryMediator.setCurrentSite(siteArrayList.get(position));
        sitesRecyclerView.setAdapter(sitesAdapter);
        Log.d("FieldAid", "recyclerViewClickListener: Sites the current site is: " + repositoryMediator.getCurrentSite().getName());
    }
}