
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public enum Operation {
    MAKE("make") {
        public Data exec() throws OperationTypeMismatch {
            Word data1 = null;
            Data data2 = null;
            try {
                data1 = (Word) Parser.go(false);
                data2 = Parser.go(false);
            } catch (NullPointerException e) {
                throw new NullPointerException("MAKE");
            }
            try {
                if (data1.getClass() != Word.class) {
                    throw new OperationTypeMismatch ("MAKE");
                } else {
                    map.put(data1, data2);
                }
            } catch (NullPointerException e) {
                throw new NullPointerException("MAKE");
            }
            return null;
        }
    },
    THING1("thing") {
        public Data exec() throws OperationTypeMismatch, ValueNotFound {
            Word data1 = null;
            try {
                data1 = (Word) Parser.go(false);
            } catch (NullPointerException e) {
                throw new NullPointerException("THING");
            }
            try {
                if (data1.getClass() != Word.class) {
                    throw new OperationTypeMismatch("THING");
                } else if (!map.containsKey(data1)) {
                    throw new ValueNotFound("THING");
                } else if (map.get(data1) instanceof List) {
                    Parser.addContent((ArrayList<Data>) map.get(data1).getValue());
                    return null;
                } else {
                    return map.get(data1);
                }
            } catch (NullPointerException e) {
                throw new NullPointerException("THING");
            }
        }
    },
    THING2(":") {
        public Data exec() throws OperationTypeMismatch, ValueNotFound {
            Word data1 = null;
            try {
                data1 = (Word) Parser.go(true);
            } catch (NullPointerException e) {
                throw new NullPointerException("THING");
            }
            try {
                if (data1.getClass() != Word.class) {
                    throw new OperationTypeMismatch("THING");
                } else if (!map.containsKey(data1)) {
                    throw new ValueNotFound("THING");
                } else if (map.get(data1) instanceof List) {
                    Parser.addContent((ArrayList<Data>) map.get(data1).getValue());
                    return null;
                } else {
                    return map.get(data1);
                }
            } catch (NullPointerException e) {
                throw new NullPointerException("THING");
            }
        }
    },
    ERASE("erase") {
        public Data exec() throws OperationTypeMismatch, ValueNotFound {
            Word data1 = null;
            try {
                data1 = (Word) Parser.go(false);
            } catch (NullPointerException e) {
                throw new NullPointerException("ERASE");
            }
            try {
                if (data1.getClass() != Word.class) {
                    throw new OperationTypeMismatch("ERASE");
                } else if (!map.containsKey(data1)) {
                    throw new ValueNotFound("ERASE");
                } else {
                    map.remove(data1);
                    return null;
                }
            } catch (NullPointerException e) {
                throw new NullPointerException("REASE");
            }
        }
    },
    ISNAME("isname") {
        public Data exec() throws OperationTypeMismatch {
            Word data1 = null;
            try {
                data1 = (Word) Parser.go(false);
            } catch (NullPointerException e) {
                throw new NullPointerException("ISNAME");
            }
            try {
                if (data1.getClass() != Word.class) {
                    throw new OperationTypeMismatch("ISNAME");
                } else if (map.containsKey(data1)) {
                    return new Boolean("true");
                } else {
                    return new Boolean("false");
                }
            } catch (NullPointerException e) {
                throw new NullPointerException("ISNAME");
            }
        }
    },
    PRINT("print") {
        public Data exec() {
            Data data1;
            try {
                data1 = Parser.go(false);
            } catch (NullPointerException e) {
                throw new NullPointerException("PRINT");
            }
            if (data1 == null) {
                throw new NullPointerException("PRINT");
            }
            System.out.println(data1);
            return null;
        }
    },
    READ("read") {
        public Data exec() throws OperationTypeMismatch {
            Scanner in = new Scanner(System.in);
            String str = in.next();
            Data data = null;
            Class c = null;
            try {
                c = Class.forName(Data.getType(str, true));
                Constructor cc = c.getDeclaredConstructor(new Class[]{String.class});
                cc.setAccessible(true);
                data = (Data) cc.newInstance(new Object[]{str});
            } catch (Exception e) {
                throw new OperationTypeMismatch("READ");
            }
            return data;
        }
    },
    READLINST("readlinst") {
        public Data exec() throws OperationTypeMismatch {
            List list = new List("]");
            Scanner in = new Scanner(System.in);
            String str = in.nextLine();
            str = str.replaceAll("\\s+", " ");
            String[] strArray = str.split(" ");
            Data data = null;
            Class c = null;
            for (int i = 0; i < strArray.length; ++i) {
                str = strArray[i];
                try {
                    c = Class.forName(Data.getType(str, true));
                    Constructor cc = c.getDeclaredConstructor(new Class[]{String.class});
                    cc.setAccessible(true);
                    data = (Data) cc.newInstance(new Object[]{str});
                } catch (Exception e) {
                    throw new OperationTypeMismatch("READ");
                }
                list.getValue().add(data);
            }
            return list;
        }
    },
    ADD("add") {
        public Data exec() throws OperationTypeMismatch {
            Number data1 = null;
            Number data2 = null;
            try {
                data1 = (Number)Parser.go(false);
                data2 = (Number)Parser.go(false);
            } catch (NullPointerException e) {
                throw new NullPointerException("ADD");
            }
            try {
                if (data1.getClass() != Number.class || data2.getClass() != Number.class) {
                    throw new OperationTypeMismatch("ADD");
                }
            } catch (NullPointerException e) {
                throw new NullPointerException("ADD");
            }
            return new Number(Double.toString(data1.getValue() + data2.getValue()));
        }
    },
    SUB("sub") {
        public Data exec() throws OperationTypeMismatch {
            Number data1 = null;
            Number data2 = null;
            try {
                data1 = (Number)Parser.go(false);
                data2 = (Number)Parser.go(false);
            } catch (NullPointerException e) {
                throw new NullPointerException("ADD");
            }
            try {
                if (data1.getClass() != Number.class || data2.getClass() != Number.class) {
                    throw new OperationTypeMismatch("ADD");
                }
            } catch (NullPointerException e) {
                throw new NullPointerException("ADD");
            }
            return new Number(Double.toString(data1.getValue() - data2.getValue()));
        }
    },
    MUL("mul") {
        public Data exec() throws OperationTypeMismatch {
            Number data1 = null;
            Number data2 = null;
            try {
                data1 = (Number)Parser.go(false);
                data2 = (Number)Parser.go(false);
            } catch (NullPointerException e) {
                throw new NullPointerException("ADD");
            }
            try {
                if (data1.getClass() != Number.class || data2.getClass() != Number.class) {
                    throw new OperationTypeMismatch("ADD");
                }
            } catch (NullPointerException e) {
                throw new NullPointerException("ADD");
            }
            return new Number(Double.toString(data1.getValue() * data2.getValue()));
        }
    },
    DIV("div") {
        public Data exec() throws OperationTypeMismatch {
            Number data1 = null;
            Number data2 = null;
            try {
                data1 = (Number)Parser.go(false);
                data2 = (Number)Parser.go(false);
            } catch (NullPointerException e) {
                throw new NullPointerException("ADD");
            }
            try {
                if (data1.getClass() != Number.class || data2.getClass() != Number.class) {
                    throw new OperationTypeMismatch("ADD");
                }
            } catch (NullPointerException e) {
                throw new NullPointerException("ADD");
            }
            // (double)0 can be divisor in Java!
            if (data2.getValue() == 0) {
                throw new ArithmeticException();
            }
            return new Number(Double.toString(data1.getValue() / data2.getValue()));
        }
    },
    MOD("mod") {
        public Data exec() throws OperationTypeMismatch {
            Number data1 = null;
            Number data2 = null;
            try {
                data1 = (Number)Parser.go(false);
                data2 = (Number)Parser.go(false);
            } catch (NullPointerException e) {
                throw new NullPointerException("ADD");
            }
            try {
                if (data1.getClass() != Number.class || data2.getClass() != Number.class) {
                    throw new OperationTypeMismatch("ADD");
                }
            } catch (NullPointerException e) {
                throw new NullPointerException("ADD");
            }
            return new Number(Double.toString((int)data1.getValue().doubleValue() % (int)data2.getValue().doubleValue()));
        }
    },
    EQ("eq") {
        public Data exec() throws OperationTypeMismatch {
            Data data1 = null;
            Data data2 = null;
            try {
                data1 = Parser.go(false);
                data2 = Parser.go(false);
            } catch (NullPointerException e) {
                throw new NullPointerException("EQ");
            }
            try {
                if (data1.getClass() == Number.class && data2.getClass() == Number.class) {
                    Number num1 = (Number)data1;
                    Number num2 = (Number)data2;
                    if (num1.getValue() == num2.getValue()) {
                        return new Boolean("true");
                    } else {
                        return new Boolean("false");
                    }
                } else if (data1.getClass() == Number.class && data2.getClass() == Number.class) {
                    Word word1 = (Word)data1;
                    Word word2 = (Word)data2;
                    if (word1.getValue().equals(word2.getValue())) {
                        return new Boolean("true");
                    } else {
                        return new Boolean("false");
                    }
                } else {
                    throw new OperationTypeMismatch("EQ");
                }
            } catch (NullPointerException e) {
                throw new NullPointerException("EQ");
            }
        }
    },
    GT("gt") {
        public Data exec() throws OperationTypeMismatch {
            Data data1 = null;
            Data data2 = null;
            try {
                data1 = Parser.go(false);
                data2 = Parser.go(false);
            } catch (NullPointerException e) {
                throw new NullPointerException("GT");
            }
            try {
                if (data1.getClass() == Number.class && data2.getClass() == Number.class) {
                    Number num1 = (Number)data1;
                    Number num2 = (Number)data2;
                    if (num1.getValue() > num2.getValue()) {
                        return new Boolean("true");
                    } else {
                        return new Boolean("false");
                    }
                } else if (data1.getClass() == Number.class && data2.getClass() == Number.class) {
                    Word word1 = (Word)data1;
                    Word word2 = (Word)data2;
                    if (word1.getValue().compareTo(word2.getValue()) > 0) {
                        return new Boolean("true");
                    } else {
                        return new Boolean("false");
                    }
                } else {
                    throw new OperationTypeMismatch("GT");
                }
            } catch (NullPointerException e) {
                throw new NullPointerException("GT");
            }
        }
    },
    LT("lt") {
        public Data exec() throws OperationTypeMismatch {
            Data data1 = null;
            Data data2 = null;
            try {
                data1 = Parser.go(false);
                data2 = Parser.go(false);
            } catch (NullPointerException e) {
                throw new NullPointerException("GT");
            }
            try {
                if (data1.getClass() == Number.class && data2.getClass() == Number.class) {
                    Number num1 = (Number)data1;
                    Number num2 = (Number)data2;
                    if (num1.getValue() < num2.getValue()) {
                        return new Boolean("true");
                    } else {
                        return new Boolean("false");
                    }
                } else if (data1.getClass() == Number.class && data2.getClass() == Number.class) {
                    Word word1 = (Word)data1;
                    Word word2 = (Word)data2;
                    if (word1.getValue().compareTo(word2.getValue()) < 0) {
                        return new Boolean("true");
                    } else {
                        return new Boolean("false");
                    }
                } else {
                    throw new OperationTypeMismatch("GT");
                }
            } catch (NullPointerException e) {
                throw new NullPointerException("GT");
            }
        }
    },
    AND("and") {
        public Data exec() throws OperationTypeMismatch {
            Boolean data1 = null;
            Boolean data2 = null;
            try {
                data1 = (Boolean) Parser.go(false);
                data2 = (Boolean) Parser.go(false);
            } catch (NullPointerException e) {
                throw new NullPointerException("AND");
            } catch (ClassCastException e) {
                throw new OperationTypeMismatch("NOT");
            }
            try {
                if (data1.getClass() != Boolean.class || data2.getClass() != Boolean.class) {
                    throw new OperationTypeMismatch("AND");
                }
                if (data1.getValue() && data2.getValue()) {
                    return new Boolean("true");
                } else {
                    return new Boolean("false");
                }
            } catch (NullPointerException e) {
                throw new NullPointerException("AND");
            }
        }
    },
    OR("or") {
        public Data exec() throws OperationTypeMismatch {
            Boolean data1 = null;
            Boolean data2 = null;
            try {
                data1 = (Boolean) Parser.go(false);
                data2 = (Boolean) Parser.go(false);
            } catch (NullPointerException e) {
                throw new NullPointerException("OR");
            } catch (ClassCastException e) {
                throw new OperationTypeMismatch("NOT");
            }
            try {
                if (data1.getClass() != Boolean.class || data2.getClass() != Boolean.class) {
                    throw new OperationTypeMismatch("OR");
                }
                if (data1.getValue() || data2.getValue()) {
                    return new Boolean("true");
                } else {
                    return new Boolean("false");
                }
            } catch (NullPointerException e) {
                throw new NullPointerException("OR");
            }
        }
    },
    NOT("not") {
        public Data exec() throws OperationTypeMismatch {
            Boolean data1 = null;
            try {
                data1 = (Boolean) Parser.go(false);
            } catch (NullPointerException e) {
                throw new NullPointerException("NOT");
            } catch (ClassCastException e) {
                throw new OperationTypeMismatch("NOT");
            }
            try {
                if (data1.getClass() != Boolean.class) {
                    throw new OperationTypeMismatch("NOT");
                }
                if (data1.getValue()) {
                    return new Boolean("false");
                } else {
                    return new Boolean("true");
                }
            } catch (NullPointerException e) {
                throw new NullPointerException("NOT");
            }
        }
    };
    abstract public Data exec () throws OperationTypeMismatch, ValueNotFound;

    private String type;
    Operation(String type) { this.type = type; }
    public String getType() { return type; }

    private static HashMap<Word, Data> map = new HashMap<Word, Data>();
    
    public static Operation getOperation (String word) {
        for (Operation o : Operation.values()) {
            if (word.equals(o.getType())) {
                return o;
            }
        }
        return null;
    }

    public static boolean isOperation (String word) {
        for (Operation o : Operation.values()) {
            if (word.equals(o.getType())) {
                return true;
            }
        }
        return false;
    }
}
