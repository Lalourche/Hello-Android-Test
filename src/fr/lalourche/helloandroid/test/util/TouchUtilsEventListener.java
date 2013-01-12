/**
 * Interface for handling TouchScreen events.
 * @see android.test.TouchUtils
 */
package fr.lalourche.helloandroid.test.util;

import android.view.MotionEvent;


/**
 * @author Lalourche
 */
public interface TouchUtilsEventListener
{
  /**
   * Called when a drag step is performed.
   * @param event the MotionEvent
   */
  void onDragStep(MotionEvent event);
}
