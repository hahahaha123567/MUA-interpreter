public class ValueNotFound extends Exception {
    String opType = null;
    public ValueNotFound (String str) {
        opType = str;
    }
}
