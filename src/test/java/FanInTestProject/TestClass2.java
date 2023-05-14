package FanInTestProject;

public class TestClass2 {
    public void tryMethods() {
        var test = new TestClass();

        test.getName();
        test.doNothing();
        var isTime = test.isTime();
    }

    public void tryOtherMethods() {
        var test = new TestClass3();

        test.isBad();
        test.getName();
    }
}
