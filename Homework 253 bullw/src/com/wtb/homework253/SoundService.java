package com.wtb.homework253;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class SoundService extends Service implements OnCompletionListener {
	
	private static final String TAG = "Sound Service";
	private NotificationManager mNotificationManager;
	private boolean mIsRunning = false;
	MediaPlayer mediaPlayer;
	private Handler mSoundServiceHandler = new Handler();
    private UpdateRunnable mUpdateRunnable = new UpdateRunnable();    
    private int NOTIFICATION = R.string.sound_service_started;
    
    private class UpdateRunnable implements Runnable {
    
        @Override
        public void run() {
            if (mIsRunning) {
               Log.d(TAG, "Playing sound via AsynchTaskTimer");
               AsynchTaskTimer();
            } 
        }        
    }
    
    //
    public void AsynchTaskTimer() {
        final Handler handler = new Handler();
        final Timer timer = new Timer(); //This is new
        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                    	if (mIsRunning){
                    		try {
                    			Log.d(TAG, "Playing from mediaPlayer");
                    			mediaPlayer.start();
                        	} catch (Exception e) {
                        		Log.e(TAG, "exception whne trying to run");
                        	}
                    	} else {
                    		timer.cancel();
                    	}
                    }
                });
            }
        };
      
        timer.schedule(timertask, 0, 5000); // execute once every 5 seconds
    }
	


    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class SoundBinder extends Binder {
        SoundService getService() {
            return SoundService.this;
        }
    }
    
    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new SoundBinder();
    
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "IN ON BIND");
        startSound();
		return mBinder;
	}

    @Override
    public void onCreate() {
    	Log.d(TAG, "OnCreate - service");
        // Create media player to handle playing the beep.
        mediaPlayer = MediaPlayer.create(this, R.raw.beep);
        mediaPlayer.setOnCompletionListener(this);
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {   	    	
    	return START_NOT_STICKY;
    }
    
    
    @SuppressWarnings("deprecation")
	@Override
    public void onStart(Intent intent, int startId) {
    	Log.d(TAG, "onStart() intent: " + intent);
    	// Start playing the sound.
    	startSound();
    	super.onStart(intent, startId);
    }
    
    private void startSound () {    	
    	mIsRunning = true;
    	mSoundServiceHandler.post(mUpdateRunnable);   	
    }
    
    private void stopSound() {
    	mIsRunning = false;
    	
    }
     
    public void onDestroy() {
    	Log.d(TAG, "In on destroy.  Stopping sound.");
    	stopSound();
		mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		mNotificationManager.cancel(NOTIFICATION);
        super.onDestroy();
    }

	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.d(TAG, "Completed sound--stopping mediaPlayer");
		stopSelf();
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
