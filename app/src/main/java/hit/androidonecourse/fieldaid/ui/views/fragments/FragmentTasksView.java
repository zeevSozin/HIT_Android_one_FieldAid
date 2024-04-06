package hit.androidonecourse.fieldaid.ui.views.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.domain.RepositoryMediator;
import hit.androidonecourse.fieldaid.domain.models.Job;
import hit.androidonecourse.fieldaid.domain.models.Task;
import hit.androidonecourse.fieldaid.ui.adapters.RecyclerViewClickListener;
import hit.androidonecourse.fieldaid.ui.adapters.TasksAdapter;
import hit.androidonecourse.fieldaid.util.TimeStamp;

public class FragmentTasksView extends Fragment implements RecyclerViewClickListener {
    private RepositoryMediator repositoryMediator;
    private ArrayList<Task> taskArrayList = new ArrayList<>();
    private TextView textViewTaskTitleFilter;

    private RecyclerView tasksRecyclerView;

    private TasksAdapter tasksAdapter;
    private FloatingActionButton fabAddTask;
    private FloatingActionButton fabBack;

    private boolean isFiltered = false;
    private Job jobFilter;

    // Dialog
    private Dialog addTaskDialog;
    private EditText editTextTaskName;
    private EditText editTextTaskDescription;
    private Button btnAddTask;
    private Button btnCancel;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tasks_view, container, false);

        repositoryMediator = RepositoryMediator.getInstance(this.getContext());


        if(repositoryMediator.getTaskFiltetByJob() != null){
            isFiltered = true;
            jobFilter = repositoryMediator.getTaskFiltetByJob();
        }

        textViewTaskTitleFilter = view.findViewById(R.id.textView_title_fragment_tasks);
        textViewTaskTitleFilter.setText(isFiltered? jobFilter.getName() + "'s Tasks" : "All tasks");



        //dialog

        addTaskDialog = new Dialog(this.getContext());
        addTaskDialog.setContentView(R.layout.dialog_add_task);
        addTaskDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addTaskDialog.getWindow().setBackgroundDrawableResource(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark_normal_background);
        addTaskDialog.setCancelable(true);

        editTextTaskName = addTaskDialog.findViewById(R.id.dialog_addTask_editText_TaskName);
        editTextTaskDescription = addTaskDialog.findViewById(R.id.dialog_addTask_editText_TaskDescription);
        btnAddTask = addTaskDialog.findViewById(R.id.dialog_addTask_btn_add);
        btnCancel = addTaskDialog.findViewById(R.id.dialog_addTask_btn_cancel);

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextTaskName.getText().toString().isEmpty()){
                    addTask(editTextTaskName.getText().toString(), editTextTaskDescription.getText().toString());
                    addTaskDialog.dismiss();
                }

            }

        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskDialog.dismiss();
            }
        });

        //fragment

        tasksRecyclerView = view.findViewById(R.id.tasks_recyclerView);
        fabAddTask = view.findViewById(R.id.FAB_Task_add);
        fabBack = view.findViewById(R.id.FAB_Task_back);

        fabAddTask.setOnClickListener(v -> addTaskDialog.show());
        fabBack.setOnClickListener(v -> navigateBackToJobs());

        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        tasksRecyclerView.setHasFixedSize(true);

        repositoryMediator.getTaskLiveData().observe(getViewLifecycleOwner(), tasks -> {
            taskArrayList.clear();
            List<Task> filteredTaskList = tasks.stream().filter(t -> t.getJobId() == jobFilter.getId()).collect(Collectors.toList());
            taskArrayList.addAll(filteredTaskList);
            tasksAdapter.setTasks(taskArrayList);
        });

        tasksAdapter = new TasksAdapter(taskArrayList, getContext(), this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        return view;
    }



    private void addTask(String name, String description){
        long jobId = repositoryMediator.getCurrentJob().getId();
        Task task = new Task(0,name,description, TimeStamp.getTimeStamp(), TimeStamp.getTimeStamp(),jobId);
        repositoryMediator.insertTask(task);

    }
    private void navigateBackToJobs() {
        String origin = repositoryMediator.getNavigationOriginToTasks();
        if(origin.equals("jobs")){
            Navigation.findNavController(this.getView()).navigate(R.id.action_fragmentTasksView_to_fragmentJobsView);
        } else if (origin.equals("status")) {
            Navigation.findNavController(this.getView()).navigate(R.id.action_fragmentTasksView2_to_fragmentStatusView);
        }
    }

    @Override
    public void recyclerViewClickListener(int position) {
        repositoryMediator.setCurrentTask(taskArrayList.get(position));
        tasksRecyclerView.setAdapter(tasksAdapter);


    }
}