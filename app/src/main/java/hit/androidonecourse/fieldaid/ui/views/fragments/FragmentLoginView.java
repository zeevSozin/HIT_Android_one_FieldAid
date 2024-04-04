package hit.androidonecourse.fieldaid.ui.views.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.databinding.FragmentLoginViewBinding;
import hit.androidonecourse.fieldaid.ui.viewmodels.FragmentLoginViewModel;
import hit.androidonecourse.fieldaid.ui.views.activities.ActivityMainView;


public class FragmentLoginView extends Fragment {
    private FragmentLoginViewBinding binding;
    FragmentLoginViewModel viewModel;



    public FragmentLoginView() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_view, container, false);


        binding = DataBindingUtil.setContentView(this.getActivity(), R.layout.fragment_login_view);

        viewModel = new ViewModelProvider(this).get(FragmentLoginViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.getNavigateToRegister().observe(getViewLifecycleOwner(),nav -> {
            if(nav){
                navigateToRegister();
            }
        });

        viewModel.getNavigateToMainActivity().observe(getViewLifecycleOwner(), nav ->{
            if(nav){
                navigateToMainActivity();
            }
        });

        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void navigateToRegister(){
        Navigation.findNavController(this.getView()).navigate(R.id.action_fragmentLoginView_to_fragmentRegisterView);


    }

    public void navigateToMainActivity(){
        Intent intent = new Intent(this.getActivity(), ActivityMainView.class);
        intent.putExtra("userAccountId", viewModel.userAccountId);
        startActivity(intent);

    }








}