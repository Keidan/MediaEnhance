package fr.ralala.mediaenhance.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import fr.ralala.mediaenhance.MediaEnhanceService;

/**
 *******************************************************************************
 * <p><b>Project MediaEnhance</b><br/>
 * Management of restart notifications
 * </p>
 * @author Keidan
 *
 *******************************************************************************
 */
public class RestartServiceReceiver extends BroadcastReceiver {

  /**
   * Called by the system when it is fully started.
   * @param context An Android context.
   * @param intent Not used.
   */
  @Override
  public void onReceive(final Context context, final Intent intent) {
    if(intent.getAction() == null) {
      Log.w(getClass().getSimpleName(), "System fully started with null action");
    }
    final Intent service = new Intent(context, MediaEnhanceService.class);
    context.startService(service);
  }
}
