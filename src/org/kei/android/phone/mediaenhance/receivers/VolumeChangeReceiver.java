package org.kei.android.phone.mediaenhance.receivers;

import org.kei.android.phone.mediaenhance.MediaEnhanceApp;
import org.kei.android.phone.mediaenhance.MediaEnhanceService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 *******************************************************************************
 * @file VolumeChangeReceiver.java
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
public class VolumeChangeReceiver extends BroadcastReceiver {
  public static final String VOLUME_CHANGED_ACTION    = "android.media.VOLUME_CHANGED_ACTION";
  public static final String VOLUME_STREAM_VALUE      = "VOLUME_STREAM_VALUE";
  public static final String PREV_VOLUME_STREAM_VALUE = "PREV_VOLUME_STREAM_VALUE";

  @Override
  public void onReceive(final Context context, final Intent intent) {
    if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
      final int newVolume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", 0);
      final int oldVolume = intent.getIntExtra("android.media.EXTRA_PREV_VOLUME_STREAM_VALUE", 0);

      final Intent i = new Intent(context, MediaEnhanceService.class);
      i.putExtra(MediaEnhanceApp.MEDIA_ACTION_KEY,
          MediaEnhanceApp.VOLUME_METHOD_HIDDEN);
      i.putExtra(VOLUME_STREAM_VALUE, newVolume);
      i.putExtra(PREV_VOLUME_STREAM_VALUE, oldVolume);
      context.startService(i);
    }
  }
}