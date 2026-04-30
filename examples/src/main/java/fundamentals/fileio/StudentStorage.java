package fundamentals.fileio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates file I/O with {@code java.nio.file}: writing a list of records
 * with {@code Files.write}, reading lines with {@code Files.readAllLines},
 * and appending a record with {@code StandardOpenOption.APPEND}.
 * Creates {@code data/students.txt} in the working directory at runtime.
 *
 * <p>Lesson 10 — File I/O
 * (<a href="https://github.com/minhdao-dev/java-docs/blob/main/docs/java/fundamentals/10-file-io.en.md">docs/java/fundamentals/10-file-io.en.md</a>)
 */
public class StudentStorage {

    private static final Path DATA_FILE = Path.of("data/students.txt");

    public static void main(String[] args) throws IOException {
        Files.createDirectories(DATA_FILE.getParent());

        List<String> students = List.of(
            "Nguyen Van A,8.5,9.0,7.5",
            "Tran Thi B,9.0,8.5,9.5",
            "Le Van C,6.5,7.0,7.5"
        );
        saveStudents(students);
        System.out.println("Saved " + students.size() + " students.");

        List<String[]> loaded = loadStudents();
        System.out.println("\nStudent list:");
        for (String[] parts : loaded) {
            String name = parts[0];
            double avg  = calcAverage(parts);
            System.out.printf("  %-15s GPA: %.2f%n", name, avg);
        }

        appendStudent("Pham Thi D,8.0,8.0,9.0");
        System.out.println("\nAdded new student.");
        System.out.println("Total: " + loadStudents().size() + " students.");
    }

    static void saveStudents(List<String> records) throws IOException {
        Files.write(DATA_FILE, records, StandardCharsets.UTF_8);
    }

    static List<String[]> loadStudents() throws IOException {
        if (!Files.exists(DATA_FILE)) return new ArrayList<>();

        List<String[]> result = new ArrayList<>();
        for (String line : Files.readAllLines(DATA_FILE, StandardCharsets.UTF_8)) {
            if (!line.isBlank()) {
                result.add(line.split(","));
            }
        }
        return result;
    }

    static void appendStudent(String record) throws IOException {
        Files.writeString(DATA_FILE, record + System.lineSeparator(),
                          StandardCharsets.UTF_8, StandardOpenOption.APPEND);
    }

    static double calcAverage(String[] parts) {
        double sum = 0;
        for (int i = 1; i < parts.length; i++) {
            sum += Double.parseDouble(parts[i].trim());
        }
        return sum / (parts.length - 1);
    }
}
