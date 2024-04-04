package hit.androidonecourse.fieldaid.data.DAO;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import hit.androidonecourse.fieldaid.domain.models.User;

public interface UserDAO {

    void insert(User user);
    void delete(User user);
    void update(User user);
    MutableLiveData<List<User>> getAllUsers();
}
