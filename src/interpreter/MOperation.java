package interpreter;

import java.util.*;

public enum MOperation implements Executable {
    MAKE("make", (Parser parser) -> {
        MWord key = parser.readMWord();
        Token value = parser.readToken();
        // search in the current AR
        parser.mapStack.peek().put(key, value);
        return null;
    }),
    THING1("thing", (Parser parser) -> {
        if (!parser.lexer.hasNext()) {
            throw new Exception(prompt[0]);
        }
        Token token = parser.stepParse();
        if (token.getType().equals(TokenType.MList)) {
            MList list = (MList) token;
            parser.lexer.addTokens(list.getValue());
            return null;
        } else {
            Token ret = null;
            MWord key = (MWord) token;
            Stack<Map<MWord, Token>> tempStack = new Stack<>();
            Map<MWord, Token> tempMap;
            // mapStack -> tempStack, search
            while (!parser.mapStack.empty()) {
                tempMap = parser.mapStack.pop();
                tempStack.push(tempMap);
                if (tempMap.containsKey(key)) {
                    ret = tempMap.get(key);
                    break;
                }
            }
            // tempStack -> mapStack, recover
            while (!tempStack.empty()) {
                parser.mapStack.push(tempStack.pop());
            }
            if (ret == null) {
                throw new Exception("Thing: variable doesn't exist!");
            } else {
                return ret;
            }
        }
    }),
    THING2(":", (Parser parser) -> {
        return MOperation.valueOf("THING1").exec(parser);
    }),
    ERASE("erase", (Parser parser) -> {
            MWord key = parser.readMWord();
            // can only erase variable in this scope
            if (!parser.mapStack.peek().containsKey(key)) {
                throw new Exception("Erase: variable doesn't exist in the scope!");
            } else {
                parser.mapStack.peek().remove(key);
            }
            return null;
    }),
    ISNAME("isname", (Parser parser) -> {
        MWord key = parser.readMWord();
        boolean ret = false;
        Stack<Map<MWord, Token>> tempStack = new Stack<>();
        Map<MWord, Token> tempMap;
        // mapStack -> tempStack, search
        while (!parser.mapStack.empty()) {
            tempMap = parser.mapStack.pop();
            tempStack.push(tempMap);
            if (tempMap.containsKey(key)) {
                ret = true;
                break;
            }
        }
        // tempStack -> mapStack, recover
        while (!tempStack.empty()) {
            parser.mapStack.push(tempStack.pop());
        }
        return new MBool(String.valueOf(ret));
    }),
    PRINT("print", (Parser parser) -> {
        if (!parser.lexer.hasNext()) {
            throw new Exception(prompt[0]);
        } else {
            System.out.println(parser.stepParse());
        }
        return null;
    }),
    READ("read", (Parser parser) -> {
        Token ret;
        Scanner in = new Scanner(System.in);
        String str = in.next();
        in.close();
        if (MNumber.isMNumber(str)) {
            ret = new MNumber(str);
        } else {
            ret = new MWord(str);
        }
        return ret;
    }),
    READLINST("readlinst", (Parser parser) -> {
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        in.close();

        // what the lexer do
        ArrayList<Token> tokens = new ArrayList<>();
        input = input.replaceAll("\\s+", " ");
        String[] stringArray = input.split(" ");
        StringBuilder sb = new StringBuilder("");
        for (String s : stringArray) {
            if (!s.startsWith("\"")) {
                s = Lexer.addSpace(s);
            }
            sb.append(s);
            sb.append(" ");
        }
        input = sb.toString();
        input = input.replaceAll("\\s+", " ");
        stringArray = input.split(" ");
        // can't interpret []
        for (String s : stringArray) {
            if (MSign.isMSign(s)) {
                tokens.add(new MSign(s));
            } else if (MNumber.isMNumber(s)) {
                tokens.add(new MNumber(s));
            } else {
                tokens.add(new MWord(s));
            }
        }
        return new MList(tokens);
    }),
    ADD("add", (Parser parser) -> {
        MNumber num1 = parser.readMNumber();
        MNumber num2 = parser.readMNumber();
        return new MNumber(num1.getValue() + num2.getValue());
    }),
    SUB("sub", (Parser parser) -> {
        MNumber num1 = parser.readMNumber();
        MNumber num2 = parser.readMNumber();
        return new MNumber(num1.getValue() - num2.getValue());
    }),
    MUL("mul", (Parser parser) -> {
        MNumber num1 = parser.readMNumber();
        MNumber num2 = parser.readMNumber();
        return new MNumber(num1.getValue() * num2.getValue());
    }),
    DIV("div", (Parser parser) -> {
        MNumber num1 = parser.readMNumber();
        MNumber num2 = parser.readMNumber();
        return new MNumber(num1.getValue() / num2.getValue());
    }),
    MOD("mod", (Parser parser) -> {
        MNumber num1 = parser.readMNumber();
        MNumber num2 = parser.readMNumber();
        return new MNumber(num1.getValue() % num2.getValue());
    }),
    EQ("eq", (Parser parser) -> {
        Comparable[] c = new Comparable[2];
        for (int i = 0; i < 2; ++i) {
            if (!parser.lexer.hasNext()) {
                throw new Exception(prompt[0]);
            }
            c[i] = (Comparable) parser.stepParse();
        }
        if (c[0].getType() != c[1].getType()) {
            throw new Exception(": argument type mismatch!");
        } else {
            return new MBool(c[0].equal(c[1]));
        }
    }),
    GT("gt", (Parser parser) -> {
            Comparable[] c = new Comparable[2];
            for (int i = 0; i < 2; ++i) {
                if (!parser.lexer.hasNext()) {
                    throw new Exception(prompt[0]);
                }
                c[i] = (Comparable) parser.stepParse();
            }
            if (c[0].getType() != c[1].getType()) {
                throw new Exception(": argument type mismatch!");
            } else {
                return new MBool(c[0].isBiggerThan(c[1]));
            }
    }),
    LT("lt", (Parser parser) -> {
        Comparable[] c = new Comparable[2];
        for (int i = 0; i < 2; ++i) {
            if (!parser.lexer.hasNext()) {
                throw new Exception(prompt[0]);
            }
            c[i] = (Comparable) parser.stepParse();
        }
        if (c[0].getType() != c[1].getType()) {
            throw new Exception(": argument type mismatch!");
        } else {
            return new MBool(c[0].isSmallerThan(c[1]));
        }
    }),
    AND("and", (Parser parser) -> {
        MBool[] arg = new MBool[2];
        for (int i = 0; i < 2; ++i) {
            if (!parser.lexer.hasNext()) {
                throw new Exception(prompt[0]);
            } else {
                arg[i] = (MBool) parser.stepParse();
            }
        }
        return new MBool(arg[0].getValue() && arg[1].getValue());
    }),
    OR("or", (Parser parser) -> {
        MBool[] arg = new MBool[2];
        for (int i = 0; i < 2; ++i) {
            if (!parser.lexer.hasNext()) {
                throw new Exception(prompt[0]);
            } else {
                arg[i] = (MBool) parser.stepParse();
            }
        }
        return new MBool(arg[0].getValue() || arg[1].getValue());
    }),
    NOT("not", (Parser parser) -> {
        if (!parser.lexer.hasNext()) {
            throw new Exception(prompt[0]);
        }
        MBool arg = (MBool) parser.stepParse();
        return new MBool(!arg.getValue());
    }),
    ISNUMBER("isnumber", (Parser parser) -> {
        if (!parser.lexer.hasNext()) {
            throw new Exception(prompt[0]);
        }
        Token token = parser.stepParse();
        return new MBool(token.getType().equals(TokenType.MNumber));
    }),
    ISWORD("isword", (Parser parser) -> {
        if (!parser.lexer.hasNext()) {
            throw new Exception(prompt[0]);
        }
        Token token = parser.stepParse();
        return new MBool(token.getType().equals(TokenType.MWord));
    }),
    ISLIST("islist", (Parser parser) -> {
        if (!parser.lexer.hasNext()) {
            throw new Exception(prompt[0]);
        }
        Token token = parser.stepParse();
        return new MBool(token.getType().equals(TokenType.MList));
    }),
    ISBOOL("isbool", (Parser parser) -> {
        if (!parser.lexer.hasNext()) {
            throw new Exception(prompt[0]);
        }
        Token token = parser.stepParse();
        return new MBool(token.getType().equals(TokenType.MBool));
    }),
    ISEMPTY("isempty", (Parser parser) -> {
        if (!parser.lexer.hasNext()) {
            throw new Exception(prompt[0]);
        }
        Token token = parser.stepParse();
        if (token instanceof WordOrList) {
            return new MBool(( (WordOrList) token).isEmpty());
        } else {
            throw new Exception(prompt[1]);
        }
    }),
    FIRST ("first", (Parser parser) -> {
        if (!parser.lexer.hasNext()) {
            throw new Exception(prompt[0]);
        }
        WordOrList wordOrList;
        try {
            wordOrList = (WordOrList) parser.stepParse();
        } catch (Exception e) {
            throw new Exception(prompt[1]);
        }
        return wordOrList.getFirst();
    }),
    LAST ("last", (Parser parser) -> {
        if (!parser.lexer.hasNext()) {
            throw new Exception(prompt[0]);
        }
        WordOrList wordOrList;
        try {
            wordOrList = (WordOrList) parser.stepParse();
        } catch (Exception e) {
            throw new Exception(prompt[1]);
        }
        return wordOrList.getLast();
    }),
    BUTFIRST ("butfirst", (Parser parser) -> {
        if (!parser.lexer.hasNext()) {
            throw new Exception(prompt[0]);
        }
        WordOrList wordOrList;
        try {
            wordOrList = (WordOrList) parser.stepParse();
        } catch (Exception e) {
            throw new Exception(prompt[1]);
        }
        return wordOrList.getButFirst();
    }),
    BUTLAST ("butlast", (Parser parser) -> {
        if (!parser.lexer.hasNext()) {
            throw new Exception(prompt[0]);
        }
        WordOrList wordOrList;
        try {
            wordOrList = (WordOrList) parser.stepParse();
        } catch (Exception e) {
            throw new Exception(prompt[1]);
        }
        return wordOrList.getButLast();
    }),
    WORD ("word", (Parser parser) -> {
        MWord word = parser.readMWord();
        Token token = parser.stepParse();
        return new MWord(word.toString() + token.toString());
    }),
    SENTENCE ("sentence", (Parser parser) -> {
        List<Token> list = new ArrayList<>();
        for (int i = 0; i < 2; ++i) {
            Token token = parser.readToken();
            if (token.getType().equals(TokenType.MList)) {
                list.addAll(list.size(), ((MList) token).getValue());
            } else {
                list.add(token);
            }
        }
        return new MList(list);
    }),
    LIST ("list", (Parser parser) -> {
        List<Token> list = new ArrayList<>();
        list.add(parser.readToken());
        list.add(parser.readToken());
        return new MList(list);
    }),
    JOIN ("join", (Parser parser) -> {
        List<Token> list = new ArrayList<>(parser.readMList().getValue());
        list.add(parser.readToken());
        return new MList(list);
    }),
    RANDOM ("random", (Parser parser) -> {
        return new MNumber(Math.random()*parser.readMNumber().getValue());
    }),
    INT ("int", (Parser parser) -> {
        return new MNumber(Math.floor(parser.readMNumber().getValue()));
    }),
    SQRT ("sqrt", (Parser parser) -> {
        return new MNumber(Math.sqrt(parser.readMNumber().getValue()));
    }),
    WAIT ("wait", (Parser parser) -> {
        Thread.sleep( (long)parser.readMNumber().getValue() );
        return null;
    }),
    REPEAT ("repeat", (Parser parser) -> {
        int n = (int) parser.readMNumber().getValue();
        List<Token> list = parser.readMList().getValue();
        for (int i = 0; i < n; ++i) {
            parser.lexer.addTokens(list);
        }
        return null;
    }),
    IF ("if", (Parser parser) -> {
        boolean flag = parser.readMBool().getValue();
        List<Token> list1 = parser.readMList().getValue();
        List<Token> list2 = parser.readMList().getValue();
        if (flag) {
            parser.lexer.addTokens(list1);
        } else {
            parser.lexer.addTokens(list2);
        }
        return null;
    }),
    ERALL ("erall", (Parser parser) -> {
        parser.mapStack.peek().clear();
        return null;
    }),
    POALL ("poall", (Parser parser) -> {
        Set<MWord> set = parser.mapStack.peek().keySet();
        for (MWord mWord : set) {
            System.out.println(mWord);
        }
        return null;
    }),
    OUTPUT ("output", (Parser parser) -> {
        parser.mapStack.peek().put(new MWord("__ret"), parser.readToken());
        return null;
    }),
    STOP ("stop", (Parser parser) -> {
        throw new StopException();
    });

    private final String type;
    private final Executable executor;

    MOperation(String type, Executable executor) {
        this.type = type;
        this.executor = executor;
    }

    @Override
    public Token exec (Parser parser) throws Exception {
        return executor.exec(parser);
    }

    static boolean isMOperation (Token token) {
        if (token.getType().equals(TokenType.MWord)) {
            for (MOperation op : MOperation.values()) {
                if (token.toString().equals(op.type)) {
                    return true;
                }
            }
        }
        return false;
    }

    static MOperation createMOperation (Token token) {
        MOperation ret = null;
        for (MOperation op : MOperation.values()) {
            if (token.toString().equals(op.type)) {
                ret = op;
                break;
            }
        }
        return ret;
    }
}

