import java.lang.reflect.Field;

// -------------------------------------------------------------------------
/**
 * Tests for ChorePlanner, following the planner test plan: each public
 * method has a normal case and a bad-input or boundary case, plus the
 * section 8 integration scenario.
 *
 * @author Group 50
 * @version Oct 15, 2026
 */
public class ChorePlannerTest
    extends student.TestCase
{
    private ChorePlanner planner;

    // ----------------------------------------------------------
    /**
     * Creates a fresh planner before each test.
     */
    public void setUp()
    {
        planner = new ChorePlanner();
    }


    // ----------------------------------------------------------
    /**
     * Adds the three roommates from the specification example.
     */
    private void addExampleRoommates()
    {
        planner.addRoommate(10, "Aniket");
        planner.addRoommate(20, "Sam");
        planner.addRoommate(30, "Joe");
    }


    // ----------------------------------------------------------
    /**
     * Adds the four chores from the specification example.
     */
    private void addExampleChores()
    {
        planner.addChore("Trash", "");
        planner.addChore("Dishes", "");
        planner.addChore("Bathroom", "");
        planner.addChore("Vacuum", "");
    }


    // ----------------------------------------------------------
    /**
     * Pulls the roommate IDs out of a schedule, one per chore line.
     *
     * @param schedule
     *            the text from generateSchedule
     * @return the IDs in chore order, separated by commas
     */
    private String assignedIds(String schedule)
    {
        String[] lines = schedule.split("\n");
        StringBuilder ids = new StringBuilder();
        for (int i = 1; i < lines.length; i++)
        {
            int open = lines[i].lastIndexOf("(ID ");
            int close = lines[i].lastIndexOf(")");
            if (i > 1)
            {
                ids.append(",");
            }
            ids.append(lines[i].substring(open + 4, close));
        }
        return ids.toString();
    }


    // ----------------------------------------------------------
    /**
     * Joins the IDs of the roommates in a snapshot, e.g. "10,30".
     *
     * @param people
     *            the roommates
     * @return the IDs separated by commas
     */
    private String idsOf(Roommate[] people)
    {
        StringBuilder ids = new StringBuilder();
        for (int i = 0; i < people.length; i++)
        {
            if (i > 0)
            {
                ids.append(",");
            }
            ids.append(people[i].getId());
        }
        return ids.toString();
    }


    // ----------------------------------------------------------
    /**
     * Adds a roommate and returns the exception thrown, or null.
     *
     * @param id
     *            the ID to pass
     * @param name
     *            the name to pass
     * @return the exception, or null if the add succeeded
     */
    private Exception tryAddRoommate(int id, String name)
    {
        try
        {
            planner.addRoommate(id, name);
        }
        catch (Exception e)
        {
            return e;
        }
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Adds a chore and returns the exception thrown, or null.
     *
     * @param name
     *            the name to pass
     * @param description
     *            the description to pass
     * @return the exception, or null if the add succeeded
     */
    private Exception tryAddChore(String name, String description)
    {
        try
        {
            planner.addChore(name, description);
        }
        catch (Exception e)
        {
            return e;
        }
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Removes a roommate and returns the exception thrown, or null.
     *
     * @param index
     *            the zero-based position to remove
     * @return the exception, or null if the removal succeeded
     */
    private Exception tryRemoveRoommate(int index)
    {
        try
        {
            planner.removeRoommate(index);
        }
        catch (Exception e)
        {
            return e;
        }
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Removes a chore and returns the exception thrown, or null.
     *
     * @param index
     *            the zero-based position to remove
     * @return the exception, or null if the removal succeeded
     */
    private Exception tryRemoveChore(int index)
    {
        try
        {
            planner.removeChore(index);
        }
        catch (Exception e)
        {
            return e;
        }
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Advances the week and returns the exception thrown, or null.
     *
     * @return the exception, or null if the week advanced
     */
    private Exception tryAdvance()
    {
        try
        {
            planner.advanceWeek();
        }
        catch (Exception e)
        {
            return e;
        }
        return null;
    }


    // ----------------------------------------------------------
    /**
     * A new planner is in week 1 and has no roommates or chores.
     */
    public void testConstructor()
    {
        assertEquals(1, planner.getWeekNumber());
        assertEquals(0, planner.getRoommates().length);
        assertEquals(0, planner.getChores().length);
    }


    // ----------------------------------------------------------
    /**
     * A second planner shares no entries with the first.
     */
    public void testConstructorIndependent()
    {
        planner.addRoommate(10, "Sam");
        planner.addChore("Trash", "");
        ChorePlanner other = new ChorePlanner();
        assertEquals(0, other.getRoommates().length);
        assertEquals(0, other.getChores().length);
        assertEquals(1, planner.getRoommates().length);
    }


    // ----------------------------------------------------------
    /**
     * Two roommates with the same name but different IDs are both kept,
     * in insertion order.
     */
    public void testAddRoommate()
    {
        planner.addRoommate(10, "Sam");
        planner.addRoommate(20, "Sam");
        Roommate[] people = planner.getRoommates();
        assertEquals(2, people.length);
        assertEquals(10, people[0].getId());
        assertEquals(20, people[1].getId());
        assertEquals("Sam", people[1].getName());
    }


    // ----------------------------------------------------------
    /**
     * A duplicate ID, a non-positive ID, and a blank or null name are
     * rejected, and nothing is added.
     */
    public void testAddRoommateBad()
    {
        planner.addRoommate(10, "Sam");

        Exception e = tryAddRoommate(10, "Joe");
        assertTrue(e instanceof IllegalArgumentException);
        assertEquals("Roommate ID already exists.", e.getMessage());

        e = tryAddRoommate(0, "Joe");
        assertTrue(e instanceof IllegalArgumentException);
        e = tryAddRoommate(-5, "Joe");
        assertTrue(e instanceof IllegalArgumentException);
        e = tryAddRoommate(20, "");
        assertTrue(e instanceof IllegalArgumentException);
        assertEquals("Roommate name cannot be blank.", e.getMessage());
        e = tryAddRoommate(20, "   ");
        assertTrue(e instanceof IllegalArgumentException);
        e = tryAddRoommate(20, null);
        assertTrue(e instanceof IllegalArgumentException);

        assertEquals(1, planner.getRoommates().length);
        assertEquals("10", idsOf(planner.getRoommates()));
    }


    // ----------------------------------------------------------
    /**
     * A removed ID may be used again, since history is out of scope.
     */
    public void testAddRoommateReuseRemovedId()
    {
        planner.addRoommate(10, "Sam");
        planner.removeRoommate(0);
        assertNull(tryAddRoommate(10, "Joe"));
        assertEquals("Joe", planner.getRoommates()[0].getName());
    }


    // ----------------------------------------------------------
    /**
     * Removing position 1 of three returns that roommate and keeps the
     * other two in order.
     */
    public void testRemoveRoommate()
    {
        addExampleRoommates();
        Roommate removed = planner.removeRoommate(1);
        assertEquals(20, removed.getId());
        assertEquals("10,30", idsOf(planner.getRoommates()));
    }


    // ----------------------------------------------------------
    /**
     * Removal by position removes the selected roommate even when another
     * roommate has the same name.
     */
    public void testRemoveRoommateSameName()
    {
        planner.addRoommate(10, "Sam");
        planner.addRoommate(20, "Sam");
        Roommate removed = planner.removeRoommate(1);
        assertEquals(20, removed.getId());
        assertEquals("10", idsOf(planner.getRoommates()));
    }


    // ----------------------------------------------------------
    /**
     * A negative position, a position equal to the size, and any position
     * in an empty list are rejected without changing anything.
     */
    public void testRemoveRoommateBad()
    {
        assertTrue(tryRemoveRoommate(0) instanceof IndexOutOfBoundsException);

        addExampleRoommates();
        assertTrue(tryRemoveRoommate(-1) instanceof IndexOutOfBoundsException);
        assertTrue(tryRemoveRoommate(3) instanceof IndexOutOfBoundsException);
        assertEquals("10,20,30", idsOf(planner.getRoommates()));
    }


    // ----------------------------------------------------------
    /**
     * The snapshot holds the roommates in insertion order.
     */
    public void testGetRoommates()
    {
        planner.addRoommate(20, "Sam");
        planner.addRoommate(10, "Aniket");
        Roommate[] people = planner.getRoommates();
        assertEquals(2, people.length);
        assertEquals("Sam", people[0].getName());
        assertEquals("Aniket", people[1].getName());
    }


    // ----------------------------------------------------------
    /**
     * An empty planner gives a length-0 array, and changing a returned
     * array does not change the planner.
     */
    public void testGetRoommatesSnapshot()
    {
        assertEquals(0, planner.getRoommates().length);

        addExampleRoommates();
        Roommate[] people = planner.getRoommates();
        people[0] = null;
        assertEquals("10,20,30", idsOf(planner.getRoommates()));
    }


    // ----------------------------------------------------------
    /**
     * Chores are stored in the order they were added.
     */
    public void testAddChore()
    {
        planner.addChore("Trash", "Tie bag");
        planner.addChore("Dishes", "");
        Chore[] tasks = planner.getChores();
        assertEquals(2, tasks.length);
        assertEquals("Trash", tasks[0].getName());
        assertEquals("Tie bag", tasks[0].getDescription());
        assertEquals("Dishes", tasks[1].getName());
        assertEquals("", tasks[1].getDescription());
    }


    // ----------------------------------------------------------
    /**
     * A blank or null name and a null description are rejected without
     * adding anything; a repeated name and an empty description are fine.
     */
    public void testAddChoreBad()
    {
        Exception e = tryAddChore("", "Tie bag");
        assertTrue(e instanceof IllegalArgumentException);
        assertEquals("Chore name cannot be blank.", e.getMessage());
        e = tryAddChore("   ", "Tie bag");
        assertTrue(e instanceof IllegalArgumentException);
        e = tryAddChore(null, "Tie bag");
        assertTrue(e instanceof IllegalArgumentException);
        e = tryAddChore("Trash", null);
        assertTrue(e instanceof IllegalArgumentException);
        assertEquals(0, planner.getChores().length);

        assertNull(tryAddChore("Trash", ""));
        assertNull(tryAddChore("Trash", "Again"));
        assertEquals(2, planner.getChores().length);
    }


    // ----------------------------------------------------------
    /**
     * Removing position 0 of two returns the first chore and keeps the
     * second.
     */
    public void testRemoveChore()
    {
        planner.addChore("Trash", "");
        planner.addChore("Dishes", "");
        Chore removed = planner.removeChore(0);
        assertEquals("Trash", removed.getName());
        Chore[] left = planner.getChores();
        assertEquals(1, left.length);
        assertEquals("Dishes", left[0].getName());
    }


    // ----------------------------------------------------------
    /**
     * Removal by position removes only the selected chore when two chores
     * share a title.
     */
    public void testRemoveChoreSameName()
    {
        planner.addChore("Trash", "First");
        planner.addChore("Trash", "Second");
        Chore removed = planner.removeChore(1);
        assertEquals("Second", removed.getDescription());
        assertEquals("First", planner.getChores()[0].getDescription());
    }


    // ----------------------------------------------------------
    /**
     * Bad positions, including any position in an empty list, are rejected
     * and leave the chores unchanged.
     */
    public void testRemoveChoreBad()
    {
        assertTrue(tryRemoveChore(0) instanceof IndexOutOfBoundsException);

        planner.addChore("Trash", "");
        planner.addChore("Dishes", "");
        assertTrue(tryRemoveChore(-1) instanceof IndexOutOfBoundsException);
        assertTrue(tryRemoveChore(2) instanceof IndexOutOfBoundsException);
        assertEquals(2, planner.getChores().length);
    }


    // ----------------------------------------------------------
    /**
     * The snapshot holds the chores in insertion order.
     */
    public void testGetChores()
    {
        planner.addChore("Trash", "");
        planner.addChore("Dishes", "");
        Chore[] tasks = planner.getChores();
        assertEquals(2, tasks.length);
        assertEquals("Trash", tasks[0].getName());
        assertEquals("Dishes", tasks[1].getName());
    }


    // ----------------------------------------------------------
    /**
     * An empty planner gives a length-0 array, and changing a returned
     * array does not change the planner.
     */
    public void testGetChoresSnapshot()
    {
        assertEquals(0, planner.getChores().length);

        planner.addChore("Trash", "");
        Chore[] tasks = planner.getChores();
        tasks[0] = null;
        assertEquals("Trash", planner.getChores()[0].getName());
    }


    // ----------------------------------------------------------
    /**
     * The week starts at 1 and goes to 2 after one successful advance.
     */
    public void testGetWeekNumber()
    {
        assertEquals(1, planner.getWeekNumber());
        addExampleRoommates();
        addExampleChores();
        planner.advanceWeek();
        assertEquals(2, planner.getWeekNumber());
    }


    // ----------------------------------------------------------
    /**
     * A rejected advance leaves the week number unchanged.
     */
    public void testGetWeekNumberAfterRejectedAdvance()
    {
        assertNotNull(tryAdvance());
        assertEquals(1, planner.getWeekNumber());
    }


    // ----------------------------------------------------------
    /**
     * The schedule text lists the chore position, name, description,
     * roommate name and ID.
     */
    public void testGenerateScheduleFormat()
    {
        planner.addRoommate(10, "Aniket");
        planner.addChore("Trash", "Tie bag");
        planner.addChore("Dishes", "");
        assertEquals("Week 1 schedule:\n"
            + "1. Trash (Tie bag): Aniket (ID 10)\n"
            + "2. Dishes: Aniket (ID 10)",
            planner.generateSchedule());
    }


    // ----------------------------------------------------------
    /**
     * Three roommates and four chores rotate as in the specification
     * example, and the schedule repeats after three weeks.
     */
    public void testGenerateScheduleRotation()
    {
        addExampleRoommates();
        addExampleChores();
        assertEquals("10,20,30,10", assignedIds(planner.generateSchedule()));
        planner.advanceWeek();
        assertEquals("20,30,10,20", assignedIds(planner.generateSchedule()));
        planner.advanceWeek();
        assertEquals("30,10,20,30", assignedIds(planner.generateSchedule()));
        planner.advanceWeek();
        assertEquals(4, planner.getWeekNumber());
        assertEquals("10,20,30,10", assignedIds(planner.generateSchedule()));
    }


    // ----------------------------------------------------------
    /**
     * Generating the schedule again does not change the week or the text.
     */
    public void testGenerateScheduleRepeatable()
    {
        addExampleRoommates();
        addExampleChores();
        String first = planner.generateSchedule();
        assertEquals(first, planner.generateSchedule());
        assertEquals(1, planner.getWeekNumber());
    }


    // ----------------------------------------------------------
    /**
     * With no chores the schedule is the no-chores message, whether or
     * not there are roommates.
     */
    public void testGenerateScheduleNoChores()
    {
        assertEquals("Week 1: No chores to assign.",
            planner.generateSchedule());
        addExampleRoommates();
        assertEquals("Week 1: No chores to assign.",
            planner.generateSchedule());
    }


    // ----------------------------------------------------------
    /**
     * Chores with no roommates give guidance and do not divide by zero.
     */
    public void testGenerateScheduleNoRoommates()
    {
        addExampleChores();
        assertEquals("Week 1: Add at least one roommate before generating "
            + "a schedule.", planner.generateSchedule());
    }


    // ----------------------------------------------------------
    /**
     * One roommate keeps every chore each week.
     */
    public void testGenerateScheduleOneRoommate()
    {
        planner.addRoommate(10, "Aniket");
        addExampleChores();
        assertEquals("10,10,10,10", assignedIds(planner.generateSchedule()));
        planner.advanceWeek();
        assertEquals("10,10,10,10", assignedIds(planner.generateSchedule()));
    }


    // ----------------------------------------------------------
    /**
     * Four roommates and one chore: the assignee cycles through all four
     * IDs and then repeats.
     */
    public void testGenerateScheduleOneChore()
    {
        planner.addRoommate(10, "A");
        planner.addRoommate(20, "B");
        planner.addRoommate(30, "C");
        planner.addRoommate(40, "D");
        planner.addChore("Trash", "");
        assertEquals("10", assignedIds(planner.generateSchedule()));
        planner.advanceWeek();
        assertEquals("20", assignedIds(planner.generateSchedule()));
        planner.advanceWeek();
        assertEquals("30", assignedIds(planner.generateSchedule()));
        planner.advanceWeek();
        assertEquals("40", assignedIds(planner.generateSchedule()));
        planner.advanceWeek();
        assertEquals("10", assignedIds(planner.generateSchedule()));
    }


    // ----------------------------------------------------------
    /**
     * Advancing increments the week by one and rotates the assignments.
     */
    public void testAdvanceWeek()
    {
        addExampleRoommates();
        addExampleChores();
        String before = planner.generateSchedule();
        planner.advanceWeek();
        assertEquals(2, planner.getWeekNumber());
        assertFalse(before.equals(planner.generateSchedule()));
    }


    // ----------------------------------------------------------
    /**
     * Advancing with no roommates, no chores, or neither is rejected and
     * the week stays at 1.
     */
    public void testAdvanceWeekEmpty()
    {
        assertTrue(tryAdvance() instanceof IllegalStateException);

        addExampleChores();
        assertTrue(tryAdvance() instanceof IllegalStateException);

        planner.removeChore(0);
        planner.removeChore(0);
        planner.removeChore(0);
        planner.removeChore(0);
        addExampleRoommates();
        assertTrue(tryAdvance() instanceof IllegalStateException);

        assertEquals(1, planner.getWeekNumber());
    }


    // ----------------------------------------------------------
    /**
     * At the largest week number the advance is rejected and the week
     * does not wrap around. The week is set with reflection because the
     * planner has no public setter.
     */
    public void testAdvanceWeekAtMaximum()
    {
        addExampleRoommates();
        addExampleChores();
        try
        {
            Field week = ChorePlanner.class.getDeclaredField("weekNumber");
            week.setAccessible(true);
            week.setInt(planner, Integer.MAX_VALUE);
        }
        catch (Exception e)
        {
            fail("Could not set the week for this test: " + e);
        }
        Exception e = tryAdvance();
        assertTrue(e instanceof IllegalStateException);
        assertEquals("Week limit reached.", e.getMessage());
        assertEquals(Integer.MAX_VALUE, planner.getWeekNumber());
        assertTrue(planner.generateSchedule().startsWith("Week "
            + Integer.MAX_VALUE + " schedule:"));
    }


    // ----------------------------------------------------------
    /**
     * Edits in week 2 keep the week and recompute the assignments from
     * the updated lists (specification section 8 integration check).
     */
    public void testMidWeekEdits()
    {
        addExampleRoommates();
        addExampleChores();
        planner.advanceWeek();

        planner.removeRoommate(1);
        assertEquals(2, planner.getWeekNumber());
        assertEquals("10,30", idsOf(planner.getRoommates()));
        assertEquals("30,10,30,10", assignedIds(planner.generateSchedule()));

        planner.addRoommate(40, "Pat");
        assertEquals(2, planner.getWeekNumber());
        assertEquals("30,40,10,30", assignedIds(planner.generateSchedule()));

        planner.removeChore(3);
        String schedule = planner.generateSchedule();
        assertEquals("30,40,10", assignedIds(schedule));
        assertFalse(schedule.contains("Vacuum"));
    }


    // ----------------------------------------------------------
    /**
     * Removing the last roommate shows guidance; adding one back restores
     * the schedule. Removing the last chore shows the no-chores message
     * and then advancing is rejected.
     */
    public void testEmptyAfterRemoval()
    {
        planner.addRoommate(10, "Aniket");
        planner.addChore("Trash", "");
        planner.removeRoommate(0);
        assertEquals("Week 1: Add at least one roommate before generating "
            + "a schedule.", planner.generateSchedule());

        planner.addRoommate(20, "Sam");
        assertEquals("20", assignedIds(planner.generateSchedule()));

        planner.removeChore(0);
        assertEquals("Week 1: No chores to assign.",
            planner.generateSchedule());
        assertTrue(tryAdvance() instanceof IllegalStateException);
    }
}