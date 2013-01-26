/**
 * Test Vador sub activity
 */
package fr.lalourche.helloandroid.test;

import fr.lalourche.helloandroid.MainActivity;
import fr.lalourche.helloandroid.R;
import fr.lalourche.helloandroid.VadorActivity;

import com.jayway.android.robotium.solo.Solo;

import android.app.Activity;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;


/**
 * @author Lalourche
 *
 */
public abstract class VadorAbstractTest
  extends ActivityInstrumentationTestCase2<MainActivity>
{

  /** Main activity to test. */
  private Activity activity_;
  /** Activity resources. */
  private Resources resources_;

  /** Robotium solo. */
  private Solo solo_;

  /**
   * Default constructor.
   */
  public VadorAbstractTest()
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
    solo_ = new Solo(getInstrumentation(), activity_);
    solo_.assertCurrentActivity("Wrong activity !", MainActivity.class);

    // Customize solo
    updateSolo();
    getInstrumentation().waitForIdleSync();

    // Retrieve activity (taking into account orientation changes)
    activity_ = solo_.getCurrentActivity();

    // Retrieve main components
    resources_ = activity_.getResources();
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
   * Testing the starting of the sub activity.
   */
  public final void testOpenVador()
  {
    // Click on Vador
    solo_.clickOnText(resources_.getString(R.string.darthvader));
    // Test appearance of the new activity
    solo_.waitForActivity("VadorActivity", 2000);
    solo_.assertCurrentActivity("Expected Vador activity !",
        VadorActivity.class);
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
