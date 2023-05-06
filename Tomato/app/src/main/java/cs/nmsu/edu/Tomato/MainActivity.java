package cs.nmsu.edu.Tomato;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import cs.nmsu.edu.Tomato.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    //notification variables
    private static final String CHANNEL_ID = "tomato";
    private CharSequence textTitle = "Reminder";
    private CharSequence textContent = "fix this";

    private int notificationId = 0;

    //list variables
    private ListView languageLV;
    private Button btnStart;
    private Button addBtn;
    private EditText itemEdt;
    private ArrayList<String> lngList;

    //notifications channel
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for the add button
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //for the notifications
        createNotificationChannel();

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, AlertDetails.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        //notification object builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        //actually show the notification?
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);



        // on below line we are initializing our variables.
        languageLV = findViewById(R.id.);
        btnStart = findViewById(R.id.btnStart);
        addBtn = findViewById(R.id.idBtnAdd);
        itemEdt = findViewById(R.id.idEdtItemName);
        lngList = new ArrayList<>();

        // on below line we are adding items to our list
        lngList.add("demo");

        // on the below line we are initializing the adapter for our list view.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lngList);

        // on below line we are setting adapter for our list view.
        languageLV.setAdapter(adapter);
//
//        btnStart.setOnClickListener(v -> {
//            Intent startIntent = new Intent(v.getContext(),AddTask.class);
//            startActivity(startIntent);
//        }); //start button listener

        // on below line we are adding click listener for our button.
        addBtn.setOnClickListener(v -> {
            // on below line we are getting text from edit text
            String item = itemEdt.getText().toString();

            textContent = item;

            // on below line we are checking if item is not empty
            if (!item.isEmpty()) {

                // on below line we are adding item to our list.
                lngList.add(item);

                // on below line we are notifying adapter
                // that data in list is updated to
                // update our list view.
                adapter.notifyDataSetChanged();

            }
        }); //addBtn listener
    }//oncreate
}
