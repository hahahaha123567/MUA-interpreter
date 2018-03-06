package interpreter;

import java.util.Scanner;

public class Main {
    public static void main (String[] args) {
        Scanner in = new Scanner(System.in);

        Lexer lexer = new Lexer(in);
        lexer.tokenize();
//        lexer.testPrint();

        Parser parser = new Parser(lexer);
        try {
            parser.parse();
        } catch (StopException ignore) { }
    }
}
