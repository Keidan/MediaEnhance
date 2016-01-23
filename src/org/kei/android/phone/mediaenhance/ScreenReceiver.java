package org.kei.android.phone.mediaenhance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 *******************************************************************************
 * @file ScreenReceiver.java
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
public class ScreenReceiver extends BroadcastReceiver {
  public static final String SCREEN_STATE_KEY = "screen_state";
  private boolean            screenOff        = false;
  
  @Override
  public void onReceive(final Context context, final Intent intent) {
    if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
      screenOff = true;
    } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
      screenOff = false;
    }
    final Intent i = new Intent(context, MediaEnhanceService.class);
    i.putExtra(SCREEN_STATE_KEY, screenOff);
    context.startService(i);
  }
  
}