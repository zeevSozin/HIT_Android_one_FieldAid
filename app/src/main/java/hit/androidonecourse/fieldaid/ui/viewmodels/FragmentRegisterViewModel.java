package hit.androidonecourse.fieldaid.ui.viewmodels;

import android.app.Application;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.data.repositories.UserAccountRepo;
import hit.androidonecourse.fieldaid.data.repositories.UserRepo;
import hit.androidonecourse.fieldaid.domain.models.User;
import hit.androidonecourse.fieldaid.domain.models.UserAccount;

public class FragmentRegisterViewModel extends AndroidViewModel {

    public MutableLiveData<User> user = new MutableLiveData<>();
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> confirmPassword = new MutableLiveData<>();

    public MutableLiveData<Boolean> navigateToMainActivity = new MutableLiveData<>();


    private FirebaseAuth mAuth;

    private UserAccountRepo userAccountRepo;
    private UserRepo userRepo;

    public FragmentRegisterViewModel(@NonNull Application application) {
        super(application);
        user.setValue(new User());
        mAuth = FirebaseAuth.getInstance();
        userAccountRepo = new UserAccountRepo(application.getApplicationContext().getString(R.string.FireBase_UserAccount));
        userRepo = new UserRepo(application.getApplicationContext().getString(R.string.FireBase_User));
    }


    public LiveData<Boolean> getNavigateToMainActivity(){
        return navigateToMainActivity;
    }

    public void navigateToMainActivity(){
        navigateToMainActivity.setValue(true);
    }

    public void onRegisterClicked(View view) {
        Log.d("zeev", "onRegisterClicked: with email:"+ email.getValue().toString() + "And password of:" + password.getValue().toString());
        if (confirmPassword.getValue().toString().equals(password.getValue().toString())) {
            mAuth.createUserWithEmailAndPassword(email.getValue(), password.getValue())
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(view.getContext(), "Sucessfully Registerd" + email.getValue(), Toast.LENGTH_SHORT).show();
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                user.getValue().setUserAccountId(firebaseUser.getUid());
                                userAccountRepo.insert(new UserAccount(user.getValue().getUserAccountId(), email.getValue()));
                                userRepo.insert(user.getValue());
                                navigateToMainActivity();

                            } else {
                                Toast.makeText(view.getContext(), "Failed Registerd user", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }
}
