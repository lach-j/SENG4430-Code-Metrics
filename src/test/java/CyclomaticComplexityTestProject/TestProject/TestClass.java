package CyclomaticComplexityTestProject.TestProject;

public class TestClass {

    public String testMethod(String name, long age) {
        var format = "Hi my name is %s, I am %d years old!";
        return String.format(format, name, age);
    }
}
