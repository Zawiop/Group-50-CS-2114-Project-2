
import java.util.Scanner;
import student.TestCase;

/**
 * Tests the ChorePlannerApp console application.
 *
 * @author Group 50
 * @version Oct 16, 2026
 */
public class ChorePlannerAppTest extends TestCase
{
    /**
     * Tests exiting from the menu.
     */
    public void testExit()
    {
        Scanner scanner = new Scanner("0\n");
        ChorePlannerApp app = new ChorePlannerApp(scanner);

        app.run();

        assertFalse(scanner.hasNextLine());
        scanner.close();
    }


    /**
     * Tests invalid menu input followed by a valid exit.
     */
    public void testInvalidMenuInput()
    {
        Scanner scanner = new Scanner("hello\n9\n0\n");
        ChorePlannerApp app = new ChorePlannerApp(scanner);

        app.run();

        assertFalse(scanner.hasNextLine());
        scanner.close();
    }


    /**
     * Tests that EOF ends the application cleanly.
     */
    public void testEndOfInput()
    {
        Scanner scanner = new Scanner("");
        ChorePlannerApp app = new ChorePlannerApp(scanner);

        app.run();

        assertFalse(scanner.hasNextLine());
        scanner.close();
    }


    /**
     * Tests adding roommates and chores, then viewing the schedule.
     */
    public void testAddAndSchedule()
    {
        String commands =
            "1\n10\nAlex\n"
            + "1\n20\nSam\n"
            + "3\nTrash\nTake out trash\n"
            + "6\n0\n";

        Scanner scanner = new Scanner(commands);
        ChorePlannerApp app = new ChorePlannerApp(scanner);

        app.run();

        assertFalse(scanner.hasNextLine());
        scanner.close();
    }


    /**
     * Tests removal positions and invalid positions.
     */
    public void testRemoval()
    {
        String commands =
            "1\n10\nAlex\n"
            + "1\n20\nSam\n"
            + "2\n2\n"
            + "3\nTrash\nTake out trash\n"
            + "4\n1\n"
            + "0\n";

        Scanner scanner = new Scanner(commands);
        ChorePlannerApp app = new ChorePlannerApp(scanner);

        app.run();

        assertFalse(scanner.hasNextLine());
        scanner.close();
    }


    /**
     * Tests that a null Scanner is rejected.
     */
    public void testNullScanner()
    {
        try
        {
            new ChorePlannerApp(null);
            fail("Expected IllegalArgumentException.");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Scanner cannot be null.", e.getMessage());
        }
    }
}

