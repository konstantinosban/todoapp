package com.example.todoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    //interface που δηλωνει τις 2 ενεργειες που μπορουν να γινουν στην εργασια, επιλογη εργασιας, και ολοκληρωση εργασιας. Γινεται implement στην mainactivity
    public interface TaskActionListener {
        void onTaskClicked(int position);
        void onTaskCheckedChanged(int position, boolean isChecked);
    }

    private ArrayList<Task> tasks;
    private TaskActionListener listener;
    private boolean readOnly;

    public TaskAdapter(ArrayList<Task> tasks, TaskActionListener listener, boolean readOnly) {
        this.tasks = tasks; //Η λίστα των εργασιών προς εμφάνιση
        this.listener = listener; //Η activity που χειρίζεται τα events
        this.readOnly = readOnly; //Αν true, δεν μπορείς να επεξεργαστείς/τσεκάρεις
    }

    //καλειται απο το recyclerview για οταν θελουμε ενα νεο item view (task_item.xml)
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    //Παίρνει το Task στη συγκεκριμένη θέση, Εμφανίζει την περιγραφή, Ορίζει αν είναι τσεκαρισμένο (isDone)
    // Ενεργοποιεί/απενεργοποιεί το checkbox ανάλογα με readOnly
    //Συνδέει τα click events με τα callbacks του listener
    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.taskDescription.setText(task.getDescription());

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(task.isDone());
        holder.checkBox.setEnabled(!readOnly);

        if (!readOnly) {
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                listener.onTaskCheckedChanged(position, isChecked);
            });
        }

        holder.itemView.setOnClickListener(v -> listener.onTaskClicked(position));
    }

    //helper clases
    @Override
    public int getItemCount() {
        return tasks.size();
    } //Επιστρέφει πόσα items υπάρχουν (δηλαδή πόσα tasks)

    public Task getTaskAt(int position) {
        return tasks.get(position);
    } //Επιστρέφει το Task σε μία συγκεκριμένη θέση

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskDescription; //για την περιγραφή
        CheckBox checkBox; //για ολοκλήρωση

        public TaskViewHolder(View itemView) { //UI στοιχεία που εμφανίζονται, Αναφορές σε αυτά, για να μπορεί ο adapter να τα τροποποιεί γρήγορα
            super(itemView);
            taskDescription = itemView.findViewById(R.id.task_description);
            checkBox = itemView.findViewById(R.id.checkbox);
            //Κάθε γραμμή της λίστας (task) χρειάζεται έναν ViewHolder που ξέρει πού είναι τι,
            // ώστε ο adapter να μπορεί να βάλει τα σωστά δεδομένα στο σωστό σημείο.
        }
    }
}
