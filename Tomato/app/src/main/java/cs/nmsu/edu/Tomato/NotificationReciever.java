import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "REMINDER_CHANNEL_ID";
    private static final CharSequence CHANNEL_NAME = "REMINDER_CHANNEL_NAME";
    private static final String REMINDER_TITLE = "Reminder";
    private static final String REMINDER_TEXT = "Don't forget to do the task!";

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra("notificationId", 0);
        String message = intent.getStringExtra("message");
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(REMINDER_TITLE)
                .setContentText(message)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setSound(soundUri);

        // Schedule the notification based on user input
        int delayInMinutes = intent.getIntExtra("delayInMinutes", 0);
        if (delayInMinutes > 0) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, delayInMinutes);
            long futureInMillis = cal.getTimeInMillis();

            PendingIntent scheduledIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, scheduledIntent);
        }

        notificationManager.notify(notificationId, builder.build());
    }
}
