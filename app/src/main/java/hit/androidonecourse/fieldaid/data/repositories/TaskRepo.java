package hit.androidonecourse.fieldaid.data.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hit.androidonecourse.fieldaid.domain.models.Site;
import hit.androidonecourse.fieldaid.domain.models.Task;

public class TaskRepo extends RepoBase<Task>{
    public TaskRepo(String collectionName) {
        super(collectionName);
        ValueEventListener collectionListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Task> tempCollection = new ArrayList<>();
                for (DataSnapshot entry: snapshot.getChildren()) {
                    Task obj = entry.getValue(Task.class);
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
    public void insert(Task object) {
        long newId = maxId +1;
        object.setId(newId);
        mDatabaseRef.child(String.valueOf(newId)).setValue(object);

    }

    @Override
    public long insertAndGetId(Task object) {
        long newId = maxId +1;
        object.setId(newId);
        mDatabaseRef.child(String.valueOf(newId)).setValue(object);
        return newId;
    }

    @Override
    public void delete(Task object) {
        mDatabaseRef.child(String.valueOf(object.getId())).removeValue();

    }

    @Override
    public void update(Task object) {
        mDatabaseRef.child(String.valueOf(object.getId())).setValue(object);

    }

    @Override
    public void getAllObjects(FirebaseCollectionCallback<MutableLiveData<List<Task>>> callback) {

    }

    @Override
    public MutableLiveData<List<Task>> getCollection() {
        return this.collection;
    }
}
