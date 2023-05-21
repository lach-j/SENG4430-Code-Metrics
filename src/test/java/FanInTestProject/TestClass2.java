package FanInTestProject;

public class TestClass2 {
    public void tryMethods() {
        TestClass test = new TestClass();

        test.getName();
        test.doNothing();
        boolean isTime = test.isTime();
    }

    public void tryOtherMethods() {
        TestClass3 test = new TestClass3();

        test.isBad();
        test.getName();
    }
}
