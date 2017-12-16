package fr.ralala.mediaenhance.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import fr.ralala.mediaenhance.MediaEnhanceApp;
import fr.ralala.mediaenhance.MediaEnhanceService;
import fr.ralala.mediaenhance.mediakey.MediaKeyMethod;

/**
 *******************************************************************************
 * <p><b>Project MediaEnhance</b><br/>
 * Management of audio volume key notifications
 * </p>
 * @author Keidan
 *
 *******************************************************************************
 */
public class VolumeChangeReceiver extends BroadcastReceiver {
  public static final String VOLUME_CHANGED_ACTION    = "android.media.VOLUME_CHANGED_ACTION";
  public static final String VOLUME_STREAM_VALUE      = "android.media.EXTRA_VOLUME_STREAM_VALUE";
  public static final String PREV_VOLUME_STREAM_VALUE = "android.media.EXTRA_PREV_VOLUME_STREAM_VALUE";

  /**
   * Called by the system when audio volume key state is change.
   * @param context An Android context.
   * @param intent Associated intent
   */
  @Override
  public void onReceive(final Context context, final Intent intent) {
    final String action = intent.getAction();
    if (action != null && action.equals(VOLUME_CHANGED_ACTION)) {
      final Intent i = new Intent(context, MediaEnhanceService.class);
      i.putExtra(MediaEnhanceApp.MEDIA_ACTION_KEY, MediaKeyMethod.HIDDEN.toString());
      i.putExtra(VOLUME_STREAM_VALUE, intent.getIntExtra(VOLUME_STREAM_VALUE, 0));
      i.putExtra(PREV_VOLUME_STREAM_VALUE, intent.getIntExtra(PREV_VOLUME_STREAM_VALUE, 0));
      context.startService(i);
    }
  }
}