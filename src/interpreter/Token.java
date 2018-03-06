package interpreter;


enum TokenType {
    MNumber, MList, MBool, MWord, MCalcuSign, MArchSignLeft, MArchSignRight
}

interface Token {
    TokenType getType ();
}
