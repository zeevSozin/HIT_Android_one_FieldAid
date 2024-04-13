package hit.androidonecourse.fieldaid.ui.viewmodels;

import android.app.Application;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.data.repositories.FirebaseCollectionCallback;
import hit.androidonecourse.fieldaid.data.repositories.ProjectRepo;
import hit.androidonecourse.fieldaid.domain.models.Project;

public class FragmentProjectsViewModel extends AndroidViewModel {
    public MutableLiveData<List<Project>> projects = new MutableLiveData<>();
    private ProjectRepo projectRepo ;

    private MutableLiveData<Boolean> addProjectCommand = new MutableLiveData<>();







    public FragmentProjectsViewModel(@NonNull Application application) {
        super(application);
        projectRepo = new ProjectRepo(application.getApplicationContext().getString(R.string.FireBase_Project));
        projectRepo.getAllObjects(new FirebaseCollectionCallback<MutableLiveData<List<Project>>>() {
            @Override
            public void onSuccess(MutableLiveData<List<Project>> result) {
                projects = result;
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(application,"Error" + e.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        // init dialog

    }

    public LiveData<Boolean> getAddProjectCommand(){
        return addProjectCommand;
    }

    public void addProjectCommand(){
        addProjectCommand.setValue(true);
    }

    public void onFABaddClicked(View view){
        addProjectCommand();
    }

    public void addProject(String name, String Description){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String timeStamp = dateFormat.format(Calendar.getInstance().getTime());
        Project projectToAdd = new Project(0, name, Description, timeStamp, timeStamp, new ArrayList<>(),"");
        projectRepo.insert(projectToAdd);

    }






}
