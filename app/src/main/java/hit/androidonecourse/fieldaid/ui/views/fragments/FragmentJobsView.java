package hit.androidonecourse.fieldaid.ui.views.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.domain.RepositoryMediator;
import hit.androidonecourse.fieldaid.domain.models.Job;
import hit.androidonecourse.fieldaid.domain.models.Project;
import hit.androidonecourse.fieldaid.domain.models.Site;
import hit.androidonecourse.fieldaid.ui.adapters.JobsAdapter;
import hit.androidonecourse.fieldaid.ui.adapters.RecyclerViewClickListener;
import hit.androidonecourse.fieldaid.ui.adapters.SitesAdapter;
import hit.androidonecourse.fieldaid.util.TimeStamp;


public class FragmentJobsView extends Fragment implements RecyclerViewClickListener {
    private RepositoryMediator repositoryMediator;
    private ArrayList<Job> jobArrayList = new ArrayList<>();
    private ArrayList<Site> siteArrayList = new ArrayList<>();
    private TextView textViewJobTitleFilter;

    private RecyclerView jobsRecyclerView;

    private JobsAdapter jobsAdapter;
    private SitesAdapter sitesAdapter;
    private FloatingActionButton fabAddJob;
    private boolean isFiltered = false;
    private Site siteFilter;

    // Dialog fields
    private Dialog addJobDialog;
    private Spinner spinnerSites;
    private EditText editTextJobName;
    private EditText editTextJobDescription;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button buttonAddJob;
    private Button buttonCancel;
    private Calendar calendar;

    //date
    private int dateDay;
    private int dateMonth;
    private int dateYear;
    private int dateHour;
    private int dateMinute;







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jobs_view, container, false);

        repositoryMediator = RepositoryMediator.getInstance(this.getContext());
        calendar = Calendar.getInstance();




        if(repositoryMediator.getJobFilterBySite() != null){
            isFiltered = true;
            siteFilter = repositoryMediator.getJobFilterBySite();
        }

        textViewJobTitleFilter = view.findViewById(R.id.textView_title_fragment_jobs);
        textViewJobTitleFilter.setText(isFiltered? siteFilter.getName() + "'s Jobs ": "All Jobs");

        jobsRecyclerView = view.findViewById(R.id.jobs_recycler_view);
        fabAddJob = view.findViewById(R.id.FAB_Job_add);





        // Dialog
        addJobDialog = new Dialog(this.getContext());
        addJobDialog.setContentView(R.layout.dialog_add_job);
        addJobDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addJobDialog.getWindow().setBackgroundDrawableResource(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark_normal_background);
        addJobDialog.setCancelable(true);

        spinnerSites = addJobDialog.findViewById(R.id.dialog_addJob_spinner);
        editTextJobName = addJobDialog.findViewById(R.id.dialog_addJob_editText_JobName);
        editTextJobDescription = addJobDialog.findViewById(R.id.dialog_addJob_editText_JobDescription);
        datePicker = addJobDialog.findViewById(R.id.dialog_addJob_datePicker);
        timePicker = addJobDialog.findViewById(R.id.dialog_addJob_timePicker);
        buttonCancel = addJobDialog.findViewById(R.id.dialog_addJob_btn_cancel);
        buttonAddJob = addJobDialog.findViewById(R.id.dialog_addJob_btn_add);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    dateDay = dayOfMonth;
                    dateMonth = monthOfYear;
                    dateYear = year;
                }
            });
        }

        timePicker.setOnTimeChangedListener((view1, hourOfDay, minute) -> {
            dateHour = hourOfDay;
            dateMinute = minute;
        });


        buttonAddJob.setOnClickListener(v -> {
            if(!editTextJobName.getText().toString().isEmpty()){
                int siteListItemPos = spinnerSites.getSelectedItemPosition();
                long selectedSiteId = siteArrayList.get(siteListItemPos).getId();
                calendar.set(dateYear,dateMonth,dateDay,dateHour,dateMinute);
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String dueDateString = dateFormat.format(calendar.getTime());
                Log.d("FieldAid", "onClick: calendar time: " + dueDateString);
                addJob(selectedSiteId,editTextJobName.getText().toString(),editTextJobDescription.getText().toString(),dueDateString);
                addJobDialog.dismiss();
            }
        });
        buttonCancel.setOnClickListener(v -> addJobDialog.dismiss());

        addJobDialog.setOnShowListener(dialog -> {
            List<String> siteNames = new ArrayList<>();
            if(!isFiltered){
                for (Site s : siteArrayList) {
                    siteNames.add(s.getName());
                }
            }
            else {
                siteNames.add(siteFilter.getName());
            }
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    siteNames
            );
            spinnerSites.setAdapter(spinnerAdapter);
        });


        // Fragment

        jobsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        jobsRecyclerView.setHasFixedSize(true);

        repositoryMediator.getJobLiveData().observe(getViewLifecycleOwner(), jobs -> {
            jobArrayList.clear();
            if(!isFiltered){
                jobArrayList.addAll(jobs);
                jobsAdapter.setJobs(jobArrayList);
            }
            else{
                jobArrayList.addAll(jobs.stream().filter(j -> j.getSiteId() == siteFilter.getId()).collect(Collectors.toList()));
            }
        });

        repositoryMediator.getSiteLiveData().observe(getViewLifecycleOwner(), sites -> {
            siteArrayList.clear();
            siteArrayList.addAll(sites);

        });

        jobsAdapter = new JobsAdapter(jobArrayList, siteArrayList, getContext(),this );
        jobsRecyclerView.setAdapter(jobsAdapter);


        fabAddJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addJobDialog.show();
            }
        });



        return view;
    }

    private void addJob(Long siteId, String jobName, String jobDescription, String dueDate){

        Job job = new Job(0,jobName,jobDescription, TimeStamp.getTimeStamp(),TimeStamp.getTimeStamp(),siteId,"new",new ArrayList<Long>(),dueDate);
        repositoryMediator.insertJob(job);
    }

    @Override
    public void recyclerViewClickListener(int position) {
        repositoryMediator.setCurrentJob(jobArrayList.get(position));
        jobsRecyclerView.setAdapter(jobsAdapter);

    }
}