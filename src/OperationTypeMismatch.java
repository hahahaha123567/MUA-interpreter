public class OperationTypeMismatch extends Exception {
    String opType = null;
    public OperationTypeMismatch (String str) {
        opType = str;
    }
}
