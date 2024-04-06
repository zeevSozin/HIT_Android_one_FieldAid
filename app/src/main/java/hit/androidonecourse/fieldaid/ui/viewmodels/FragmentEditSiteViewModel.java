package hit.androidonecourse.fieldaid.ui.viewmodels;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FragmentEditSiteViewModel extends AndroidViewModel {
    public MutableLiveData<Boolean> navigateToSites = new MutableLiveData<>();
    public FragmentEditSiteViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<Boolean> getNavigateToSites(){
        return navigateToSites;
    }

    public void navigateToSites(){
        navigateToSites.setValue(true);
    }

    public void onBtnSubmitClicked(View view){
        navigateToSites();
    }
}
