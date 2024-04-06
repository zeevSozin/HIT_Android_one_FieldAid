package hit.androidonecourse.fieldaid.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import hit.androidonecourse.fieldaid.data.repositories.ProjectRepo;
import hit.androidonecourse.fieldaid.data.repositories.SiteRepo;
import hit.androidonecourse.fieldaid.domain.models.Project;
import hit.androidonecourse.fieldaid.domain.models.Site;

public class FragmentSitesViewModel extends AndroidViewModel {

    public MutableLiveData<List<Site>> sites = new MutableLiveData<>();
    public MutableLiveData<List<Project>> Projects = new MutableLiveData<>();

    private ProjectRepo projectRepo;
    private SiteRepo sitesRepo;

    public FragmentSitesViewModel(@NonNull Application application) {
        super(application);
    }
}
