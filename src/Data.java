
import java.util.regex.Pattern;

public interface Data {
    public static boolean isData (String word, boolean inList) {
        return isNumber(word) || isWord(word, inList) || isList(word) || isBoolean(word);
    }
    public static boolean isNumber (String word) {
        return Pattern.matches("[0-9].*", word) || word.startsWith("-");
    }
    public static boolean isWord (String word, boolean inList) {
        // I don't know why word in a list can start without "
        if (!inList) {
            return word.startsWith("\"");
        } else {
            if (word.equals("]")) {
                return false;
            } else {
                return true;
            }
        }
    }
    public static boolean isBoolean (String word) {
        return word.equals("true") || word.equals("false");
    }
    public static boolean isList (String word) {
        return word.equals("[") || word.equals("]");
    }

    public static String getType (String word, boolean inList) {
        if (isNumber(word)) {
            return "Number";
        } else if (isWord(word, inList)) {
            return "Word";
        } else if (isBoolean(word)) {
            return "Boolean";
        } else
            return "List";
    }
    public abstract Object getValue();
}
