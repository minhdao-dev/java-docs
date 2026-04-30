package fundamentals.strings;

public class StringDemo {

    static boolean isPalindrome(String s) {
        String cleaned = s.toLowerCase().replaceAll("[^a-z0-9]", "");
        String reversed = new StringBuilder(cleaned).reverse().toString();
        return cleaned.equals(reversed);
    }

    static String repeat(String s, int n) {
        return s.repeat(n); // Java 11+
    }

    static String buildCsv(String[] values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            sb.append(values[i]);
            if (i < values.length - 1) sb.append(",");
        }
        return sb.toString();
    }

    static int countOccurrences(String text, String word) {
        int count = 0, idx = 0;
        while ((idx = text.indexOf(word, idx)) != -1) {
            count++;
            idx += word.length();
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(isPalindrome("A man, a plan, a canal: Panama")); // true
        System.out.println(isPalindrome("race a car"));                     // false

        System.out.println(repeat("ab", 4)); // "abababab"

        String[] names = {"An", "Binh", "Chi"};
        System.out.println(buildCsv(names)); // "An,Binh,Chi"

        System.out.println(countOccurrences("banana", "an")); // 2

        System.out.println(String.join(",", names)); // "An,Binh,Chi"
    }
}
