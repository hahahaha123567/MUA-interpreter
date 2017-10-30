public class Number implements Data {

    private double value = 0;

    public Number (String word) {
        value = Double.valueOf(word);
    }

    public Double getValue() {
        return Double.valueOf(value);
    }

    public String toString () {
        return String.valueOf(value);
    }
}
