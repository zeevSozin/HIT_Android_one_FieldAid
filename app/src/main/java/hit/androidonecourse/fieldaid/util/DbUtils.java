package hit.androidonecourse.fieldaid.util;

import java.util.ArrayList;
import java.util.List;

public class DbUtils<T> {
    public List<T> removeItemFromList(List<T> collection, T object) {
        List<T> resultCollection = new ArrayList<>();
        if (!collection.isEmpty()) {
            if (collection.contains(object)) {
                collection.remove(object);
                resultCollection = collection;
            }
        }
        return resultCollection;
    }



}
