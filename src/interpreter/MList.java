package interpreter;

import java.util.ArrayList;
import java.util.List;

public class MList implements Token, WordOrList {
    private List<Token> list;

    MList (List<Token> list) {
        this.list = list;
    }

    List<Token> getValue () {
        return list;
    }

    void replace (Token from, Token to) {
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).equals(from)) {
                list.set(i, to);
            } else if (list.get(i).getType().equals(TokenType.MList)) {
                ((MList) list.get(i)).replace(from, to);
            }
        }
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (Token token : list) {
            sb.append(token).append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public TokenType getType() {
        return TokenType.MList;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public Token getFirst() {
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return new MList(new ArrayList<>());
        }
    }

    @Override
    public Token getLast() {
        if (list.size() > 0) {
            return list.get(list.size() - 1);
        } else {
            return new MList(new ArrayList<>());
        }
    }

    @Override
    public Token getButFirst() {
        if (list.size() > 1) {
            return new MList(list.subList(1, list.size()));
        } else {
            return new MList(new ArrayList<>());
        }
    }

    @Override
    public Token getButLast() {
        if (list.size() > 1) {
            return new MList(list.subList(0, list.size() - 1));
        } else {
            return new MList(new ArrayList<>());
        }
    }
}
