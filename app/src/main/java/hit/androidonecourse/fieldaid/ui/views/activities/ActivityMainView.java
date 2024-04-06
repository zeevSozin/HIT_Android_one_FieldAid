package hit.androidonecourse.fieldaid.ui.views.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.databinding.ActivityMainViewBinding;
import hit.androidonecourse.fieldaid.domain.RepositoryMediator;
import hit.androidonecourse.fieldaid.domain.models.Site;
import hit.androidonecourse.fieldaid.ui.viewmodels.ActivityMainViewModel;
import hit.androidonecourse.fieldaid.ui.views.fragments.FragmentProjectHostView;

public class ActivityMainView extends AppCompatActivity {

    private ActivityMainViewBinding binding;

    private RepositoryMediator repositoryMediator;





    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        repositoryMediator = repositoryMediator.getInstance(this);

        repositoryMediator.getIsUserCollectionReady().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    repositoryMediator.setCurrentUserAccountId(getIntent().getExtras().get("userAccountId").toString());
                    actionBar.setSubtitle(repositoryMediator.getCurrentUserName());
                }
            }
        });



        binding = ActivityMainViewBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_status, R.id.navigation_projects, R.id.navigation_sites, R.id.navigation_jobs)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerViewMain);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupActionBarWithNavController(this, navController);
        NavigationUI.setupWithNavController(binding.navView, navController);

//       navView.setOnItemSelectedListener(menuItem -> {
//           if (menuItem.getItemId() == R.id.navigation_projects) {
//              Fragment projectHostFragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout_project_host_view);
//               if(projectHostFragment instanceof  FragmentProjectHostView){
//                   ((FragmentProjectHostView)projectHostFragment).navigateToProjectStartDestination();
//               }
//
//           }
//           return NavigationUI.onNavDestinationSelected(menuItem, navController);
//       });
    }

}