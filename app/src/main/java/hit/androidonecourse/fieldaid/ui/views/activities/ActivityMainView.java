package hit.androidonecourse.fieldaid.ui.views.activities;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.databinding.ActivityMainViewBinding;
import hit.androidonecourse.fieldaid.domain.models.Site;
import hit.androidonecourse.fieldaid.ui.viewmodels.ActivityMainViewModel;

public class ActivityMainView extends AppCompatActivity {

    private ActivityMainViewBinding binding;
    ActivityMainViewModel viewModel;

    Site selectedSiteItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainViewBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_projects, R.id.navigation_sites, R.id.navigation_jobs)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupActionBarWithNavController(this, navController);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public void setSelectedSiteItem(Site currentSite){
        selectedSiteItem = currentSite;
    }

}