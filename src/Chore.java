// -------------------------------------------------------------------------
/**
 * One chore in the chore planner: a required name and optional
 * instructions. Chore objects are immutable: there are no setters.
 *
 * Chore does not override equals, so a LinkedChain compares chores by
 * reference identity. Two chores with the same name are separate entries.
 *
 * @author Arrush Shah
 * @version Oct 15, 2026
 */
public class Chore
{
    private final String name;
    private final String description;

    // ----------------------------------------------------------
    /**
     * Creates a chore. Both strings are trimmed; the description may be
     * empty.
     *
     * @param name
     *            the chore name; must not be null or blank
     * @param description
     *            the instructions; may be empty but must not be null
     * @throws IllegalArgumentException
     *             if name is null or blank, or description is null
     */
    public Chore(String name, String description)
    {
        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("Chore name cannot be blank.");
        }
        if (description == null)
        {
            throw new IllegalArgumentException(
                "Chore description cannot be null.");
        }
        this.name = name.trim();
        this.description = description.trim();
    }


    // ----------------------------------------------------------
    /**
     * Gets this chore's trimmed name.
     *
     * @return the name
     */
    public String getName()
    {
        return name;
    }


    // ----------------------------------------------------------
    /**
     * Gets this chore's trimmed description.
     *
     * @return the description, or "" if none was given
     */
    public String getDescription()
    {
        return description;
    }


    // ----------------------------------------------------------
    /**
     * Returns the name, followed by the description if there is one,
     * e.g. "Trash - Tie bag".
     *
     * @return a readable description of this chore
     */
    @Override
    public String toString()
    {
        if (description.isEmpty())
        {
            return name;
        }
        return name + " - " + description;
    }
}
