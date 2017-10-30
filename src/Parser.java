import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;

public class Parser {

    private static ArrayList<String> content = null;
    private static int contentIndex = 0;

    public Parser (String txt) {
        // delete duplicated space
        txt = txt.replaceAll("\\s+", " ");
        // add space around [], by cutting into string array
        String contentString = new String("");
        String[] tempArray = txt.split(" ");
        for (String s : tempArray) {
            if (!s.startsWith("\"")) {
                s = s.replace("[", "[ ");
                s = s.replace("]", " ]");
                s = s.replace(":", ": ");
            }
            contentString += s;
            contentString += " ";
        }
        contentString = contentString.replaceAll("\\s+", " ");
        tempArray = contentString.split(" ");
        content = new ArrayList<String>(Arrays.asList(tempArray));
    }

    public static void addContent (ArrayList<Data> list) {
        for (int i = list.size(); i >= 1; --i) {
            content.add(contentIndex, list.get(i-1).toString());
        }
    }

    private static String nextWord () {
        String tempString = content.get((contentIndex));
        contentIndex++;
        return tempString;
    }

    public void run () {
        while (contentIndex < content.size()) {
            go(false);
        }
    }
    public static Data go (boolean inList) {
        String word = null;
        try {
            word = nextWord();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NullPointerException();
        }
        // System.out.print(word + " ");
        if (Operation.isOperation(word) && !inList) {
            Operation op = Operation.getOperation(word);
            try {
                return op.exec();
            } catch (OperationTypeMismatch e) {
                System.out.println("Error: " + e.opType + ": Type Mismatch");
            } catch (NullPointerException e) {
                System.out.println("Error: " + e.getMessage() + ": More Argument Needed");
            } catch (ValueNotFound e) {
                System.out.println("Error: " + e.opType + ": Value Not Found");
            } catch (ArithmeticException e) {
                System.out.println("Error: " + "DIV" + ": Zero Cannot be the Divisor");
            }
            return null;
        } else if (Data.isData(word, inList)) {
            // reflection, for runtime type declare
            Data data = null;
            Class c = null;
            try {
                c = Class.forName(Data.getType(word, inList));
                Constructor cc = c.getDeclaredConstructor(new Class[]{String.class});
                cc.setAccessible(true);
                data = (Data) cc.newInstance(new Object[]{word});
            } catch (Exception e) {
                System.out.println("Error: Input Data Format Error");
            }
            return data;
        } else {
            //exception
            System.out.println("Error: Input Format Error");
            return null;
        }
    }
    private String exec(Operation op) {
        String data1 = null, data2 = null;

        return "";
    }
}
