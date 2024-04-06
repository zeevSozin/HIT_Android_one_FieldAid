package hit.androidonecourse.fieldaid.data.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hit.androidonecourse.fieldaid.domain.models.User;

public class UserRepo  extends RepoBase<User> {


    public UserRepo(String collectionName) {
        super(collectionName);
        ValueEventListener collectionListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> tempCollection = new ArrayList<>();
                for (DataSnapshot entry: snapshot.getChildren()) {
                    User obj = entry.getValue(User.class);
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
    public void insert(User user) {
        long newId = maxId +1;
        user.setId(newId);
        mDatabaseRef.child(String.valueOf(newId)).setValue(user);

    }

    @Override
    public long insertAndGetId(User object) {
        long newId = maxId +1;
        object.setId(newId);
        mDatabaseRef.child(String.valueOf(newId)).setValue(object);
        return newId;
    }

    @Override
    public void delete(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void getAllObjects(FirebaseCollectionCallback<MutableLiveData<List<User>>> callback) {
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
                    collection.setValue(tempUsers);
                    callback.onSuccess(collection);
                }
            }
        });

    }
    @Override
    public MutableLiveData<List<User>> getCollection() {
        return collection;
    }







}
