package org.kei.android.phone.mediaenhance;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 *******************************************************************************
 * @file MediaEnhanceApp.java
 * @author Keidan
 * @date 24/01/2016
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
public class MediaEnhanceApp extends Application {
  public static final String MEDIA_ACTION_KEY       = "media_volume";
  public static final String VOLUME_METHOD_HIDDEN   = "Hidden";
  public static final String VOLUME_METHOD_SETTINGS = "Settings";
  public static final String KEY_VOLUME_METHOD      = "volumeMethod";
  public static final String KEY_TIME_DELAY_UP      = "timeDelayUp";
  public static final String KEY_TIME_DELAY_DOWN    = "timeDelayDown";
  public static final String KEY_DELTA_DOWN         = "deltaDown";
  public static final String KEY_DELTA_UP           = "deltaUp";
  public static final int    DEFAULT_TIME_DELAY     = 250;
  public static final int    DEFAULT_DELTA          = 2;
  private long               timeDelayUp            = DEFAULT_TIME_DELAY;
  private long               timeDelayDown          = DEFAULT_TIME_DELAY;
  private int                deltaUp                = DEFAULT_DELTA;
  private int                deltaDown              = DEFAULT_DELTA;
  private boolean            senpuku                = false;
  private String             volumeMethod           = VOLUME_METHOD_HIDDEN;
  
  

  public void loadConfig() {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    timeDelayUp = prefs.getLong(KEY_TIME_DELAY_UP, DEFAULT_TIME_DELAY);
    timeDelayDown = prefs.getLong(KEY_TIME_DELAY_DOWN, DEFAULT_TIME_DELAY);
    deltaUp = prefs.getInt(KEY_DELTA_UP, DEFAULT_DELTA);
    deltaDown = prefs.getInt(KEY_DELTA_DOWN, DEFAULT_DELTA);
    volumeMethod = prefs.getString(KEY_VOLUME_METHOD, VOLUME_METHOD_HIDDEN);
  }
  
  public void saveConfig() {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    Editor e = prefs.edit();
    e.putLong(KEY_TIME_DELAY_UP, timeDelayUp);
    e.putLong(KEY_TIME_DELAY_DOWN, timeDelayDown);
    e.putInt(KEY_DELTA_UP, deltaUp);
    e.putInt(KEY_DELTA_DOWN, deltaDown);
    e.putString(KEY_VOLUME_METHOD, volumeMethod);
    e.commit();
  }

  /**
   * @return the timeDelayUp
   */
  public long getTimeDelayUp() {
    return timeDelayUp;
  }

  /**
   * @param timeDelayUp the timeDelayUp to set
   */
  public void setTimeDelayUp(long timeDelayUp) {
    this.timeDelayUp = timeDelayUp;
  }

  /**
   * @return the timeDelayDown
   */
  public long getTimeDelayDown() {
    return timeDelayDown;
  }

  /**
   * @param timeDelayDown the timeDelayDown to set
   */
  public void setTimeDelayDown(long timeDelayDown) {
    this.timeDelayDown = timeDelayDown;
  }

  /**
   * @return the deltaUp
   */
  public int getDeltaUp() {
    return deltaUp;
  }

  /**
   * @param deltaUp the deltaUp to set
   */
  public void setDeltaUp(int deltaUp) {
    this.deltaUp = deltaUp;
  }

  /**
   * @return the deltaDown
   */
  public int getDeltaDown() {
    return deltaDown;
  }

  /**
   * @param deltaDown the deltaDown to set
   */
  public void setDeltaDown(int deltaDown) {
    this.deltaDown = deltaDown;
  }

  /**
   * @return the volumeMethod
   */
  public String getVolumeMethod() {
    return volumeMethod;
  }

  /**
   * @param volumeMethod the volumeMethod to set
   */
  public void setVolumeMethod(String volumeMethod) {
    this.volumeMethod = volumeMethod;
  }

  /**
   * @return the senpuku
   */
  public boolean isSenpuku() {
    return senpuku;
  }

  /**
   * @param senpuku the senpuku to set
   */
  public void setSenpuku(boolean senpuku) {
    this.senpuku = senpuku;
  }

}
