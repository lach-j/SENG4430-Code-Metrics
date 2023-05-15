package TestProject;

public class ConditionalNestingTestClass {
    public static void main(String[] args) {
        int a = 10;
        int b = 20;
        int c = 30;

        if (true) {
            System.out.println("a is positive");
            if (true) {
                System.out.println("b is positive");
                if (true) {
                    System.out.println("c is positive");
                }
            }
        }
    }
}
