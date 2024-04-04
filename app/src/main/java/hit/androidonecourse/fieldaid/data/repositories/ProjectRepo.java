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
        long newId = maxId +1;
        object.setId(newId);
        mDatabaseRef.child(String.valueOf(newId)).setValue(object);
    }

    @Override
    public void delete(Project object) {
        mDatabaseRef.child(String.valueOf(object.getId())).removeValue();

    }

    @Override
    public void update(Project object) {
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

    public MutableLiveData<List<Project>> getCollection(){
        return this.collection;
    }


}
