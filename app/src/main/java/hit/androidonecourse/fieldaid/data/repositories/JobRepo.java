package hit.androidonecourse.fieldaid.data.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hit.androidonecourse.fieldaid.domain.models.Job;
import hit.androidonecourse.fieldaid.domain.models.Site;
import hit.androidonecourse.fieldaid.util.TimeStamp;

public class JobRepo extends RepoBase<Job>{
    public JobRepo(String collectionName) {
        super(collectionName);
        ValueEventListener collectionListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Job> tempCollection = new ArrayList<>();
                for (DataSnapshot entry: snapshot.getChildren()) {
                    Job obj = entry.getValue(Job.class);
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
    public void insert(Job object) {
        if(object.getTaskIds() == null || object.getTaskIds().isEmpty()){
            List<Long> tempList = new ArrayList<>();
            tempList.add(0L);
            object.setTaskIds(tempList);
        }
        long newId = maxId +1;
        object.setId(newId);
        mDatabaseRef.child(String.valueOf(newId)).setValue(object);

    }

    @Override
    public long insertAndGetId(Job object) {
        if(object.getTaskIds() == null || object.getTaskIds().isEmpty()){
            List<Long> tempList = new ArrayList<>();
            tempList.add(0L);
            object.setTaskIds(tempList);
        }
        long newId = maxId +1;
        object.setId(newId);
        mDatabaseRef.child(String.valueOf(newId)).setValue(object);
        return newId;
    }

    @Override
    public void delete(Job object) {
        mDatabaseRef.child(String.valueOf(object.getId())).removeValue();
    }

    @Override
    public void update(Job object) {
        object.setUpdateDateTime(TimeStamp.getTimeStamp());
        mDatabaseRef.child(String.valueOf(object.getId())).setValue(object);

    }

    @Override
    public void getAllObjects(FirebaseCollectionCallback<MutableLiveData<List<Job>>> callback) {

    }

    @Override
    public MutableLiveData<List<Job>> getCollection() {
        return this.collection;
    }
}
