package hit.androidonecourse.fieldaid.ui.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.domain.RepositoryMediator;


public class FragmentSiteHostView extends Fragment {
    RepositoryMediator repositoryMediator;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_site_host_view, container, false);
        repositoryMediator = RepositoryMediator.getInstance(this.getContext());
        repositoryMediator.setSiteFilterByProject(null);
        return view;
    }
}