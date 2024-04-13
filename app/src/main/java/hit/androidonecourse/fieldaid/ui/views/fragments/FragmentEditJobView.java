package hit.androidonecourse.fieldaid.ui.views.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.domain.RepositoryMediator;
import hit.androidonecourse.fieldaid.domain.models.EntityBase;
import hit.androidonecourse.fieldaid.domain.models.Job;
import hit.androidonecourse.fieldaid.domain.models.Site;
import hit.androidonecourse.fieldaid.util.ListUtil;


public class FragmentEditJobView extends Fragment {
    private RepositoryMediator repositoryMediator;
    private List<Site> sites;
    private List<Site> orderedSites;
    private Job currentJob;
    private Site selectedSite;
    private Spinner spinnerJobs;
    private EditText editTextJobName;
    private EditText editTextJobDescription;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button buttonCancel;
    private Button buttonSubmit;
    private ImageButton imageButtonDeleteJob;


    // date
    private int dateDay;
    private int dateMonth;
    private int dateYear;
    private int dateHour;
    private int dateMinute;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_job_view, container, false);
        repositoryMediator = RepositoryMediator.getInstance(this.getContext());
        sites = repositoryMediator.getSiteLiveData().getValue();
        currentJob = repositoryMediator.getCurrentJob();
        Optional<Site> optionalSite = sites.stream().filter(s -> s.getId() == currentJob.getId()).findFirst();
        selectedSite = optionalSite.orElse(null);

        spinnerJobs = view.findViewById(R.id.fragment_editJob_spinner);
        editTextJobName = view.findViewById(R.id.editText_name_job_edit_fragment);
        editTextJobDescription = view.findViewById(R.id.editText_description_job_edit_fragment);
        datePicker = view.findViewById(R.id.date_picker_edit_job);
        timePicker = view.findViewById(R.id.time_picker_edit_job);
        buttonCancel = view.findViewById(R.id.btn_Job_edit_cancel);
        buttonSubmit = view.findViewById(R.id.btn_Job_edit_submit);
        imageButtonDeleteJob = view.findViewById(R.id.imageBtn_delete_job_edit_job_fragment);

        // spinner

        ListUtil<Site> sileListUtil = new ListUtil<>();
        orderedSites = sileListUtil.setFirst(sites, selectedSite);
        List<String> orderedSiteName = orderedSites.stream().map(EntityBase::getName).collect(Collectors.toList());

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                orderedSiteName
        );
        spinnerJobs.setAdapter(spinnerAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    dateYear = year;
                    dateMonth = monthOfYear;
                    dateDay = dayOfMonth;
                }
            });
        }

        editTextJobName.setText(currentJob.getName());
        editTextJobDescription.setText(currentJob.getDescription());

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                dateHour = hourOfDay;
                dateMinute = minute;
            }
        });

        buttonSubmit.setOnClickListener(v -> submitChanges());
        buttonCancel.setOnClickListener(v -> cancelChanges());
        imageButtonDeleteJob.setOnClickListener(v -> deleteJob());


        return view;
    }



    private void cancelChanges() {
        navigateToJobs();
    }

    private void submitChanges() {
        if(!editTextJobName.getText().toString().isEmpty()){
            int siteListItemPos = spinnerJobs.getSelectedItemPosition();
            long selectedSiteId = orderedSites.get(siteListItemPos).getId();
            Calendar calendar = Calendar.getInstance();
            calendar.set(dateYear, dateMonth, dateDay, dateHour, dateMinute);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String dueDateString = dateFormat.format(calendar.getTime());
            currentJob.setSitetId(selectedSiteId);
            currentJob.setName(editTextJobName.getText().toString());
            currentJob.setDescription(editTextJobDescription.getText().toString());
            currentJob.setDueDateTime(dueDateString);
            repositoryMediator.updateJob(currentJob);
        }
        navigateToJobs();
    }

    private void deleteJob() {
        repositoryMediator.deleteJob();

        navigateToJobs();
    }
    private void navigateToJobs(){
        Navigation.findNavController(this.getView()).navigate(R.id.action_fragmentEditJobView_to_fragmentJobsView);
    }
}