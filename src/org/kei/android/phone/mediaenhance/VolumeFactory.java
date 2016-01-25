package org.kei.android.phone.mediaenhance;

import org.kei.android.phone.mediaenhance.receivers.VolumeChangeReceiver;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RemoteController;
import android.util.Log;
import android.view.KeyEvent;

/**
 *******************************************************************************
 * @file VolumeFactory.java
 * @author Keidan
 * @date 25/01/2016
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
public class VolumeFactory {
  private long             previousDelayUp   = 0;
  private long             previousDelayDown = 0;
  private int              previousVolume    = 0;
  private boolean          echo              = false;
  private AudioManager     audio             = null;
  private RemoteController mRemoteController = null;
  private MediaEnhanceApp  app               = null;
  
  public VolumeFactory(final Context context, final MediaEnhanceApp app, final RemoteController mRemoteController) {
    this.mRemoteController = mRemoteController;
    this.app = app;
    this.audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    previousVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
    previousDelayUp = System.currentTimeMillis();
    previousDelayDown = System.currentTimeMillis();
  }
  
  
  public void apply(final String method, final Intent intent) {
    if(echo) {
      echo = false;
    } else {
      final long now = System.currentTimeMillis();
      if (method.equals(MediaEnhanceApp.VOLUME_METHOD_SETTINGS)) {
        manageVolumeSettings(audio, now);
      } else {
        int currentVolume = intent.getIntExtra(VolumeChangeReceiver.VOLUME_STREAM_VALUE, 0);
        manageVolumeHidden(audio, now, currentVolume);
      }
    }
  }

  /**
   * @return the echo
   */
  public boolean isEcho() {
    return echo;
  }
  
  private void manageVolumeSettings(final AudioManager audio, final long now) {
    boolean down = false;
    int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
    final int delta = previousVolume - currentVolume;
    //Log.i(getClass().getSimpleName(), "manageVolumeSettings -> currentVolume:" + currentVolume + ", previousVolume:"+previousVolume + ", delta:" + delta);
    if (delta == app.getDeltaDown()) {
      down = true;
      //Log.i(getClass().getSimpleName(), "manageVolumeSettings -> Volume down (" + app.getTimeDelayDown() + ")");
      //Log.i(getClass().getSimpleName(), "manageVolumeSettings -> previousDelayDown:" + previousDelayDown + ", app.getTimeDelayDown():" + app.getTimeDelayDown() + ", now:" + now);
      //Log.i(getClass().getSimpleName(), "manageVolumeSettings -> real: " +  (previousDelayDown + app.getTimeDelayDown()));
      if (previousDelayDown + app.getTimeDelayDown() <= now) {
        //Log.i(getClass().getSimpleName(), "manageVolumeSettings -> Volume down in delay");
        /* send the command and restore the volume */
        if (sendKeyEvent(KeyEvent.KEYCODE_MEDIA_PREVIOUS)) {
          //Log.i(getClass().getSimpleName(), "manageVolumeHidden -> update volume with value:" + currentVolume);
          audio.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
        }
      }
    } else if (delta == (-app.getDeltaUp())) {
      down = false;
      //Log.i(getClass().getSimpleName(), "manageVolumeSettings -> Volume up (" + app.getTimeDelayUp() + ")");
      //Log.i(getClass().getSimpleName(), "manageVolumeSettings -> previousDelayUp:" + previousDelayUp + ", app.getTimeDelayUp():" + app.getTimeDelayUp()+ ", now:" + now);
      //Log.i(getClass().getSimpleName(), "manageVolumeSettings -> real: " +  (previousDelayUp + app.getTimeDelayUp()));
      if (previousDelayUp + app.getTimeDelayUp() <= now) {
        //Log.i(getClass().getSimpleName(), "manageVolumeSettings -> Volume up in delay");
        /* send the command and restore the volume */
        if (sendKeyEvent(KeyEvent.KEYCODE_MEDIA_NEXT)) {
          //Log.i(getClass().getSimpleName(), "manageVolumeHidden -> update volume with value:" + previousVolume);
          audio.setStreamVolume(AudioManager.STREAM_MUSIC, previousVolume, 0);
        }
      }
    }
    previousVolume = currentVolume;
    if(down)
      previousDelayDown = now;
    else
      previousDelayUp = now;
  }

  private void manageVolumeHidden(final AudioManager audio, final long now, final int currentVolume) {
    boolean down = false;
    final int delta = previousVolume - currentVolume;
    Log.i(getClass().getSimpleName(), "manageVolumeHidden -> currentVolume:" + currentVolume + ", previousVolume:"+previousVolume + ", delta:" + delta);
    if (delta >= 1
        || (previousVolume == 0 && currentVolume == 0)) {
      //Log.i(getClass().getSimpleName(), "manageVolumeHidden -> Volume down (" + app.getTimeDelayDown() + ")");
      //Log.i(getClass().getSimpleName(), "manageVolumeHidden -> previousDelayDown:" + previousDelayDown + ", app.getTimeDelayDown():" + app.getTimeDelayDown() + ", now:" + now);
      //Log.i(getClass().getSimpleName(), "manageVolumeHidden -> real: " +  (now - previousDelayDown));
      down = true;
      if ((now - previousDelayDown) <= app.getTimeDelayDown()) {
        //Log.i(getClass().getSimpleName(), "manageVolumeHidden -> Volume down in delay");
        /* send the command and restore the volume */
        if (sendKeyEvent(KeyEvent.KEYCODE_MEDIA_PREVIOUS)) {
          //Log.i(getClass().getSimpleName(), "manageVolumeHidden -> update volume with value:" + currentVolume);
          audio.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
        }
      }
    } else if (delta <= (-1)
        || (previousVolume == audio
            .getStreamMaxVolume(AudioManager.STREAM_MUSIC) && currentVolume == audio
            .getStreamMaxVolume(AudioManager.STREAM_MUSIC))) {
      //Log.i(getClass().getSimpleName(), "manageVolumeHidden -> Volume up (" + app.getTimeDelayUp() + ")");
      //Log.i(getClass().getSimpleName(), "manageVolumeHidden -> previousDelayUp:" + previousDelayUp + ", app.getTimeDelayUp():" + app.getTimeDelayUp()+ ", now:" + now);
      //Log.i(getClass().getSimpleName(), "manageVolumeHidden -> real: " +  (now - previousDelayUp));
      down = false;
      if ((now - previousDelayUp) <= app.getTimeDelayUp()) {
        //Log.i(getClass().getSimpleName(), "manageVolumeHidden -> Volume up in delay");
        /* send the command and restore the volume */
        if (sendKeyEvent(KeyEvent.KEYCODE_MEDIA_NEXT)) {
          //Log.i(getClass().getSimpleName(), "manageVolumeHidden -> update volume with value:" + previousVolume);
          audio.setStreamVolume(AudioManager.STREAM_MUSIC, previousVolume, 0);
        }
      }
    }
    previousVolume = currentVolume;
    if(down)
      previousDelayDown = now;
    else
      previousDelayUp = now;
  }
  

  private boolean sendKeyEvent(final int keyCode) {
    echo = true;
    //Log.i(getClass().getSimpleName(), "sendKeyEvent");
    // send "down" and "up" keyevents.
    KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
    final boolean first = mRemoteController.sendMediaKeyEvent(keyEvent);
    keyEvent = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
    final boolean second = mRemoteController.sendMediaKeyEvent(keyEvent);
    return first && second; // if both clicks were delivered successfully
  }
}