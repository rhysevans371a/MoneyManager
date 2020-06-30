package com.stu54259.MoneyManager;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.stu54259.MoneyManager.Helpers.NotificationPublisher;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Reminders extends MainActivity {
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    final static int req1 = 1;
    private final static String default_notification_channel_id = "default";
    private static final int RQS_PICK_CONTACT = 0;
    final Calendar cldr = Calendar.getInstance();
    public String a = "0";
    EditText eText;
    EditText reminder;
    DatePickerDialog picker;
    Button home;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Set Reminders");

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentHome);
                onBackPressed();
            }
        });

        reminder = findViewById(R.id.reminderText);
        eText = findViewById(R.id.reminderDate);
        eText = findViewById(R.id.reminderDate);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                int hour = 0;
                int minute = 0;
                // date picker dialog
                picker = new DatePickerDialog(Reminders.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Log.e("day check", String.valueOf(dayOfMonth));
                            }
                        }, year, month, day);
                picker.show();
            }

        });
    }

    private void scheduleNotification(Notification notification,
                                      long delay) {
        Intent notificationIntent = new Intent(Reminders.this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, delay, pendingIntent);
    }

    private Notification getNotification(String content) {
        String description = reminder.getText().toString();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_channel_id);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(description);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.income:
                Toast.makeText(this, "Income", Toast.LENGTH_SHORT).show();
                Intent intentIncome = new Intent(getApplicationContext(), Income.class);
                startActivity(intentIncome);
                break;
            case R.id.expenses:
                Toast.makeText(this, "Expenses", Toast.LENGTH_SHORT).show();
                Intent intentExpense = new Intent(getApplicationContext(), Expense.class);
                startActivity(intentExpense);
                break;
            case R.id.categories:
                Toast.makeText(this, "Categories", Toast.LENGTH_SHORT).show();
                Intent intentCategories = new Intent(getApplicationContext(), Categories.class);
                startActivity(intentCategories);
                break;
            case R.id.targets:
                Toast.makeText(this, "Targets", Toast.LENGTH_SHORT).show();
                Intent intentTarget = new Intent(getApplicationContext(), Targets.class);
                startActivity(intentTarget);
                break;
            case R.id.reminders:
                Toast.makeText(this, "Reminders", Toast.LENGTH_SHORT).show();
                Intent intentReminder = new Intent(getApplicationContext(), Reminders.class);
                startActivity(intentReminder);
                break;
            case R.id.send_sms:
                Toast.makeText(getApplicationContext(), "Send Sms", Toast.LENGTH_SHORT).show();
                Intent intentSms = new Intent(getApplicationContext(), SendSms.class);
                startActivity(intentSms);
                break;
            case R.id.logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveReminder(View view) {
        String myFormat = "yy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        Date date = cldr.getTime();
        eText.setText(sdf.format(date));

        scheduleNotification(getNotification(eText.getText().toString()), date.getTime());

        Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intentHome);
    }
}
