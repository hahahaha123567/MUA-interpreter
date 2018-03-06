package interpreter;

public class MNumber implements Token, Comparable {

    private double num;

    MNumber (double value) {
        num = value;
    }

    MNumber (String str) {
        this(Double.parseDouble(str));
    }

    static boolean isMNumber (String str) {
        boolean ret = true;
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException e) {
            ret = false;
        }
        return ret;
    }

    double getValue() {
        return num;
    }

    @Override
    public String toString () {
        return "" + num;
    }

    @Override
    public TokenType getType() {
        return TokenType.MNumber;
    }

    @Override
    public boolean equal(Comparable c) {
        MNumber arg = (MNumber) c;
        return this.getValue() == arg.getValue();
    }

    @Override
    public boolean isBiggerThan(Comparable c) {
        MNumber arg = (MNumber) c;
        return this.getValue() > arg.getValue();
    }

    @Override
    public boolean isSmallerThan(Comparable c) {
        return !equals(c) && !isBiggerThan(c);
    }
}
