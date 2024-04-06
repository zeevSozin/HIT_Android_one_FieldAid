package hit.androidonecourse.fieldaid.data.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hit.androidonecourse.fieldaid.domain.models.UserAccount;

public class UserAccountRepo extends RepoBase<UserAccount>{

    public UserAccountRepo(String collectionName) {
        super(collectionName);
        ValueEventListener collectionListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<UserAccount> tempCollection = new ArrayList<>();
                for (DataSnapshot entry: snapshot.getChildren()) {
                    UserAccount obj = entry.getValue(UserAccount.class);
                    tempCollection.add(obj);
                }
                collection.setValue(tempCollection);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabaseRef.addValueEventListener(collectionListener);


    }

    @Override
    public void insert(UserAccount userAccount) {

        mDatabaseRef.child(String.valueOf(maxId + 1)).setValue(userAccount);

    }

    @Override
    public long insertAndGetId(UserAccount object) {
        long newId = maxId +1;
        mDatabaseRef.child(String.valueOf(newId)).setValue(object);
        return newId;
    }

    @Override
    public void delete(UserAccount userAccount) {

    }

    @Override
    public void update(UserAccount userAccount) {

    }

    @Override
    public void getAllObjects(FirebaseCollectionCallback<MutableLiveData<List<UserAccount>>> callback) {

    }

    @Override
    public MutableLiveData<List<UserAccount>> getCollection() {
        return null;
    }


}
