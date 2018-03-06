package interpreter;

public interface Comparable extends Token {
    boolean equal (Comparable c);
    boolean isBiggerThan (Comparable c);
    boolean isSmallerThan (Comparable c);
}
