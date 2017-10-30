import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Load {
    private List<String> txt1 = null;
    private String txt2 = new String("");

    public Load() {
        // read from file
        Path path = Paths.get("src", "input.txt");
        try {
            txt1 = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // drop comment
        for (int i = 0; i < txt1.size(); ++i) {
            if (txt1.get(i).startsWith("//")) {
                txt1.remove(i);
                i--;
            }
        }
        // connect lines together
        for (int i = 0; i < txt1.size(); ++i) {
            txt2 += txt1.get(i);
            txt2 += " ";
        }
    }

    public static void main (String[] argv) {
        Load load = new Load();
        Parser parser = new Parser(load.txt2.trim());
        parser.run();
    }
}
