package com.wtb.homework253;

import com.wtb.homework253.SoundService.SoundBinder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Notification.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
    private SoundService mService;
    private SoundBinder mBinder;
    private boolean mServiceIsBound;
    private NotificationManager mNotificationManager;
    private int NOTIFICATION = R.string.sound_service_started;
    
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, "onServiceConnected()");            
            mBinder = (SoundBinder) service;
            mService = mBinder.getService();            
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.d(TAG, "onServiceDisconnected()");            
            mServiceIsBound = false;
            mBinder = null;            
        }
    };    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		final Button startButton = (Button) findViewById(R.id.start_button);
		startButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (startButton.getText().equals("Stop")) {					
					stopSoundService();
			        startButton.setText(R.string.start_button);			        
				} else {	                
					startSoundService();
					startButton.setText(R.string.stop_button);
				}
			}
			
		});
	}
	
	public void startSoundService() {
		Log.d(TAG, "Starting service...");
		bindService(new Intent(MainActivity.this, SoundService.class), mConnection, Context.BIND_AUTO_CREATE);
        startService(new Intent(this, SoundService.class));        
        mServiceIsBound = true;		
	}
	
	public void stopSoundService() {
		Log.d(TAG, "Stopping service...");
		stopService(new Intent(this, SoundService.class));
		unbindService(mConnection);		
        mServiceIsBound = false;        
	}
	
	@Override
	public void onResume() {
		mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		mNotificationManager.cancel(NOTIFICATION);
		super.onResume();
	}
	
	@Override
	public void onStop() {
		mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		if (mServiceIsBound)
			showNotification();
		
		super.onStop();
	}
	
	@Override
	public void onDestroy() { 
		if (mServiceIsBound)
			unbindService(mConnection);
		
		super.onDestroy();
	}
	
    @SuppressLint("NewApi")
	private void showNotification() {
    	
    	Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
    	
    	notificationIntent.setAction(Intent.ACTION_MAIN);
    	notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    	
    	PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
    	        0, notificationIntent,
    	        PendingIntent.FLAG_UPDATE_CURRENT);

        Builder builder = new Notification.Builder(getApplicationContext())
        	.setContentIntent(contentIntent)
        	.setContentText(getText(R.string.sound_service_started))
        	.setContentTitle(getText(R.string.sound_service_label))
        	.setSmallIcon(R.drawable.ic_launcher);
        
        Notification n = builder.build();    
        
        mNotificationManager.notify(NOTIFICATION, n);
    }
}