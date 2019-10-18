package projects.phonebook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import projects.phonebook.hashes.*;
import projects.phonebook.utils.NoMorePrimesException;
import projects.phonebook.utils.Probes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import static org.junit.Assert.*;
import static projects.phonebook.hashes.CollisionResolver.*;


/**
 * <p> {@link ReleaseTests} is a jUnit-test suite for {@link Phonebook} and {@link HashTable}.</p>
 *
 * @author <a href = "mailto:jason.filippou@gmail.com">jason.filippou@gmail.com</a>
 *
 * @see Phonebook
 * @see HashTable
 * @see SeparateChainingHashTable
 * @see LinearProbingHashTable
 * @see QuadraticProbingHashTable
 */
public class ReleaseTests {

    private Phonebook pb;
    private CollisionResolver[] resolvers = {SEPARATE_CHAINING, LINEAR_PROBING, ORDERED_LINEAR_PROBING, QUADRATIC_PROBING};
    private String[] names = new String[] {"Arnold", "Tiffany", "Jessie", "Mary", "DeAndre", "Charles", "Jacqueline", "Christine", "Paulette", "Nakeesha", "Alexander", "Aditya", "Jason", "Yi", "Helen", "Carl" };
    private String[] numbers = new String[]{"894-590-0011", "810-279-0711", "705-120-7500", "888-121-3340", "367-900-1199", "667-093-4567", "321-990-2801", "104-356-2111", "215-334-6807", "708-890-2234", "590-260-9001", "890-123-0209", "900-701-2902", "921-350-4314", "810-206-9450", "850-102-8974" };
    private HashMap<String, String> testingPhoneBook;
    private HashMap<CollisionResolver, Integer[]> probesNameIn = new HashMap<>();
    private HashMap<CollisionResolver, Integer[]> probesNumberIn = new HashMap<>();
    private HashMap<CollisionResolver, Integer[]> probesNameOut = new HashMap<>();
    private HashMap<CollisionResolver, Integer[]> probesNumberOut = new HashMap<>();
    private HashMap<CollisionResolver, Integer[]> probesNumberFind = new HashMap<>();
    private HashMap<CollisionResolver, Integer[]> probesNameFind = new HashMap<>();
    private HashMap<CollisionResolver, Integer[]> softprobesNameOut = new HashMap<>();
    private HashMap<CollisionResolver, Integer[]> softprobesNumberOut = new HashMap<>();
    private static final long SEED = 47;
    private static final Random RNG = new Random(SEED);
    private static final int NUMS = 1000;
    private static final int UPPER_BOUND = 100;
    private int hash(String key){
        return key.hashCode() & 0x7fffffff;
    }

    private String[] list0mod13 = new String[]{"Terry", "Alexander", "Moey"};
    private String[] list1mod13 = new String[]{"Mary", "Jacqueline"};
    private String[] list2mod13 = new String[]{"Arnold", "Christine"};
    private String[] list3mod13 = new String[]{"Nakeesha", "Tiffany", "Mon"};
    private String[] list4mod13 = new String[]{"Charles", "Yi", "Ray"};
    private String[] list5mod13 = new String[]{"Tian", "Aditya", "Jessj"};
    private String[] list6mod13 = new String[]{"Jessie", "Jessk"};
    private String[] list7mod13 = new String[]{};
    private String[] list8mod13 = new String[]{};
    private String[] list9mod13 = new String[]{"DeAndre", "Paulette", "Jason", "Helen", "Jing"};
    private String[] list10mod13 = new String[]{};
    private String[] list11mod13 = new String[]{"Amg"};
    private String[] list12mod13 = new String[]{"God", "Carl", "Money", "Monkey"};

    private String[] list0mod23 = new String[]{};
    private String[] list1mod23 = new String[]{"Tiffany", "Helen"};
    private String[] list2mod23 = new String[]{"Alexander"};
    private String[] list3mod23 = new String[]{"Paulette", "Jason", "Money"};
    private String[] list4mod23 = new String[]{"Nakeesha", "Ray"};
    private String[] list5mod23 = new String[]{"Jing"};
    private String[] list6mod23 = new String[]{"Amg"};
    private String[] list7mod23 = new String[]{};
    private String[] list8mod23 = new String[]{"Moey"};
    private String[] list9mod23 = new String[]{"Aditya", "Jessj"};
    private String[] list10mod23 = new String[]{"Jessk"};
    private String[] list11mod23 = new String[]{"Charles"};
    private String[] list12mod23 = new String[]{"Yi", "God"};
    private String[] list13mod23 = new String[]{};
    private String[] list14mod23 = new String[]{"Arnold", "Tian"};
    private String[] list15mod23 = new String[]{"Mon"};
    private String[] list16mod23 = new String[]{};
    private String[] list17mod23 = new String[]{"Christine", "Carl"};
    private String[] list18mod23 = new String[]{"Terry", "DeAndre", "Monkey"};
    private String[] list19mod23 = new String[]{};
    private String[] list20mod23 = new String[]{};
    private String[] list21mod23 = new String[]{"Mary", "Jacqueline", "Jessie"};
    private String[] list22mod23 = new String[]{};



    private String format(String error, CollisionResolver namesToPhones, CollisionResolver phonesToNames){
        return error + "Collision resolvers:" + namesToPhones + ", " + phonesToNames + ". ";
    }

    private String errorData(Throwable t){
        return "Received a " + t.getClass().getSimpleName() + " with message: " + t.getMessage() + ".";
    }

    @Before
    public void setUp(){


        testingPhoneBook = new HashMap<>();


        for(int i = 0; i < names.length; i++) {
            testingPhoneBook.put(names[i], numbers[i]);
        }
/*      The order of the insertion in the test
        DeAndre
        367-900-1199
        Charles
        667-093-4567
        Christine
        104-356-2111
        Alexander
        590-260-9001
        Carl
        850-102-8974
        Paulette
        215-334-6807
        Aditya
        890-123-0209
        Arnold
        894-590-0011
        Jacqueline
        321-990-2801
        Yi
        921-350-4314
        Tiffany
        810-279-0711
        Nakeesha
        708-890-2234
        Jason
        900-701-2902
        Jessie
        705-120-7500
        Helen
        810-206-9450
        Mary
        888-121-3340
*/



        // This is setting up the number of probes needed for different hash table and different function calls

        // Some naming convention:

        // In - for put method
        // Out -- for remove method
        // Find -- for get method

        // probesName -- for the list with name as the key
        // probesNumber -- for the list with number as the key

        probesNameIn.put(SEPARATE_CHAINING, new Integer[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        probesNameIn.put(LINEAR_PROBING, new Integer[]{1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 3, 1});
        probesNameIn.put(ORDERED_LINEAR_PROBING, new Integer[]{1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 3, 1});
        probesNameIn.put(QUADRATIC_PROBING, new Integer[]{1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 3, 2, 1});



        probesNumberIn.put(SEPARATE_CHAINING, new Integer[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        probesNumberIn.put(LINEAR_PROBING, new Integer[]{1, 1, 1, 2, 1, 4, 1, 1, 2, 1, 1, 3, 1, 1, 1, 1});
        probesNumberIn.put(ORDERED_LINEAR_PROBING, new Integer[]{1, 1, 1, 2, 1, 4, 1, 1, 2, 1, 1, 3, 1, 1, 1, 1});
        probesNumberIn.put(QUADRATIC_PROBING, new Integer[]{1, 1, 1, 3, 1, 3, 1, 1, 5, 1, 1, 1, 1, 1, 1, 1});



        probesNameOut.put(SEPARATE_CHAINING, new Integer[]{2, 1, 4, 3, 2, 1, 1, 5, 4, 3, 3, 2, 2, 1, 1, 1});
        probesNameOut.put(LINEAR_PROBING, new Integer[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        probesNameOut.put(ORDERED_LINEAR_PROBING, new Integer[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1});
        probesNameOut.put(QUADRATIC_PROBING, new Integer[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});

        probesNumberOut.put(SEPARATE_CHAINING, new Integer[]{3, 3, 5, 2, 2, 1, 4, 1, 2, 2, 1, 1, 3, 1, 2, 1});
        probesNumberOut.put(LINEAR_PROBING, new Integer[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        probesNumberOut.put(ORDERED_LINEAR_PROBING, new Integer[]{1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        probesNumberOut.put(QUADRATIC_PROBING, new Integer[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});


        probesNameFind.put(SEPARATE_CHAINING, new Integer[]{2, 1, 4, 3, 2, 1, 1, 5, 4, 3, 3, 2, 2, 1, 1, 1});
        probesNameFind.put(LINEAR_PROBING, new Integer[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 2, 3, 1});
        probesNameFind.put(ORDERED_LINEAR_PROBING, new Integer[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1});
        probesNameFind.put(QUADRATIC_PROBING, new Integer[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 3, 2, 1});

        probesNumberFind.put(SEPARATE_CHAINING, new Integer[]{3, 3, 5, 2, 2, 1, 4, 1, 2, 2, 1, 1, 3, 1, 2, 1});
        probesNumberFind.put(LINEAR_PROBING, new Integer[]{1, 1, 1, 1, 1, 2, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1});
        probesNumberFind.put(ORDERED_LINEAR_PROBING, new Integer[]{1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        probesNumberFind.put(QUADRATIC_PROBING, new Integer[]{1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1});

        softprobesNameOut.put(LINEAR_PROBING, new Integer[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 2, 3, 1});
        softprobesNameOut.put(ORDERED_LINEAR_PROBING, new Integer[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 3, 1, 1, 2, 1, 1});
        softprobesNameOut.put(QUADRATIC_PROBING, new Integer[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 3, 2, 1});

        softprobesNumberOut.put(LINEAR_PROBING, new Integer[]{1, 1, 1, 1, 1, 2, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1});
        softprobesNumberOut.put(ORDERED_LINEAR_PROBING, new Integer[]{1, 1, 1, 2, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1});
        softprobesNumberOut.put(QUADRATIC_PROBING, new Integer[]{1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1});

    }


    @After
    public void tearDown(){
        testingPhoneBook.clear();
    }

    @Test
    public void testPBBehaviorWhenEmpty(){
        for(CollisionResolver namesToPhones : resolvers){
            for(CollisionResolver phonesToNames: resolvers){
                pb = new Phonebook(namesToPhones, phonesToNames);
                assertTrue(format("Phonebook should be empty", namesToPhones, phonesToNames), pb.isEmpty());
                assertNull(format("Any search by name should fail in an empty phonebook.", namesToPhones, phonesToNames), pb.getNumberOf("Jessie"));

                assertNull(format("Any search by phone should fail in an empty phonebook.", namesToPhones, phonesToNames), pb.getOwnerOf("301-478-9012"));
            }
        }
    }

    @Test
    public void testPBInsertions(){

        for(CollisionResolver namesToPhones : resolvers){
            for(CollisionResolver phonesToNames: resolvers){
                pb = new Phonebook(namesToPhones, phonesToNames);

                for(Map.Entry<String, String> entry : testingPhoneBook.entrySet()){ // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
                    try {

                        pb.addEntry(entry.getKey(), entry.getValue());

                    }catch(Throwable t){
                        fail(format("Failed to add entry <" + entry.getKey() + ", " + entry.getValue() + ">. ", namesToPhones, phonesToNames) + errorData(t));
                    }
                }
                assertEquals("Phonebook size was different from the one expected.", testingPhoneBook.size(), pb.size());
            }
        }


    }

    @Test
    public void testQPDeletions(){
        QuadraticProbingHashTable qp = new QuadraticProbingHashTable();
        for(Map.Entry<String, String> entry : testingPhoneBook.entrySet()){
            qp.put(entry.getValue(), entry.getKey());
        }
        assertEquals("Count should be " + testingPhoneBook.size() + ".", testingPhoneBook.size(), qp.size());

        for(Map.Entry<String, String> entry : testingPhoneBook.entrySet()) {
            int prevCount = qp.size();
            qp.remove(entry.getValue());
            assertEquals("Phone: " + entry.getValue() + " and person: " + entry.getKey() + ". Count should have been decremented by 1.",
                    prevCount - 1, qp.size());
        }
        assertEquals("After deleting all elements, count should be 0.", 0, qp.size());
    }

    @Test
    public void testPBDeletions(){
        // For all possible pairs of CollisionResolver instances, insert all the keys and delete all the keys.
        for(CollisionResolver namesToPhones : resolvers){
            for(CollisionResolver phonesToNames: resolvers){
                pb = new Phonebook(namesToPhones, phonesToNames);

                // Insert everything contained in our library container...
                for(Map.Entry<String, String> entry : testingPhoneBook.entrySet()){
                    pb.addEntry(entry.getKey(), entry.getValue());
                }

                // Phonebook size should be the same as the library container's...
                assertEquals("After 16 successful insertions, the phonebook's size should be 16", 16, pb.size());
                // Try to delete every entry and report any Throwables that come your way.
                for(Map.Entry<String, String> entry : testingPhoneBook.entrySet()){ // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
                    try {
                        pb.deleteEntry(entry.getKey(), entry.getValue());

                    }catch(Throwable t){
                        fail(format("Failed to delete entry <" + entry.getKey() + ", " + entry.getValue() + ">. ", namesToPhones, phonesToNames) + errorData(t));
                    }
                }
                assertTrue(format("After deleting all of its entries, the phonebook should be empty!", namesToPhones, phonesToNames), pb.isEmpty());

            }
        }
    }

    @Test
    public void testPBGetNumberOf(){
        for(CollisionResolver namesToPhones : resolvers){
            for(CollisionResolver phonesToNames: resolvers) {
                pb = new Phonebook(namesToPhones, phonesToNames);
                ArrayList<String> name = new ArrayList<>();
                ArrayList<Probes> list = new ArrayList<>();
                // Insert everything contained in our library container...
                for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) {
                    pb.addEntry(entry.getKey(), entry.getValue());
                }
                // Check all applications of getNumberOf()...
                for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
                    try {
                        assertEquals("After inserting <" + entry.getKey() + ", " + entry.getValue() + ">" +
                                        "getNumberOf(" + "\"" + entry.getKey() + "\" returned the wrong number.", entry.getValue(),
                                pb.getNumberOf(entry.getKey()));
                    } catch (AssertionError ae) { // Separate error logging for AssertionErrors and other Throwables.
                        throw ae;
                    } catch (Throwable t) {
                        fail(format("Failed to get the number of person:" + entry.getKey() + ". ", namesToPhones, phonesToNames) + errorData(t));
                    }
                }
                assertEquals("After calling getNumberOf() on all of its contained persons, the phonebook's size should not have" +
                        "changed!", testingPhoneBook.size(), pb.size());

            }
        }

    }

    @Test
    public void testPBGetOwnerOf(){
        for(CollisionResolver namesToPhones : resolvers) {
            for (CollisionResolver phonesToNames : resolvers) {
                pb = new Phonebook(namesToPhones, phonesToNames);
                ArrayList<String> number = new ArrayList<>();
                ArrayList<Probes> list = new ArrayList<>();
                // Insert everything contained in our library container...
                for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) {
                    pb.addEntry(entry.getKey(), entry.getValue());
                }


                // Check all applications of getOwnerOf()....
                for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
                    try {
                        assertEquals("After inserting <" + entry.getKey() + ", " + entry.getValue() + ">" +
                                        "getOwnerOf(" + "\"" + entry.getValue() + "\" returned the wrong name.", entry.getKey(),
                                pb.getOwnerOf(entry.getValue()));
                    } catch (AssertionError ae) { // Separate error logging for AssertionErrors and other Throwables.
                        throw ae;
                    } catch (Throwable t) {
                        fail(format("Failed to get the owner of number:" + entry.getValue() + ".", namesToPhones, phonesToNames) + errorData(t));
                    }
                }
                assertEquals("After calling getOwnerOf() on all of its contained phone numbers, the phonebook's size should not have" +
                        "changed!", testingPhoneBook.size(), pb.size());

            }
        }
    }

    @Test
    public void testPutSCProbes() {
        SeparateChainingHashTable sc = new SeparateChainingHashTable();
        int count = 0;
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = sc.put(entry.getKey(), entry.getValue());
            assertEquals("Unexpected number of probes in Seperate chaining", (int)probesNameIn.get(SEPARATE_CHAINING)[count], temp.getProbes());
            count++;
        }

        sc = new SeparateChainingHashTable();
        count = 0;
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = sc.put(entry.getValue(), entry.getKey());
            assertEquals("Unexpected number of probes in Seperate chaining", (int)probesNumberIn.get(SEPARATE_CHAINING)[count], temp.getProbes());
            count++;
        }
    }


    @Test
    public void testPutLProbes() {
        LinearProbingHashTable lp = new LinearProbingHashTable();
        int count = 0;
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = lp.put(entry.getKey(), entry.getValue());
            assertEquals("Unexpected number of probes in Linear probing", (int)probesNameIn.get(LINEAR_PROBING)[count], temp.getProbes());
            count++;
        }

        lp = new LinearProbingHashTable();
        count = 0;
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = lp.put(entry.getValue(), entry.getKey());
            assertEquals("Unexpected number of probes in Linear probing", (int)probesNumberIn.get(LINEAR_PROBING)[count], temp.getProbes());
            count++;
        }
    }

    @Test
    public void testPutOLProbes() {

        OrderLinearProbingHashTable olp = new OrderLinearProbingHashTable();
        int count = 0;
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = olp.put(entry.getKey(), entry.getValue());
            assertEquals("Unexpected number of probes in Ordered Linear probing", (int)probesNameIn.get(ORDERED_LINEAR_PROBING)[count], temp.getProbes());
            count++;
        }

        olp = new OrderLinearProbingHashTable();
        count = 0;
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = olp.put(entry.getValue(), entry.getKey());
            assertEquals("Unexpected number of probes in Ordered Linear probing", (int)probesNumberIn.get(ORDERED_LINEAR_PROBING)[count], temp.getProbes());
            count++;
        }

    }


    @Test
    public void testPutQProbes() {
        QuadraticProbingHashTable qp = new QuadraticProbingHashTable();
        int count = 0;
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = qp.put(entry.getKey(), entry.getValue());
            assertEquals("Unexpected number of probes in Quadratic probing", (int)probesNameIn.get(QUADRATIC_PROBING)[count], temp.getProbes());
            count++;
        }

        qp = new QuadraticProbingHashTable();
        count = 0;
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = qp.put(entry.getValue(), entry.getKey());
            assertEquals("Unexpected number of probes in Quadratic probing", (int)probesNumberIn.get(QUADRATIC_PROBING)[count], temp.getProbes());
            count++;
        }
    }

    @Test
    public void testRemoveSCProbes() {
        SeparateChainingHashTable sc = new SeparateChainingHashTable();
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            sc.put(entry.getKey(), entry.getValue());
        }
        int count = 0;

        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = sc.remove(entry.getKey());
            assertEquals("Unexpected number of probes in Seperate chaining", (int)probesNameOut.get(SEPARATE_CHAINING)[count], temp.getProbes());
            count++;
        }

        sc = new SeparateChainingHashTable();
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            sc.put(entry.getValue(), entry.getKey());
        }
        count = 0;
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = sc.remove(entry.getValue());
            assertEquals("Unexpected number of probes in Seperate chaining", (int)probesNumberOut.get(SEPARATE_CHAINING)[count], temp.getProbes());
            count++;
        }
    }

    @Test
    public void testRemoveLProbes() {
        LinearProbingHashTable lp = new LinearProbingHashTable();
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            lp.put(entry.getKey(), entry.getValue());
        }
        int count = 0;

        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = lp.remove(entry.getKey());
            assertEquals("Unexpected number of probes in Linear probing", (int)probesNameOut.get(LINEAR_PROBING)[count], temp.getProbes());
            count++;
        }

        lp = new LinearProbingHashTable();
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            lp.put(entry.getValue(), entry.getKey());
        }
        count = 0;

        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = lp.remove(entry.getValue());
            assertEquals("Unexpected number of probes in Linear probing", (int)probesNumberOut.get(LINEAR_PROBING)[count], temp.getProbes());
            count++;
        }
    }

    @Test
    public void testRemoveOLProbes() {

        OrderLinearProbingHashTable olp = new OrderLinearProbingHashTable();
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            olp.put(entry.getKey(), entry.getValue());
        }
        int count = 0;

        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = olp.remove(entry.getKey());
            assertEquals("Unexpected number of probes in Ordered Linear probing", (int)probesNameOut.get(ORDERED_LINEAR_PROBING)[count], temp.getProbes());
            count++;
        }

        olp = new OrderLinearProbingHashTable();
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            olp.put(entry.getValue(), entry.getKey());
        }
        count = 0;

        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = olp.remove(entry.getValue());
            assertEquals("Unexpected number of probes in Ordered Linear probing", (int)probesNumberOut.get(ORDERED_LINEAR_PROBING)[count], temp.getProbes());
            count++;
        }
    }


    @Test
    public void testRemoveQProbes() {
        QuadraticProbingHashTable qp = new QuadraticProbingHashTable();
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            qp.put(entry.getKey(), entry.getValue());
        }
        int count = 0;

        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = qp.remove(entry.getKey());
            assertEquals("Unexpected number of probes in Quadratic probing", (int)probesNameOut.get(QUADRATIC_PROBING)[count], temp.getProbes());
            count++;
        }

        qp = new QuadraticProbingHashTable();
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            qp.put(entry.getValue(), entry.getKey());
        }
        count = 0;

        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = qp.remove(entry.getValue());
            assertEquals("Unexpected number of probes in Quadratic probing", (int)probesNumberOut.get(QUADRATIC_PROBING)[count], temp.getProbes());
            count++;
        }
    }

    @Test
    public void testFindSCProbes() {
        SeparateChainingHashTable sc = new SeparateChainingHashTable();
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            sc.put(entry.getKey(), entry.getValue());
        }
        int count = 0;

        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = sc.get(entry.getKey());
            assertEquals("Unexpected number of probes in Seperate chaining", (int)probesNameFind.get(SEPARATE_CHAINING)[count], temp.getProbes());
            count++;
        }


        sc = new SeparateChainingHashTable();
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            sc.put(entry.getValue(), entry.getKey());
        }
        count = 0;
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = sc.get(entry.getValue());
            assertEquals("Unexpected number of probes in Seperate chaining", (int)probesNumberFind.get(SEPARATE_CHAINING)[count], temp.getProbes());
            count++;
        }
    }

    @Test
    public void testFindLProbes() {
        LinearProbingHashTable lp = new LinearProbingHashTable();
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            lp.put(entry.getKey(), entry.getValue());
        }
        int count = 0;

        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = lp.get(entry.getKey());
            assertEquals("Unexpected number of probes in Linear probing", (int)probesNameFind.get(LINEAR_PROBING)[count], temp.getProbes());
            count++;
        }

        lp = new LinearProbingHashTable();
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            lp.put(entry.getValue(), entry.getKey());
        }
        count = 0;

        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = lp.get(entry.getValue());
            assertEquals("Unexpected number of probes in Linear probing", (int)probesNumberFind.get(LINEAR_PROBING)[count], temp.getProbes());
            count++;
        }
    }

    @Test
    public void testFindOLProbes() {

        OrderLinearProbingHashTable olp = new OrderLinearProbingHashTable();
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            olp.put(entry.getKey(), entry.getValue());
        }
        int count = 0;

        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = olp.remove(entry.getKey());
            assertEquals("Unexpected number of probes in Ordered Linear probing", (int)probesNameFind.get(ORDERED_LINEAR_PROBING)[count], temp.getProbes());
            count++;
        }

        olp = new OrderLinearProbingHashTable();
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            olp.put(entry.getValue(), entry.getKey());
        }
        count = 0;

        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = olp.remove(entry.getValue());
            assertEquals("Unexpected number of probes in Ordered Linear probing", (int)probesNumberFind.get(ORDERED_LINEAR_PROBING)[count], temp.getProbes());
            count++;
        }
    }


    @Test
    public void testFindQProbes() {
        QuadraticProbingHashTable qp = new QuadraticProbingHashTable();
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            qp.put(entry.getKey(), entry.getValue());
        }
        int count = 0;

        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = qp.get(entry.getKey());
            assertEquals("Unexpected number of probes in Quadratic probing", (int)probesNameFind.get(QUADRATIC_PROBING)[count], temp.getProbes());
            count++;
        }

        qp = new QuadraticProbingHashTable();
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            qp.put(entry.getValue(), entry.getKey());
        }
        count = 0;

        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = qp.get(entry.getValue());
            assertEquals("Unexpected number of probes in Quadratic probing", (int)probesNumberFind.get(QUADRATIC_PROBING)[count], temp.getProbes());
            count++;
        }
    }

    @Test
    public void testSeparateChainingResizings(){
        SeparateChainingHashTable sc = new SeparateChainingHashTable();
        assertEquals("Separate Chaining hash should have a capacity of 7 at startup.", 7, sc.capacity());
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            sc.put(entry.getKey(), entry.getValue());
        }
        assertEquals("Separate Chaining hash should have a capacity of 7 after 15 insertions.", 7, sc.capacity());
        sc.enlarge();
        assertEquals("Separate Chaining hash should have a capacity of 13 after 1 call to enlarge().", 13, sc.capacity());
        assertEquals("The amount of elements in the Separate Chaining hash should not change after an enlargement.", testingPhoneBook.size(), sc.size());
        sc.enlarge();
        assertEquals("Separate Chaining hash should have a capacity of 23 after 2 calls to enlarge().", 23, sc.capacity());
        sc.enlarge();
        assertEquals("Separate Chaining hash should have a capacity of 43 after 3 calls to enlarge().", 43, sc.capacity());
        sc.shrink();
        assertEquals("Separate Chaining hash should have a capacity of 23 after 3 calls to enlarge() and one to shrink().", 23, sc.capacity());
        assertEquals("The amount of elements in the Separate Chaining hash should not change after a shrinking.", testingPhoneBook.size(), sc.size());
        sc.shrink();
        assertEquals("Separate Chaining hash should have a capacity of 13 after 3 calls to enlarge() and two to shrink().", 13, sc.capacity());
        sc.shrink();
        assertEquals("Separate Chaining hash should have a capacity of 7 after 3 calls to enlarge() and 3 to shrink().", 7, sc.capacity());
        sc.shrink();
        assertEquals("Separate Chaining hash should have a capacity of 5 after 3 calls to enlarge() and 4 to shrink().", 5, sc.capacity());
        sc.shrink();
        assertEquals("Separate Chaining hash should have a capacity of 3 after 3 calls to enlarge() and 5 to shrink().", 3, sc.capacity());
        sc.shrink();
        assertEquals("Separate Chaining hash should have a capacity of 2 after 3 calls to enlarge() and 6 to shrink().", 2, sc.capacity());
    }

    @Test
    public void testOpenAddressingResizeWhenInsert() {
        HashTable lp = new LinearProbingHashTable();
        HashTable qp = new QuadraticProbingHashTable();
        assertEquals("Linear Probing hash should have a capacity of 7 at startup.", 7, lp.capacity());
        assertEquals("Quadratic Probing hash should have a capacity of 7 at startup.", 7, qp.capacity());
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            lp.put(entry.getKey(), entry.getValue());
            qp.put(entry.getKey(), entry.getValue());
        }
        assertEquals("Linear Probing hash should have a capacity of 43 after 16 insertions.", 43, lp.capacity());
        assertEquals("Quadratic Probing hash should have a capacity of 43 after 16 insertions.", 43, qp.capacity());
        assertEquals("Linear Probing hash should have a count of 16 after 16 insertions.", 16, lp.size());
        assertEquals("Quadratic Probing hash should have a count of 16 after 16 insertions.", 16, qp.size());

    }

    // An example of a stress test to catch any insertion errors that you might get.
    @Test
    public void insertionStressTest() {
        HashTable sc = new SeparateChainingHashTable();
        HashTable lp = new LinearProbingHashTable();
        HashTable qp = new QuadraticProbingHashTable();
        for (int i = 0; i < NUMS; i++) {
            String randomNumber = Integer.toString(RNG.nextInt(UPPER_BOUND));
            String randomNumber2 = Integer.toString(RNG.nextInt(UPPER_BOUND));
            try {
                sc.put(randomNumber, randomNumber2);
            } catch (NoMorePrimesException ignored) {
                // To have this exception thrown is not a problem; we have a finite #primes to generate resizings for.
            } catch (Throwable t) {
                fail("Separate Chaining hash failed insertion #" + i + ". Error message: " + errorData(t));
            }

            try {
                lp.put(randomNumber, randomNumber2);
            } catch (NoMorePrimesException ignored) {
                // To have this exception thrown is not a problem; we have a finite #primes to generate resizings for.
            } catch (Throwable t) {
                fail("Linear Probing hash failed insertion #" + i + ". Error message: " + errorData(t));
            }


            try {
                qp.put(randomNumber, randomNumber2);
            } catch (NoMorePrimesException ignored) {
                // To have this exception thrown is not a problem; we have a finite #primes to generate resizings for.
            } catch (Throwable t) {
                fail("Quadratic Probing hash failed insertion #" + i + ". Error message: " + errorData(t));
            }
        }

    }

    @Test
    public void testLPResizingWithSoftDeletion() {


        String[] list = new String[] {"God", "Carl", "Money", "Monkey", "DeAndre", "Paulette", "Jason"};

        HashTable lp = new LinearProbingHashTable();
        HashTable slp = new LinearProbingHashTable(true);

        for (int i = 0;i < list.length;i++) {

            lp.put(list[i], Integer.toString(i));
            slp.put(list[i], Integer.toString(i));

        }


        lp.remove("Money");
        lp.remove("Monkey");
        slp.remove("Money");
        slp.remove("Monkey");

        assertEquals(13, lp.capacity());
        assertEquals(13, slp.capacity());
        assertEquals(5, lp.size());
        assertEquals(5, slp.size());

        lp.put("Charles", "C");
        slp.put("Charles", "C");
        assertEquals(13, lp.capacity());
        assertEquals(13, slp.capacity());
        assertEquals(6, lp.size());
        assertEquals(6, slp.size());

        lp.put("Yi", "Y");
        slp.put("Yi", "Y");
        assertEquals(13, lp.capacity());
        assertEquals(13, slp.capacity());
        assertEquals(7, lp.size());
        assertEquals(7, slp.size());

    }

    @Test
    public void testComplicatedQP() {

        String[] list = new String[] {"DeAndre", "Paulette", "Jason", "Helen"};

        HashTable qp = new QuadraticProbingHashTable();
        HashTable sqp = new QuadraticProbingHashTable(true);

        for (int i = 0;i < list.length;i++) {

            qp.put(list[i], Integer.toString(i));
            sqp.put(list[i], Integer.toString(i));

        }
        // Insertion order now: Helen, Jason, Paulette, DeAndre, Jing

        qp.put("Tian", "T");
        sqp.put("Tian", "T");

        qp.remove("Jason");
        sqp.remove("Jason");


        assertTrue(qp.containsKey("Helen"));
        assertTrue(sqp.containsKey("Helen"));

        assertFalse(qp.containsKey("Jason"));
        assertFalse(sqp.containsKey("Jason"));

        assertTrue(qp.containsKey("Paulette"));
        assertTrue(sqp.containsKey("Paulette"));

        assertTrue(qp.containsKey("DeAndre"));
        assertTrue(sqp.containsKey("DeAndre"));

        assertFalse(qp.containsKey("Jing"));
        assertFalse(sqp.containsKey("Jing"));

        assertTrue(qp.containsKey("Tian"));
        assertTrue(sqp.containsKey("Tian"));


        qp.put("Aditya", "A");
        sqp.put("Aditya", "A");
        qp.put("Jessj", "J");
        sqp.put("Jessj", "J");
        qp.remove("Aditya");
        sqp.remove("Aditya");

        assertTrue(qp.containsKey("Helen"));
        assertTrue(sqp.containsKey("Helen"));
        assertTrue(qp.containsKey("Paulette"));
        assertTrue(sqp.containsKey("Paulette"));
        assertTrue(qp.containsKey("DeAndre"));
        assertTrue(sqp.containsKey("DeAndre"));
        assertTrue(qp.containsKey("Tian"));
        assertTrue(sqp.containsKey("Tian"));
        assertFalse(qp.containsKey("Aditya"));
        assertFalse(sqp.containsKey("Aditya"));
        assertTrue(qp.containsKey("Jessj"));
        assertTrue(sqp.containsKey("Jessj"));

    }


    @Test
    public void testRemoveSoftLProbes() {
        LinearProbingHashTable lp = new LinearProbingHashTable(true);
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            lp.put(entry.getKey(), entry.getValue());
        }
        int count = 0;

        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = lp.remove(entry.getKey());
            assertEquals("Unexpected number of probes in Linear probing with soft deletion", (int)softprobesNameOut.get(LINEAR_PROBING)[count], temp.getProbes());
            count++;
        }


        lp = new LinearProbingHashTable(true);
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            lp.put(entry.getValue(), entry.getKey());
        }
        count = 0;

        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = lp.remove(entry.getValue());
            assertEquals("Unexpected number of probes in Linear probing with soft deletion", (int)softprobesNumberOut.get(LINEAR_PROBING)[count], temp.getProbes());
            count++;
        }

    }

    @Test
    public void testRemoveSoftOLProbes() {

        OrderLinearProbingHashTable olp = new OrderLinearProbingHashTable(true);
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            olp.put(entry.getKey(), entry.getValue());
        }
        int count = 0;

        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = olp.remove(entry.getKey());
            assertEquals("Unexpected number of probes in Ordered Linear probing with soft deletion", (int)softprobesNameOut.get(ORDERED_LINEAR_PROBING)[count], temp.getProbes());
            count++;
        }


        olp = new OrderLinearProbingHashTable(true);
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            olp.put(entry.getValue(), entry.getKey());
        }
        count = 0;

        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = olp.remove(entry.getValue());
            assertEquals("Unexpected number of probes in Ordered Linear probing with soft deletion", (int)softprobesNumberOut.get(ORDERED_LINEAR_PROBING)[count], temp.getProbes());
            count++;
        }

    }


    @Test
    public void testRemoveSoftQProbes() {
        QuadraticProbingHashTable qp = new QuadraticProbingHashTable(true);
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            qp.put(entry.getKey(), entry.getValue());
        }
        int count = 0;

        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = qp.remove(entry.getKey());
            assertEquals("Unexpected number of probes in Quadratic probing with soft deletion", (int)softprobesNameOut.get(QUADRATIC_PROBING)[count], temp.getProbes());
            count++;
        }

        qp = new QuadraticProbingHashTable(true);
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            qp.put(entry.getValue(), entry.getKey());
        }
        count = 0;

        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            Probes temp = qp.remove(entry.getValue());
            assertEquals("Unexpected number of probes in Quadratic probing with soft deletion", (int)softprobesNumberOut.get(QUADRATIC_PROBING)[count], temp.getProbes());
            count++;
        }
    }


    @Test
    public void testResizeSoftOLProbes() {

        OrderLinearProbingHashTable olp = new OrderLinearProbingHashTable(true);
        String[] add1 = new String[]{"Tiffany", "Helen", "Alexander", "Paulette", "Jason", "Money", "Nakeesha", "Ray", "Jing", "Amg"};
        String[] remove1 = new String[]{"Helen", "Alexander", "Paulette", "Jason", "Money", "Nakeesha", "Ray", "Jing", "Amg"};
        String[] add2 = new String[]{"Christine", "Carl"};

        for(String s: add1) {
            olp.put(s, s);
        }

        for (String s: remove1) {
            olp.remove(s);
        }

        for(String s: add2) {
            olp.put(s, s);
        }

        assertEquals("After additions and deletions, and additions again, the capacity should be 23, but get " + olp.capacity() + ".", 23, olp.capacity());

        assertEquals("After resizing, you my have not deleted the tombstone.", 1, olp.put("Terry", "new").getProbes());

        assertEquals("After additions and deletions, and additions again, resize should be triggered and the capacity should be shrink to 7, but get " + olp.capacity() + ".", 7, olp.capacity());

    }

    @Test
    public void testResizeSoftQProbes() {

        QuadraticProbingHashTable qp = new QuadraticProbingHashTable(true);
        String[] add1 = new String[]{"Tiffany", "Helen", "Alexander", "Paulette", "Jason", "Money", "Nakeesha", "Ray", "Jing", "Amg"};
        String[] remove1 = new String[]{"Helen", "Alexander", "Paulette", "Jason", "Money", "Nakeesha", "Ray", "Jing", "Amg"};
        String[] add2 = new String[]{"Christine", "Carl"};

        for(String s: add1) {
            qp.put(s, s);
        }

        for (String s: remove1) {
            qp.remove(s);
        }

        for(String s: add2) {
            qp.put(s, s);
        }

        assertEquals("After additions and deletions, and additions again, the capacity should be 23, but get " + qp.capacity() + ".", 23, qp.capacity());

        assertEquals("After resizing, you my have not deleted the tombstone.", 1, qp.put("Terry", "new").getProbes());

        assertEquals("After additions and deletions, and additions again, resize should be triggered and the capacity should be shrink to 7, but get " + qp.capacity() + ".", 7, qp.capacity());

    }


}