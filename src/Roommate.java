// -------------------------------------------------------------------------
/**
 * One roommate in the chore planner. A roommate is identified by a
 * positive ID, so two roommates may share a name. Roommate objects are
 * immutable: there are no setters.
 *
 * Roommate does not override equals, so a LinkedChain compares roommates
 * by reference identity. Unique-ID checks belong to ChorePlanner.
 *
 * @author Arrush Shah
 * @version Oct 15, 2026
 */
public class Roommate
{
    private final int id;
    private final String name;

    // ----------------------------------------------------------
    /**
     * Creates a roommate with the given ID and name. The name is trimmed.
     *
     * @param id
     *            the roommate's ID; must be positive
     * @param name
     *            the roommate's name; must not be null or blank
     * @throws IllegalArgumentException
     *             if id is not positive or name is null or blank
     */
    public Roommate(int id, String name)
    {
        if (id <= 0)
        {
            throw new IllegalArgumentException(
                "Roommate ID must be positive.");
        }
        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "Roommate name cannot be blank.");
        }
        this.id = id;
        this.name = name.trim();
    }


    // ----------------------------------------------------------
    /**
     * Gets this roommate's ID.
     *
     * @return the positive ID
     */
    public int getId()
    {
        return id;
    }


    // ----------------------------------------------------------
    /**
     * Gets this roommate's trimmed name.
     *
     * @return the name
     */
    public String getName()
    {
        return name;
    }


    // ----------------------------------------------------------
    /**
     * Returns the name and ID, e.g. "Sam (ID 10)", so roommates with the
     * same name can be told apart in listings.
     *
     * @return a readable description of this roommate
     */
    @Override
    public String toString()
    {
        return name + " (ID " + id + ")";
    }
}
