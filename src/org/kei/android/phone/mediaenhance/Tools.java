package org.kei.android.phone.mediaenhance;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.PowerManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 *******************************************************************************
 * @file Tools.java
 * @author Keidan
 * @date 22/01/2016
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
public class Tools {

  public static boolean isScreenOn(final Context context) {
    final PowerManager pm = (PowerManager) context
        .getSystemService(Context.POWER_SERVICE);
    return pm.isScreenOn();
  }

  public static void toast(final Context context, final CharSequence message) {
    final Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
    final TextView tv = (TextView) toast.getView().findViewById(
        android.R.id.message);
    if (null != tv) {

      final Drawable drawable = context.getResources().getDrawable(
          R.drawable.ic_launcher);
      final Bitmap b = ((BitmapDrawable) drawable).getBitmap();
      final Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 32, 32, false);
      tv.setCompoundDrawablesWithIntrinsicBounds(
          new BitmapDrawable(context.getResources(), bitmapResized), null,
          null, null);
      tv.setCompoundDrawablePadding(5);
    }
    toast.show();
  }

  public static boolean isServiceRunning(final Context context,
      final Class<?> serviceClass) {
    final ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    for (final RunningServiceInfo service : manager
        .getRunningServices(Integer.MAX_VALUE)) {
      if (serviceClass.getName().equals(service.service.getClassName())) {
        return true;
      }
    }
    return false;
  }

}
