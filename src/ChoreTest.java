// -------------------------------------------------------------------------
/**
 * Tests for Chore, following the storage and model test plan.
 *
 * @author Arrush Shah
 * @version Oct 15, 2026
 */
public class ChoreTest
    extends student.TestCase
{
    private Chore trash;

    // ----------------------------------------------------------
    /**
     * Creates a valid chore before each test.
     */
    public void setUp()
    {
        trash = new Chore(" Trash ", " Tie bag ");
    }


    // ----------------------------------------------------------
    /**
     * Tries to build a chore and returns the exception thrown, or null.
     *
     * @param name
     *            the name to pass
     * @param description
     *            the description to pass
     * @return the exception, or null if construction succeeded
     */
    private Exception construct(String name, String description)
    {
        try
        {
            new Chore(name, description);
        }
        catch (Exception e)
        {
            return e;
        }
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Both strings are stored trimmed.
     */
    public void testConstructor()
    {
        assertEquals("Trash", trash.getName());
        assertEquals("Tie bag", trash.getDescription());
    }


    // ----------------------------------------------------------
    /**
     * Null and blank names are rejected with the name message.
     */
    public void testConstructorBadName()
    {
        Exception e = construct(null, "x");
        assertTrue(e instanceof IllegalArgumentException);
        assertEquals("Chore name cannot be blank.", e.getMessage());

        e = construct("", "x");
        assertTrue(e instanceof IllegalArgumentException);

        e = construct("   ", "x");
        assertTrue(e instanceof IllegalArgumentException);
    }


    // ----------------------------------------------------------
    /**
     * A null description is rejected; empty and blank descriptions are
     * allowed.
     */
    public void testConstructorDescription()
    {
        Exception e = construct("Trash", null);
        assertTrue(e instanceof IllegalArgumentException);
        assertEquals("Chore description cannot be null.", e.getMessage());

        assertNull(construct("Trash", ""));
        assertNull(construct("Trash", "   "));
    }


    // ----------------------------------------------------------
    /**
     * getName returns the trimmed name.
     */
    public void testGetName()
    {
        assertEquals("Trash", trash.getName());
    }


    // ----------------------------------------------------------
    /**
     * A blank name cannot create an object; duplicate names are allowed.
     */
    public void testGetNameInvalid()
    {
        assertNotNull(construct("\t", ""));
        Chore secondTrash = new Chore("Trash", "");
        assertEquals(trash.getName(), secondTrash.getName());
    }


    // ----------------------------------------------------------
    /**
     * getDescription returns the trimmed description.
     */
    public void testGetDescription()
    {
        assertEquals("Tie bag", trash.getDescription());
    }


    // ----------------------------------------------------------
    /**
     * An empty or space-only description comes back as "".
     */
    public void testGetDescriptionEmpty()
    {
        assertEquals("", new Chore("Dishes", "").getDescription());
        assertEquals("", new Chore("Dishes", "   ").getDescription());
    }


    // ----------------------------------------------------------
    /**
     * toString includes the description only when there is one.
     */
    public void testToString()
    {
        assertEquals("Trash - Tie bag", trash.toString());
        assertEquals("Dishes", new Chore("Dishes", "").toString());
    }


    // ----------------------------------------------------------
    /**
     * Chore does not override equals: same fields, different objects.
     */
    public void testEqualsIsIdentity()
    {
        assertFalse(trash.equals(new Chore("Trash", "Tie bag")));
        assertTrue(trash.equals(trash));
    }
}
