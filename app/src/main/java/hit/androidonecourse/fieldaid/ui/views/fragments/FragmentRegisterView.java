package hit.androidonecourse.fieldaid.ui.views.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.ui.viewmodels.FragmentRegisterViewModel;
import hit.androidonecourse.fieldaid.ui.views.activities.ActivityMainView;


public class FragmentRegisterView extends Fragment {
    private hit.androidonecourse.fieldaid.databinding.FragmentRegisterViewBinding binding;
    FragmentRegisterViewModel viewModel;

    public FragmentRegisterView(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.setContentView(this.getActivity(), R.layout.fragment_register_view);

        viewModel = new ViewModelProvider(this).get(FragmentRegisterViewModel.class);

        binding.setFragmentRegisterViewModel(viewModel);

        viewModel.navigateToMainActivity.observe(getViewLifecycleOwner(), (nav) ->{
            if(nav){
                navigateToMainActivity();
            }
        } );



        binding.setLifecycleOwner(this);

        return inflater.inflate(R.layout.fragment_register_view, container, false);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this.getActivity(), ActivityMainView.class);
        intent.putExtra("userAccountId", viewModel.user.getValue().getUserAccountId());
        startActivity(intent);
        if(this.getActivity()!= null){
            this.getActivity().finish();
        }
    }
}