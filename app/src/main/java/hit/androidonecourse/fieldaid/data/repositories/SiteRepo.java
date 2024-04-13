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
import hit.androidonecourse.fieldaid.domain.models.Site;
import hit.androidonecourse.fieldaid.util.TimeStamp;

public class SiteRepo extends RepoBase<Site>{

    public SiteRepo(String collectionName) {
        super(collectionName);
        ValueEventListener collectionListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Site> tempCollection = new ArrayList<>();
                for (DataSnapshot entry: snapshot.getChildren()) {
                    Site obj = entry.getValue(Site.class);
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
    public void insert(Site object) {
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
    public long insertAndGetId(Site object) {
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
    public void delete(Site object) {
        mDatabaseRef.child(String.valueOf(object.getId())).removeValue();

    }

    @Override
    public void update(Site object) {
        object.setUpdateDateTime(TimeStamp.getTimeStamp());
        mDatabaseRef.child(String.valueOf(object.getId())).setValue(object);

    }

    @Override
    public void getAllObjects(FirebaseCollectionCallback<MutableLiveData<List<Site>>> callback) {
        mDatabaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    List<Site> tempCollection = new ArrayList<>();
                    DataSnapshot snapshot = task.getResult();
                    for (DataSnapshot entry: snapshot.getChildren()) {
                        Site site = entry.getValue(Site.class);
                        tempCollection.add(site);
                    }
                    collection.setValue(tempCollection);
                    callback.onSuccess(collection);
                }
            }
        });

    }
    @Override
    public MutableLiveData<List<Site>> getCollection(){
        return this.collection;
    }
}
