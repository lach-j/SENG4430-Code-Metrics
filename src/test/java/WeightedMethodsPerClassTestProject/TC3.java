/*
File: TC3.java
Author: George Davis (c3350434)
Date: 26/5/23
Description: Assignment 2
    avgWmc 
        = totalWmc/classCount
        = characters/methods
    }*/

package WeightedMethodsPerClassTestProject;

public class TC3 { //avgWmc = (56+56+72)/3 = 61.3333...
    int x = 0;
    int y = 0;

    public void method1() { //4+26+26 = 56chars
        x++;
        System.out.println("abc");
        System.out.println("abc");
    }

    public void method2() { //4+26+26 = 56chars
        y++;
        System.out.println("def");
        System.out.println("def");
    }

    public void method3() { //10+10+26+26 = 72chars
        method1();
        method2();
        System.out.println("abc");
        System.out.println("def");    
    }
}
