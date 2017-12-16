package fr.ralala.mediaenhance.receivers;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;

import fr.ralala.mediaenhance.MediaEnhanceApp;
import fr.ralala.mediaenhance.MediaEnhanceService;
import fr.ralala.mediaenhance.mediakey.MediaKeyMethod;

/**
 *******************************************************************************
 * @author ssuukk
 *         (http://stackoverflow.com/questions/10154118/listen-to-volume-buttons
 *         -in-background-service)
 *
 *******************************************************************************
 */
public class SettingsContentObserver extends ContentObserver {
  private Context mContext = null;

  public SettingsContentObserver(final Context c, final Handler handler) {
    super(handler);
    mContext = c;
  }

  @Override
  public boolean deliverSelfNotifications() {
    return super.deliverSelfNotifications();
  }

  /**
   * Starts the MediaEnhanceService service when the system call this method.
   * @param selfChange Self change.
   */
  @Override
  public void onChange(final boolean selfChange) {
    super.onChange(selfChange);
    final Intent i = new Intent(mContext, MediaEnhanceService.class);
    i.putExtra(MediaEnhanceApp.MEDIA_ACTION_KEY, MediaKeyMethod.SETTINGS.toString());
    mContext.startService(i);
  }
}