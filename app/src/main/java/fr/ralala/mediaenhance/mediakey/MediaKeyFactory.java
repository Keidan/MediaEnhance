package fr.ralala.mediaenhance.mediakey;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RemoteController;
import android.view.KeyEvent;

import fr.ralala.mediaenhance.MediaEnhanceApp;
import fr.ralala.mediaenhance.receivers.VolumeChangeReceiver;

/**
 *******************************************************************************
 * <p><b>Project MediaEnhance</b><br/>
 * Management of media keys (volumes).
 * </p>
 * @author Keidan
 *
 *******************************************************************************
 */
@SuppressWarnings("deprecation")
public class MediaKeyFactory {
  private long mPreviousDelayUp = 0;
  private long mPreviousDelayDown = 0;
  private int mPreviousVolume = 0;
  private boolean mEcho = false;
  private AudioManager mAudio = null;
  private RemoteController mRemoteController = null;
  private MediaEnhanceApp mApp = null;

  /**
   * The factory constructor.
   * @param context The Android context.
   * @param app The reference to the application context.
   * @param remoteController The reference to the remote context.
   */
  public MediaKeyFactory(final Context context, final MediaEnhanceApp app, final RemoteController remoteController) {
    mRemoteController = remoteController;
    mApp = app;
    mAudio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    if(mAudio == null)
      return;
    mPreviousVolume = mAudio.getStreamVolume(AudioManager.STREAM_MUSIC);
    mPreviousDelayUp = System.currentTimeMillis();
    mPreviousDelayDown = System.currentTimeMillis();
  }


  /**
   * Applies the volume update.
   * @param method The method/
   * @param intent The current intent (used with MediaKeyMethod.HIDDEN to retrieve the current volume).
   */
  public void apply(final MediaKeyMethod method, final Intent intent) {
    final long now = System.currentTimeMillis();
    if(mEcho) {
      mEcho = false;
    } else {
      if (method == MediaKeyMethod.SETTINGS) {
        manageVolumeSettings(mAudio, now);
      } else {
        int currentVolume = intent.getIntExtra(VolumeChangeReceiver.VOLUME_STREAM_VALUE, 0);
        manageVolumeHidden(mAudio, now, currentVolume);
      }
    }
  }

  /**
   * Returns the echo status.
   * @return boolean
   */
  public boolean isEcho() {
    return mEcho;
  }

  /**
   * Manages the media keys using the settings API.
   * @param audio The reference to the audio manager.
   * @param now The current delay in ms.
   */
  private void manageVolumeSettings(final AudioManager audio, final long now) {
    boolean down = false;
    int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
    final int delta = mPreviousVolume - currentVolume;
    if (delta == mApp.getDeltaDown()) {
      down = true;
      if (mPreviousDelayDown + mApp.getTimeDelayDown() <= now && mPreviousDelayDown + mApp.getTimeDelayDown() >= mApp.getTimeMinDown()) {
        /* send the command and restore the volume */
        if (sendKeyEvent(KeyEvent.KEYCODE_MEDIA_PREVIOUS)) {
          audio.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume + mApp.getDeltaDown(), 0);
        }
      }
    } else if (delta == (-mApp.getDeltaUp())) {
      down = false;
      if (mPreviousDelayUp + mApp.getTimeDelayUp() <= now && mPreviousDelayUp + mApp.getTimeDelayUp() >= mApp.getTimeMinUp()) {
        /* send the command and restore the volume */
        if (sendKeyEvent(KeyEvent.KEYCODE_MEDIA_NEXT)) {
          audio.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume - mApp.getDeltaUp(), 0);
        }
      }
    }
    mPreviousVolume = currentVolume;
    if(down)
      mPreviousDelayDown = now;
    else
      mPreviousDelayUp = now;
  }

  /**
   * Manages the media keys using the hidden API.
   * @param audio The reference to the audio manager.
   * @param now The current delay in ms.
   * @param currentVolume The current volume.
   */
  private void manageVolumeHidden(final AudioManager audio, final long now, final int currentVolume) {
    boolean down = false;
    final int delta = mPreviousVolume - currentVolume;
    if (delta >= 1
        || (mPreviousVolume == 0 && currentVolume == 0)) {
      down = true;
      if ((now - mPreviousDelayDown) <= mApp.getTimeDelayDown() && (now - mPreviousDelayDown) >= mApp.getTimeMinDown()) {
        /* send the command and restore the volume */
        if (sendKeyEvent(KeyEvent.KEYCODE_MEDIA_PREVIOUS)) {
          audio.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume + 2, 0);
        }
      }
    } else if (delta <= (-1)
        || (mPreviousVolume == audio
        .getStreamMaxVolume(AudioManager.STREAM_MUSIC) && currentVolume == audio
        .getStreamMaxVolume(AudioManager.STREAM_MUSIC))) {
      down = false;
      if ((now - mPreviousDelayUp) <= mApp.getTimeDelayUp() && (now - mPreviousDelayUp) >= mApp.getTimeMinUp()) {
        /* send the command and restore the volume */
        if (sendKeyEvent(KeyEvent.KEYCODE_MEDIA_NEXT)) {
          audio.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume - 2, 0);
        }
      }
    }
    mPreviousVolume = currentVolume;
    if(down)
      mPreviousDelayDown = now;
    else
      mPreviousDelayUp = now;
  }

  /**
   * Sends the media key to the remote controller.
   * @param keyCode The key coded.
   * @return true if both clicks were delivered successfully.
   */
  private boolean sendKeyEvent(final int keyCode) {
    mEcho = true;
    // send "down" and "up" keyevents.
    KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
    final boolean first = mRemoteController.sendMediaKeyEvent(keyEvent);
    keyEvent = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
    final boolean second = mRemoteController.sendMediaKeyEvent(keyEvent);
    return first && second; // if both clicks were delivered successfully
  }
}
