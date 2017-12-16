package fr.ralala.mediaenhance.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.PowerManager;
import android.provider.Settings;


/**
 *******************************************************************************
 * <p><b>Project MediaEnhance</b><br/>
 * Helper functions.
 * </p>
 * @author Keidan
 *
 *******************************************************************************
 */
public class Helper {

  /**
   * Returns the permissions status for audio control.
   * @param context The Android context.
   * @return boolean
   */
  public static boolean hasAudioControlPermissions(final Context context) {
    return Settings.Secure.getString(context.getContentResolver(),
        "enabled_notification_listeners").contains(context.getApplicationContext().getPackageName());
  }

  /**
   * Returns the screen status (true=on, false=off).
   * @param context The Android context.
   * @return boolean
   */
  @SuppressWarnings("deprecation")
  public static boolean isScreenOn(final Context context) {
    final PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    return pm == null || pm.isScreenOn();
  }


  /**
   * Returns whether the service is running or not.
   * @param context The Android context.
   * @param serviceClass The service class.
   * @return boolean
   */
  public static boolean isServiceRunning(final Context context, final Class<?> serviceClass) {
    final ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    if(manager == null) return false;
    for (final ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
      if (serviceClass.getName().equals(service.service.getClassName())) {
        return true;
      }
    }
    return false;
  }


  /**
   * Returns the long value from a String.
   * @param val The long in String.
   * @param def The default value.
   * @return long
   */
  static long getLong(final String val, final long def) {
    try {
      return Long.parseLong(val);
    } catch(Exception ex) {
      return def;
    }
  }

  /**
   * Returns the int value from a String.
   * @param val The int in String.
   * @param def The default value.
   * @return int
   */
  static int getInt(final String val, final int def) {
    try {
      return Integer.parseInt(val);
    } catch(Exception ex) {
      return def;
    }
  }
}
