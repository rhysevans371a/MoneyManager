package com.stu54259.MoneyManager.Helpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.stu54259.MoneyManager.MainActivity;
import com.stu54259.MoneyManager.R;
import com.stu54259.MoneyManager.sql.DatabaseManager;


public class NotificationWorker extends Worker {
    private static final String WORK_RESULT = "work_result";
    DatabaseManager mDatabase;
    private double savingsMonth;
    private double savingsTarget;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }
    @NonNull
    @Override
    public Result doWork() {

        String Message = "Congratulations you have reached your savings target for the month";
        mDatabase= new DatabaseManager(this.getApplicationContext());
        String contactNumber = MainActivity.contactNumber;
        savingsMonth = mDatabase.savings_month(savingsMonth);
        savingsTarget = mDatabase.savings_target(savingsTarget);
        Log.e("Check contact number", contactNumber);
        if (savingsMonth == savingsTarget) { SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(contactNumber,null,Message,null,null);
            Log.e("Mobile", contactNumber);
        }


        Data taskData = getInputData();
        String taskDataString = taskData.getString(MainActivity.MESSAGE_STATUS);
        showNotification("WorkManager", taskDataString != null ? taskDataString : "Message has been Sent");
        Data outputData = new Data.Builder().putString(WORK_RESULT, "Jobs Finished").build();
        return Result.success(outputData);
    }
    private void showNotification(String task, String desc) {
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "task_channel";
        String channelName = "task_name";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new
                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setContentTitle(task)
                .setContentText(desc)
                .setSmallIcon(R.mipmap.ic_launcher);
        manager.notify(1, builder.build());
    }
}