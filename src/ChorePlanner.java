// -------------------------------------------------------------------------
/**
 * Manages the roommates, the chores and the current week number, and
 * computes the weekly assignments. The planner owns two LinkedChain fields
 * (one per model type) and never inherits from LinkedChain. All validation
 * of the planner's own rules (unique IDs, scheduling preconditions) lives
 * here; field-level validation lives in the Roommate and Chore
 * constructors, and position checking is done by LinkedChain.get.
 *
 * Entries are appended with addToEnd so chain order matches display order.
 * No schedule is stored: generateSchedule derives it from the chains and
 * the week number every time, so it can never show a removed entry.
 *
 * @author Group 50
 * @version Oct 15, 2026
 */
public class ChorePlanner
{
    private final LinkedChain<Roommate> roommates;
    private final LinkedChain<Chore> chores;
    private int weekNumber;

    // ----------------------------------------------------------
    /**
     * Creates a planner with no roommates, no chores and week 1.
     */
    public ChorePlanner()
    {
        roommates = new LinkedChain<Roommate>();
        chores = new LinkedChain<Chore>();
        weekNumber = 1;
    }


    // ----------------------------------------------------------
    /**
     * Adds a roommate to the end of the roommate list.
     *
     * @param id
     *            the roommate's ID; must be positive and not already used
     * @param name
     *            the roommate's name; must not be null or blank
     * @throws IllegalArgumentException
     *             if the ID or name is invalid, or the ID already exists
     */
    public void addRoommate(int id, String name)
    {
        Roommate newRoommate = new Roommate(id, name);
        Roommate[] current = getRoommates();
        for (Roommate existing : current)
        {
            if (existing.getId() == id)
            {
                throw new IllegalArgumentException(
                    "Roommate ID already exists.");
            }
        }
        roommates.addToEnd(newRoommate);
    }


    // ----------------------------------------------------------
    /**
     * Removes the roommate at a zero-based position.
     *
     * @param index
     *            the zero-based position of the roommate to remove
     * @return the removed roommate
     * @throws IndexOutOfBoundsException
     *             if index is negative or not less than the number of
     *             roommates
     */
    public Roommate removeRoommate(int index)
    {
        Roommate stored = roommates.get(index);
        roommates.remove(stored);
        return stored;
    }


    // ----------------------------------------------------------
    /**
     * Gets a snapshot of the roommates in insertion order. Changing the
     * returned array does not change the planner.
     *
     * @return a new array of the roommates; length 0 if there are none
     */
    public Roommate[] getRoommates()
    {
        return roommates.toArray(new Roommate[roommates.size()]);
    }


    // ----------------------------------------------------------
    /**
     * Adds a chore to the end of the chore list. Chore names may repeat.
     *
     * @param name
     *            the chore name; must not be null or blank
     * @param description
     *            the instructions; may be empty but must not be null
     * @throws IllegalArgumentException
     *             if the name is blank or the description is null
     */
    public void addChore(String name, String description)
    {
        chores.addToEnd(new Chore(name, description));
    }


    // ----------------------------------------------------------
    /**
     * Removes the chore at a zero-based position.
     *
     * @param index
     *            the zero-based position of the chore to remove
     * @return the removed chore
     * @throws IndexOutOfBoundsException
     *             if index is negative or not less than the number of
     *             chores
     */
    public Chore removeChore(int index)
    {
        Chore stored = chores.get(index);
        chores.remove(stored);
        return stored;
    }


    // ----------------------------------------------------------
    /**
     * Gets a snapshot of the chores in insertion order. Changing the
     * returned array does not change the planner.
     *
     * @return a new array of the chores; length 0 if there are none
     */
    public Chore[] getChores()
    {
        return chores.toArray(new Chore[chores.size()]);
    }


    // ----------------------------------------------------------
    /**
     * Gets the current week number.
     *
     * @return the week number, which is always positive
     */
    public int getWeekNumber()
    {
        return weekNumber;
    }


    // ----------------------------------------------------------
    /**
     * Builds the schedule for the current week without changing the week.
     * Chore i goes to roommate (start + i % r) % r, where r is the number
     * of roommates and start is (weekNumber - 1) % r.
     *
     * @return the schedule text, or a one-line message if there are no
     *         chores or no roommates
     */
    public String generateSchedule()
    {
        if (chores.isEmpty())
        {
            return "Week " + weekNumber + ": No chores to assign.";
        }
        if (roommates.isEmpty())
        {
            return "Week " + weekNumber + ": Add at least one roommate "
                + "before generating a schedule.";
        }
        Roommate[] people = getRoommates();
        Chore[] tasks = getChores();
        int r = people.length;
        int start = (weekNumber - 1) % r;

        StringBuilder result = new StringBuilder();
        result.append("Week ").append(weekNumber).append(" schedule:");
        for (int i = 0; i < tasks.length; i++)
        {
            int who = (int)(((long)start + (i % r)) % r);
            result.append("\n").append(i + 1).append(". ");
            result.append(tasks[i].getName());
            if (!tasks[i].getDescription().isEmpty())
            {
                result.append(" (").append(tasks[i].getDescription())
                    .append(")");
            }
            result.append(": ").append(people[who].getName());
            result.append(" (ID ").append(people[who].getId()).append(")");
        }
        return result.toString();
    }


    // ----------------------------------------------------------
    /**
     * Moves to the next week so that assignments rotate.
     *
     * @throws IllegalStateException
     *             if there are no roommates or no chores, or the week
     *             number cannot be increased any further
     */
    public void advanceWeek()
    {
        if (roommates.isEmpty() || chores.isEmpty())
        {
            throw new IllegalStateException(
                "Add at least one roommate and one chore before "
                + "advancing the week.");
        }
        if (weekNumber == Integer.MAX_VALUE)
        {
            throw new IllegalStateException("Week limit reached.");
        }
        weekNumber++;
    }
}