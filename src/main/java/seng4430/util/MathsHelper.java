package seng4430.util;

public class MathsHelper {
    public static double divideByOrZero(int numerator, int denominator) {
        if (denominator == 0) return 0;

        return (double) numerator / denominator;
    }

    public static double divideByOrZero(double numerator, int denominator) {
        if (denominator == 0) return 0;

        return numerator / denominator;
    }
}
