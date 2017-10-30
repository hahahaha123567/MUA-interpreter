public class Word implements Data {

    private String value = null;

    public Word (String word) {
        if (word.startsWith("\"")) {
            value = word.substring(1);
        } else {
            value = word;
        }
    }

    public String getValue () {
        return value;
    }

    public String toString () {
        return value;
    }

    public int hashCode() {
        return value.hashCode();
    }

    public boolean equals (Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Word word = (Word)obj;
        return word.value.equals(this.value);
    }
}
