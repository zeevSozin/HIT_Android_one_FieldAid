package hit.androidonecourse.fieldaid.data.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hit.androidonecourse.fieldaid.domain.models.Contact;
import hit.androidonecourse.fieldaid.domain.models.Site;

public class ContacRepo extends RepoBase<Contact> {
    public ContacRepo(String collectionName) {
        super(collectionName);
        ValueEventListener collectionListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Contact> tempCollection = new ArrayList<>();
                for (DataSnapshot entry: snapshot.getChildren()) {
                    Contact obj = entry.getValue(Contact.class);
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
    public void insert(Contact object) {
        long newId = maxId +1;
        object.setId(newId);
        mDatabaseRef.child(String.valueOf(newId)).setValue(object);

    }

    @Override
    public long insertAndGetId(Contact object) {
        long newId = maxId +1;
        object.setId(newId);
        mDatabaseRef.child(String.valueOf(newId)).setValue(object);
        return newId;
    }

    @Override
    public void delete(Contact object) {
        mDatabaseRef.child(String.valueOf(object.getId())).removeValue();

    }

    @Override
    public void update(Contact object) {
        mDatabaseRef.child(String.valueOf(object.getId())).setValue(object);

    }

    @Override
    public void getAllObjects(FirebaseCollectionCallback<MutableLiveData<List<Contact>>> callback) {

    }

    @Override
    public MutableLiveData<List<Contact>> getCollection() {
        return this.collection;
    }
}
