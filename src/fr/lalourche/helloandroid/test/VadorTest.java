/**
 * Main test for Portrait view.
 */
package fr.lalourche.helloandroid.test;

import com.jayway.android.robotium.solo.Solo;


/**
 * @author Lalourche
 *
 */
public class VadorTest extends VadorAbstractTest
{
  /* (non-Javadoc)
   * @see MainActivityAbstract#updateSolo()
   */
  @Override
  protected final void updateSolo()
  {
    getSolo().setActivityOrientation(Solo.PORTRAIT);
  }
}
