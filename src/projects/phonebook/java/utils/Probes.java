package projects.phonebook.java.utils;

public class Probes {

    public String value;
    public int probes;

    public Probes(String value, int probes) {
        this.value = value;
        this.probes = probes;
    }
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Probes))
            return false;

        Probes temp = (Probes) o;

        return temp.value.equals(this.value) && temp.probes == this.probes;
    }

}
