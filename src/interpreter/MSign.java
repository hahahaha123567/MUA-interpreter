package interpreter;

interface Sign {
    char getSign();
    TokenType getType();
}

enum CalcuSign implements Sign {
    ADDSIGN('+'), SUBSIGN('-'), MULSIGN('*'), DIVSIGN('/'), PERCENTSIGN('%'),
    LBRA('('), RBRA(')');

    char sign;

    CalcuSign(char sign) {
        this.sign = sign;
    }

    public char getSign() {
        return sign;
    }

    @Override
    public TokenType getType() {
        return TokenType.MCalcuSign;
    }
}

enum ArchSign implements Sign {
    LSBRA('['), RSBRA(']');

    char sign;

    ArchSign(char sign) {
        this.sign = sign;
    }

    public char getSign() {
        return sign;
    }

    @Override
    public TokenType getType() {
        if (sign == '[') {
            return TokenType.MArchSignLeft;
        } else {
            return TokenType.MArchSignRight;
        }
    }
}

public class MSign implements Token {
    private Sign innerSign;

    MSign (String str) {
        char c = str.charAt(0);
        for (CalcuSign s : CalcuSign.values()) {
            if (s.sign == c) {
                innerSign = s;
            }
        }
        for (ArchSign s : ArchSign.values()) {
            if (s.sign == c) {
                innerSign = s;
            }
        }
    }

    static boolean isMSign (String str) {
        if (str.length() == 1) {
            char c = str.charAt(0);
            for (CalcuSign s : CalcuSign.values()) {
                if (s.sign == c) {
                    return true;
                }
            }
            for (ArchSign s : ArchSign.values()) {
                if (s.sign == c) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "" + innerSign.getSign();
    }

    @Override
    public TokenType getType() {
        return innerSign.getType();
    }
}