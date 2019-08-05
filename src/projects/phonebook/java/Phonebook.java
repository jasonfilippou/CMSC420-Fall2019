package projects.phonebook.java;

import projects.phonebook.java.hashes.*;
import projects.phonebook.java.utils.Probes;


/**
 * <p>{@link Phonebook} is an abstraction over phonebooks: databases of &lt; Full Name,
 * Phone Number&gt; pairs. It allows for <b>both</b> phone <b>and</b> name search, both in
 * <em>amortized constant</em> time. The efficiency of either search will be dependent on
 * the nature of the underlying hash table. No null entries are allowed. </p>
 *
 * <p>{@link Phonebook} only allows for <b>unique</b> Person / Phone pairs. That is, every person will have
 * <b>exactly one</b> phone number associated with them, and every phone number will be associated with
 * <b>exactly one</b> person. Study the implementation of this class to see for yourselves how this is attained by
 * interfacing with both internal hash tables. </p>
 *
 * <p>The Release Tests on the <a href ="https://submit.cs.umd.edu/">submit server</a> primarily test the methods of {@link Phonebook}
 * Since {@link Phonebook}'s methods rely on methods of {@link HashTable} instances, by parameterizing
 * {@link Phonebook} instances in all 3^2 = 9 possible ways, we can run the same tests against all of the hash
 * tables that you will have to implement. </p>
 *
 * <p><b>**** STUDY, BUT DO NOT EDIT THIS CLASS' SOURCE CODE! </b></p>
 *
 * @author <a href="https://github.com/JasonFil">Jason Filippou</a>
 * @see HashTable
 * @see SeparateChainingHashTable
 * @see LinearProbingHashTable
 * @see QuadraticProbingHashTable
 */
public class Phonebook {

    private HashTable namesToNumbers ;
    private HashTable numbersToNames ;


    /**
     * Instantiates a new {@link Phonebook}. The parameters provide the collision resolution strategy
     * for lookups based on name or number, respectively.
     *
     * @param namesToNumbersHash A {@link CollisionResolver} that will govern which subtype of {@link HashTable} will be used to
     *                           create our hash table with <b>peoples' names</b> as keys.
     * @param numbersToNamesHash A {@link CollisionResolver} that will govern which subtype of {@link HashTable} will be used to
     *                           create our hash table with <b>phone numbers</b> as keys.
     * @see CollisionResolver
     */
    public Phonebook(CollisionResolver namesToNumbersHash, CollisionResolver numbersToNamesHash) {

        switch(namesToNumbersHash){
            case SEPARATE_CHAINING:
                namesToNumbers = new SeparateChainingHashTable();
                break;
            case LINEAR_PROBING:
                namesToNumbers = new LinearProbingHashTable();
                break;
            case ORDERED_LINEAR_PROBING:
                namesToNumbers = new OrderLinearProbingHashTable();
                break;
            case QUADRATIC_PROBING:
                namesToNumbers = new QuadraticProbingHashTable();
                break;
            default:
                throw new RuntimeException("Encountered unsupported Collision Resolver " + namesToNumbersHash  + "." );
        }

        switch(numbersToNamesHash){
            case SEPARATE_CHAINING:
                numbersToNames = new SeparateChainingHashTable();
                break;
            case LINEAR_PROBING:
                numbersToNames = new LinearProbingHashTable();
                break;
            case ORDERED_LINEAR_PROBING:
                numbersToNames = new OrderLinearProbingHashTable();
                break;
            case QUADRATIC_PROBING:
                numbersToNames = new QuadraticProbingHashTable();
                break;
            default:
                throw new RuntimeException("Encountered unsupported Collision Resolver " + numbersToNamesHash + ".");
        }
    }

    /** Retrieves the phone number associated with the provided full name. If the name is not in the database,
     * this method returns null.
     * @param name The full name of the owner of the phone number that is being searched for.
     * @return The phone number associated with name, or null if name is null or if name
     * is not in the {@link Phonebook}.
     */
    public String getNumberOf(String name) {
        return (name == null) ? null : namesToNumbers.get(name).value;
    }

    /** Retrieves the full name of the owner of the provided phone number. If the phone number is not in the database,
     * this method returns null.
     * @param number The phone number whose owner is being searched for.
     * @return The full name of the owner of number, or null if number is null or if number
     * is not in the {@link Phonebook}.
     */
    public String getOwnerOf(String number) {
        return (number == null) ? null : numbersToNames.get(number).value;
    }

    /** Adds the tuple &lt; name, number &gt; in the {@link Phonebook}. If either name or
     * number are already in the collection, then the entire entry is <b>updated</b>.
     * @param name The full name of the number's owner.
     * @param number The phone number of the person.
     * @throws IllegalArgumentException if either name or number is null.
     */
    public void addEntry(String name, String number) {
        if(name == null || number == null)
            throw new IllegalArgumentException("Provided: name=" + name + " and number= " + number);
        Probes[] p = new Probes[2];

        p[0] = namesToNumbers.put(name, number);
        p[1] = numbersToNames.put(number, name);
//        return p;
    }

    /** Deletes the entry characterized by the arguments provided. If either argument is null, or if the
     * entry is <b>not</b> contained by this {@link Phonebook} instance, this method has <b>no effect</b>.
     * @param name The &quot;owner&quot; part of the &lt; owner, phone number &gt; tuple.
     * @param number The &quot;number&quot; part of the &lt; owner, phone number &gt; tuple.
     */
    public void deleteEntry(String name, String number) {
//        if(number == null || name == null)
//            throw new IllegalArgumentException("Provided: name=" + name + " and number= " + number);
        Probes[] p = new Probes[2];

        p[0] = namesToNumbers.remove(name);
        p[1] = numbersToNames.remove(number);
//        return p;
    }

    /** Returns the number of entries in the phonebook.
     * @return the number of entries in the phonebook.
     */
    public int size() {
        assert namesToNumbers.size() == numbersToNames.size() :
                "Mismatch in internal hash table counts. Names->Numbers has count: " +
                        namesToNumbers.size() + ", while Numbers->Names has count:  " +
                        numbersToNames.size() + ".";
        return namesToNumbers.size();
    }

    /** Queries the phonebook for emptiness.
     * @return true if, and only if, there are 0 entries in this {@link Phonebook}, false otherwise.
     */
    public boolean isEmpty() {
        return size() == 0;
    }
}
