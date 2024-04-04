package hit.androidonecourse.fieldaid.data.repositories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.data.DAO.UserAccountDAO;
import hit.androidonecourse.fieldaid.domain.models.UserAccount;

public class UserAccountRepo implements UserAccountDAO{
    private DatabaseReference mDatabaseRef;
    private LiveData<UserAccount> userAccounts;

    private long maxId = 0;

    public UserAccountRepo(Application application) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().
                child(application.getApplicationContext().
                        getString(R.string.FireBase_UserAccount));
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


    }

    @Override
    public void insert(UserAccount userAccount) {

        mDatabaseRef.child(String.valueOf(maxId + 1)).setValue(userAccount);


    }

    @Override
    public void delete(UserAccount userAccount) {

    }

    @Override
    public void update(UserAccount userAccount) {

    }

    @Override
    public MutableLiveData<List<UserAccount>> getAllUserAccounts() {
        return null;
    }


}
