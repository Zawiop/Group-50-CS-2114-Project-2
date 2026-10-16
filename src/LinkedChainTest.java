// -------------------------------------------------------------------------
/**
 * Tests the supplied LinkedChain against the method contracts in section 4
 * of the specification, and checks that the same generic class stores both
 * application types (Roommate and Chore) with reference-identity equality.
 *
 * @author Aniket Nair-Das
 * @version Oct 15, 2026
 */
public class LinkedChainTest
    extends student.TestCase
{
    private LinkedChain<String> chain;

    // ----------------------------------------------------------
    /**
     * Creates a fresh, empty chain before each test.
     */
    public void setUp()
    {
        chain = new LinkedChain<String>();
    }


    // ----------------------------------------------------------
    /**
     * Fills the chain so that it reads, first to last, the given values.
     *
     * @param values
     *            the entries in order
     */
    private void fill(String... values)
    {
        for (String value : values)
        {
            chain.addToEnd(value);
        }
    }


    // ----------------------------------------------------------
    /**
     * Calls get and returns the exception it throws, or null.
     *
     * @param index
     *            the index to get
     * @return the exception, or null if get succeeded
     */
    private Exception tryGet(int index)
    {
        try
        {
            chain.get(index);
        }
        catch (Exception e)
        {
            return e;
        }
        return null;
    }


    // ----------------------------------------------------------
    /**
     * A new chain is empty.
     */
    public void testConstructor()
    {
        assertEquals(0, chain.size());
        assertTrue(chain.isEmpty());
        assertEquals("[]", chain.toString());
    }


    // ----------------------------------------------------------
    /**
     * Two chains do not share storage.
     */
    public void testConstructorIndependent()
    {
        LinkedChain<String> second = new LinkedChain<String>();
        chain.add("A");
        assertTrue(second.isEmpty());
        assertEquals(1, chain.size());
    }


    // ----------------------------------------------------------
    /**
     * add places the entry at the front.
     */
    public void testAdd()
    {
        chain.add("B");
        assertTrue(chain.add("A"));
        assertEquals("[A, B]", chain.toString());
        assertEquals(2, chain.size());
    }


    // ----------------------------------------------------------
    /**
     * add(null) throws and leaves the chain unchanged.
     */
    public void testAddNull()
    {
        chain.add("B");
        Exception thrown = null;
        try
        {
            chain.add(null);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals("[B]", chain.toString());
        assertEquals(1, chain.size());
    }


    // ----------------------------------------------------------
    /**
     * addToEnd appends, and creates the head on an empty chain.
     */
    public void testAddToEnd()
    {
        assertTrue(chain.addToEnd("A"));
        assertEquals("[A]", chain.toString());
        assertTrue(chain.addToEnd("B"));
        assertEquals("[A, B]", chain.toString());
    }


    // ----------------------------------------------------------
    /**
     * addToEnd(null) throws and leaves the chain unchanged.
     */
    public void testAddToEndNull()
    {
        fill("A");
        Exception thrown = null;
        try
        {
            chain.addToEnd(null);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals("[A]", chain.toString());
    }


    // ----------------------------------------------------------
    /**
     * remove() returns and removes the first entry.
     */
    public void testRemoveFirst()
    {
        fill("A", "B");
        assertEquals("A", chain.remove());
        assertEquals("[B]", chain.toString());
        assertEquals(1, chain.size());
    }


    // ----------------------------------------------------------
    /**
     * remove() on an empty chain returns null.
     */
    public void testRemoveFirstEmpty()
    {
        assertNull(chain.remove());
        assertEquals(0, chain.size());
    }


    // ----------------------------------------------------------
    /**
     * remove(T) removes only the first matching entry.
     */
    public void testRemoveEntry()
    {
        fill("A", "B", "A");
        assertTrue(chain.remove("A"));
        assertEquals("[B, A]", chain.toString());
        assertTrue(chain.remove("A"));
        assertEquals("[B]", chain.toString());
    }


    // ----------------------------------------------------------
    /**
     * remove(T) with an absent value, null, or an empty chain is false.
     */
    public void testRemoveEntryMissing()
    {
        assertFalse(chain.remove("A"));
        fill("A", "B");
        assertFalse(chain.remove("Z"));
        assertFalse(chain.remove(null));
        assertEquals("[A, B]", chain.toString());
        assertEquals(2, chain.size());
    }


    // ----------------------------------------------------------
    /**
     * clear empties the chain; get(0) then throws.
     */
    public void testClear()
    {
        fill("A", "B");
        chain.clear();
        assertTrue(chain.isEmpty());
        assertTrue(tryGet(0) instanceof IndexOutOfBoundsException);
    }


    // ----------------------------------------------------------
    /**
     * Clearing an empty chain twice is safe.
     */
    public void testClearEmpty()
    {
        chain.clear();
        chain.clear();
        assertEquals(0, chain.size());
    }


    // ----------------------------------------------------------
    /**
     * get returns the entry at a valid index without removing it.
     */
    public void testGet()
    {
        fill("A", "B");
        assertEquals("B", chain.get(1));
        assertEquals("A", chain.get(0));
        assertEquals(2, chain.size());
    }


    // ----------------------------------------------------------
    /**
     * get throws for negative, too-large, and empty-chain indices.
     */
    public void testGetOutOfBounds()
    {
        assertTrue(tryGet(0) instanceof IndexOutOfBoundsException);
        fill("A", "B");
        assertTrue(tryGet(-1) instanceof IndexOutOfBoundsException);
        assertTrue(tryGet(2) instanceof IndexOutOfBoundsException);
    }


    // ----------------------------------------------------------
    /**
     * contains finds a stored entry.
     */
    public void testContains()
    {
        fill("A", "B");
        assertTrue(chain.contains("B"));
    }


    // ----------------------------------------------------------
    /**
     * contains is false for absent values and null, on empty and nonempty
     * chains.
     */
    public void testContainsMissing()
    {
        assertFalse(chain.contains("Z"));
        assertFalse(chain.contains(null));
        fill("A", "B");
        assertFalse(chain.contains("Z"));
        assertFalse(chain.contains(null));
    }


    // ----------------------------------------------------------
    /**
     * count counts every equal entry.
     */
    public void testCount()
    {
        fill("A", "B", "A");
        assertEquals(2, chain.count("A"));
        assertEquals(1, chain.count("B"));
    }


    // ----------------------------------------------------------
    /**
     * count is 0 for absent values, null, and an empty chain.
     */
    public void testCountMissing()
    {
        assertEquals(0, chain.count("A"));
        fill("A");
        assertEquals(0, chain.count("Z"));
        assertEquals(0, chain.count(null));
    }


    // ----------------------------------------------------------
    /**
     * size tracks additions and removals.
     */
    public void testSize()
    {
        chain.add("A");
        chain.add("B");
        chain.remove();
        assertEquals(1, chain.size());
    }


    // ----------------------------------------------------------
    /**
     * size is 0 for a new chain and does not change on a failed removal.
     */
    public void testSizeEmpty()
    {
        assertEquals(0, chain.size());
        chain.remove("Z");
        assertEquals(0, chain.size());
    }


    // ----------------------------------------------------------
    /**
     * isEmpty is false with one entry.
     */
    public void testIsEmpty()
    {
        chain.add("A");
        assertFalse(chain.isEmpty());
    }


    // ----------------------------------------------------------
    /**
     * isEmpty is true for new, cleared, and last-entry-removed chains.
     */
    public void testIsEmptyBoundary()
    {
        assertTrue(chain.isEmpty());
        chain.add("A");
        chain.remove("A");
        assertTrue(chain.isEmpty());
        chain.add("A");
        chain.clear();
        assertTrue(chain.isEmpty());
    }


    // ----------------------------------------------------------
    /**
     * toArray fills and returns the same array, first entry first.
     */
    public void testToArray()
    {
        fill("A", "B");
        String[] values = new String[2];
        String[] result = chain.toArray(values);
        assertSame(values, result);
        assertEquals("A", result[0]);
        assertEquals("B", result[1]);
    }


    // ----------------------------------------------------------
    /**
     * toArray rejects null and too-small arrays; an empty chain fills a
     * length-0 array; surplus slots are left unchanged.
     */
    public void testToArrayBad()
    {
        fill("A", "B");
        Exception thrown = null;
        try
        {
            chain.toArray(null);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);

        thrown = null;
        try
        {
            chain.toArray(new String[1]);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);

        String[] big = { "x", "x", "SENTINEL" };
        chain.toArray(big);
        assertEquals("SENTINEL", big[2]);

        LinkedChain<String> empty = new LinkedChain<String>();
        assertEquals(0, empty.toArray(new String[0]).length);
    }


    // ----------------------------------------------------------
    /**
     * toString lists entries in chain order.
     */
    public void testToString()
    {
        fill("A", "B");
        assertEquals("[A, B]", chain.toString());
    }


    // ----------------------------------------------------------
    /**
     * toString of an empty chain is "[]".
     */
    public void testToStringEmpty()
    {
        assertEquals("[]", chain.toString());
    }


    // ----------------------------------------------------------
    /**
     * The same generic class stores Roommates in one chain and Chores in
     * another, returning correctly typed arrays.
     */
    public void testReuseWithBothModelTypes()
    {
        LinkedChain<Roommate> roommates = new LinkedChain<Roommate>();
        LinkedChain<Chore> chores = new LinkedChain<Chore>();

        Roommate aniket = new Roommate(10, "Aniket");
        Roommate sam = new Roommate(20, "Sam");
        roommates.addToEnd(aniket);
        roommates.addToEnd(sam);

        Chore trash = new Chore("Trash", "Tie bag");
        Chore dishes = new Chore("Dishes", "");
        chores.addToEnd(trash);
        chores.addToEnd(dishes);

        Roommate[] people = roommates.toArray(new Roommate[roommates.size()]);
        Chore[] tasks = chores.toArray(new Chore[chores.size()]);
        assertEquals(2, people.length);
        assertSame(aniket, people[0]);
        assertSame(sam, people[1]);
        assertEquals(2, tasks.length);
        assertSame(trash, tasks[0]);
        assertSame(dishes, tasks[1]);
    }


    // ----------------------------------------------------------
    /**
     * Model objects are compared by reference: an equal-looking new object
     * is not "contained", but the stored reference is.
     */
    public void testReferenceEquality()
    {
        LinkedChain<Roommate> roommates = new LinkedChain<Roommate>();
        Roommate stored = new Roommate(10, "Sam");
        roommates.addToEnd(stored);
        assertFalse(roommates.contains(new Roommate(10, "Sam")));
        assertTrue(roommates.contains(stored));

        LinkedChain<Chore> chores = new LinkedChain<Chore>();
        Chore first = new Chore("Trash", "");
        Chore second = new Chore("Trash", "");
        chores.addToEnd(first);
        chores.addToEnd(second);
        assertEquals(1, chores.count(first));
        assertFalse(chores.remove(new Chore("Trash", "")));
        assertEquals(2, chores.size());
    }


    // ----------------------------------------------------------
    /**
     * Removal by position as the planner does it (get then remove(T))
     * removes the exact stored object, not another roommate with the same
     * name.
     */
    public void testRemoveByPositionPattern()
    {
        LinkedChain<Roommate> roommates = new LinkedChain<Roommate>();
        Roommate firstSam = new Roommate(10, "Sam");
        Roommate secondSam = new Roommate(20, "Sam");
        roommates.addToEnd(firstSam);
        roommates.addToEnd(secondSam);

        Roommate target = roommates.get(1);
        assertTrue(roommates.remove(target));
        assertEquals(1, roommates.size());
        assertSame(firstSam, roommates.get(0));
    }
}
