/**
 * Util methods for testing images.
 */
package fr.lalourche.helloandroid.test.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * @author Lalourche
 *
 */
public final class TestImageUtils
{
  /**
   * Preventing instanciation of this utility class.
   */
  private TestImageUtils()
  {
    // Forbidden constructor
  }

  /**
   * Compares an Image view with a resource Drawable.
   * Only supports BitmapDrawable.
   * For testing only as it may have poor performances.
   * @param context current context
   * @param view the view to compare
   * @param drawableId the id of the resource drawable
   * @return true if the ImageView contains the same Drawable
   */
  public static boolean isEqual(
      Context context,
      ImageView view,
      int drawableId)
  {
    boolean result = false;

    Drawable drawable = view.getDrawable();
    Drawable referenceDrawable = context.getResources().getDrawable(drawableId);

    if (referenceDrawable instanceof BitmapDrawable &&
        drawable instanceof BitmapDrawable) {
      Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
      Bitmap referenceBitmap = ((BitmapDrawable) referenceDrawable).getBitmap();
      result = bitmap == referenceBitmap;
    }

    return result;
  }
}
