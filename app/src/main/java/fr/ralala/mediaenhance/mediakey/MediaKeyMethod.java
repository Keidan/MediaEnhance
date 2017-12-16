package fr.ralala.mediaenhance.mediakey;

/**
 *******************************************************************************
 * <p><b>Project MediaEnhance</b><br/>
 * MediaKey method
 * </p>
 * @author Keidan
 *
 *******************************************************************************
 */
public enum MediaKeyMethod {
  HIDDEN(0, "Hidden"),
  SETTINGS(1, "Settings");

  private String mMethod;
  private int mId;

  MediaKeyMethod(int id, String method) {
    mId = id;
    mMethod = method;
  }

  public int id() {
    return mId;
  }

  public String toString() {
    return mMethod;
  }

  public static MediaKeyMethod fromString(String method) {
    switch(method) {
      case "Hidden":
        return MediaKeyMethod.HIDDEN;
      case "Settings":
        return MediaKeyMethod.SETTINGS;
    }
    return null;
  }
}
