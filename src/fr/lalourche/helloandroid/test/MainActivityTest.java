/**
 * Main test.
 */
package fr.lalourche.helloandroid.test;

import java.text.MessageFormat;

import fr.lalourche.helloandroid.MainActivity;
import fr.lalourche.helloandroid.R;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
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

  /**
   * Default constructor.
   */
  public MainActivityTest()
  {
    super(MainActivity.class);
  }

  /**
   * Testing hello.
   */
  public final void testHello()
  {
    // Start the main activity of the application under test
    MainActivity activity = getActivity();

    // Input a name
    String theName = "Chub";
    EditText name = (EditText) activity.findViewById(R.id.name);
    TouchUtils.tapView(this, name);
    getInstrumentation().sendStringSync(theName);

    // Click on valid button
    Button validButton = (Button) activity.findViewById(R.id.nameButton);
    TouchUtils.tapView(this, validButton);

    // Check the Hello
    TextView text = (TextView) activity.findViewById(R.id.text);
    String format = (String) activity.getResources().getText(R.string.hello);
    assertEquals(MessageFormat.format(format, theName), text.getText());
  }

}
