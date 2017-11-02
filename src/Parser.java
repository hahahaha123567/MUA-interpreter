import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Parser {

    private static ArrayList<String> content = null;
    private static int contentIndex = 0;

    private static String nextWord () {
        String tempString = content.get(contentIndex);
        contentIndex++;
        return tempString;
    }

    static Data go (boolean inList) {
        String word;
        Data data = null;
        try {
            word = nextWord();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NullPointerException();
        }
        // System.out.print(word + " ");
        if (Operation.isOperation(word) && !inList) {
            Operation op = Operation.getOperation(word);
            try {
                data = op != null ? op.exec() : null;
            } catch (NullPointerException e) {
                System.out.println("Error: " + e.getMessage() + ": More Argument Needed");
            } catch (OperationTypeMismatch e) {
                System.out.println("Error: " + e.opType + ": Type Mismatch");
            } catch (ValueNotFound e) {
                System.out.println("Error: " + e.opType + ": Value Not Found");
            } catch (ArithmeticException e) {
                System.out.println("Error: " + "DIV" + ": Zero Cannot be the Divisor");
            }
        } else if (Data.isData(word, inList)) {
            // reflection, for runtime type declare
            Class c;
            try {
                c = Class.forName(Data.getType(word, inList));
                Constructor cc = c.getDeclaredConstructor(new Class[]{String.class});
                cc.setAccessible(true);
                data = (Data) cc.newInstance(new Object[]{word});
            } catch (Exception e) {
                System.out.println("Error: Input Data Format Error");
            }
        } else {
            //exception
            System.out.println("Error: Input Format Error, Neither Operation Nor Word");
            data = null;
        }
        return data;
    }

    static void addContent (ArrayList<Data> list) {
        for (int i = list.size(); i >= 1; --i) {
            content.add(contentIndex, list.get(i-1).toString());
        }
    }

    private static void run () {
        contentIndex = 0;
        while (contentIndex < content.size()) {
            try {
                go(false);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Error: More Argument Needed");
                break;
            }
        }
    }

    public static void main (String[] argv) {
        Scanner in = new Scanner(System.in);
        String line;
        while (in.hasNext()) {
            line = in.nextLine();
            // drop comment
            if (!line.startsWith("//")) {
                // drop duplicated space
                line = line.replaceAll("\\s+", " ");
                // since "[a is an legal word, add space around :[] by cutting into an array
                // some students say FM changed the word format, but I'm not informed
                String[] tempArray = line.split(" ");
                StringBuffer sb = new StringBuffer("");
                for (String s : tempArray) {
                    if (!s.startsWith("\"")) {
                        s = s.replace("[", "[ ");
                        s = s.replace("]", " ]");
                        s = s.replace(":", ": ");
                    }
                    sb.append(s);
                    sb.append(" ");
                }
                // drop duplicated space again
                line = sb.toString();
                line = line.replaceAll("\\s+", " ");
                tempArray = line.split(" ");
                content = new ArrayList<>(Arrays.asList(tempArray));
            }
            run();
        }
    }
}
