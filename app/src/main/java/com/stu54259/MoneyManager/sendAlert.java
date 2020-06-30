package com.stu54259.MoneyManager;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.stu54259.MoneyManager.Helpers.Util;
import com.stu54259.MoneyManager.sql.DatabaseManager;

import static com.stu54259.MoneyManager.MainActivity.contactNumber;

@SuppressLint("SpecifyJobSchedulerIdRange")
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class sendAlert extends JobService {
    private static final String TAG = "SyncService";
    DatabaseManager mDatabaseManager;
    double savingsmonth;
    double savingstarget;
    double expenseMonth;
    double expenseLimit;

    @Override
    public boolean onStartJob(JobParameters params) {
        mDatabaseManager = new DatabaseManager(this.getApplicationContext());
        savingstarget = mDatabaseManager.savings_target(savingstarget);
        savingsmonth = mDatabaseManager.savings_month(savingsmonth);
        expenseMonth = mDatabaseManager.expense_month(expenseMonth);
        expenseLimit = mDatabaseManager.expense_limit(expenseLimit);

        Intent service = new Intent(getApplicationContext(), sendAlert.class);
        getApplicationContext().startService(service);

        // Check if savings limit has been reached
        if (savingsmonth >= savingstarget) {
            Log.e("Compare check", String.valueOf(savingsmonth + savingstarget));
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Savings Alert")
                    .setContentText("Congratulations you have reached your savings target for the month")
                    .setAutoCancel(true);
            // Notification Intent
            Intent intentNot = new Intent(this, MainActivity.class);
            @SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentNot, Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            mBuilder.setContentIntent(pendingIntent);
            // Show Notification
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());

            //Sms intent call
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

            //Get the SmsManager instance and call the sendTextMessage method to send message
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(contactNumber, null, "Congratulations you have reached your savings target for the month", pi, null);
        }

        // Check if expense limit has been reached
        if (expenseMonth >= expenseLimit) {
            Log.e("Compare expense target", String.valueOf(expenseMonth + expenseLimit));
            NotificationCompat.Builder myExpenseBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Spending Alert")
                    .setContentText("Warning! You have exceeded your spending limit this month")
                    .setAutoCancel(true);
            // Notification Intent
            Intent intentExpense = new Intent(this, MainActivity.class);
            @SuppressLint("WrongConstant") PendingIntent expenseIntent = PendingIntent.getActivity(this, 0, intentExpense, Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            myExpenseBuilder.setContentIntent(expenseIntent);
            // Show Notification
            NotificationManager myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            myNotificationManager.notify(0, myExpenseBuilder.build());
        } else
            Util.scheduleJob(getApplicationContext()); // reschedule the job
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}