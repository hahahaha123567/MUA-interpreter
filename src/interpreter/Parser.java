package interpreter;

import java.util.*;

class Parser {
    Lexer lexer;
    Stack<Map<MWord, Token>> mapStack = new Stack<>();
    private int listTimes = 0;

    Parser (Lexer lexer) {
        this.lexer = lexer;
        Map<MWord, Token> map = new HashMap<>();
        map.put(new MWord("pi"), new MNumber(3.14159));
        map.put(new MWord("run"), null);
        mapStack.push(map);
    }

    void parse () throws StopException {
        while (lexer.hasNext()) {
            stepParse();
        }
    }

    Token stepParse() throws StopException {
        Token ret = null;
        if (lexer.nextToken(TokenType.MNumber) || lexer.nextToken(TokenType.MCalcuSign)) {
            ArrayList<Token> calcArray = new ArrayList<>();
            calcArray.add(lexer.getToken());
            while (lexer.hasNext() && (lexer.nextToken(TokenType.MNumber) || lexer.nextToken(TokenType.MCalcuSign))) {
                calcArray.add(lexer.getToken());
            }
            ret = new ExpressionCalculator(calcArray).calculate();
        } else if (lexer.nextToken(TokenType.MBool)) {
            ret = lexer.getToken();
        } else if (lexer.nextToken(TokenType.MArchSignLeft)) {
            ArrayList<Token> tempList = new ArrayList<>();
            listTimes++;
            lexer.getToken();
            while (!lexer.nextToken(TokenType.MArchSignRight)) {
                tempList.add(stepParse());
            }
            lexer.getToken();
            listTimes--;
            ret = new MList(tempList);
        } else if (lexer.nextToken(TokenType.MWord)) {
            MWord temp = (MWord) lexer.getToken();
            if (inList()) {
                ret = temp;
            } else if (MOperation.isMOperation(temp)) {
                try {
                    // you will be sorry when debugging if you join the two sentence into one
                    MOperation op = MOperation.createMOperation(temp);
                    ret = op.exec(this);
                } catch (StopException e) {
                    throw e;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (isFunction(temp)) {
                ret = funcExec(temp);
            } else {
                ret = temp;
            }
        } else if (lexer.nextToken(TokenType.MList)) {
            ret = lexer.getToken();
        }
        return ret;
    }

    private boolean isFunction (MWord token) {
        Token tokenValue = getValue(token);
        if (tokenValue == null) {
            return false;
        }
        if (tokenValue.getType().equals(TokenType.MList)) {
            MList function = (MList) tokenValue;
            List<Token> list = function.getValue();
            return list.size() == 2
                    && list.get(0).getType().equals(TokenType.MList)
                    && list.get(1).getType().equals(TokenType.MList);
        } else {
            return false;
        }
    }

    private Token funcExec (MWord funcName) {
        // new ar
        mapStack.peek().put(new MWord("__ret"), null);
        HashMap<MWord, Token> currMap = new HashMap<>();
        // bind parameters
        MList function = (MList) getValue(funcName);
        List<Token> parameter = ((MList) function.getValue().get(0)).getValue();
        List<Token> operationList = new ArrayList<>(((MList) function.getValue().get(1)).getValue());
        operationList.add(new MWord("stop"));
        MList operation = new MList(operationList);
        for (Token para : parameter) {
            try {
                currMap.put( (MWord) para, getValue((MWord) readToken()));
            } catch (Exception e) {
                System.out.println("function bind error");
            }
        }
//        System.out.println(currMap); // for test
        int tempIndex = lexer.index;
        List<Token> tempList = lexer.tokens;
        mapStack.push(currMap);
        // execute
        try {
            lexer.index = 0;
            lexer.tokens = operation.getValue();
            parse();
        } catch (StopException ignore) { }
        // delete ar
        Token ret = currMap.get(new MWord("__ret"));
        mapStack.pop();
        lexer.index = tempIndex;
        lexer.tokens = tempList;
        return ret;
    }

    // variable dynamic binding
    private Token getValue (MWord token) {
        Token ret = null;
        Stack<Map<MWord, Token>> tempStack = new Stack<>();
        Map<MWord, Token> tempMap;
        // mapStack -> tempStack, search
        while (!mapStack.empty()) {
            tempMap = mapStack.pop();
            tempStack.push(tempMap);
            if (tempMap.containsKey(token)) {
                ret = tempMap.get(token);
                break;
            }
        }
        // tempStack -> mapStack, recover
        while (!tempStack.empty()) {
            mapStack.push(tempStack.pop());
        }
        return ret;
    }

    // the code is very very ugly, maybe converted with generify
    Token readToken () throws Exception {
        if (!lexer.hasNext()) {
            throw new Exception();
        }
        Token ret = stepParse();
        if (ret == null) {
            throw new Exception();
        } else {
            return ret;
        }
    }

    MBool readMBool () throws Exception {
        if (!lexer.hasNext()) {
            throw new Exception();
        }
        MBool ret = (MBool) stepParse();
        if (ret == null) {
            throw new Exception();
        } else {
            return ret;
        }
    }

    MNumber readMNumber () throws Exception {
        if (!lexer.hasNext()) {
            throw new Exception();
        }
        MNumber ret = (MNumber) stepParse();
        if (ret == null) {
            throw new Exception();
        } else {
            return ret;
        }
    }

    MWord readMWord () throws Exception {
        if (!lexer.hasNext()) {
            throw new Exception();
        }
        MWord ret = (MWord) stepParse();
        if (ret == null) {
            throw new Exception();
        } else {
            return ret;
        }
    }

    MList readMList () throws Exception {
        if (!lexer.hasNext()) {
            throw new Exception();
        }
        MList ret = (MList) stepParse();
        if (ret == null) {
            throw new Exception();
        } else {
            return ret;
        }
    }

    private boolean inList () {
        return listTimes != 0;
    }
}