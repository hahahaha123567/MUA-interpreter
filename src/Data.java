
import java.util.regex.Pattern;

public interface Data {
    static boolean isData (String word, boolean inList) {
        return isNumber(word) || isWord(word, inList) || isList(word) || isBoolean(word);
    }
    static boolean isNumber (String word) {
        return Pattern.matches("[0-9].*", word) || word.startsWith("-");
    }
    static boolean isWord (String word, boolean inList) {
        // I don't know why word in a list can start without "
        if (inList) {
            return !word.equals("]");
        } else {
            return word.startsWith("\"");
        }
    }
    static boolean isBoolean (String word) {
        return word.equals("true") || word.equals("false");
    }
    static boolean isList (String word) {
        return word.equals("[") || word.equals("]");
    }

    static String getType (String word, boolean inList) {
        if (isNumber(word)) {
            return "Number";
        } else if (isWord(word, inList)) {
            return "Word";
        } else if (isBoolean(word)) {
            return "Boolean";
        } else
            return "List";
    }
    Object getValue();
}
