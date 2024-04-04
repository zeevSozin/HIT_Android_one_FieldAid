package hit.androidonecourse.fieldaid.data.repositories;

public interface FirebaseCollectionCallback<T> {
    void onSuccess(T result);
    void onFailure(Exception e);
}
