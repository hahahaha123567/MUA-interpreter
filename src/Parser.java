import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Parser {

    private static ArrayList<String> content = null;
    private static int contentIndex = 0;

    public static void addContent (ArrayList<Data> list) {
        for (int i = list.size(); i >= 1; --i) {
            content.add(contentIndex, list.get(i-1).toString());
        }
    }

    private static String nextWord () {
        String tempString = content.get(contentIndex);
        contentIndex++;
        return tempString;
    }

    public static void run () {
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
            System.out.println("Error: Input Format Error, Neither Operation Nor Word");
            return null;
        }
    }

    public static void main (String[] argv) {
        Scanner in = new Scanner(System.in);
        String line = null;
        while (in.hasNext()) {
            line = in.nextLine();
            // drop comment
            if (!line.startsWith("//")) {
                // drop duplicated space
                line = line.replaceAll("\\s+", " ");
                // since "[a is an legal word, add space around :[] by cutting into an array
                // some students say FM changed the word format, but I'm not informed
                String[] tempArray = line.split(" ");
                line = "";
                for (String s : tempArray) {
                    if (!s.startsWith("\"")) {
                        s = s.replace("[", "[ ");
                        s = s.replace("]", " ]");
                        s = s.replace(":", ": ");
                    }
                    line += s;
                    line += " ";
                }
                // drop duplicated space again
                line = line.replaceAll("\\s+", " ");
                tempArray = line.split(" ");
                content = new ArrayList<String>(Arrays.asList(tempArray));
            }
            run();
        }
    }
}
