package com.example.ting.mometest.Manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.ting.mometest.Activity.AlertActivity;
import com.example.ting.mometest.Activity.MainActivity;
import com.example.ting.mometest.Activity.R;

import java.sql.Time;


public class AlarmReceiver extends BroadcastReceiver {

	private NotificationManager manager;
	@Override
	public void onReceive(Context context, Intent intent) {

	    String title = intent.getStringExtra("Title");
	    String time = intent.getStringExtra("Time");/*
        Intent alaramIntent = new Intent(context, AlertActivity.class);
        alaramIntent.putExtra("Title",title);
        alaramIntent.putExtra("Time",time);
        alaramIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alaramIntent);
*/
        Intent intent2 = new Intent(context,MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context,0,intent2,0);
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        //NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.pic_alert)
                .setContentIntent(pi)
                .build();
        //.build();
        manager.notify(1,notification);

	}
	}
