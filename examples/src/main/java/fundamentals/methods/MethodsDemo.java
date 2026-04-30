package fundamentals.methods;

import java.util.Arrays;

public class MethodsDemo {

    // Overloading — find maximum
    static int    max(int a, int b)       { return a > b ? a : b; }
    static double max(double a, double b) { return a > b ? a : b; }

    // Varargs — calculate average
    static double average(double... nums) {
        if (nums.length == 0) return 0;
        double sum = 0;
        for (double n : nums) sum += n;
        return sum / nums.length;
    }

    // Pass-by-value: primitive — caller unchanged
    static void tryDouble(int n) {
        n *= 2;
    }

    // Pass-by-value: reference — array contents mutated
    static void doubleAll(int[] arr) {
        for (int i = 0; i < arr.length; i++) arr[i] *= 2;
    }

    // Recursion
    static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    // Early return instead of nested if
    static String classify(int score) {
        if (score >= 90) return "Excellent";
        if (score >= 75) return "Good";
        if (score >= 50) return "Average";
        return "Below average";
    }

    public static void main(String[] args) {
        System.out.println(max(3, 7));            // 7
        System.out.println(max(3.5, 2.8));        // 3.5
        System.out.println(average(80, 90, 70));  // 80.0

        int x = 10;
        tryDouble(x);
        System.out.println(x);                    // 10 — unchanged

        int[] arr = {1, 2, 3};
        doubleAll(arr);
        System.out.println(Arrays.toString(arr)); // [2, 4, 6] — mutated

        System.out.println(gcd(48, 18));          // 6
        System.out.println(classify(85));         // Good
    }
}
