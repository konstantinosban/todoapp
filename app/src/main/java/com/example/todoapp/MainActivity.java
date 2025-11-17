package com.example.todoapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TaskAdapter.TaskActionListener {

    private RecyclerView recyclerView; //λιστα εργασιων
    private TaskAdapter adapter; // συνδεση δεδομενων με το UI
    private ArrayList<Task> tasks = new ArrayList<>(); //Όλες οι εργασίες σε μνήμη
    private TextView emptyView;
    private Button btnActive, btnCompleted; // Κουμπιά φιλτραρίσματος
    private boolean showingCompleted = false; //Καθορίζει τι εμφανίζεται

    //Δημιουργια νεας εργασιας
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        emptyView = findViewById(R.id.emptyView);
        btnActive = findViewById(R.id.btn_active);
        btnCompleted = findViewById(R.id.btn_completed);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnActive.setOnClickListener(v -> {  // ο χρηστης πατηθει το κουμπει ενεργες
            showingCompleted = false;
            refreshTaskList();
        });

        btnCompleted.setOnClickListener(v -> { // οταν πατηθει το κουμπι ολοκληρωμενες
            showingCompleted = true;
            refreshTaskList();
        });

        FloatingActionButton fab = findViewById(R.id.fab_add); //Όταν πατηθεί το κουμπί +
        fab.setOnClickListener(v -> showAddTaskDialog());   // ανοίγει το παράθυρο προσθήκης νέας εργασίας.

        refreshTaskList(); //για να εμφανιστεί η λίστα με βάση το αρχικό φίλτρο (δηλ. ενεργές).
    }

    //Σε καθε ανανεωση μετραμε ποσες εργασιες εχει η καθε κατηγορια και ανανεωνουμε το κειμενο
    private void refreshTaskList() {
        ArrayList<Task> filtered = new ArrayList<>(); //Λίστα για τις εργασίες που θα εμφανιστούν αυτή τη στιγμή
        int activeCount = 0, completedCount = 0; //Δείκτες για πόσες είναι ενεργές και πόσες ολοκληρωμένες

        for (Task task : tasks) {
            if (task.isDone()) {
                completedCount++;
                if (showingCompleted) filtered.add(task);
            } else {
                activeCount++;
                if (!showingCompleted) filtered.add(task);
            }
        }

        btnActive.setText("ΕΡΓΑΣΙΕΣ (" + activeCount + ")");
        btnCompleted.setText("ΟΛΟΚΛΗΡΩΜΕΝΕΣ (" + completedCount + ")");

        emptyView.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);

        adapter = new TaskAdapter(filtered, this, showingCompleted);
        recyclerView.setAdapter(adapter);
    }

    //Εμφανιση του παραθυρου δημιουργιας εργασιας
    private void showAddTaskDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
        final EditText input = dialogView.findViewById(R.id.edit_task_description); //πεδίο για να γράψει ο χρήστης την περιγραφή της εργασίας.



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ΔΗΜΙΟΥΡΓΙΑ");
        builder.setView(dialogView);
        builder.setPositiveButton("ΑΠΟΘΗΚΕΥΣΗ", (dialog, which) -> {
            String taskDesc = input.getText().toString();
            if (!taskDesc.isEmpty()) {  //Αν δεν είναι άδειο, δημιουργεί νέο αντικείμενο Task και το προσθέτει στη λίστα
                tasks.add(new Task(taskDesc));
                refreshTaskList();
            }
        });
        builder.setNegativeButton("ΑΚΥΡΩΣΗ", null);
        builder.show();
    }

    //Επιλογη της εργασιας και εμφανιση σχετικου modal για επεξεργασια
    @Override
    public void onTaskClicked(int position) {
        Task filteredTask = adapter.getTaskAt(position);
        int realIndex = tasks.indexOf(filteredTask); //Παίρνει την εργασία από την φιλτραρισμένη λίστα

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
        final EditText input = dialogView.findViewById(R.id.edit_task_description);
        input.setText(filteredTask.getDescription());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ΕΠΕΞΕΡΓΑΣΙΑ");
        builder.setView(dialogView);
        builder.setPositiveButton("ΑΠΟΘΗΚΕΥΣΗ", (dialog, which) -> {
            filteredTask.setDescription(input.getText().toString());
            tasks.set(realIndex, filteredTask);
            refreshTaskList();
        });
        builder.setNeutralButton("ΔΙΑΓΡΑΦΗ", (dialog, which) -> {
            tasks.remove(realIndex);
            refreshTaskList();
        });
        builder.setNegativeButton("ΑΚΥΡΩΣΗ", null);
        builder.show();
    }

    //Ολοκληρωνουμε μια εργασια οταν επιλεχτει το checkbox, καλουμε στο τελος την refreshTaskList για να
    //φανουν οι αλλαγες αμεσως στον χρηστη
    @Override
    public void onTaskCheckedChanged(int position, boolean isChecked) { //Καλείται όταν ο χρήστης τσεκάρει ή ξε-τσεκάρει μια εργασία από τη λίστα.
        Task changedTask = adapter.getTaskAt(position);
        changedTask.setDone(isChecked); // αν ο χρήστης την τσεκάρει (ολοκληρώθηκε),αν την ξε-τσεκάρει (μη ολοκληρωμένη).
        refreshTaskList();
    }
}
