package projects.huffman;

public class CharPair {
    private char chr;
    private int occurrence;
    private int timestamp;

    CharPair(char chr, int occurrence, int timestamp) {
        this.chr = chr;
        this.occurrence = occurrence;
        this.timestamp = timestamp;
    }

    CharPair(int occurrence, int timestamp) {
        this.chr = (char) 0; // ASCII code for null
        this.occurrence = occurrence;
        this.timestamp = timestamp;
    }

    public char getChr() {
        return this.chr;
    }

    public int getOccurrence() {
        return this.occurrence;
    }

    public int getTimestamp() {
        return this.timestamp;
    }

    @Override
    public String toString() {
        return "A TrieNode with character: " + this.chr +
                ", # of occurrence: " + this.occurrence;
    }
}
