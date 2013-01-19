/**
 * Main test (landscape).
 */
package fr.lalourche.helloandroid.test;

import com.jayway.android.robotium.solo.Solo;


/**
 * @author Lalourche
 *
 */
public class MainActivityLandscapeTest extends MainActivityAbstractTest
{
  /* (non-Javadoc)
   * @see MainActivityAbstract#updateSolo()
   */
  @Override
  protected final void updateSolo()
  {
    getSolo().setActivityOrientation(Solo.LANDSCAPE);
  }
}
