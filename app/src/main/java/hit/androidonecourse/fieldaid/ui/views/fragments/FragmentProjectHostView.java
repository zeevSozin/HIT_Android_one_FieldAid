package hit.androidonecourse.fieldaid.ui.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hit.androidonecourse.fieldaid.R;


public class FragmentProjectHostView extends Fragment {
    NavController navController;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_project_host_view, container, false);
        navController = NavHostFragment.findNavController(this);


        return view;
    }
    public void navigateToProjectStartDestination(){
//        NavController navController = NavHostFragment.findNavController(this.getChildFragmentManager().findFragmentById(R.id.fragmentContainerViewProjects));
//        NavController navController = NavHostFragment.findNavController(requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerViewProjects));
        navController.popBackStack(R.id.fragmentProjectsView, false);

    }
}