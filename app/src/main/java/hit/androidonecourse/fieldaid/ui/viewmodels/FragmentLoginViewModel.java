package hit.androidonecourse.fieldaid.ui.viewmodels;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FragmentLoginViewModel extends ViewModel {
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    private MutableLiveData<Boolean> navigateToRegister = new MutableLiveData<>();


    private MutableLiveData<Boolean> navigateToMainActivity = new MutableLiveData<>();
    private FirebaseAuth mAuth;

    public String userAccountId;


    public FragmentLoginViewModel() {
        mAuth = FirebaseAuth.getInstance();

    }

    public LiveData<Boolean> getNavigateToRegister(){
        return navigateToRegister;
    }

    public LiveData<Boolean> getNavigateToMainActivity() {
        return navigateToMainActivity;
    }




    public void navigateToRegister(){
        navigateToRegister.setValue(true);
    }

    public void navigateToMainActivity(){
        navigateToMainActivity.setValue(true);
    }

    public void onLoginClicked(View view){
        mAuth.signInWithEmailAndPassword(email.getValue(), password.getValue())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(view.getContext(), "Successfully logged in", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            userAccountId = firebaseUser.getUid();


                            navigateToMainActivity();


                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(view.getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }






}



