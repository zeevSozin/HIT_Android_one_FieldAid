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

import hit.androidonecourse.fieldaid.domain.models.Project;
import hit.androidonecourse.fieldaid.util.TimeStamp;

public class ProjectRepo extends RepoBase<Project>{


    public ProjectRepo(String collectionName) {
        super(collectionName);

        ValueEventListener collectionListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Project> tempCollection = new ArrayList<>();
                for (DataSnapshot entry: snapshot.getChildren()) {
                    Project obj = entry.getValue(Project.class);
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
    public void insert(Project object) {
        if(object.getContactIds() == null || object.getContactIds().isEmpty()){
            List<Long> tempList = new ArrayList<>();
            tempList.add(0L);
            object.setContactIds(tempList);
        }
        long newId = maxId +1;
        object.setId(newId);
        mDatabaseRef.child(String.valueOf(newId)).setValue(object);
    }

    @Override
    public long insertAndGetId(Project object) {
        if(object.getContactIds() == null || object.getContactIds().isEmpty()){
            List<Long> tempList = new ArrayList<>();
            tempList.add(0L);
            object.setContactIds(tempList);
        }
        long newId = maxId +1;
        object.setId(newId);
        mDatabaseRef.child(String.valueOf(newId)).setValue(object);
        return newId;
    }

    @Override
    public void delete(Project object) {
        mDatabaseRef.child(String.valueOf(object.getId())).removeValue();

    }

    @Override
    public void update(Project object) {
        object.setUpdateDateTime(TimeStamp.getTimeStamp());
        mDatabaseRef.child(String.valueOf(object.getId())).setValue(object);

    }

    @Override
    public void getAllObjects(FirebaseCollectionCallback<MutableLiveData<List<Project>>> callback) {
        mDatabaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    List<Project> tempCollection = new ArrayList<>();
                    DataSnapshot snapshot = task.getResult();
                    for (DataSnapshot entry: snapshot.getChildren()) {
                        Project project = entry.getValue(Project.class);
                        tempCollection.add(project);
                    }
                    collection.setValue(tempCollection);
                    callback.onSuccess(collection);
                }
            }
        });

    }
    @Override

    public MutableLiveData<List<Project>> getCollection(){
        return this.collection;
    }


}
