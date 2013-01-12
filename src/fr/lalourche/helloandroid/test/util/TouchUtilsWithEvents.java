/**
 * Added event handling in TouchUtils method in order to intercept intermediate
 * events during gesture simulation.
 */
package fr.lalourche.helloandroid.test.util;

import android.app.Instrumentation;
import android.os.SystemClock;
import android.test.InstrumentationTestCase;
import android.test.TouchUtils;
import android.view.MotionEvent;

/**
 * @author Lalourche
 */
public class TouchUtilsWithEvents extends TouchUtils
{

  /**
   * Simulate touching a specific location and dragging to a new location.
   * Notifies an interface at each step of the move.
   * @param test
   *          The test case that is being run
   * @param fromX
   *          X coordinate of the initial touch, in screen coordinates
   * @param toX
   *          Xcoordinate of the drag destination, in screen coordinates
   * @param fromY
   *          X coordinate of the initial touch, in screen coordinates
   * @param toY
   *          Y coordinate of the drag destination, in screen coordinates
   * @param stepCount
   *          How many move steps to include in the drag
   * @param listener
   *          listener for handling the event generated at each step
   */
  public static void drag(
      InstrumentationTestCase test,
      float fromX, float toX,
      float fromY, float toY,
      int stepCount,
      TouchUtilsEventListener listener)
  {
    Instrumentation inst = test.getInstrumentation();

    long downTime = SystemClock.uptimeMillis();
    long eventTime = SystemClock.uptimeMillis();

    float y = fromY;
    float x = fromX;

    float yStep = (toY - fromY) / stepCount;
    float xStep = (toX - fromX) / stepCount;

    MotionEvent event = MotionEvent.obtain(downTime, eventTime,
        MotionEvent.ACTION_DOWN, x, y, 0);
//    Log.d("Lalourche", "Avant for " + event.getX() + " : " + event.getY());
    inst.sendPointerSync(event);
    inst.waitForIdleSync();
    listener.onDragStep(event);

    for (int i = 0; i < stepCount; ++i) {
      y += yStep;
      x += xStep;
      eventTime = SystemClock.uptimeMillis();
      event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE,
          x, y, 0);
//      Log.d("Lalourche", "Dans for " + event.getX() + " : " + event.getY());
      inst.sendPointerSync(event);
      inst.waitForIdleSync();
      listener.onDragStep(event);
    }

    eventTime = SystemClock.uptimeMillis();
    event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x,
        y, 0);
//    Log.d("Lalourche", "Après for " + event.getX() + " : " + event.getY());
    inst.sendPointerSync(event);
    inst.waitForIdleSync();
    listener.onDragStep(event);
  }
}
