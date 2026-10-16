// -------------------------------------------------------------------------
/**
 * Tests for Roommate, following the storage and model test plan.
 *
 * @author Aniket Nair-Das
 * @version Oct 15, 2026
 */
public class RoommateTest
    extends student.TestCase
{
    private Roommate sam;

    // ----------------------------------------------------------
    /**
     * Creates a valid roommate before each test.
     */
    public void setUp()
    {
        sam = new Roommate(10, " Sam ");
    }


    // ----------------------------------------------------------
    /**
     * Tries to build a roommate and returns the exception thrown, or null.
     *
     * @param id
     *            the ID to pass
     * @param name
     *            the name to pass
     * @return the exception, or null if construction succeeded
     */
    private Exception construct(int id, String name)
    {
        try
        {
            new Roommate(id, name);
        }
        catch (Exception e)
        {
            return e;
        }
        return null;
    }


    // ----------------------------------------------------------
    /**
     * A valid ID and padded name are stored, with the name trimmed.
     */
    public void testConstructor()
    {
        assertEquals(10, sam.getId());
        assertEquals("Sam", sam.getName());
    }


    // ----------------------------------------------------------
    /**
     * ID 0 and negative IDs are rejected with the ID message.
     */
    public void testConstructorBadId()
    {
        Exception e = construct(0, "Sam");
        assertTrue(e instanceof IllegalArgumentException);
        assertEquals("Roommate ID must be positive.", e.getMessage());

        e = construct(-1, "Sam");
        assertTrue(e instanceof IllegalArgumentException);
    }


    // ----------------------------------------------------------
    /**
     * Null, empty, and space-only names are rejected with the name message.
     */
    public void testConstructorBadName()
    {
        Exception e = construct(10, null);
        assertTrue(e instanceof IllegalArgumentException);
        assertEquals("Roommate name cannot be blank.", e.getMessage());

        e = construct(10, "");
        assertTrue(e instanceof IllegalArgumentException);

        e = construct(10, "   ");
        assertTrue(e instanceof IllegalArgumentException);
    }


    // ----------------------------------------------------------
    /**
     * getId returns the stored ID; the largest int is a legal ID.
     */
    public void testGetId()
    {
        assertEquals(10, sam.getId());
        assertEquals(Integer.MAX_VALUE,
            new Roommate(Integer.MAX_VALUE, "Max").getId());
    }


    // ----------------------------------------------------------
    /**
     * An invalid ID never produces an object whose getId could be called.
     */
    public void testGetIdInvalid()
    {
        assertNotNull(construct(Integer.MIN_VALUE, "Sam"));
    }


    // ----------------------------------------------------------
    /**
     * getName returns the trimmed name; inner spaces are kept.
     */
    public void testGetName()
    {
        assertEquals("Sam", sam.getName());
        assertEquals("Mary Ann",
            new Roommate(5, "\tMary Ann \n").getName());
    }


    // ----------------------------------------------------------
    /**
     * A blank name never produces an object; duplicate names are legal.
     */
    public void testGetNameInvalid()
    {
        assertNotNull(construct(10, "\t\n"));
        Roommate otherSam = new Roommate(20, "Sam");
        assertEquals(sam.getName(), otherSam.getName());
        assertFalse(sam.getId() == otherSam.getId());
    }


    // ----------------------------------------------------------
    /**
     * toString shows the name and the ID.
     */
    public void testToString()
    {
        assertEquals("Sam (ID 10)", sam.toString());
    }


    // ----------------------------------------------------------
    /**
     * Roommate does not override equals: same fields, different objects.
     */
    public void testEqualsIsIdentity()
    {
        assertFalse(sam.equals(new Roommate(10, "Sam")));
        assertTrue(sam.equals(sam));
    }
}
