package hit.androidonecourse.fieldaid.data.DAO;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import hit.androidonecourse.fieldaid.data.repositories.FirebaseCollectionCallback;

public interface DaoBase<T> {
    void insert(T object);
    long insertAndGetId(T object);
    void delete(T object);
    void update(T object);
    void getAllObjects(FirebaseCollectionCallback<MutableLiveData<List<T>>> callback);
    MutableLiveData<List<T>> getCollection();
}
