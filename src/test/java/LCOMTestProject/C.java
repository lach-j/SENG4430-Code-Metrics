package LCOMTestProject;

public class C {
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
    }

    public void method4() {
        y++;
    }

    public void method5() {
        //do something
    }
}
