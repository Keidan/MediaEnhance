package org.kei.android.phone.mediaenhance;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.RemoteController;
import android.media.RemoteController.MetadataEditor;
import android.media.RemoteController.OnClientUpdateListener;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;

/**
 *******************************************************************************
 * @file MediaEnhanceService.java
 * @author Keidan
 * @date 21/01/2016
 * @par Project MediaEnhance
 *
 * @par Copyright 2016 Keidan, all right reserved
 *
 *      This software is distributed in the hope that it will be useful, but
 *      WITHOUT ANY WARRANTY.
 *
 *      License summary : You can modify and redistribute the sources code and
 *      binaries. You can send me the bug-fix
 *
 *      Term of the license in in the file license.txt.
 *
 *******************************************************************************
 */

@SuppressWarnings("deprecation")
public class MediaEnhanceService extends Service implements OnClientUpdateListener{
  public static final String      KEY_TIME_DELAY_UP        = "timeDelayUp";
  public static final String      KEY_TIME_DELAY_DOWN      = "timeDelayDown";
  public static final String      KEY_DELTA_DOWN           = "deltaDown";
  public static final String      KEY_DELTA_UP             = "deltaUp";
  public static final String      ACTION_SENPUKU           = "action_senpuku";
  public static final String      ACTION_APPLY             = "action_apply";
  public static final int         DEFAULT_TIME_DELAY       = 1000;
  public static final int         DEFAULT_DELTA            = 2;
  private ScreenReceiver          mScreenReceiver          = null;
  private final IBinder           mBinder                  = new MediaEnhanceBinder();
  private SettingsContentObserver mSettingsContentObserver = null;
  private boolean                 screenOff                = false;
  private long                    previousDelayUp          = 0;
  private long                    previousDelayDown        = 0;
  private long                    timeDelayUp              = DEFAULT_TIME_DELAY;
  private long                    timeDelayDown            = DEFAULT_TIME_DELAY;
  private int                     deltaUp                  = -(DEFAULT_DELTA);
  private int                     deltaDown                = DEFAULT_DELTA;
  private int                     previousVolume           = 0;
  private RemoteController        mRemoteController        = null;
  private boolean                 senpuku                  = false;
  private boolean                 createError              = false;

  public class MediaEnhanceBinder extends Binder {
    MediaEnhanceService getService() {
      return MediaEnhanceService.this;
    }
  }

  @Override
  public void onCreate() {
    super.onCreate();
    loadConfig();
    // register receiver that handles screen on and screen off logic
    final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
    filter.addAction(Intent.ACTION_SCREEN_OFF);
    mScreenReceiver = new ScreenReceiver();
    registerReceiver(mScreenReceiver, filter);
    
    mSettingsContentObserver = new SettingsContentObserver(this, new Handler());
    getApplicationContext().getContentResolver().registerContentObserver(
        android.provider.Settings.System.CONTENT_URI, true,
        mSettingsContentObserver);
    
    final AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    previousVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
    mRemoteController = new RemoteController(this, this);
    try {
      audio.registerRemoteController(mRemoteController);
    } catch(Exception e) {
      createError = true;
    }
  }
  
  private void loadConfig() {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    timeDelayUp = prefs.getLong(KEY_TIME_DELAY_UP, DEFAULT_TIME_DELAY);
    timeDelayDown = prefs.getLong(KEY_TIME_DELAY_DOWN, DEFAULT_TIME_DELAY);
    deltaUp = -(prefs.getInt(KEY_DELTA_UP, DEFAULT_DELTA));
    deltaDown = prefs.getInt(KEY_DELTA_DOWN, DEFAULT_DELTA);
    previousDelayUp = System.currentTimeMillis();
    previousDelayDown = System.currentTimeMillis();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Tools.toast(this, getResources().getString(R.string.toast_stopped));
    if (mScreenReceiver != null) {
      unregisterReceiver(mScreenReceiver);
      mScreenReceiver = null;
    }
    if (mSettingsContentObserver != null) {
      getApplicationContext().getContentResolver().unregisterContentObserver(
          mSettingsContentObserver);
      mSettingsContentObserver = null;
    }
    if (mRemoteController != null) {
      final AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
      audio.unregisterRemoteController(mRemoteController);
      mRemoteController = null;
    }
    if(!senpuku && !createError) {
      /* restart the activity */
      final Intent intent = new Intent(this, MediaEnhanceActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    }
  }

  @Override
  public int onStartCommand(final Intent intent, final int flags,
      final int startId) {
    if(createError)
      Tools.toast(this, getResources().getString(R.string.toast_permission));
    else
      Tools.toast(this, getResources().getString(R.string.toast_started));
    screenOff = !Tools.isScreenOn(this);
    boolean volumeUpdate = false;
    if (intent != null) {
      if (intent.hasExtra(ScreenReceiver.SCREEN_STATE_KEY))
        screenOff = intent.getBooleanExtra(ScreenReceiver.SCREEN_STATE_KEY,
            !Tools.isScreenOn(this));
      else if (intent.hasExtra(SettingsContentObserver.MEDIA_ACTION_KEY))
        volumeUpdate = true;
      else if (intent.hasExtra(ACTION_APPLY))
        loadConfig();
      else if (intent.hasExtra(ACTION_SENPUKU)) {
        senpuku = true;
        stopSelf();
        return START_NOT_STICKY;
      }
    }
    
     Log.i(getClass().getSimpleName(), "Service started with i:" + intent +
     ", id:" + startId + ", screen off:" + screenOff + ", " + volumeUpdate);
     
    if (screenOff && volumeUpdate) {
      final AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
      final int currentVolume = audio
          .getStreamVolume(AudioManager.STREAM_MUSIC);
      final int delta = previousVolume - currentVolume;
      final long delay = System.currentTimeMillis();

      if (delta == deltaDown) {
        if (previousDelayDown + timeDelayDown <= delay) {
          /* send the command and restore the volume */
          if (sendKeyEvent(KeyEvent.KEYCODE_MEDIA_PREVIOUS))
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, previousVolume, 0);
        }
      } else if (delta == deltaUp) {
        if (previousDelayUp + timeDelayUp <= delay) {
          /* send the command and restore the volume */
          if (sendKeyEvent(KeyEvent.KEYCODE_MEDIA_NEXT))
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, previousVolume, 0);
        }
      }
      /* save the new states */
      previousDelayUp = delay;
      previousDelayDown = delay;
      previousVolume = currentVolume;
    }
    return START_STICKY;

  }

  @Override
  public IBinder onBind(final Intent intent) {
    return mBinder;
  }

  private boolean sendKeyEvent(final int keyCode) {
    // send "down" and "up" keyevents.
    KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
    final boolean first = mRemoteController.sendMediaKeyEvent(keyEvent);
    keyEvent = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
    final boolean second = mRemoteController.sendMediaKeyEvent(keyEvent);
    return first && second; // if both clicks were delivered successfully
  }

  @Override
  public void onClientChange(boolean clearing) { }

  @Override
  public void onClientPlaybackStateUpdate(int state) { }

  @Override
  public void onClientPlaybackStateUpdate(int state, long stateChangeTimeMs,
      long currentPosMs, float speed) { }

  @Override
  public void onClientTransportControlUpdate(int transportControlFlags) { }

  @Override
  public void onClientMetadataUpdate(MetadataEditor metadataEditor) { }
}