package FanInTestProject;

public class TestClass3 {
    public boolean isTime() {
        new TestClass2().tryMethods();
        return false;
    }

    public boolean isBad() {
        return new TestClass().isBad();
    }

    public String getName() {
        return new TestClass().getName();
    }

    public void doNothing() {
    }
}
