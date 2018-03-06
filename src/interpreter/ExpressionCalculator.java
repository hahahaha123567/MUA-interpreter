package interpreter;

import java.util.ArrayList;

public class ExpressionCalculator {
    private ArrayList<Token> calcuArray;
    private int index;

    ExpressionCalculator(ArrayList<Token> calcuArray) {
        this.calcuArray = calcuArray;
    }

    Token calculate () {
        double ans = expression();
//        System.out.println(ans);
        return new MNumber("" + ans);
    }

    private double expression () {
        double ret = term();
        while (hasNext() && addOrSub()) {
            switch (getSign()) {
                case '+': ret += term(); break;
                case '-': ret -= term(); break;
            }
        }
        return ret;
    }

    private double term () {
        double ret = value();
        while (hasNext() && mulOrDiv()) {
            switch (getSign()) {
                case '*': ret *= value(); break;
                case '/': ret /= value(); break;
                case '%': ret %= value(); break;
            }
        }
        return ret;
    }

    private double value () {
        double ret = 0;
        if (leftBra()) {
            getSign();
            ret = expression();
            getSign();
        } else if (addOrSub()) {
            switch (getSign()) {
                case '+': ret = value(); break;
                case '-': ret = 0 - value(); break;
            }
        } else {
            ret = getDouble();
        }
        return ret;
    }

    private double getDouble () {
        double ret = Double.parseDouble(calcuArray.get(index).toString());
        index++;
        return ret;
    }

    private char getSign () {
        char ret = calcuArray.get(index).toString().charAt(0);
        index++;
        return ret;
    }

    private boolean addOrSub () {
        char haha = calcuArray.get(index).toString().charAt(0);
        return hasNext() && ( haha == '+' || haha == '-' );
    }

    private boolean mulOrDiv () {
        char haha = calcuArray.get(index).toString().charAt(0);
        return hasNext() && ( haha == '*' || haha == '/' || haha == '%' );
    }

    private boolean leftBra () {
        char haha = calcuArray.get(index).toString().charAt(0);
        return hasNext() && haha == '(';
    }

    private boolean hasNext () {
        return index < calcuArray.size();
    }
}
