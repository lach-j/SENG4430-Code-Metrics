package LCOMTestProject;

public class B {
    int x = 0;
    int y = 0;
    public void method1(){
        method2();
    }

    public void method2() {
        x++;
    }

    public void method3() {
        y++;
        method5();
    }

    public void method4() {
        y++;
        x++;
    }

    public void method5() {
        //do something
    }
}
