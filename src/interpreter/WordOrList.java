package interpreter;

public interface WordOrList {
    boolean isEmpty ();
    Token getFirst ();
    Token getLast ();
    Token getButFirst ();
    Token getButLast ();
}
