package hit.androidonecourse.fieldaid.domain;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import hit.androidonecourse.fieldaid.data.repositories.ProjectRepo;
import hit.androidonecourse.fieldaid.data.repositories.SiteRepo;
import hit.androidonecourse.fieldaid.data.repositories.UserAccountRepo;
import hit.androidonecourse.fieldaid.data.repositories.UserRepo;
import hit.androidonecourse.fieldaid.domain.models.Project;
import hit.androidonecourse.fieldaid.domain.models.Site;
import hit.androidonecourse.fieldaid.domain.models.User;
import hit.androidonecourse.fieldaid.domain.models.UserAccount;

public class RepositoryMediator  {
    private static volatile RepositoryMediator instance = null;

    // repos
    private UserAccountRepo userAccountRepo;
    private UserRepo userRepo;
    private ProjectRepo projectRepo;
    private SiteRepo siteRepo;

    //collections
    private MutableLiveData<List<UserAccount>> userAccountLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> userLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Project>> projectLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Site>> siteLiveData = new MutableLiveData<>();

    //current states
    private UserAccount currentUserAccount = new UserAccount();
    private User currentUser = new User();
    private Project currentProject = new Project();
    private Site currentSite = new Site();

    private RepositoryMediator(){



    }

    public static synchronized RepositoryMediator getInstance() {
        if(instance == null){
            instance = new RepositoryMediator();
        }
        return instance;
    }
}
