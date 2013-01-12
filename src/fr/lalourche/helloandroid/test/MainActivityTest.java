/**
 * Main test.
 */
package fr.lalourche.helloandroid.test;

import java.text.MessageFormat;

import fr.lalourche.helloandroid.MainActivity;
import fr.lalourche.helloandroid.R;
import fr.lalourche.helloandroid.test.util.TouchUtilsEventListener;
import fr.lalourche.helloandroid.test.util.TouchUtilsWithEvents;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



/**
 * @author Lalourche
 *
 */
public class MainActivityTest
  extends ActivityInstrumentationTestCase2<MainActivity>
{
  /** Main activity to test. */
  private MainActivity activity_;
  /** Input text field for name. */
  private EditText nameInput_;
  /** Valid button. */
  private Button validButton_;
  /** Greetings text. */
  private TextView helloText_;

  /**
   * Default constructor.
   */
  public MainActivityTest()
  {
    super(MainActivity.class);
  }

  /* (non-Javadoc)
   * @see android.test.ActivityInstrumentationTestCase2#setUp()
   */
  @Override
  protected final void setUp() throws Exception
  {
    super.setUp();

    // Retrieve main components
    activity_ = getActivity();
    nameInput_ = (EditText) activity_.findViewById(R.id.name);
    validButton_ = (Button) activity_.findViewById(R.id.nameButton);
    helloText_ = (TextView) activity_.findViewById(R.id.text);
  }

  /* (non-Javadoc)
   * @see android.test.ActivityInstrumentationTestCase2#tearDown()
   */
  @Override
  protected final void tearDown() throws Exception
  {
    getActivity().finish();
    super.tearDown();
  }

  /**
   * Testing initial conditions.
   */
  public final void testPreConditions()
  {
    assertTrue("Initial message should be empty !",
        helloText_.getText().length() == 0);
  }

  /**
   * Testing hello.
   */
  public final void testHello()
  {
    // Input a name
    String theName = "Chub";
    TouchUtils.tapView(this, nameInput_);
    getInstrumentation().sendStringSync(theName);

    // Click on valid button
    TouchUtils.tapView(this, validButton_);

    // Check the Hello
    String format = (String) activity_.getResources().getText(R.string.hello);
    assertEquals(MessageFormat.format(format, theName), helloText_.getText());
  }

  /**
   * Testing size of valid button text according to touch position.
   */
  public final void testValidButtonTextSize()
  {
    // Retrieve the absolute top/left coordinate of the application
    int[] buttonTopLeft = new int[2];
    validButton_.getLocationOnScreen(buttonTopLeft);
    // Retrieve the absolute center coordinate of the validate button
    float buttonCenterX = buttonTopLeft[0] + (validButton_.getWidth() / 2);
    float buttonCenterY = buttonTopLeft[1] + (validButton_.getHeight() / 2);

    // Touch on the edge of the screen and drag to the button center
    TouchUtilsEventListener listener = new TouchUtilsEventListener()
    {
      public void onDragStep(MotionEvent event)
      {
        checkValidButtonTextSize(event.getX(), event.getY());
      }
    };
    TouchUtilsWithEvents.drag(this,
        buttonTopLeft[0], buttonCenterX, buttonTopLeft[1], buttonCenterY,
        10, listener);

    // Check police size at the end of the movement
    checkValidButtonTextSize(buttonCenterX, buttonCenterY);
  }

  /**
   * Checks the size of the valid button text according to current
   * gesture position.
   * @param x coordinate
   * @param y coordinate
   */
  private void checkValidButtonTextSize(float x, float y)
  {
    float epsilon = 1f;

//    Log.d("Lalourche", "checkValidButtonTextSize " + x + " : " + y);

    float expectedSize =
        Math.abs(x - (validButton_.getWidth() / 2)) +
        Math.abs(y - (validButton_.getHeight() / 2));
    float delta = expectedSize - validButton_.getTextSize();

//    Log.d("Lalourche", Float.toString(delta));
    assertTrue("Incorrect size for valid button !", delta < epsilon);
  }
}
