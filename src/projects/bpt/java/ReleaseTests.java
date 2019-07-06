package projects.bpt.java;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

//@SuppressWarnings("unused")

/**
 * A jUnit test suite for {@link BinaryPatriciaTrie}.
 *
 * @author <a href = "mailto:jason.filippou@gmail.com">Jason Filippou</a>.
 */
public class ReleaseTests {


    /************************************************* General test ****************************************/

    @Test public void testSimple1() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();

        assertTrue("Trie should be empty",trie.isEmpty());
        assertTrue("Trie size should be 0",trie.getSize() == 0);

        assertFalse("No string inserted so search should fail", trie.search("0101"));
        assertTrue("String should be inserted successfully",trie.insert("0101"));
        assertFalse("String should not be inserted as it already exists" , trie.insert("0101"));

    }

    @Test public void testSimple2() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();

        assertTrue("String should be inserted successfully",trie.insert("00000"));
        assertTrue("String should be inserted successfully",trie.insert("00011"));
        assertFalse("Search should fail as string does not exist",trie.search("000"));
        assertTrue("String should be inserted successfully",trie.insert("000"));
        assertTrue("String exists and should be found", trie.search("000"));
        assertTrue("String should be inserted successfully",trie.insert("11011"));
        assertTrue("String should be inserted successfully",trie.insert("1"));
        String s = trie.getLongest();
        assertEquals("longest string is 11011", "11011", s );
    }



    @Test public void testSimple3() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();

        assertTrue("String should be inserted successfully" , trie.insert("00000"));
        assertTrue("String should be deleted successfully" ,trie.delete("00000"));

        assertEquals("Trie size should be 0", 0, trie.getSize());

        assertTrue("After inserting and deleting 00000. your trie had some junk!", trie.isJunkFree());
    }




    @Test public void testSimple4() {

        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();

        assertTrue("String should be inserted successfully" , trie.insert("100"));
        assertTrue("String should be inserted successfully" , trie.insert("101"));


        // delete process
        assertTrue("String should be deleted successfully",trie.delete("100"));
        assertFalse("String 100 should not be found in the trie",trie.search("100"));
        assertTrue("String should be deleted successfully",trie.delete("101"));
        assertFalse("String 100 should not be found in the trie",trie.search("101"));
        assertEquals("Trie size should be 0" ,0, trie.getSize());
        assertTrue("After inserting 100 and 101 and deleting them, your trie had some junk!", trie.isJunkFree());

    }



    @Test public void testSimple5() {

        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();

        assertTrue("String should be inserted successfully" , trie.insert("100"));
        assertTrue("String should be inserted successfully" , trie.insert("101"));
        assertTrue("String should be inserted successfully" , trie.insert("0"));
        assertTrue("String should be inserted successfully" , trie.insert("10111"));
        assertTrue("String should be inserted successfully" , trie.insert("1011"));
        assertTrue("String should be inserted successfully" , trie.insert("1010"));
        assertTrue("String should be inserted successfully" , trie.insert("1"));
        assertTrue("String should be inserted successfully" , trie.insert("111"));
        assertTrue("String should be inserted successfully" , trie.insert("11"));
        assertTrue("String should be inserted successfully" , trie.insert("110"));
        assertTrue("String should be inserted successfully" , trie.insert("10110"));

        assertTrue("String should be deleted successfully",trie.delete("101"));
        assertFalse("String 101 should not be found in the trie",trie.search("101"));
        assertFalse("String should not be inserted as it already exists", trie.insert("100"));
        assertFalse("String 10 should not be found in the trie",trie.search("10"));

        assertTrue("After 11 insertions and 2 successful deletions, your trie had some junk! ", trie.isJunkFree());

    }


    /************************************** Testing each function ****************************/



    //Testing search function
    @Test public void testSearch() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();
        //Case 1: Searching in an empty trie
        assertFalse("No strings have been inserted yet search should return false",trie.search("0101"));

        //Case 2: Searching for an existing string
        trie.insert("0101");
        assertTrue("String has been insert so it should be found",trie.search("0101"));

        //Case 3: Searching for an non-existing string
        assertFalse("String has not been insert so it should not be found",trie.search("0111"));
    }

    //testing isEmpty function
    @Test public void testisEmpty() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();
        //Case 1: isEmpty for an empty trie
        assertTrue("Trie should be empty",trie.isEmpty());

        //Case 2: isEmpty for a non-empty trie
        trie.insert("0101");
        assertFalse("Trie should be not be empty as it has one string",trie.isEmpty());

        //Case 3: isEmpty after inserting and deleting a string
        trie.delete("0101");
        assertTrue("Trie should be empty",trie.isEmpty());
    }

    //testing getSize function
    @Test public void testGetSize() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();
        //Case 1: getSize for an empty trie
        assertEquals("Trie is empty so size should be zero" ,0, trie.getSize() );

        //Case 2: getSize  after adding strings
        trie.insert("0101");
        assertEquals("Trie should size should be 1",1, trie.getSize());

        trie.insert("0100");
        assertEquals("Trie should size should be 2",2, trie.getSize());

        trie.insert("1100");
        assertEquals("Trie should size should be 3",3, trie.getSize());


        //Case 3: getSize after deleting strings
        trie.delete("0101");
        assertEquals("Trie should size should be 2",2, trie.getSize());

        trie.delete("0100");
        assertEquals("Trie should size should be 1",1, trie.getSize() );

        trie.delete("1100");
        assertEquals("Trie should size should be 0",0, trie.getSize());


    }


    //testing getLongest function
    @Test public void testGetLongest() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();
        //Case 1: getLongest for an empty trie
        assertEquals("Trie is empty so no strings" , "", trie.getLongest());

        //Case 2: getLongest  after adding strings

        //A) Adding a string with length 2
        trie.insert("11");
        assertEquals("Longest string is 11","11", trie.getLongest());

        //B) Adding a string with length 3
        trie.insert("0101");
        assertEquals("Longest string is 0101","0101", trie.getLongest());

        //C)Adding a string with length 3 but smaller then the previous one
        trie.insert("0100");
        assertEquals("Longest string is 0101","0101", trie.getLongest());

        //D)Adding a string with length 3 larger then the prevous one
        trie.insert("1100");
        assertEquals("Longest string is 1100" , "1100", trie.getLongest());


        //Case 3: getLongest after deleting strings

        trie.delete("1100");
        assertEquals("Longest string is 0101" ,  "0101", trie.getLongest());

        trie.delete("0101");
        assertEquals("Longest string is 0100",  "0100" ,trie.getLongest() );

    }



    /**
     * Make sure the trie generate the proper inorder traversal of strings.
     */


    /**************** SIMPLE InorderTraversal ***********************/


    @Test
    public void testSimpleInorderTraversal(){
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();

        //Simple case when all strings have same length
        trie.insert("000001"); //1
        trie.insert("010010");//2
        trie.insert("010111");//3
        trie.insert("011011");//4
        trie.insert("011101");//5
        trie.insert("011111");//6
        trie.insert("100100");//7
        trie.insert("110001");//8
        trie.insert("110110");//9



        Iterator<String> inorder = trie.inorderTraversal();

        String[] orderedstrings = {"000001", "010010", "010111", "011011","011101", "011111","100100", "110001","110110"};
        int NUMS = trie.getSize();
        for(int i = 0; i <NUMS ; i++){
            assertTrue("Generated inorder traversal unexpectedly ran out of keys  "
                    + "after key #" +   i + ".",inorder.hasNext() );
            String next = inorder.next();


            assertEquals("Mismatch between expected value and generated value at string " + next
                    + ". ", new String(orderedstrings[i]), next);

        }
        String[] NewOrderedstrings =  { "010010", "010111", "011011", "011111","100100","110110"};

        // delete process
        trie.delete("000001");
        trie.delete("011101");
        trie.delete("110001");

        NUMS = trie.getSize();
        assertEquals("After some deletions, size of the trie should be 6" , 6, NUMS);
        inorder = trie.inorderTraversal();
        for(int i = 0; i <NUMS ; i++){

            assertTrue("Generated inorder traversal unexpectedly ran out of keys "
                    + "after key #" +   i + ".",inorder.hasNext() );


            String next = inorder.next();
            assertEquals("Mismatch between expected value and generated value at string " + next
                    + ". ", NewOrderedstrings[i], next);

        }

        assertTrue("After inserting 9 keys and deleting 3 of them, your trie had some junk!", trie.isJunkFree());

    }


    /**************** complex InorderTraversal ***********************/


    @Test
    public void testComplexInorderTraversal(){
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();


        //Case 1: Add strings with different size and check if in order traversal works properly


        trie.insert("100");//4
        trie.insert("101");//5
        trie.insert("0");//1
        trie.insert("10111");//11
        trie.insert("1011");//9
        trie.insert("1010");//8
        trie.insert("1");//2
        trie.insert("111");//7
        trie.insert("11");//3
        trie.insert("110");//6
        trie.insert("10110");//10


        String[] orderedstrings = {"0", "100", "1010", "101", "10110", "1011", "10111", "1", "110", "11", "111"};


        Iterator<String> inorder = trie.inorderTraversal();
        int NUMS = trie.getSize();
        assertEquals("Size of trie should be 11" , 11, NUMS);
        for(int i = 0; i <NUMS ; i++){

            assertTrue("Generated inorder traversal unexpectedly ran out of keys "
                    + "after key #" +   i + ".",inorder.hasNext() );

            String next = inorder.next();

            assertEquals("Mismatch between expected value and generated value at string " + next
                    + ". ",new String( orderedstrings[i]), next);

        }


        //Case 2: Inorder After deleting strings

        String[] NewOrderedstrings = {"0", "100", "1010", "101", "10110", "1011",  "11", "111"};



        // delete process
        trie.delete("1");
        trie.delete("110");
        trie.delete("10111");

        NUMS = trie.getSize();
        inorder = trie.inorderTraversal();
        assertEquals("Size of trie should be 8" , 8, NUMS);
        for(int i = 0; i <NUMS ; i++){

            assertTrue("Generated inorder traversal unexpectedly ran out of keys "
                    + "after key #" +   i + ".",inorder.hasNext() );


            String next = inorder.next();
            assertEquals("Mismatch between expected value and generated value at string " + next
                    + ". ", NewOrderedstrings[i], next);

        }

        assertTrue("After inserting 11 keys and deleting 3 of them, your trie had some junk!", trie.isJunkFree());

    }


    //testing insert function
    @Test public void testInsert() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();
        //case 1: Added a new string
        assertTrue("String should be inserted successfully",trie.insert("0101"));

        //case 2: Adding a string that is already in the trie
        assertFalse("String already exists in the trie",trie.insert("0101"));

        //case 3: Inserting a string with equal length but different keys
        assertTrue("String should be inserted successfully",trie.insert("1101"));
        //Checking that they are inserted correctly

        Iterator<String> inorder = trie.inorderTraversal();
        String[] orderedstringsCase3 = {"0101" , "1101"};

        int NUMS = trie.getSize();
        for(int i = 0; i <NUMS ; i++){
            String next = inorder.next();

            assertEquals("Mismatch between expected value and output value value at string " + next
                    + ". ", new String(orderedstringsCase3[i]), next);

        }

        //case4: Inserting a key with a smaller length but is a prefix of an existing string
        assertTrue("String should be inserted successfully",trie.insert("11"));
        assertTrue("String should be inserted successfully",trie.insert("0"));
        inorder = trie.inorderTraversal();
        String[] orderedstringsCase4 = {"0" , "0101" , "1101" , "11"};

        NUMS = trie.getSize();
        for(int i = 0; i <NUMS ; i++){
            String next = inorder.next();

            assertEquals("Mismatch between expected value and output value value at string " + next
                    + ". ", new String(orderedstringsCase4[i]), next);

        }



        //case5: Insert a string which has a prefix in the trie
        assertTrue("String should be inserted successfully",trie.insert("111"));
        assertTrue("String should be inserted successfully",trie.insert("00"));

        inorder = trie.inorderTraversal();
        String[] orderedstringsCase5 = {"00" , "0" , "0101" , "1101","11" , "111"};

        NUMS = trie.getSize();
        for(int i = 0; i <NUMS ; i++){
            String next = inorder.next();
            assertEquals("Mismatch between expected value and output value value at string " + next
                    + ". ", new String(orderedstringsCase5[i]), next);

        }


        //case6: Insert a longer string with prefix not in Trie
        trie.delete("11");
        assertTrue("String should be inserted successfully",trie.insert("1000000000"));
        inorder = trie.inorderTraversal();
        String[] orderedstringsCase6 = {"00" , "0" , "0101" ,"1000000000",  "1101" , "111"};

        NUMS = trie.getSize();
        for(int i = 0; i <NUMS ; i++){
            String next = inorder.next();
            assertEquals("Mismatch between expected value and output value value at string " + next
                    + ". ", new String(orderedstringsCase6[i]), next);

        }


    }



    @Test public void testDelete() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();
        //Delete node from an empty trie
        assertFalse("Trie is empty can not should be able to delete any string" ,trie.delete("0"));

        /*********************BPTNode of interest is the root***************/

        //Case1: Has a left and a right child
        trie.insert("0");
        trie.insert("00");
        trie.insert("01");

        assertTrue("String should be deleted successfully" ,trie.delete("0"));
        String[] rootCase1 = {"00" , "01"};
        Iterator<String> it = trie.inorderTraversal();
        int NUMS = trie.getSize();
        for(int i = 0; i <NUMS ; i++){
            String next = it.next();
            assertEquals("Mismatch between expected value and output value value at string " + next
                    + ". ", new String(rootCase1[i]), next);

        }
        assertTrue("After inserting 0, 00 and 01 into the trie and deleting 0, your trie had some junk!", trie.isJunkFree());


        //Case2: Has a left child only
        trie.insert("0");
        trie.delete("01");

        assertTrue("String should be deleted successfully" ,trie.delete("0"));
        String[] rootCase2 = {"00"};
        it = trie.inorderTraversal();
        NUMS = trie.getSize();
        for(int i = 0; i <NUMS ; i++){
            String next = it.next();
            assertEquals("Mismatch between expected value and output value value at string " + next
                    + ". ", new String(rootCase2[i]), next);
        }
        assertTrue("After inserting 0, 00 and 01 and deleting 01 and 0, your trie was found to have some junk!", trie.isJunkFree());

        //Case3: Has right child only
        trie.insert("0");
        trie.insert("01");
        trie.delete("00");
        assertTrue("After inserting 0, 00 and 01 and deleting 00, your trie was found to have some junk!", trie.isJunkFree());
        assertTrue("String should be deleted successfully" ,trie.delete("0"));
        assertTrue("After inserting 0, 00 and 01, followed by deleting 00 and 0, your trie was found to have some junk!", trie.isJunkFree());
        String[] rootCase3 = {"01"};
        it = trie.inorderTraversal();
        NUMS = trie.getSize();
        for(int i = 0; i <NUMS ; i++){
            String next = it.next();
            assertEquals("Mismatch between expected value and output value value at string " + next
                    + ". ", new String(rootCase3[i]), next);

        }
        trie.delete("01");
        assertTrue("After deleting the last element from a trie, it was found to have some junk!", trie.isJunkFree());
        NUMS = trie.getSize();

        assertEquals("Size of trie should be 0" , 0, NUMS);

        /*********************BPTNode of interest is the left child***************/


        //Case1: Has a left and a right child
        trie.insert("10");
        trie.insert("100");
        trie.insert("101");
        trie.insert("1000");
        trie.insert("10000");
        trie.insert("10001");
        trie.insert("10010");
        trie.insert("10011");
        trie.insert("1001");

        assertTrue("String should be deleted successfully" ,trie.delete("100"));
        assertTrue("After inserting 9 strings and deleting 1, your trie was found to have some junk!", trie.isJunkFree());
        String[] leftCase1 = {"10000" , "1000", "10001" , "10010","1001","10011","10","101"};
        it = trie.inorderTraversal();
        NUMS = trie.getSize();
        for(int i = 0; i <NUMS ; i++){
            String next = it.next();
            assertEquals("Mismatch between expected value and output value value at string " + next
                    + ". ", new String(leftCase1[i]), next);

        }
        assertFalse("String can't be deleted as it no longer exists in trie" ,trie.delete("100"));
        assertTrue("After a *failed* deletion attempt, your trie was found to have some junk!", trie.isJunkFree());

        //Case2: Has a left child only
        trie.insert("100");
        trie.delete("1001");

        assertTrue("String should be deleted successfully" ,trie.delete("100"));
        String[] leftCase2 = {"10000" , "1000", "10001" , "10010","10011","10","101"};
        it = trie.inorderTraversal();
        NUMS = trie.getSize();
        for(int i = 0; i <NUMS ; i++){
            String next = it.next();
            assertEquals("Mismatch between expected value and output value value at string " + next
                    + ". ", new String(leftCase2[i]), next);

        }
        assertFalse("String can't be deleted as it no longer exists in trie" ,trie.delete("100"));
        assertTrue("After a *failed* deletion attempt, your trie was found to have some junk!", trie.isJunkFree());


        //Case3: Has right child only
        trie.insert("100");
        trie.insert("1001");
        trie.delete("1000");
        assertTrue("String should be deleted successfully" ,trie.delete("100"));
        String[] leftCase3 ={"10000" , "10001" , "10010","1001","10011","10","101"};
        it = trie.inorderTraversal();
        NUMS = trie.getSize();
        for(int i = 0; i <NUMS ; i++){
            String next = it.next();
            assertEquals("Mismatch between expected value and output value value at string " + next
                    + ". ", new String(leftCase3[i]), next);

        }

        assertFalse("String can't be deleted as it no longer exists in trie" ,trie.delete("100"));
        assertTrue("After a *failed* deletion attempt, your trie was found to have some junk!", trie.isJunkFree());


        /*********************BPTNode of interest is the right child***************/


        //Case1: Has a left and a right child
        trie.insert("100");
        trie.insert("1010");
        trie.insert("1011");
        trie.insert("10100");
        trie.insert("10101");
        trie.insert("10110");
        trie.insert("10111");

        assertTrue("String should be deleted successfully" ,trie.delete("101"));
        String[] rightCase1 = {"10000" ,  "10001" ,"100", "10010","1001","10011","10","10100","1010","10101","10110","1011","10111"};

        it = trie.inorderTraversal();
        NUMS = trie.getSize();
        for(int i = 0; i <NUMS ; i++){
            String next = it.next();
            assertEquals("Mismatch between expected value and output value value at string " + next
                    + ". ", new String(rightCase1[i]), next);

        }
        assertFalse("String can't be deleted as it no longer exists in trie" ,trie.delete("101"));
        assertTrue("After a *failed* deletion attempt, your trie was found to have some junk!", trie.isJunkFree());

        //Case2: Has a left child only
        trie.insert("101");
        trie.delete("1010");

        assertTrue("String should be deleted successfully" ,trie.delete("101"));
        String[] rightCase2 =
                {"10000" ,  "10001" , "100","10010","1001","10011","10","10100","10101","10110","1011","10111"};
        it = trie.inorderTraversal();
        NUMS = trie.getSize();
        for(int i = 0; i <NUMS ; i++){
            String next = it.next();
            assertEquals("Mismatch between expected value and output value value at string " + next
                    + ". ", new String(rightCase2[i]), next);

        }
        assertFalse("String can't be deleted as it no longer exists in trie" ,trie.delete("101"));
        assertTrue("After a *failed* deletion attempt, your trie was found to have some junk!", trie.isJunkFree());


        //Case3: Has right child only
        trie.insert("101");
        trie.insert("1010");
        trie.delete("1011");
        assertTrue("String should be deleted successfully" ,trie.delete("101"));
        String[] rightCase3 = {"10000" , "10001" , "100","10010","1001","10011","10","10100","1010","10101","10110","10111"};
        it = trie.inorderTraversal();
        NUMS = trie.getSize();
        for(int i = 0; i <NUMS ; i++){
            String next = it.next();
            assertEquals("Mismatch between expected value and output value value at string " + next
                    + ". ", new String(rightCase3[i]), next);

        }

        assertFalse("String can't be deleted as it no longer exists in trie" ,trie.delete("101"));
        assertTrue("After a *failed* deletion attempt, your trie was found to have some junk!", trie.isJunkFree());
    }


}