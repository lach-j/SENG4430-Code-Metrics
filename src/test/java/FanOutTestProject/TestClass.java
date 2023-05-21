package FanOutTestProject;

public class TestClass {
    public boolean isTime() {
        return true;
    }

    public boolean isBad() {
        return false;
    }

    public String getName() {
        return "Name";
    }

    public void doNothing() {
        TestClass2 test = new TestClass2();
        test.tryMethods();
        test.tryMethods();
        test.tryMethods();

    }
}
