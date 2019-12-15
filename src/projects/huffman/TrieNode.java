package projects.huffman;

public class TrieNode {
    private TrieNode left, right;
    private CharPair charPair;
    private Boolean isLeft;

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

    public void setIsLeft(Boolean isLeft) {
        this.isLeft = isLeft;
    }

    public Boolean getIsLeft() {
        return this.isLeft;
    }


    @Override
    public String toString() {
        return "A TrieNode with character: " + this.charPair.getChr() +
                ", # of occurrence: " + this.charPair.getOccurrence() +
                ", timestamp: " + this.charPair.getTimestamp();
    }

}
