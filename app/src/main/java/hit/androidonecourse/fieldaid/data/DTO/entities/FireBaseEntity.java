package hit.androidonecourse.fieldaid.data.DTO.entities;

public abstract class FireBaseEntity<T> {
    private long id;
    private T object;

    public FireBaseEntity() {
    }

    public FireBaseEntity(long id, T object) {
        this.id = id;
        this.object = object;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
