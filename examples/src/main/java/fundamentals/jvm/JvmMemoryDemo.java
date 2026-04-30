package fundamentals.jvm;

/**
 * Demonstrates what lives on the Stack vs the Heap at runtime:
 * primitives and references in Stack frames, objects in the Heap,
 * and static fields in the Method Area (Metaspace).
 *
 * <p>Lesson 01 — JVM: How It Works
 * (<a href="https://github.com/minhdao-dev/java-docs/blob/main/docs/java/fundamentals/01-jvm-how-it-works.en.md">docs/java/fundamentals/01-jvm-how-it-works.en.md</a>)
 */
public class JvmMemoryDemo {

    // Static field — sống trong Method Area (Metaspace)
    private static final String APP_NAME = "JvmDemo";

    public static void main(String[] args) {
        System.out.println("=== JVM Memory Demo ===\n");

        // Static field — Method Area (Metaspace), shared across all instances
        System.out.println("[Metaspace ] APP_NAME = \"" + APP_NAME + "\" (static final — loaded once, lives until class is unloaded)");

        // Primitives — live in the Stack frame of main()
        int count = 5;
        double price = 9.99;
        System.out.println("[Stack/main] count   = " + count  + "  (primitive int — stored directly in Stack frame)");
        System.out.println("[Stack/main] price   = " + price  + " (primitive double — stored directly in Stack frame)");

        // Reference on Stack, actual object on Heap
        String label = new String("item");
        System.out.println("[Stack/main] label   = <ref>   (reference variable — lives in Stack frame)");
        System.out.println("[Heap      ] \"item\"            (String object — allocated on Heap, label points here)");

        // Calling calculate() pushes a NEW Stack frame on top of main()'s frame
        System.out.println("\n[Stack     ] Calling calculate(" + count + ", " + price + ") → new frame pushed on top of main()");
        int result = calculate(count, price);
        System.out.println("[Stack     ] calculate() returned → frame popped, local vars qty/unitPrice/total are gone");

        System.out.println("\n[Stack/main] result  = " + result + "  (5 × 9.99 = 49.95, truncated to int → 49)");
        System.out.println("\n[Stack     ] main() ending → 'label' reference gone from Stack");
        System.out.println("[Heap      ] String \"item\" is now unreachable → eligible for GC");
    }

    private static int calculate(int qty, double unitPrice) {
        // qty and unitPrice are COPIES (pass-by-value) — changing them here would NOT affect main()
        System.out.println("[Stack/calc] qty=" + qty + ", unitPrice=" + unitPrice + "  (copies — pass-by-value)");
        double total = qty * unitPrice;
        System.out.println("[Stack/calc] total = " + total + " (local var — lives only in this frame)");
        return (int) total;
    } // frame popped — total, qty, unitPrice are gone
}
