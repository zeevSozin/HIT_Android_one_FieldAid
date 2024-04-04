package hit.androidonecourse.fieldaid.data.DAO;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import java.util.List;

import hit.androidonecourse.fieldaid.domain.models.UserAccount;


public interface UserAccountDAO {

    void insert(UserAccount userAccount);

    void delete(UserAccount userAccount);


    void update(UserAccount userAccount);

    MutableLiveData<List<UserAccount>> getAllUserAccounts();

}
