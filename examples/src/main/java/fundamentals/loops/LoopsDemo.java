package fundamentals.loops;

import java.util.ArrayList;
import java.util.List;

public class LoopsDemo {

    static int sum(int[] arr) {
        int total = 0;
        for (int x : arr) total += x;
        return total;
    }

    static int max(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) max = arr[i];
        }
        return max;
    }

    static void reverse(int[] arr) {
        for (int i = 0, j = arr.length - 1; i < j; i++, j--) {
            int temp = arr[i];
            arr[i]   = arr[j];
            arr[j]   = temp;
        }
    }

    static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) return i;
        }
        return -1;
    }

    static List<Integer> evenOnly(List<Integer> numbers) {
        List<Integer> result = new ArrayList<>(numbers);
        result.removeIf(n -> n % 2 != 0);
        return result;
    }

    public static void main(String[] args) {
        int[] scores = {85, 92, 78, 95, 88};

        System.out.println("Sum: "     + sum(scores));                  // 438
        System.out.println("Max: "     + max(scores));                  // 95
        System.out.println("Index 78: " + linearSearch(scores, 78));    // 2

        reverse(scores);
        System.out.print("Reversed: ");
        for (int s : scores) System.out.print(s + " ");                  // 88 95 78 92 85
        System.out.println();

        var nums = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));
        System.out.println("Even only: " + evenOnly(nums));              // [2, 4, 6]
    }
}
