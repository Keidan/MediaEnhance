package fr.ralala.mediaenhance;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.RemoteController;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import fr.ralala.mediaenhance.receivers.RestartServiceReceiver;
import fr.ralala.mediaenhance.receivers.ScreenReceiver;
import fr.ralala.mediaenhance.receivers.SettingsContentObserver;
import fr.ralala.mediaenhance.receivers.VolumeChangeReceiver;
import fr.ralala.mediaenhance.utils.Helper;
import fr.ralala.mediaenhance.mediakey.MediaKeyFactory;
import fr.ralala.mediaenhance.mediakey.MediaKeyMethod;
import fr.ralala.mediaenhance.utils.HelperUI;

/**
 *******************************************************************************
 * <p><b>Project MediaEnhance</b><br/>
 * Main service
 * </p>
 * @author Keidan
 *
 *******************************************************************************
 */
@SuppressWarnings("deprecation")
public class MediaEnhanceService extends Service implements RemoteController.OnClientUpdateListener {
  private ScreenReceiver mScreenReceiver = null;
  private SettingsContentObserver mSettingsContentObserver = null;
  private RemoteController mRemoteController = null;
  private boolean mCreateError = false;
  private MediaEnhanceApp mApp = null;
  private boolean mStarted = false;
  private VolumeChangeReceiver mVolumeChangeReceiver = null;
  private MediaKeyFactory mFactory = null;

  @Override
  public void onCreate() {
    super.onCreate();
    mApp = ((MediaEnhanceApp) getApplication());
    mApp.loadConfig();
    mStarted = false;
    // register receiver that handles screen on and screen off logic
    final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
    filter.addAction(Intent.ACTION_SCREEN_OFF);
    mScreenReceiver = new ScreenReceiver();
    registerReceiver(mScreenReceiver, filter);

    if (mApp.getMediaKeyMethod() == MediaKeyMethod.SETTINGS) {
      mSettingsContentObserver = new SettingsContentObserver(this,
          new Handler());
      getApplicationContext().getContentResolver().registerContentObserver(
          android.provider.Settings.System.CONTENT_URI, true,
          mSettingsContentObserver);
    } else {
      final IntentFilter ii = new IntentFilter(VolumeChangeReceiver.VOLUME_CHANGED_ACTION);
      mVolumeChangeReceiver = new VolumeChangeReceiver();
      registerReceiver(mVolumeChangeReceiver, ii);
    }
    try {
      final AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
      mRemoteController = new RemoteController(this, this);
      if(audio != null)
        audio.registerRemoteController(mRemoteController);
    } catch (final Exception e) {
      mCreateError = true;
      Log.e(getClass().getSimpleName(), "Exception: " + e.getMessage(), e);
    }
    mFactory = new MediaKeyFactory(this, mApp, mRemoteController);
  }

  @Override
  public void onDestroy() {
    Log.i(getClass().getSimpleName(), "onDestroy.");
    mStarted = false;
    if(mApp.isDisplayToast())
      HelperUI.toast(this, getResources().getString(R.string.toast_stopped));
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
      if(audio != null)
        audio.unregisterRemoteController(mRemoteController);
      mRemoteController = null;
    }
    if (mVolumeChangeReceiver != null) {
      unregisterReceiver(mVolumeChangeReceiver);
      mVolumeChangeReceiver = null;
    }
    if (!mApp.isSenpuku() && !mCreateError) {
      Log.i(getClass().getSimpleName(), "Restart service.");
      /* restart the activity */
      final Intent intent = new Intent(this, RestartServiceReceiver.class);
      intent.setAction("fr.ralala.mediaenhance.RESTART");
      sendBroadcast(intent);
    }
    super.onDestroy();
  }

  @Override
  public int onStartCommand(final Intent intent, final int flags,
                            final int startId) {
    MediaKeyMethod volumeMethod = mApp.getMediaKeyMethod();
    boolean volumeUpdate = false;
    mApp.loadConfig();
    boolean screenOff;
    if (intent != null) {
      if (intent.hasExtra(MediaEnhanceApp.MEDIA_ACTION_KEY)) {
        volumeMethod = MediaKeyMethod.fromString(intent.getStringExtra(MediaEnhanceApp.MEDIA_ACTION_KEY));
        volumeUpdate = true;
      }
    }
    if (mCreateError)
      HelperUI.toast(this, getResources().getString(R.string.toast_permission));
    else if (!mStarted && mApp.isDisplayToast()) {
      HelperUI.toast(this, getResources().getString(R.string.toast_started));
      mStarted = true;
    }
    screenOff = !Helper.isScreenOn(this);
    Log.i(getClass().getSimpleName(), "Service started with i:" + intent
        + ", id:" + startId + ", screen off:" + screenOff + ", volumeUpdate:"
        + volumeUpdate + ", volumeMethod:" + volumeMethod + ", echo:" + mFactory.isEcho());

    if (screenOff && volumeUpdate) {
      mFactory.apply(volumeMethod, intent);
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
  public void onClientMetadataUpdate(final RemoteController.MetadataEditor metadataEditor) {
  }
}
