package hit.androidonecourse.fieldaid.data.repositories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hit.androidonecourse.fieldaid.data.DAO.DaoBase;
import hit.androidonecourse.fieldaid.domain.models.User;

public abstract class RepoBase<T> implements DaoBase<T> {
    protected DatabaseReference mDatabaseRef;
    public MutableLiveData<List<T>> collection = new MutableLiveData<>();
    protected long maxId = 0;

    public RepoBase(String collectionName) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child(collectionName);
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
}
