package projects.huffman;
import java.util.Comparator;

public class TrieNodeComparator<T extends TrieNode> implements Comparator<T> {

    public int compare(T o1, T o2) {
        if (o1.getCharPair().getOccurrence() < o2.getCharPair().getOccurrence()) { return -1; }
        else if (o1.getCharPair().getOccurrence() == o2.getCharPair().getOccurrence()) {

            if (o1.getCharPair().getChr() < o2.getCharPair().getChr()) { return -1; }
            else if (o1.getCharPair().getChr() == o2.getCharPair().getChr()) {

                if (o1.getCharPair().getTimestamp() < o2.getCharPair().getTimestamp()) { return -1; }
                else { return 1; }
            }
            else { return 1; }
        }
        else { return 1; }
    }
}
