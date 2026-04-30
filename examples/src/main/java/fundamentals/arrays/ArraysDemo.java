package fundamentals.arrays;

import java.util.Arrays;

public class ArraysDemo {

    static int sum(int[] arr) {
        int total = 0;
        for (int x : arr) total += x;
        return total;
    }

    static double average(int[] arr) {
        return (double) sum(arr) / arr.length;
    }

    static int max(int[] arr) {
        int result = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > result) result = arr[i];
        }
        return result;
    }

    static int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) return new int[]{i, j};
            }
        }
        return new int[]{-1, -1};
    }

    static void printMatrix(int[][] m) {
        for (int[] row : m) System.out.println(Arrays.toString(row));
    }

    public static void main(String[] args) {
        int[] scores = {85, 92, 78, 95, 88};

        System.out.println("Sum: "     + sum(scores));      // 438
        System.out.println("Average: " + average(scores));  // 87.6
        System.out.println("Max: "     + max(scores));      // 95

        Arrays.sort(scores);
        System.out.println("Sorted: " + Arrays.toString(scores)); // [78, 85, 88, 92, 95]

        int[] nums = {2, 7, 11, 15};
        System.out.println("TwoSum(9): " + Arrays.toString(twoSum(nums, 9))); // [0, 1]

        int[][] grid = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        printMatrix(grid);
    }
}
