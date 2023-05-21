package NumberOfChildrenTestProject;

public class C extends A { //Class C is an immediate subclass of A.

    public void method3() {
        // do something else
    }

    public void method4() {
        method1();
        method3();
    }
}
