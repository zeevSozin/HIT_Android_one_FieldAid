package hit.androidonecourse.fieldaid;

import android.app.Application;

import org.junit.Test;

import hit.androidonecourse.fieldaid.data.repositories.UserAccountRepo;
import hit.androidonecourse.fieldaid.domain.models.UserAccount;

public class FireBaseUserAccountRepoUnitTest {
    Application app = new Application();
    @Test
    public void insertUserAccountToDB(){
        UserAccount userAccount = new UserAccount("testingString","zeev@text.com");
        UserAccountRepo userAccountRepo = new UserAccountRepo("UserAccount");
        userAccountRepo.insert(userAccount);

    }

}
