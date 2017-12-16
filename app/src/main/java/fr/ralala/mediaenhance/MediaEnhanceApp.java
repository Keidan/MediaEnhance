package fr.ralala.mediaenhance;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import fr.ralala.mediaenhance.mediakey.MediaKeyMethod;

/**
 *******************************************************************************
 * <p><b>Project MediaEnhance</b><br/>
 * Application context
 * </p>
 * @author Keidan
 *
 *******************************************************************************
 */
public class MediaEnhanceApp extends Application {
  public static final String MEDIA_ACTION_KEY  = "media_volume";
  public static final String KEY_VOLUME_METHOD = "volumeMethod";
  public static final String KEY_TIME_DELAY_UP = "timeDelayUp";
  public static final String KEY_TIME_DELAY_DOWN = "timeDelayDown";
  public static final String KEY_TIME_MIN_UP = "timeMinUp";
  public static final String KEY_TIME_MIN_DOWN = "timeMinDown";
  public static final String KEY_DELTA_DOWN = "deltaDown";
  public static final String KEY_DELTA_UP = "deltaUp";
  public static final String KEY_DISPLAY_TOAST = "displayToast";
  public static final int DEFAULT_TIME_DELAY = 250;
  public static final int DEFAULT_DELTA = 2;
  public static final int DEFAULT_TIME_MIN = 50;
  private long mTimeDelayUp = DEFAULT_TIME_DELAY;
  private long mTimeDelayDown = DEFAULT_TIME_DELAY;
  private long mTimeMinUp = DEFAULT_TIME_MIN;
  private long mTimeMinDown = DEFAULT_TIME_MIN;
  private int mDeltaUp = DEFAULT_DELTA;
  private int mDeltaDown = DEFAULT_DELTA;
  private boolean mSenpuku = false;
  private boolean mDisplayToast = false;
  private MediaKeyMethod mMediaKeyMethod = MediaKeyMethod.HIDDEN;


  /**
   * Loads the application configuration.
   */
  public void loadConfig() {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    mTimeDelayUp = prefs.getLong(KEY_TIME_DELAY_UP, DEFAULT_TIME_DELAY);
    mTimeDelayDown = prefs.getLong(KEY_TIME_DELAY_DOWN, DEFAULT_TIME_DELAY);
    mDeltaUp = prefs.getInt(KEY_DELTA_UP, DEFAULT_DELTA);
    mDeltaDown = prefs.getInt(KEY_DELTA_DOWN, DEFAULT_DELTA);
    mMediaKeyMethod = MediaKeyMethod.fromString(prefs.getString(KEY_VOLUME_METHOD, MediaKeyMethod.HIDDEN.toString()));
    mDisplayToast = prefs.getBoolean(KEY_DISPLAY_TOAST, false);
    mTimeMinUp = prefs.getLong(KEY_TIME_MIN_UP, DEFAULT_TIME_MIN);
    mTimeMinDown = prefs.getLong(KEY_TIME_MIN_DOWN, DEFAULT_TIME_MIN);
  }

  /**
   * Saves the application configuration.
   */
  public void saveConfig() {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    SharedPreferences.Editor e = prefs.edit();
    e.putLong(KEY_TIME_DELAY_UP, mTimeDelayUp);
    e.putLong(KEY_TIME_DELAY_DOWN, mTimeDelayDown);
    e.putInt(KEY_DELTA_UP, mDeltaUp);
    e.putInt(KEY_DELTA_DOWN, mDeltaDown);
    e.putString(KEY_VOLUME_METHOD, mMediaKeyMethod.toString());
    e.putBoolean(KEY_DISPLAY_TOAST, mDisplayToast);
    e.putLong(KEY_TIME_MIN_UP, mTimeMinUp);
    e.putLong(KEY_TIME_MIN_DOWN, mTimeMinDown);
    e.apply();
  }

  /**
   * Returns the trigger time for pressing the Volume Up button (in ms).
   * @return long
   */
  public long getTimeDelayUp() {
    return mTimeDelayUp;
  }

  /**
   * Sets the trigger time for pressing the Volume Up button (in ms).
   * @param timeDelayUp The new value.
   */
  public void setTimeDelayUp(long timeDelayUp) {
    mTimeDelayUp = timeDelayUp;
  }

  /**
   * Returns the trigger time for pressing the Volume Down button (in ms).
   * @return long
   */
  public long getTimeDelayDown() {
    return mTimeDelayDown;
  }

  /**
   * Sets the trigger time for pressing the Volume Down button (in ms).
   * @param timeDelayDown The new value.
   */
  public void setTimeDelayDown(long timeDelayDown) {
    mTimeDelayDown = timeDelayDown;
  }

  /**
   * Returns the number of presses before triggering the action on the Volume Up button.
   * @return int
   */
  public int getDeltaUp() {
    return mDeltaUp;
  }

  /**
   * Sets the number of presses before triggering the action on the Volume Up button.
   * @param deltaUp The new value.
   */
  public void setDeltaUp(int deltaUp) {
    mDeltaUp = deltaUp;
  }

  /**
   * Returns the number of presses before triggering the action on the Volume Down button.
   * @return int
   */
  public int getDeltaDown() {
    return mDeltaDown;
  }

  /**
   * Sets the number of presses before triggering the action on the Volume Down button.
   * @param deltaDown The new value.
   */
  public void setDeltaDown(int deltaDown) {
    mDeltaDown = deltaDown;
  }

  /**
   * Returns the method to use.
   * @return MediaKeyMethod
   */
  public MediaKeyMethod getMediaKeyMethod() {
    return mMediaKeyMethod;
  }

  /**
   * Sets the method to use.
   * @param mediaKeyMethod The new value.
   */
  public void setMediaKeyMethod(MediaKeyMethod mediaKeyMethod) {
    mMediaKeyMethod = mediaKeyMethod;
  }

  /**
   * Returns the senpuku status.
   * @return boolean
   */
  public boolean isSenpuku() {
    return mSenpuku;
  }

  /**
   * Sets the senpuku status.
   * @param senpuku The new value.
   */
  public void setSenpuku(boolean senpuku) {
    mSenpuku = senpuku;
  }

  /**
   * Returns whether the toast should be displayed when the service is started.
   * @return boolean
   */
  public boolean isDisplayToast() {
    return mDisplayToast;
  }

  /**
   * Indicates whether the toast should be displayed when the service is started.
   * @param displayToast The new value.
   */
  public void setDisplayToast(boolean displayToast) {
    mDisplayToast = displayToast;
  }

  /**
   * Returns the minimum delays between each press of the Volume Up button (in ms).
   * @return long
   */
  public long getTimeMinUp() {
    return mTimeMinUp;
  }

  /**
   * Sets the minimum delays between each press of the Volume Up button (in ms).
   * @param timeMinUp The new value.
   */
  public void setTimeMinUp(long timeMinUp) {
    mTimeMinUp = timeMinUp;
  }

  /**
   * Returns the minimum delays between each press of the Volume Down button (in ms).
   * @return long
   */
  public long getTimeMinDown() {
    return mTimeMinDown;
  }

  /**
   * Sets the minimum delays between each press of the Volume Down button (in ms).
   * @param timeMinDown The new value.
   */
  public void setTimeMinDown(long timeMinDown) {
    mTimeMinDown = timeMinDown;
  }

}
