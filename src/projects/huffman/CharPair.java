package projects.huffman;

/**
 * Helper class for storing character information.
 *
 * @author Yige Feng 115674202
 */

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
        char chr = this.getChr();
        int occ = this.getOccurrence();
        int ts = this.getTimestamp();
        if (chr == (char) 0) {
            return "A CharPair with character: NULL, # of occurrence: " + occ + ", timestamp: " + ts;
        }
        else if (chr == (char) 32) {
            return "A CharPair with character: SPC, # of occurrence: " + occ + ", timestamp: " + ts;
        }
        return "A CharPair with character: " + chr + ", # of occurrence: " + occ + ", timestamp: " + ts;
    }

    public String compactToString()
    {
        char chr = this.getChr();
        int occ = this.getOccurrence();
        if (chr == (char) 0) {
            return "(NULL, " + occ + ")";
        }
        else if (chr == (char) 32) {
            return "(SPC, " + occ + ")";
        }
        return "(" + chr + ", " + occ + ")";
    }
}
