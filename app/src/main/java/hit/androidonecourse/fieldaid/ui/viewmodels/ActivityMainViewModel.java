package hit.androidonecourse.fieldaid.ui.viewmodels;

import android.app.Application;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.EventListener;
import java.util.List;
import java.util.Objects;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.data.repositories.FirebaseCollectionCallback;
import hit.androidonecourse.fieldaid.data.repositories.UserAccountRepo;
import hit.androidonecourse.fieldaid.data.repositories.UserRepo;
import hit.androidonecourse.fieldaid.domain.models.User;
import hit.androidonecourse.fieldaid.domain.models.UserAccount;

public class ActivityMainViewModel extends AndroidViewModel {
    public MutableLiveData<String> userAccountId = new MutableLiveData<>();
    private User loggedInUser;
    public MutableLiveData<String> loggedInUserFirstName = new MutableLiveData<>();
    public MutableLiveData<String> currentMenuItem = new MutableLiveData<>();
    private MutableLiveData<List<UserAccount>> userAccounts = new MutableLiveData<>();
    private MutableLiveData<List<User>> users = new MutableLiveData<>();

    private UserAccountRepo userAccountRepo;
    private UserRepo userRepo;

    private EventListener UserListListener;


    public ActivityMainViewModel(@NonNull Application application) {
        super(application);

        userAccountRepo = new UserAccountRepo(application.getApplicationContext().getString(R.string.FireBase_UserAccount));
        userRepo =new UserRepo("User");

        userAccounts = userAccountRepo.getCollection();
        users = userRepo.getCollection();


        userRepo.getAllObjects(new FirebaseCollectionCallback<MutableLiveData<List<User>>>() {
            @Override
            public void onSuccess(MutableLiveData<List<User>> result) {
                users = result;
                setLoggedInUserByAccountId(userAccountId.getValue(), users.getValue());
                Toast.makeText(application, "Callback"+ users.getValue(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

    }


    private void setLoggedInUserByAccountId(String accountId, List<User> users){
        this.loggedInUser =  users.stream().filter(u -> Objects.equals(u.getUserAccountId(), accountId)).findFirst().orElse(null);
        this.loggedInUserFirstName.setValue(loggedInUser.getFirstName());
    }

    public void changeFragment(View view){
        Toast.makeText(getApplication(), "Item is:"+ view.toString(), Toast.LENGTH_SHORT).show();
    }




}
