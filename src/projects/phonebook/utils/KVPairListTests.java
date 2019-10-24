package projects.phonebook.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * <p>{@link KVPairList} is a small jUnit - based test suite for {@link KVPairList}.</p>
 *
 *  @author <a href="mailto:jason.filippou@gmail.com">Jason Filippou</a>
 *
 * @see KVPairList
 */
public class KVPairListTests {

    /* ******************************** */
    /* Private data fields and methods. */
    /* ******************************** */
    
    
    private KVPairList list;
    
    private static String format(Throwable t){
        return "Caught a " + t.getClass().getSimpleName()+ " with message: " + t.getMessage() + ".";
    }
    
    /* *********** */
    /* jUnit tests */
    /* *********** */
    
    @Before
    public void setUp(){
        list = new KVPairList();
    }

    @After
    public void tearDown(){
        list = null;
    }

    @Test
    public void testConstructors(){

        assertTrue("A KVPairList initialized to be empty should report that it's empty.", list.isEmpty());
        assertEquals("A KVPairList initialized to be empty should report a count of zero.", 0, list.size());

        list = new KVPairList("Adam", "707-890-3568"); // In the spirit of the project, let's put a name and number in
        assertFalse("A KVPairList initialized with a single node should NOT report that it's empty.", list.isEmpty());
        assertEquals("A KVPairList initialized with a single node should report a count of one.", 1, list.size());
    }

    @Test
    public void testTailInsertions() {
        try {
            list.addBack("Joseph", "890-567-9002");
            list.addBack("Adam", "707-890-3568");
            list.addBack("Rayeesha", "403-111-2000");
        } catch(Throwable t){
            fail(format(t));
        }
        assertFalse("A KVPairList with three nodes inserted in the back should NOT report that it's empty.", list.isEmpty());
        assertEquals("A KVPairList with three nodes inserted in the back should report a size of 3.", 3 , list.size());
        try {
            assertTrue("After inserting it, we should be able to find a key in the KVPairList.", list.containsKVPair("Joseph", "890-567-9002"));
            assertTrue("After inserting it, we should be able to find a key in the KVPairList.", list.containsKVPair("Adam", "707-890-3568"));
            assertTrue("After inserting it, we should be able to find a key in the KVPairList.", list.containsKVPair("Rayeesha", "403-111-2000"));
        } catch(Throwable t){
            fail(format(t));
        }
    }


    @Test
    public void testHeadInsertions() {
        try {
            list.addBack("Joseph", "890-567-9002");
            list.addBack("Adam", "707-890-3568");
            list.addBack("Rayeesha", "403-111-2000");
        } catch(Throwable t){
            fail(format(t));
        }
        assertFalse("A KVPairList with three nodes inserted in the front should NOT report that it's empty.", list.isEmpty());
        assertEquals("A KVPairList with three nodes inserted in the front should report a size of 3.", 3 , list.size());

        try {
            assertTrue("After inserting it, we should be able to find a key in the KVPairList.", list.containsKVPair("Joseph", "890-567-9002"));
            assertTrue("After inserting it, we should be able to find a key in the KVPairList.", list.containsKVPair("Adam", "707-890-3568"));
            assertTrue("After inserting it, we should be able to find a key in the KVPairList.", list.containsKVPair("Rayeesha", "403-111-2000"));
        } catch(Throwable t){
            fail(format(t));
        }

    }

    @Test
    public void testDeletions(){
        list.addBack("Joseph", "890-567-9002");
        list.addBack("Adam", "707-890-3568");
        list.addBack("Rayeesha", "403-111-2000");

        // First, a deletion of an element that does not exist in the list.
        try {
            list.remove("Marie", "789-429-1095");
        } catch(Throwable t){
            fail(format(t));
        }

        // We should be able to find all the existing nodes *and* the count should remain equal to 3.
        assertFalse("A KVPairList with three nodes inserted and a failed deletion should still NOT report that it's empty.", list.isEmpty());
        assertEquals("A KVPairList with three nodes inserted and a failed deletion should still report a size of 3.", 3 , list.size());

        try {
            assertTrue("After inserting it and NOT deleting it, we should be able to find a key in the KVPairList.", list.containsKVPair("Joseph", "890-567-9002"));
            assertTrue("After inserting it and NOT deleting it, we should be able to find a key in the KVPairList.", list.containsKVPair("Adam", "707-890-3568"));
            assertTrue("After inserting it and NOT deleting it, we should be able to find a key in the KVPairList.", list.containsKVPair("Rayeesha", "403-111-2000"));
        } catch(Throwable t){
            fail(format(t));
        }

        // One deletion.
        try {
            list.remove("Joseph", "890-567-9002");
        } catch(Throwable t){
            fail(format(t));
        }

        assertFalse("A KVPairList with three nodes inserted and one successful deletion should still NOT report that it's empty.", list.isEmpty());
        assertEquals("A KVPairList with three nodes inserted and one successful deletion should report a size of 2.", 2 , list.size());

        try {
            assertFalse("After inserting it and deleting it, we should NOT be able to find a key in the KVPairList.", list.containsKVPair("Joseph", "890-567-9002"));
            assertTrue("After inserting it and NOT deleting it, we should be able to find a key in the KVPairList.", list.containsKVPair("Adam", "707-890-3568"));
            assertTrue("After inserting it and NOT deleting it, we should be able to find a key in the KVPairList.", list.containsKVPair("Rayeesha", "403-111-2000"));
        } catch(Throwable t){
            fail(format(t));
        }

        // Another deletion.
        try {
            list.remove("Adam", "707-890-3568");
        } catch(Throwable t){
            fail(format(t));
        }
        assertFalse("A KVPairList with three nodes inserted and two successful deletions should still NOT report that it's empty.", list.isEmpty());
        assertEquals("A KVPairList with three nodes inserted and two successful deletions should report a size of 1.", 1 , list.size());

        try {
            assertFalse("After inserting it and deleting it, we should NOT be able to find a key in the KVPairList.", list.containsKVPair("Adam", "707-890-3568"));
            assertTrue("After inserting it and NOT deleting it, we should be able to find a key in the KVPairList.", list.containsKVPair("Rayeesha", "403-111-2000"));
        } catch(Throwable t){
            fail(format(t));
        }

        // A final one.
        try {
            list.remove("Rayeesha", "403-111-2000");
        } catch(Throwable t){
            fail(format(t));
        }
        assertTrue("A KVPairList with three nodes inserted and three successful deletions SHOULD  report that it's empty.", list.isEmpty());
        assertEquals("A KVPairList with three nodes inserted and three successful deletions SHOULD report a size of 0.", 0, list.size());

        try {
            assertFalse("After inserting it and deleting it, we should NOT be able to find a key in the KVPairList.", list.containsKVPair("Rayeesha", "403-111-2000"));
        } catch(Throwable t){
            fail(format(t));
        }

        // Make sure duplicates are not simultaneously deleted.
        list.addBack("Karthik", "303-678-900");
        list.addBack("Karthik", "303-678-900");

        try {
            list.remove("Karthik", "303-678-900");
        } catch(Throwable t){
            fail(format(t));
        }
        assertFalse("A KVPairList with two duplicate nodes and one successful deletion should NOT report that it's empty.", list.isEmpty());
        assertEquals("A KVPairList with two duplicate nodes and one successful deletion should report a size of 1.", 1 , list.size());
    }
}
