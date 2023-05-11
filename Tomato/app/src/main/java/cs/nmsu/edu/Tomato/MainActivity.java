package cs.nmsu.edu.Tomato;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    // Define constants for the notification
    private static final String CHANNEL_ID = "my_channel";
    private static final int NOTIFICATION_ID = 1;

    // Define variables for the notification
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    // Define UI elements
    private Button addReminderButton;
    private RecyclerView remindersRecyclerView;

    // Define data structures for reminders
    private ArrayList<Reminder> remindersList;
    private ReminderAdapter reminderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the notification channel
        createNotificationChannel();

        // Initialize the notification variables
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("My Notification")
                .setContentText("This is an example notification");

        // Initialize UI elements
        addReminderButton = findViewById(R.id.add_reminder_button);
        remindersRecyclerView = findViewById(R.id.reminders_recycler_view);

        // Initialize data structures for reminders
        remindersList = new ArrayList<>();
        reminderAdapter = new ReminderAdapter(remindersList);
        remindersRecyclerView.setAdapter(reminderAdapter);
        remindersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up the click listener for the "Add Reminder" button
        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddReminderDialog();
            }
        });
    }

    // Create the notification channel
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ReminderChannel";
            String description = "Channel for Reminder Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Show a dialog to add a new reminder
    private void showAddReminderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Reminder");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_reminder, null);
        builder.setView(view);

        EditText editTextTitle = view.findViewById(R.id.editTextTitle);
        EditText editTextDesc = view.findViewById(R.id.editTextDesc);

        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String title = editTextTitle.getText().toString().trim();
                String desc = editTextDesc.getText().toString().trim();

                int selectedRadioId = radioGroup.getCheckedRadioButtonId();

                switch (selectedRadioId) {
                    case R.id.radioButton5min:
                        addReminder(title, desc, 5 * 60 * 1000);
                        break;
                    case R.id.radioButton30min:
                        addReminder(title, desc, 30 * 60 * 1000);
                        break;
                    case R.id.radioButton1hour:
                        addReminder(title, desc, 60 * 60 * 1000);
                        break;
                    case R.id.radioButton1day:
                        addReminder(title, desc, 24 * 60 * 60 * 1000);
                        break;
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    private void addReminder(String title, String desc, long time) {
        Reminder reminder = new Reminder(title, desc, time);
        remindersList.add(reminder);
        reminderAdapter.notifyDataSetChanged();

        Intent intent = new Intent(MainActivity.this, NotificationReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("desc", desc);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,
                0, intent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pendingIntent);

        Toast.makeText(this, "Reminder added", Toast.LENGTH_SHORT).show();
    }
}
