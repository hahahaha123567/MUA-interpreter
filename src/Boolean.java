public class Boolean implements Data {

    private boolean value;

    Boolean (String word) {
        value = java.lang.Boolean.valueOf(word);
    }

    public java.lang.Boolean getValue () {
        return value;
    }

    public String toString () {
        return String.valueOf(value);
    }
}
