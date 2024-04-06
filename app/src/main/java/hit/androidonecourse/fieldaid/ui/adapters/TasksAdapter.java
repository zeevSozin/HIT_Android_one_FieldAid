package hit.androidonecourse.fieldaid.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.databinding.TaskListItemBinding;
import hit.androidonecourse.fieldaid.domain.RepositoryMediator;
import hit.androidonecourse.fieldaid.domain.models.Task;
import hit.androidonecourse.fieldaid.ui.handlers.TaskListItemHandler;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {
    private ArrayList<Task> tasks;
    private Context context;
    private RepositoryMediator repositoryMediator;
    private RecyclerViewClickListener listener;

    public TasksAdapter(ArrayList<Task> tasks, Context context, RecyclerViewClickListener listener) {
        this.tasks = tasks;
        this.context = context;
        this.listener = listener;
        repositoryMediator = RepositoryMediator.getInstance(context);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TaskListItemBinding taskListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.task_list_item,
                parent,
                false
        );
        return new TaskViewHolder(taskListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task currentTask = tasks.get(position);
        holder.taskListItemBinding.setTask(currentTask);
        holder.taskListItemBinding.setHandler(new TaskListItemHandler(context));
        holder.bind(position);
        boolean isSelected = repositoryMediator.getCurrentTask() == currentTask;
        holder.taskListItemBinding.setIsSelected(isSelected);

    }

    @Override
    public int getItemCount() {
        if(tasks !=null){
            return tasks.size();
        }
        else {
            return 0;
        }
    }

    public void setTasks(ArrayList<Task> tasks){
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder{
        private final TaskListItemBinding taskListItemBinding;
        public TaskViewHolder(@NonNull TaskListItemBinding taskListItemBinding) {
            super(taskListItemBinding.getRoot());
            this.taskListItemBinding = taskListItemBinding;
        }
        private void bind(int position){
            taskListItemBinding.setListener(listener);
            taskListItemBinding.setPosition(position);
        }
    }
}
