package interpreter;

public interface Executable {

    String[] prompt = {  ": argument missed!",
            ": argument type error!" };

    Token exec (Parser parser) throws Exception;
}
