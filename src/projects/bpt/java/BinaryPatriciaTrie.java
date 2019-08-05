package projects.bpt.java;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>BinaryPatriciaTrie is a Patricia Trie over the binary alphabet &#123;	 0, 1 &#125;. By restricting themselves
 * to this small but terrifically useful alphabet, Binary Patricia Tries combine all the positive
 * aspects of Patricia Tries while shedding the storage cost typically associated with Tries that
 * deal with huge alphabets.</p>
 *
 * @author <a href = "https://github.com/JasonFil">Jason Filippou</a> &amp; <a href ="mailto:fyang623@gmail.com">Fan Yang</a>
 */
public class BinaryPatriciaTrie {

    private class TrieNode {

        private TrieNode left, right;
        private String str;

        // isKey indicates whether the node is a key node or not, i.e. whether the string achieved by
        // concatenating the path from the root to this node is a key or not

        private boolean isKey;

        TrieNode() {
            str = "";
            isKey = false;
        }

        TrieNode(String str, boolean isKey) {
            this.str = str;
            this.isKey = isKey;
        }
    }


    // JASON: Made access private (was package-private).
    private TrieNode root;

    private int size;
    private boolean flag; // a boolean variable that will be used by insert() and delete()

    /**
     * Simple constructor that will initialize the internals of this.
     */

    public BinaryPatriciaTrie() {
        root = new TrieNode();
        flag = false;
        size = 0;
    }

    /**
     * Searches the trie for a given key.
     *
     * @param key The input String key.
     * @return true if and only if key is in the trie, false otherwise.
     */
    public boolean search(String key) {
        return search(root, key);
    }

    /**
     * <p>A helper method used by  {@link #search(String)}. Searches the subtree
     * rooted at curr for key. </p>
     *
     * @param curr The current node.
     * @param key  The key to be searched in the subtree rooted at the current node.
     * @return true if and only if key is in the trie, false otherwise.
     */

    private boolean search(TrieNode curr, String key) {
        if (curr == null || key == null) return false;

        // If curr.str is shorter than the key being searched, we check whether curr.str
        // is a prefix of the key
        if (curr.str.length() < key.length()) {
            // If curr.str is not a prefix of the key, then the key is not in the Patricia trie.
            if (!key.substring(0, curr.str.length()).equals(curr.str))
                return false;
            // If str is a prefix of the key, we recurse into the left or right child.
            if (key.charAt(curr.str.length()) == '0')
                return search(curr.left, key.substring(curr.str.length()));
            else
                return search(curr.right, key.substring(curr.str.length()));
        }

        return curr.str.length() == key.length() && curr.str.equals(key) && curr.isKey;
    }

    /**
     * Inserts key into the trie.
     *
     * @param key The input String key.
     * @return true if and only if the key was not already in the trie, false otherwise.
     */
    public boolean insert(String key) {
        if (key == null || key.trim().equals("")) return false;

        // check whether key is a valid binary string
        for (int i = 0; i < key.trim().length(); i++)
            if (key.charAt(i) != '0' && key.charAt(i) != '1') return false;

        flag = true;
        root = insert(root, key.trim());
        size = flag ? (size + 1) : size;
        return flag;
    }


    /**
     * A helper method used by {@link #insert(String key)}.
     * Inserts key into the subtree of the trie rooted at curr.
     *
     * @param curr The current node.
     * @param key  The key to be added to the subtree rooted at the current node.
     * @return The root of the subtree after the insertion of the key.
     */

    private TrieNode insert(TrieNode curr, String key) {
        // If the current node is a leaf, insert the key
        if (curr == null) {
            curr = new TrieNode(key, true);
        } else if (curr.str.equals(key)) {
            // If the key being searched is equal to the string stored in the current node, we simply
            // set curr.isKey to true and set the value of flag appropriately.
            flag = !curr.isKey; // So flag becomes false if we insert a duplicate.
            curr.isKey = true;
        }

        // If the key being inserted is a prefix of the string stored in the current node, we split
        // the current node to two and set isKey to true in the resulting upper node.
        else if (key.length() < curr.str.length() && curr.str.substring(0, key.length()).equals(key)) {

            TrieNode temp = curr;
            temp.str = temp.str.substring(key.length()); // Begins at index key.length() through the end. Fan builds the string sequentially!
            curr = new TrieNode(key, true);
            if (temp.str.charAt(0) == '0')
                curr.left = temp;
            else
                curr.right = temp;
        }

        // If the string stored in the current node is a prefix of the key being inserted, we recurse
        // into the left or right child, as appropriate.
        else if (key.length() > curr.str.length() && key.substring(0, curr.str.length()).equals(curr.str)) {
            if (key.charAt(curr.str.length()) == '0')
                curr.left = insert(curr.left, key.substring(curr.str.length()));
            else
                curr.right = insert(curr.right, key.substring(curr.str.length()));
        }

        // In all other cases, we split the current node to two and add a second child to the resulting
        // upper node appropriately, as specified in the method splitInsert().
        else
            curr = splitInsert(curr, key);

        return curr;
    }

    /**
     * <p>A helper method used by {@link #insert(TrieNode, String)}. Splits the node
     *  referred to by curr into two new nodes and creates a father node that
     *  contains the <i>longest shared prefix of the {@link String} in curr and key.
     *  Decides which one of the two nodes needs to be the right (resp. left) child of the new father node,
     *  and attaches them to said node. </p>
     *
     * @param curr The current node.
     * @param key  The key to be added to the subtree rooted at the current node.
     * @return The root of the subtree after the split.
     */

    private TrieNode splitInsert(TrieNode curr, String key) {
        int i = getSharedPrefixLength(curr.str, key);
        assert (i < Math.min(curr.str.length(), key.length()));

        TrieNode temp = curr;
        temp.str = temp.str.substring(i);
        curr = new TrieNode(key.substring(0, i), false);
        if (key.charAt(i) == '0') {
            curr.left = new TrieNode(key.substring(i), true);
            curr.right = temp;
        } else {
            curr.right = new TrieNode(key.substring(i), true);
            curr.left = temp;
        }
        return curr;
    }


    /**
     * A helper method used by {@link #splitInsert(TrieNode curr, String key)}.
     *
     * @param s1 Input string &#35;1.
     * @param s2 Input string &#35;2.
     * @return The length of the longest prefix shared by the two input strings.
     */
    private int getSharedPrefixLength(String s1, String s2) {
        int i = s1.length() < s2.length() ? s1.length() : s2.length();

        // Fan goes from the end to the beginning, implementing the logic:
        // As long as the substrings are not exactly equal, decrement the length
        // of the possible shared prefix.

        while (!s1.substring(0, i).equals(s2.substring(0, i)))
            i--;
        return i;
    }

    /**
     * Deletes key from the trie.
     *
     * @param key The String key to be deleted.
     * @return True if and only if key was contained by the trie before we attempted deletion, false otherwise.
     */
    public boolean delete(String key) {
        if (key == null) return false;
        flag = false;
        root = delete(root, key.trim());
        size = flag ? (size - 1) : size;
        if(size == 0) root = new TrieNode(); // For consistency with isEmpty().
        return flag;
    }

    /**
     * <p>A helper method used by {@link #delete(String key)}.
     * Deletes the string key from the subtree rooted at the current node. </p>
     *
     * @param curr The current node.
     * @param key  The {@link String} key to be deleted from the subtree rooted at the current node.
     * @return The root of the subtree after the deletion.
     */

    private TrieNode delete(TrieNode curr, String key) {
        // Current node is null indicates that the key is not found in the trie
        if (curr == null) return null;

        // Base case : the key is found in the current node (4 sub cases)
        if (curr.str.equals(key) && curr.isKey) {
            // sub case #1 : both children are not null. We simply set isKey of the current node to false.
            if (curr.left != null && curr.right != null)
                curr.isKey = false;
            // sub case #2 : only the left child is not null. Merge the current node with its left child.
            else if (curr.left != null) {
                curr.left.str = curr.str + curr.left.str;
                curr = curr.left; // TODO: This is assigning to a local copy that is then returned... fix with a retVal variable.
            }
            // sub case #3 : only the right child is not null. Merge the current node with its right child.
            else if (curr.right != null) {
                curr.right.str = curr.str + curr.right.str;
                curr = curr.right; /// TODO: Same
            }
            // sub case #4 : both children are null. Simply set the current node to null.
            else {
                curr = null;        // TODO: Same
            }
            // The key is successfully deleted, so we set flag to true.
            flag = true;
        }

        // If the string stored in the current node is a prefix of the key to be deleted, we recurse
        // into the left or right child as appropriate.
        else if (curr.str.length() < key.length() && key.substring(0, curr.str.length()).equals(curr.str)) {

            if (key.charAt(curr.str.length()) == '0') {
                curr.left = delete(curr.left, key.substring(curr.str.length()));
                if(!curr.isKey && curr.left == null){
                    if(curr.right != null) {
                        curr.right.str = curr.str + curr.right.str;
                    }
                    curr = curr.right;   // Throw away useless node. // TODO: Same.
                }
            }
            else {
                curr.right = delete(curr.right, key.substring(curr.str.length()));
                if(!curr.isKey && curr.right == null){
                    if(curr.left != null) {
                        curr.left.str = curr.str + curr.left.str;
                    }
                    curr = curr.left;   // Throw away useless node. // TODO: Same
                }
            }
        }

        return curr;
    }

    /**
     * Queries the trie for emptiness.
     *
     * @return true if and only if {@link #getSize()} == 0, false otherwise.
     */
    public boolean isEmpty() {
        return root.left == null && root.right == null;
    }

    /**
     * Returns the number of keys in the tree.
     *
     * @return The number of keys in the tree.
     */
    public int getSize() {
        return size;
    }

    /**
     * <p>Performs an <i>inorder (symmetric) traversal</i> of the Binary Patricia Trie. Remember from lecture that inorder
     * traversal in tries is NOT sorted traversal, unless all the stored keys have the same length. This
     * is of course not required by your implementation, so you should make sure that in your tests you
     * are not expecting this method to return keys in lexicographic order. We put this method in the
     * interface because it helps us test your submission thoroughly and it helps you debug your code! </p>
     *
     * <p>We <b>neither require nor test </b> whether the {@link Iterator} returned by this method is fail-safe or fail-fast.
     * This means that you  do <b>not</b> need to test for thrown {@link java.util.ConcurrentModificationException}s and we do
     * <b>not</b> test your code for the possible occurrence of concurrent modifications.</p>
     *
     * <p>We also assume that the {@link Iterator} is <em>immutable</em>, i,e we do <b>not</b> test for the behavior
     * of {@link Iterator#remove()}. You can handle it any way you want for your own application, yet <b>we</b> will
     * <b>not</b> test for it.</p>
     *
     * @return An {@link Iterator} over the {@link String} keys stored in the trie, exposing the elements in <i>symmetric
     * order</i>.
     */
    public Iterator<String> inorderTraversal() {
        ArrayList<String> list = new ArrayList<>();
        inOrderTraversal(root.left, list, "");
        inOrderTraversal(root.right, list, "");
        return list.iterator();
    }

    private void inOrderTraversal(TrieNode curr, List<String> list, String prefix) {
        if (curr == null) return;
        else inOrderTraversal(curr.left, list, prefix + curr.str);
        if (curr.isKey)
            list.add(prefix + curr.str);
        inOrderTraversal(curr.right, list, prefix + curr.str);
    }

    /**
     * Finds the longest {@link String} stored in the Binary Patricia Trie.
     * @return <p>The longest {@link String} stored in this. If the trie is empty, the empty string &quot;&quot; should be
     * returned. Careful: the empty string &quot;&quot;is <b>not</b> the same string as &quot; &quot;; the latter is a string
     * consisting of a single <b>space character</b>! It is also <b>not the same as the</b> null <b>reference</b>!</p>
     *
     * <p>Ties should be broken in terms of <b>value</b> of the bit string. For example, if our trie contained
     * only the binary strings 01 and 11, <b>11</b> would be the longest string. If our trie contained
     * only 001 and 010, <b>010</b> would be the longest string.</p>
     */
    public String getLongest() {
        String longest = "", temp;
        Iterator<String> iterator = inorderTraversal();
        while (iterator.hasNext()) {
            temp = iterator.next();
            if (temp.length() > longest.length())
                longest = temp;
            else if (temp.length() == longest.length()) {
                int j = 0;
                while (temp.charAt(j) == longest.charAt(j))
                    j++;
                if (temp.charAt(j) == '1')
                    longest = temp;
            }
        }
        return longest;
    }

    /**
     * Makes sure that your trie doesn't have splitter nodes with a single child. In a Patricia trie, those nodes should
     * be pruned. Be careful with the implementation of this method, since our tests call it to make sure your deletions work
     * correctly! That is to say, if your deletions work well, but you have made an error in this (far easier) method,
     * you will <b>still</b> not be passing our tests!
     *
     * @return true iff all nodes in the trie either denote stored strings or split into two subtrees, false otherwise.
     */
    public boolean isJunkFree(){
        return isEmpty() || (isJunkFree(root.left) && isJunkFree(root.right));
    }

    private boolean isJunkFree(TrieNode n){
        if(n == null){   // Null subtrees trivially junk-free
            return true;
        }
        if(!n.isKey){   // Non-key nodes need to be strict splitter nodes
            return ( (n.left != null) && (n.right != null) && isJunkFree(n.left) && isJunkFree(n.right) );
        } else {
            return ( isJunkFree(n.left) && isJunkFree(n.right) ); // But key-containing nodes need not.
        }
    }
}
