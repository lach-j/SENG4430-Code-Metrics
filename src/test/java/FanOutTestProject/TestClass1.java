package FanOutTestProject;

public class TestClass1 {
    public static void main(String[] args) {
        var exception = new Exception();
        exception.getMessage();
        exception.getCause();
        exception.getCause();
    }
}
