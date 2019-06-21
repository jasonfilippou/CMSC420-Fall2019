package projects.suffixtree;
import java.util.ArrayList;
import java.util.List;
/**
 * <p>SuffixTree is a Suffix Tree that allows you to check whether a string is a substring of another string efficiently</p>
 *
 * @author <a href = "mailto:jason.filippou@gmail.com">Jason Filippou</a> &amp; <a href ="mailto:pchiang@cs.umd.edu">Ping Chiang</a>
 */
public class SuffixTree {
    protected static final RuntimeException UNIMPL_METHOD = new RuntimeException("Implement this method!");
    private String internalString;
    private InnerNode root;

    /**
     * The constructor of the SuffixTree
     * @param inputString The string that we will build the Suffix tree for
     * @throws IllegalArgumentException when the argument is null, has length smaller than 1, or includes $ sign
     */
    public SuffixTree(String inputString) throws IllegalArgumentException {
        if (inputString ==null ){
            throw new IllegalArgumentException("Argument inputString should not be null");
        }else if(inputString.length() <1){
            throw new IllegalArgumentException("Argument inputString should have at least length 1");
        } else if(inputString.contains("$")){
            throw new IllegalArgumentException("Argument inputString should not include $ sign");
        }
        internalString = inputString + "$";
        root = new InnerNode(0,0);
        //loop through all subcomponents of the inputString to build the suffix tree
        for(int suffixStartIdx = 0; suffixStartIdx<internalString.length();suffixStartIdx++){
            insert(root, suffixStartIdx);
        }
    }
    private Node insert(Node currNode, int newStartIdx){
        int insertStrPtr = newStartIdx;
        int currNodeStrPtr = currNode.start;
        int currNodeMaxIdx = ((currNode.getClass() == LeafNode.class) ?
                internalString.length():
                currNode.start + (((InnerNode)currNode).offset));

        //Find the first position where the node string & insert string mismatch
        while(currNodeStrPtr < currNodeMaxIdx &&
                internalString.charAt(currNodeStrPtr) == internalString.charAt(insertStrPtr)){
            currNodeStrPtr++;
            insertStrPtr++;
        }

        // case 1: if the mismatch location is not at the end of the node
        if (currNodeStrPtr<currNodeMaxIdx){
            //create a new parent node with common prefix
            Node newParentNode = new InnerNode(currNode.start, currNodeStrPtr-currNode.start);

            //create a new insert node with the string after the common prefix
            Node newInsertNode = new LeafNode(insertStrPtr);

            //modify the current node to remove the common prefix
            if(currNode.getClass() == InnerNode.class) {
                currNode.start = currNodeStrPtr;
                ((InnerNode) currNode).offset = currNodeMaxIdx - currNodeStrPtr;
            }else{
                currNode.start = currNodeStrPtr;
            }

            //connect the two children to the parent node
            newParentNode.next[internalString.charAt(currNode.start)] = currNode;
            newParentNode.next[internalString.charAt(newInsertNode.start)] = newInsertNode;

            //return the parent node for connecting
            return newParentNode;
        }else if(currNodeStrPtr==currNodeMaxIdx){
            // case 2: if the mismatch location is at the end of the node,
            // then either recurse to the children node (if it exists) to find a split location OR
            // insert a new children node
            Node nextNode = currNode.next[internalString.charAt(insertStrPtr)];
            if (nextNode != null) {
                currNode.next[internalString.charAt(insertStrPtr)] = insert(nextNode, insertStrPtr);
            }else{
                currNode.next[internalString.charAt(insertStrPtr)] = new LeafNode(insertStrPtr);
            }
            return currNode;
        }
        //the code should never reach here
        throw new RuntimeException("There is some problem with the insert method");
    }

    private boolean search(Node currNode, String checkString, List<Integer> result, int accmLength){
        String currNodestr;
        if(currNode == null) return false;

        //if the checkString is longer than the currNode string, check whether the currNode string is checkString's prefix
        //if it is, try to recurse to the corresponding children
        if (currNode.getClass() == InnerNode.class) {
            currNodestr = internalString.substring(currNode.start, currNode.start + ((InnerNode) currNode).offset);

        } else{ //if currNode.getClass() == LeafNode.class
            currNodestr = internalString.substring(currNode.start);
        }

        if (checkString != null && currNodestr.length() < checkString.length()) {
            //if the current node is checked, but the check string is still not completely checked, try to find children nodes
            if (currNodestr.equals(checkString.substring(0, currNodestr.length()))) {
                Node nextNode = currNode.next[checkString.charAt(currNodestr.length())];
                if (nextNode != null) {
                    search(nextNode, checkString.substring(currNodestr.length()), result, accmLength + currNodestr.length());
                } else {
                    return false;
                }
            }else{
                return false;
            }
        }else{
            //if all check string has been matched
            if (checkString == null || currNodestr.substring(0, checkString.length()).equals(checkString)) {
                if (currNode.getClass() == LeafNode.class &&
                        (checkString == null || checkString.length() < currNodestr.length())) {
                    //if it is a leaf node
                    result.add(currNode.start - accmLength);
                } else {
                    //recurse through all children if it is not leaf
                    for (int i = 0; i < 128; i++) {
                        Node nextNode = currNode.next[i];
                        if (nextNode != null) {
                            search(nextNode, null, result, accmLength + currNodestr.length());
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     *<p> Find matches of a string in the internal string </p>
     * @param checkString the string that we will try to match to the internal string
     * @return a list of integers containing the indices where the checkString and the internal string matches
     * @throws IllegalArgumentException if checkString is an empty string or null
     */
    public List<Integer> matches(String checkString) throws IllegalArgumentException{
        if (checkString == null || checkString.length() <1){
            throw new IllegalArgumentException("Argument checkString should have at least length 1 and be not null");
        }
        ArrayList<Integer> result = new ArrayList<>();
        search(root, checkString, result, 0);
        return result;
    }

    /**
     *<p> Check whether a string is a substring of the internal string </p>
     * @param checkString the string that we will check against the internal string
     * @return true if and only if checkString is a substring of the internal string
     * @throws IllegalArgumentException if checkString is an empty string or null
     */
    public boolean isSubstring(String checkString) throws IllegalArgumentException {
        if (checkString == null || checkString.length() <1){
            throw new IllegalArgumentException("Argument checkString should have at least length 1 and be not null");
        }
        ArrayList<Integer> result = new ArrayList<>();
        search(root, checkString, result, 0);
        return (result.size() > 0);
    }
    /**
     *<p> Count the number of a times a string is repeated in the internal string </p>
     * @param checkString the string that we will check against the internal string
     * @return the number of times checkString is repeated in the internal string
     * @throws IllegalArgumentException if checkString is an empty string or null
     */
    public int numTimesOccurs(String checkString) throws IllegalArgumentException{
        if (checkString == null || checkString.length() <1){
            throw new IllegalArgumentException("Argument checkString should have at least length 1");
        }
        ArrayList<Integer> result = new ArrayList<>();
        search(root, checkString, result, 0);
        return result.size();
    }
    //helper method for finding the longest repeated substring
    private String depthFirstSearchLongest(Node currNode, String stringSoFar){
        //Check whether there are more than 2 children
        String foundString;
        String resultString = stringSoFar;
        if(countChildren(currNode) >= 2){
            stringSoFar = stringSoFar + internalString.substring(currNode.start, currNode.start+((InnerNode) currNode).offset);
            resultString = stringSoFar;
            for(int i = 0; i<128; i++){
                Node nextNode = currNode.next[i];
                if (nextNode != null){
                    foundString = depthFirstSearchLongest(nextNode, stringSoFar);
                    if (foundString.length() > resultString.length()){
                        resultString = foundString;
                    }
                }
            }
        }
        return resultString;
    }

    /**
     *<p> Find the longest repeated substring in the internal string </p>
     * @return the longest repeated string in the internal string
     * When there is a tie, break the tie based on lexicographical order
     */
    public String longestRepeatedSubstring(){
        return depthFirstSearchLongest(root, "");
    }
    private String depthFirstSearchShortest(Node currNode, String stringSoFar, int minLength){
        //Check whether there are more than 2 children
        String foundString;
        if(countChildren(currNode) >= 2){
            if (currNode.getClass() == InnerNode.class) {
                stringSoFar = stringSoFar + internalString.substring(currNode.start, currNode.start + ((InnerNode)currNode).offset);
            }else{
                stringSoFar = stringSoFar + internalString.substring(currNode.start);
            }
            if (stringSoFar.length() >= minLength){
                return stringSoFar.substring(0, minLength);
            }
            for(int i = 0; i<128; i++){
                Node nextNode = currNode.next[i];
                if (nextNode != null){
                    foundString = depthFirstSearchShortest(nextNode, stringSoFar, minLength);
                    if (foundString != null){
                        return foundString;
                    }
                }
            }
        }
        return null;
    }

    /**
     * <p> Find the shortest repeated substring in the internal string that has at least a certain length.</p>
     * @param minLength the smallest length that the repeated string has to exceed
     * @return the shortest repeated string that has at least length minLength.
     * When there is a tie, break the tie based on lexicographical order
     * @throws IllegalArgumentException if the minLength is smaller than or equal to 0
     */
    public String shortestRepeatedSubstring(int minLength) throws IllegalArgumentException {
        if(minLength <= 0){
            throw new IllegalArgumentException("Argument minLength should be larger than zero");
        }
        return depthFirstSearchShortest(root, "", minLength);
    }

    /**
     *<p> Count the number of nodes in the Suffix tree</p>
     * @return the number of nodes in the Suffix tree
     */
    public int nodeCount(){
        return nodeCount(root);
    }
    private int nodeCount(Node currNode){
        //Check whether there are more than 2 children
        int nodeCount =  1;
        for(int i = 0; i<128; i++){
            Node nextNode = currNode.next[i];
            if (nextNode != null){
                nodeCount += nodeCount(nextNode);
            }
        }
        return nodeCount;
    }

    private int countChildren(Node currNode){
        int count = 0;
        for(int i = 0; i<128; i++){
            Node nextNode = currNode.next[i];
            if (nextNode != null){
                count++;
            }
        }
        return count;
    }

    /**
     *<p> Perform an in order traversal of the Suffix tree, but only return
     *  the strings represented by the leaf nodes and not the inner nodes
     *  This is equivalent to returning a list of suffixes by lexicographical order
     *  </p>
     * @return a list of suffixes ordered by lexicographical order
     */
    public List<String> inOrderTraversal(){
        ArrayList<String> result = new ArrayList<>();
        inOrderTraversalHelper(root, "", result);
        return result;
    }
    private void inOrderTraversalHelper(Node currNode, String prefix, List<String> result){
        String currString;
        int currNodeLen;
        if (currNode.getClass() == InnerNode.class) {
            currString = prefix + internalString.substring(currNode.start, currNode.start + ((InnerNode) currNode).offset);
            currNodeLen = ((InnerNode) currNode).offset;
        }else{
            currString = prefix + internalString.substring(currNode.start);
            currNodeLen = internalString.length() - currNode.start;
        }
        if (currNode.getClass() == LeafNode.class) {
            if (currNodeLen > 1) {
                result.add(currString.substring(0, currString.length()-1));
                return;
            }else{
                return;
            }
        }
        for(int i = 0; i<128; i++){
            Node nextNode = currNode.next[i];
            if (nextNode != null){
                inOrderTraversalHelper(nextNode, currString, result);
            }
        }
    }

}