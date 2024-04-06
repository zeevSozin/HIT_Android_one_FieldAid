package hit.androidonecourse.fieldaid.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtil<T> {

    public List<T> setFirst(List<T> collection, T object){
        if(collection.contains(object)){
            List<T> resultList = new ArrayList<>();
            resultList.add(object);
            for (T item: collection) {
                if(item!= object){
                    resultList.add(item);
                }
            }
            return resultList;
        }
        return collection;
    }
}
