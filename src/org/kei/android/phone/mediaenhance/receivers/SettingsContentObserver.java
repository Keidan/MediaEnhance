package org.kei.android.phone.mediaenhance.receivers;

import org.kei.android.phone.mediaenhance.MediaEnhanceApp;
import org.kei.android.phone.mediaenhance.MediaEnhanceService;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;

/**
 *******************************************************************************
 * @file SettingsContentObserver.java
 * @author ssuukk
 *         (http://stackoverflow.com/questions/10154118/listen-to-volume-buttons
 *         -in-background-service)
 *
 *******************************************************************************
 */
public class SettingsContentObserver extends ContentObserver {
  private Context            context          = null;
  
  public SettingsContentObserver(final Context c, final Handler handler) {
    super(handler);
    context = c;
  }
  
  @Override
  public boolean deliverSelfNotifications() {
    return super.deliverSelfNotifications();
  }
  
  @Override
  public void onChange(final boolean selfChange) {
    super.onChange(selfChange);
    final Intent i = new Intent(context, MediaEnhanceService.class);
    i.putExtra(MediaEnhanceApp.MEDIA_ACTION_KEY, MediaEnhanceApp.VOLUME_METHOD_SETTINGS);
    context.startService(i);
  }
}