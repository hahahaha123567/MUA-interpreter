package interpreter;

public class MBool implements Token {

    private boolean bool;

    MBool (boolean value) {
        bool = value;
    }

    MBool (String str) {
        this(Boolean.parseBoolean(str));
    }

    static boolean isMBool (String str) {
        return str.equals("true") || str.equals("false");
    }

    boolean getValue () {
        return bool;
    }

    public boolean equals(MBool arg) {
        return this.getValue() == arg.getValue();
    }

    @Override
    public String toString () {
        return "" + bool;
    }

    @Override
    public TokenType getType () {
        return TokenType.MBool;
    }

}
