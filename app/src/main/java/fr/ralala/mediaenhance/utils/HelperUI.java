package fr.ralala.mediaenhance.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fr.ralala.mediaenhance.R;

/**
 *******************************************************************************
 * <p><b>Project MediaEnhance</b><br/>
 * Helper functions (HelperUI).
 * </p>
 * @author Keidan
 *
 *******************************************************************************
 */
public class HelperUI {


  /**
   * Displays a toast.
   * @param context The Android context.
   * @param message Toast message.
   */
  public static void toast(final Context context, CharSequence message) {
    Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
    TextView tv = toast.getView().findViewById(android.R.id.message);
    if (tv != null) {
      tv.setCompoundDrawablesWithIntrinsicBounds(resize32x32(context, R.mipmap.ic_launcher), null, null, null);
      tv.setCompoundDrawablePadding(5);
    }
    toast.show();
  }

  /**
   * Resize a drawable with a width and height at 32px
   * @param context The Android context.
   * @param image The image to resize.
   * @return Drawable
   */
  private static Drawable resize32x32(final Context context, final int image) {
    final Drawable d = ContextCompat.getDrawable(context, image);
    if(d == null)
      return null;
    final Bitmap b = ((BitmapDrawable) d).getBitmap();
    final Bitmap r = Bitmap.createScaledBitmap(b, 32, 32, false);
    return new BitmapDrawable(context.getResources(), r);
  }


  /**
   * Returns the long value from a String.
   * @param val The EditText.
   * @param def The default value.
   * @return long
   */
  public static long getLong(final EditText val, final long def) {
    return Helper.getLong(val.getText().toString(), def);
  }

  /**
   * Returns the int value from a String.
   * @param val The EditText.
   * @param def The default value.
   * @return int
   */
  public static int getInt(final EditText val, final int def) {
    return Helper.getInt(val.getText().toString(), def);
  }
}
