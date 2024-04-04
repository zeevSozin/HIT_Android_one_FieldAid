package hit.androidonecourse.fieldaid.data.repositories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.data.DAO.UserDAO;
import hit.androidonecourse.fieldaid.domain.models.User;
import hit.androidonecourse.fieldaid.domain.models.UserAccount;

public class UserRepo  implements UserDAO {
    private DatabaseReference mDatabaseRef;
    private MutableLiveData<List<User>> users = new MutableLiveData<>();

    private long maxId = 0;

    public UserRepo(Application application) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().
                child(application.getApplicationContext().
                        getString(R.string.FireBase_User));
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxId = snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ValueEventListener usersListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> tempUsers = new ArrayList<>();
                for (DataSnapshot entry: snapshot.getChildren()) {
                    tempUsers.add(entry.getValue(User.class));
                }
                users.setValue(tempUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabaseRef.addValueEventListener(usersListener);

    }

    @Override
    public void insert(User user) {
        long newId = maxId +1;
        user.setId(newId);
        mDatabaseRef.child(String.valueOf(newId)).setValue(user);

    }

    @Override
    public void delete(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public MutableLiveData<List<User>> getAllUsers() {
        getAllUsersFirebase(new UserRepoCallback<MutableLiveData<List<User>>>() {
            @Override
            public void onSuccess(MutableLiveData<List<User>> result) {
                users = result;
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        return users;
    }

    public void getAllUsersFirebase(UserRepoCallback<MutableLiveData<List<User>>> callback){
        mDatabaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    List<User> tempUsers = new ArrayList<>();
                    DataSnapshot snapshot =  task.getResult();
                    for (DataSnapshot entry: snapshot.getChildren()) {
                        User user = entry.getValue(User.class);
                        tempUsers.add(user);
                    }
                    users.setValue(tempUsers);
                    callback.onSuccess(users);
                }
            }
        });

    }

    public interface UserRepoCallback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }






}
