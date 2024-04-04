package hit.androidonecourse.fieldaid.ui.views.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.style.LeadingMarginSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.data.repositories.FirebaseCollectionCallback;
import hit.androidonecourse.fieldaid.data.repositories.ProjectRepo;
import hit.androidonecourse.fieldaid.databinding.FragmentProjectsViewBinding;
import hit.androidonecourse.fieldaid.domain.models.Project;
import hit.androidonecourse.fieldaid.ui.adapters.ProjectsAdapter;
import hit.androidonecourse.fieldaid.ui.viewmodels.FragmentProjectsViewModel;
import hit.androidonecourse.fieldaid.util.TimeStamp;


public class FragmentProjectsView extends Fragment {

    private ProjectRepo projectRepo;
    private MutableLiveData<List<Project>> projects;

    private ArrayList<Project> projectArrayList = new ArrayList<>();

    private FloatingActionButton fabAddProject;
    private RecyclerView projectsRecyclerView;
    private ProjectsAdapter projectsAdapter;

    //dialog
    private Dialog addProjectDialog;
    private EditText editTxtProjectName;
    private EditText editTxtProjectDescription;
    private Button btnAddProject;
    private Button btnCancel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        projects = new MutableLiveData<>();
//        projectRepo = new ProjectRepo(this.getString(R.string.FireBase_Project));
//
//        projectRepo.getAllObjects(new FirebaseCollectionCallback<MutableLiveData<List<Project>>>() {
//            @Override
//            public void onSuccess(MutableLiveData<List<Project>> result) {
//                projects = result;
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//
//            }
//        });


        addProjectDialog = new Dialog(this.getContext());
        addProjectDialog.setContentView(R.layout.dialog_add_project);
        addProjectDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addProjectDialog.getWindow().setBackgroundDrawableResource(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark_normal_background);
        addProjectDialog.setCancelable(true);

        editTxtProjectName = addProjectDialog.findViewById(R.id.dialog_addProject_editText_projectName);
        editTxtProjectDescription = addProjectDialog.findViewById(R.id.dialog_addProject_editText_projectDescription);
        btnAddProject = addProjectDialog.findViewById(R.id.dialog_addProject_btn_add);
        btnCancel = addProjectDialog.findViewById(R.id.dialog_addProject_btn_cancel);

        btnAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTxtProjectName.getText().toString().isEmpty()){
                    addProject(editTxtProjectName.getText().toString(), editTxtProjectDescription.getText().toString());
                    addProjectDialog.dismiss();
                }

            }

        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProjectDialog.dismiss();
            }
        });





    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_projects_view, container, false);


        projectRepo = new ProjectRepo(this.getString(R.string.FireBase_Project));

        projects = projectRepo.getCollection();

        projectRepo.getAllObjects(new FirebaseCollectionCallback<MutableLiveData<List<Project>>>() {
            @Override
            public void onSuccess(MutableLiveData<List<Project>> result) {
                projects = result;
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        projectsRecyclerView = view.findViewById(R.id.projects_recyclerView);
        fabAddProject = view.findViewById(R.id.FAB_Projects_add);
        fabAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProjectDialog.show();
            }
        });


        //RecyclerView
        RecyclerView recyclerView = projectsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);

        projects.observe(getViewLifecycleOwner(), projects -> {
            projectArrayList.clear();
            Log.d("TAG", "onChanged: " + projects.toString());
                for (Project p : projects) {
                    Log.d("FieldAid", "onChanged: " + p.getName());
                    projectArrayList.add(p);

                }
                projectsAdapter.notifyDataSetChanged();


        });

        projectsAdapter = new ProjectsAdapter(projectArrayList);

        projectsRecyclerView.setAdapter(projectsAdapter);


        return view;
    }

    private void addProject(String name, String description) {
        Project project = new Project(0,name,description, TimeStamp.getTimeStamp(),TimeStamp.getTimeStamp(),new ArrayList<>());
        projectRepo.insert(project);
    }
}