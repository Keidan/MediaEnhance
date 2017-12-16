package fr.ralala.mediaenhance.receivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import fr.ralala.mediaenhance.MediaEnhanceService;

/**
 *******************************************************************************
 * <p><b>Project MediaEnhance</b><br/>
 * Management of screen state notifications
 * </p>
 * @author Keidan
 *
 *******************************************************************************
 */
public class ScreenReceiver extends BroadcastReceiver {
  public static final String SCREEN_STATE_KEY = "screen_state";

  /**
   * Called by the system when the screen state is changed.
   * @param context An Android context.
   * @param intent Associated intent
   */
  @Override
  public void onReceive(final Context context, final Intent intent) {
    final String action = intent.getAction();
    if(action == null)
      return;
    final Intent i = new Intent(context, MediaEnhanceService.class);
    i.putExtra(SCREEN_STATE_KEY, action.equals(Intent.ACTION_SCREEN_OFF));
    context.startService(i);
  }

}