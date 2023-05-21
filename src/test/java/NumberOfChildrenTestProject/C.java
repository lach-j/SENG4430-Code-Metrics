package NumberOfChildrenTestProject;

//Class C is an immediate subclass of A.
//number of immediate subclasses for class A = 1 = C

public class C extends A { 

    public void method3() {
        // do something else
    }

    public void method4() {
        method1();
        method3();
    }
}
