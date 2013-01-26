/**
 * Main test.
 */
package fr.lalourche.helloandroid.test;

import java.text.MessageFormat;
import java.util.ArrayList;

import fr.lalourche.helloandroid.MainActivity;
import fr.lalourche.helloandroid.R;
import fr.lalourche.helloandroid.test.util.TestImageUtils;
import fr.lalourche.helloandroid.test.util.TouchUtilsEventListener;
import fr.lalourche.helloandroid.test.util.TouchUtilsWithEvents;

import com.jayway.android.robotium.solo.Solo;

import android.app.Activity;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;


/**
 * @author Lalourche
 *
 */
public abstract class MainActivityAbstractTest
  extends ActivityInstrumentationTestCase2<MainActivity>
{
  /** Current version. */
  private static final String VERSION = "0.0";

  /** Main activity to test. */
  private Activity activity_;
  /** Activity resources. */
  private Resources resources_;
  /** Input text field for name. */
  private EditText nameInput_;
  /** Menu button. */
  private ToggleButton menuButton_;
  /** Menu layout. */
  private View menuLayout_;
  /** Valid button. */
  private Button validButton_;
  /** Valid button label. */
  private String validButtonLabel_;
  /** Greetings text. */
  private TextView helloText_;

  /** Robotium solo. */
  private Solo solo_;

  /**
   * Default constructor.
   */
  public MainActivityAbstractTest()
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

    // Check that we have the right activity
    activity_ = getActivity();
//    Log.d("Lalourche", "Initial activity " + activity_);
    solo_ = new Solo(getInstrumentation(), activity_);
    solo_.assertCurrentActivity("Wrong activity !", MainActivity.class);

    // Customize solo
    updateSolo();
    getInstrumentation().waitForIdleSync();

    // Retrieve activity (taking into account orientation changes)
    activity_ = solo_.getCurrentActivity();
//    Log.d("Lalourche", "Oriented activity " + activity_);

    // Retrieve main components
    resources_ = activity_.getResources();
    nameInput_ = (EditText) activity_.findViewById(R.id.name);
    menuButton_ = (ToggleButton) activity_.findViewById(R.id.menuButton);
    menuLayout_ = activity_.findViewById(R.id.menuLayout);
    validButton_ = (Button) activity_.findViewById(R.id.nameButton);
    validButtonLabel_ = (String) resources_.getText(R.string.nameButton_label);
    helloText_ = (TextView) activity_.findViewById(R.id.text);
  }

  /**
   * Customize the Solo.
   * Ex. to change language or orientation.
   */
  protected abstract void updateSolo();

  /* (non-Javadoc)
   * @see android.test.ActivityInstrumentationTestCase2#tearDown()
   */
  @Override
  protected final void tearDown() throws Exception
  {
    activity_.finish();
    solo_.finishOpenedActivities();
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
    solo_.enterText(nameInput_, theName);

    // Click on valid button
    solo_.clickOnButton(validButtonLabel_);

    // Check the Hello
    String format = (String) resources_.getText(R.string.hello);
    assertEquals(MessageFormat.format(format, theName),
        helloText_.getText().toString());
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
   * Testing the display of the toast.
   */
  public final void testToast()
  {
    // Input non-admin name
    String theName = "NotAdmin";
    solo_.enterText(nameInput_, theName);

    // Test that the Toast does not show up
    String expectedToastString =
        (String) resources_.getText(R.string.toast);
    assertFalse("Expected a Toast !",
        solo_.waitForText(expectedToastString, 1, 2000));

    // Input admin name
    solo_.clearEditText(nameInput_);
    theName = (String) resources_.getText(R.string.adminName);
    solo_.enterText(nameInput_, theName);

    // Test that the Toast shows up
    assertTrue("Expected a Toast !",
        solo_.waitForText(expectedToastString, 1, 2000));
  }

  /**
   * Testing the display/hiding of the menu.
   */
  public final void testMenu()
  {
    // Test that menu is visible at init
    assertTrue("Menu button shall be activated at init",
        menuButton_.isChecked());
    assertTrue("Menu shall be visible at init",
        menuLayout_.getVisibility() == View.VISIBLE);

    // Test the hiding
    String toggleButtonLabel =
        (String) resources_.getText(R.string.menuButton_label);
    solo_.clickOnToggleButton(toggleButtonLabel);
    solo_.sleep(3000);
    assertTrue("Menu button shall be deactivated",
        !menuButton_.isChecked());
    assertTrue("Menu shall be hidden",
        menuLayout_.getVisibility() == View.GONE);

    // Test the showing
    solo_.clickOnToggleButton(toggleButtonLabel);
    solo_.sleep(3000);
    assertTrue("Menu button shall be activated",
        menuButton_.isChecked());
    assertTrue("Menu shall be visible",
        menuLayout_.getVisibility() == View.VISIBLE);
  }

  /**
   * Testing the about dialog.
   */
  public final void testAbout()
  {
    // Open Menu
    solo_.sendKey(Solo.MENU);

    // Select About
    solo_.clickOnText((String) resources_.getText(R.string.aboutLabel));

    // Check the version displayed
    solo_.waitForFragmentByTag(resources_.getString(R.string.tag_about_dialog));
    boolean foundVersion = solo_.searchText(VERSION);
    assertTrue("Expected current version display !", foundVersion);

    // Close dialog and check it is closed
    solo_.clickOnText((String) resources_.getText(android.R.string.ok));
    // Does not work. Bug Robotium ?
//    boolean aboutClosed = solo_.waitForDialogToClose(5000);
    boolean aboutClosed = !solo_.searchText(VERSION);
    assertTrue("Expected about dialog closed !", aboutClosed);
  }

  /**
   * Testing the content of the menu.
   */
  public final void testMenuContent()
  {
    // Test that menu is visible at init
    assertTrue("Menu shall be visible at init",
        menuLayout_.getVisibility() == View.VISIBLE);

    // Test the presence of texts
    int foundR2D2 = 0;
    int foundVador = 0;
    ArrayList<TextView> texts = solo_.getCurrentTextViews(menuLayout_);
    for (TextView text : texts)
    {
      if (text.getText().toString().equals(resources_.getText(R.string.r2d2)))
      {
        foundR2D2++;
      }
      if (text.getText().toString()
          .equals(resources_.getText(R.string.darthvader)))
      {
        foundVador++;
      }
    }
    assertEquals("One and only one R2D2 text expected !", 1, foundR2D2);
    assertEquals("One and only one Vader text expected !", 1, foundVador);

    // Test the presence of images
    foundR2D2 = 0;
    foundVador = 0;
    ArrayList<ImageView> images = solo_.getCurrentImageViews();
    for (ImageView image : images)
    {
      if (TestImageUtils.isEqual(activity_, image, R.drawable.r2d2))
      {
        foundR2D2++;
      }
      if (TestImageUtils.isEqual(activity_, image, R.drawable.darthvader))
      {
        foundVador++;
      }
    }
    assertEquals("One and only one R2D2 image expected !", 1, foundR2D2);
    assertEquals("One and only one Vader image expected !", 1, foundVador);
  }

  /**
   * Checks the size of the valid button text according to current
   * gesture position.
   * @param x coordinate
   * @param y coordinate
   */
  private void checkValidButtonTextSize(float x, float y)
  {
    float epsilon = 1.5f;

//    Log.d("Lalourche", "checkValidButtonTextSize " + x + " : " + y);

    float expectedSize =
        Math.abs(x - (validButton_.getWidth() / 2)) +
        Math.abs(y - (validButton_.getHeight() / 2));
    float delta = expectedSize - validButton_.getTextSize();

//    Log.d("Lalourche", Float.toString(delta));
    assertTrue("Incorrect size for valid button ! Delta = " + delta,
        delta < epsilon);
  }

  /**
   * @return the solo
   */
  public final Solo getSolo()
  {
    return solo_;
  }

  /**
   * @param solo the solo to set
   */
  public final void setSolo(Solo solo)
  {
    solo_ = solo;
  }
}
