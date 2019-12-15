package projects.huffman;

public class TrieNode {
    private TrieNode left, right;
    private CharPair charPair;
    private boolean isLeft;

    TrieNode(CharPair charPair) {
        this.charPair = charPair;
        this.left = this.right = null;
    }

    TrieNode(CharPair charPair, TrieNode left, TrieNode right) {
        this.charPair = charPair;
        this.left = left;
        this.right = right;
    }

    public CharPair getCharPair() {
        return this.charPair;
    }

    public void setIsLeft(boolean isLeft) {
        this.isLeft = isLeft;
    }

    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }

    public boolean isLeft() {
        return this.isLeft;
    }

    public TrieNode getLeft() {
        return this.left;
    }

    public TrieNode getRight() {
        return this.right;
    }

    @Override
    public String toString() {
        char chr = this.charPair.getChr();
        int occ = this.charPair.getOccurrence();
        int ts = this.charPair.getTimestamp();
        if (chr == (char) 0) {
            return "A TrieNode with character: NULL, # of occurrence: " + occ + ", timestamp: " + ts;
        }
        else if (chr == (char) 32) {
            return "A TrieNode with character: SPC, # of occurrence: " + occ + ", timestamp: " + ts;
        }
        return "A TrieNode with character: " + chr + ", # of occurrence: " + occ + ", timestamp: " + ts;
    }

}
