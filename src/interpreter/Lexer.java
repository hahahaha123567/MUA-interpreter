package interpreter;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Lexer {
    private Scanner in;
    List<Token> tokens;
    int index;

    Lexer (Scanner in) { this.in = in; }

    void tokenize() {
        tokens = new ArrayList<>();
        index = 0;
        String line;
        StringBuilder inputSB = new StringBuilder();
        while (in.hasNext()) {
            line = in.nextLine();
            line = line.trim();
            // drop comment
            if (line.startsWith("//")) {
                continue;
            }
            inputSB.append(line);
            inputSB.append(" ");
        }
        String input = inputSB.toString();
        // drop duplicated space
        input = input.replaceAll("\\s+", " ");
        // cut into an array by ", then add space around :[], because "[a is a legal word
        String[] stringArray = input.split(" ");
        StringBuilder sb = new StringBuilder("");
        for (String s : stringArray) {
            if (!s.startsWith("\"")) {
                s = addSpace(s);
            }
            sb.append(s);
            sb.append(" ");
        }
        // drop duplicated space again
        input = sb.toString();
        input = input.replaceAll("\\s+", " ");
        stringArray = input.split(" ");
        // --------- string to token ----------//
        for (String s : stringArray) {
            if (MSign.isMSign(s)) {
                tokens.add(new MSign(s));
            } else if (MBool.isMBool(s)) {
                tokens.add(new MBool(s));
            } else if (MNumber.isMNumber(s)) {
                tokens.add(new MNumber(s));
            } else {
                tokens.add(new MWord(s));
            }
        }
    }

    boolean hasNext () {
        return index < tokens.size();
    }

    boolean nextToken (TokenType tt) {
        return tokens.get(index).getType().equals(tt);
    }

    @NotNull
    Token getToken () {
        Token ret = null;
        try {
            ret = tokens.get(index);
            index++;
        } catch (Exception e) {
            System.out.println("exception in getToken(), index is " + index);
        }
        return ret;
    }

    static String addSpace (String s) {
        s = s.replace("[", " [ ");
        s = s.replace("]", " ] ");
        s = s.replace(":", " : ");
        s = s.replace("+", " + ");
        s = s.replace("-", " - ");
        s = s.replace("*", " * ");
        s = s.replace("/", " / ");
        s = s.replace("(", " ( ");
        s = s.replace(")", " ) ");
        s = s.replace("%", " % ");
        return s;
    }

    void addTokens (List<Token> list) {
        tokens.addAll(index, list);
    }

    void testPrint () {
        System.out.println("Tokens: ");
        for (Token token : tokens) {
            System.out.print(token + " ");
            MOperation.isMOperation(token);
        }
        System.out.println();
    }
}
