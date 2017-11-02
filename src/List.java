
import java.util.ArrayList;

public class List implements Data {

    private ArrayList<Data> value = new ArrayList<>();

    List(String word) throws Exception {
        if (!word.equals("]")) {
            while (true) {
                Data data = Parser.go(true);
                if (data == null) {
                    throw new Exception("List ctor error");
                }
                if (data.getClass() == this.getClass()) {
                    break;
                } else {
                    value.add(data);
                }
            }
        }
    }

    public ArrayList<Data> getValue() {
        return value;
    }

    public String toString () { return value.toString(); }
}
