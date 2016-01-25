package org.kei.android.phone.mediaenhance.receivers;

import org.kei.android.phone.mediaenhance.MediaEnhanceService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 *******************************************************************************
 * @file RestartServiceReceiver.java
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
public class RestartServiceReceiver extends BroadcastReceiver {
  
  @Override
  public void onReceive(final Context context, final Intent intent) {
    final Intent service = new Intent(context, MediaEnhanceService.class);
    context.startService(service);
  }
}
