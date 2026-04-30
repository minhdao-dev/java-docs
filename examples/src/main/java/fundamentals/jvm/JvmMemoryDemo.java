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
        // Primitive — sống trong Stack frame của main()
        int count = 5;
        double price = 9.99;

        // Reference sống trên Stack, object thật sống trên Heap
        String label = new String("item");

        // Gọi method — Stack frame mới được tạo
        int result = calculate(count, price);

        System.out.println(result);
    } // frame của main() bị pop. reference label biến mất.
      // String "item" trên Heap giờ eligible for GC.

    private static int calculate(int qty, double unitPrice) {
        // qty và unitPrice là bản sao (pass by value)
        // Frame này nằm trên frame của main() trong Stack
        double total = qty * unitPrice;
        return (int) total;
    } // frame bị pop
}
