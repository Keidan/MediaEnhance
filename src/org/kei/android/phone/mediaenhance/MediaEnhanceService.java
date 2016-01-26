package org.kei.android.phone.mediaenhance;

import org.kei.android.atk.utils.Tools;
import org.kei.android.phone.mediaenhance.receivers.RestartServiceReceiver;
import org.kei.android.phone.mediaenhance.receivers.ScreenReceiver;
import org.kei.android.phone.mediaenhance.receivers.SettingsContentObserver;
import org.kei.android.phone.mediaenhance.receivers.VolumeChangeReceiver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.RemoteController;
import android.media.RemoteController.MetadataEditor;
import android.media.RemoteController.OnClientUpdateListener;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

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
public class MediaEnhanceService extends Service implements
    OnClientUpdateListener {
  private ScreenReceiver          mScreenReceiver          = null;
  private SettingsContentObserver mSettingsContentObserver = null;
  private boolean                 screenOff                = false;
  private RemoteController        mRemoteController        = null;
  private boolean                 createError              = false;
  private MediaEnhanceApp         app                      = null;
  private boolean                 started                  = false;
  private VolumeChangeReceiver    volumeChangeReceiver     = null;
  private VolumeFactory           factory                  = null;
  
  @Override
  public void onCreate() {
    super.onCreate();
    app = ((MediaEnhanceApp) getApplication());
    app.loadConfig();
    started = false;
    // register receiver that handles screen on and screen off logic
    final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
    filter.addAction(Intent.ACTION_SCREEN_OFF);
    mScreenReceiver = new ScreenReceiver();
    registerReceiver(mScreenReceiver, filter);

    if (app.getVolumeMethod().equals(MediaEnhanceApp.VOLUME_METHOD_SETTINGS)) {
      mSettingsContentObserver = new SettingsContentObserver(this,
          new Handler());
      getApplicationContext().getContentResolver().registerContentObserver(
          android.provider.Settings.System.CONTENT_URI, true,
          mSettingsContentObserver);
    } else {
      final IntentFilter ii = new IntentFilter(VolumeChangeReceiver.VOLUME_CHANGED_ACTION);
      volumeChangeReceiver = new VolumeChangeReceiver();
      registerReceiver(volumeChangeReceiver, ii);
    }
    try {
      final AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
      mRemoteController = new RemoteController(this, this);
      audio.registerRemoteController(mRemoteController);
    } catch (final Exception e) {
      createError = true;
    }
    factory = new VolumeFactory(this, app, mRemoteController);
  }

  @Override
  public void onDestroy() {
    Log.i(getClass().getSimpleName(), "onDestroy.");
    started = false;
    if(app.isDisplayToast())
      Tools.toast(this, R.drawable.ic_launcher, getResources().getString(R.string.toast_stopped));
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
    if (volumeChangeReceiver != null) {
      unregisterReceiver(volumeChangeReceiver);
      volumeChangeReceiver = null;
    }
    if (!app.isSenpuku() && !createError) {
      Log.i(getClass().getSimpleName(), "Restart service.");
      /* restart the activity */
      final Intent intent = new Intent(this, RestartServiceReceiver.class);
      intent.setAction("org.kei.android.phone.mediaenhance.RESTART");
      sendBroadcast(intent);
    }
    super.onDestroy();
  }

  @Override
  public int onStartCommand(final Intent intent, final int flags,
      final int startId) {
    String volumeMethod = app.getVolumeMethod();
    boolean volumeUpdate = false;
    app.loadConfig();
    if (intent != null) {
      if (intent.hasExtra(ScreenReceiver.SCREEN_STATE_KEY))
        screenOff = intent.getBooleanExtra(ScreenReceiver.SCREEN_STATE_KEY,
            !Tools.isScreenOn(this));
      else if (intent.hasExtra(MediaEnhanceApp.MEDIA_ACTION_KEY)) {
        volumeMethod = intent.getStringExtra(MediaEnhanceApp.MEDIA_ACTION_KEY);
        volumeUpdate = true;
      }
    }
    if (createError)
      Tools.toast(this, R.drawable.ic_launcher, getResources().getString(R.string.toast_permission));
    else if (!started && app.isDisplayToast()) {
      Tools.toast(this, R.drawable.ic_launcher, getResources().getString(R.string.toast_started));
      started = true;
    }
    screenOff = !Tools.isScreenOn(this);
    Log.i(getClass().getSimpleName(), "Service started with i:" + intent
        + ", id:" + startId + ", screen off:" + screenOff + ", volumeUpdate:"
        + volumeUpdate + ", volumeMethod:" + volumeMethod + ", echo:" + factory.isEcho());

    if (screenOff && volumeUpdate) {
      factory.apply(volumeMethod, intent);
    }
    return START_STICKY;

  }

  @Override
  public IBinder onBind(final Intent intent) {
    return null;
  }

  @Override
  public void onClientChange(final boolean clearing) {
  }

  @Override
  public void onClientPlaybackStateUpdate(final int state) {
  }

  @Override
  public void onClientPlaybackStateUpdate(final int state,
      final long stateChangeTimeMs, final long currentPosMs, final float speed) {
  }

  @Override
  public void onClientTransportControlUpdate(final int transportControlFlags) {
  }

  @Override
  public void onClientMetadataUpdate(final MetadataEditor metadataEditor) {
  }
}
