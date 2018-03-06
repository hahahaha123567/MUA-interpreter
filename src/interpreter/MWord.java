package interpreter;

public class MWord implements Token, Comparable, WordOrList {

    private String word;
    private boolean startsWithQuotes;

    MWord (String str) {
        try {
            if (str.length() == 0) {
                word = str;
            } else {
                startsWithQuotes = (str.charAt(0) == '"');
                word = startsWithQuotes ? str.substring(1) : str;
            }
        } catch (Exception e) {
            System.out.println("MWord exception: " + str);
        }
    }

    @Override
    public String toString () {
        return word;
    }

    @Override
    public TokenType getType () {
        return TokenType.MWord;
    }

    private String getValue() {
        return word;
    }

    // implement comparable
    @Override
    public boolean equal(Comparable c) {
        MWord arg = (MWord) c;
        return word.equals(arg.word);
    }

    @Override
    public boolean isBiggerThan(Comparable c) {
        MWord arg = (MWord) c;
        return word.compareTo(arg.word) > 0;
    }

    @Override
    public boolean isSmallerThan(Comparable c) {
        return !isBiggerThan(c) && !equals(c);
    }

    // used in HashMap
    @Override
    public boolean equals (Object obj) {
        return getClass() == obj.getClass() && this.word.equals(((MWord)obj).getValue());
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }

    @Override
    public boolean isEmpty() {
        return word.isEmpty();
    }

    @Override
    public Token getFirst() {
        if (word.length() > 0) {
            return new MWord(word.substring(0, 1));
        } else {
            return new MWord("");
        }
    }

    @Override
    public Token getLast() {
        if (word.length() > 0) {
            return new MWord(word.substring(word.length() - 1));
        } else {
            return new MWord("");
        }
    }

    @Override
    public Token getButFirst() {
        if (word.length() > 1) {
            return new MWord(word.substring(1));
        } else {
            return new MWord("");
        }
    }

    @Override
    public Token getButLast() {
        if (word.length() > 1) {
            return new MWord(word.substring(0, word.length() - 1));
        } else {
            return new MWord("");
        }
    }
}
