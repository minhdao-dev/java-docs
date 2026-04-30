# File I/O

## 1. What is File I/O

**File I/O** (Input/Output) is the ability to read data from files and write data to files on disk. It is an essential skill for any application that needs **persistent storage** — when the program exits, the data survives.

Java has two generations of file APIs:

| | `java.io` (legacy) | `java.nio.file` (modern — use this) |
|---|---|---|
| Since | Java 1.0 | Java 7+ |
| Core classes | `File`, `FileReader`, `FileWriter` | `Path`, `Files` |
| API style | Verbose, many checked exceptions | Clean, expressive, readable |
| Default charset | Platform-dependent | Explicit UTF-8 |
| Features | Basic | Complete — atomic ops, symbolic links, etc. |

**This lesson focuses on `java.nio.file` — the modern API used in all new Java projects.**

---

## 2. Why It Matters

Without File I/O, data only lives in RAM — it's gone the moment the program exits. Every real application needs persistence:

- Console apps: save config, save user data to `.txt` / `.csv`
- Backend: read config files (`application.yml`), process file uploads, export reports
- Logging: write logs to a file for later debugging
- Student Grade Manager project: save the student list to a file and reload it on startup

---

## 3. Path — Representing a File Path

`Path` represents a file or directory path. Use `Path.of()` (Java 11+) or `Paths.get()` (Java 7+) to create one:

```java
import java.nio.file.Path;

Path p1 = Path.of("data/students.txt");          // relative path
Path p2 = Path.of("C:/projects/data/file.txt");  // absolute path (Windows)
Path p3 = Path.of("/home/user/data/file.txt");   // absolute path (Linux/Mac)

// Combine paths — use resolve(), not string concatenation
Path dir  = Path.of("data");
Path file = dir.resolve("students.txt"); // data/students.txt
```

### Useful Path methods

```java
Path p = Path.of("data/reports/2026/students.txt");

System.out.println(p.getFileName());    // students.txt
System.out.println(p.getParent());      // data/reports/2026
System.out.println(p.toAbsolutePath()); // /home/user/project/data/reports/2026/students.txt
System.out.println(p.toString());       // data/reports/2026/students.txt
```

!!! tip "Always use Path.of(), never concatenate path strings manually"
    `Path.of("data", "reports", "file.txt")` automatically uses the correct separator for the OS (Windows: `\`, Unix: `/`). Manual string concatenation (`"data" + "/" + "file.txt"`) breaks on other operating systems.

---

## 4. Files — File Operations

`java.nio.file.Files` is a utility class with static methods for reading, writing, creating, deleting, and copying files.

### Check existence and create directories

```java
import java.nio.file.Files;
import java.nio.file.Path;

Path dir  = Path.of("data");
Path file = dir.resolve("students.txt");

// Check
System.out.println(Files.exists(file));        // true/false
System.out.println(Files.isDirectory(dir));    // true/false
System.out.println(Files.isRegularFile(file)); // true/false

// Create directory tree (no error if it already exists)
Files.createDirectories(dir);
```

---

## 5. Reading Files

### readString() — entire file into a String (Java 11+)

```java
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

Path path = Path.of("data/note.txt");

try {
    String content = Files.readString(path, StandardCharsets.UTF_8);
    System.out.println(content);
} catch (IOException e) {
    System.err.println("Cannot read file: " + e.getMessage());
}
```

### readAllLines() — each line as a List\<String\>

```java
import java.util.List;

try {
    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
    for (String line : lines) {
        System.out.println(line);
    }
} catch (IOException e) {
    System.err.println("Error: " + e.getMessage());
}
```

### lines() — Stream\<String\> for large files (lazy loading)

```java
import java.util.stream.Stream;

try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) { // (1)
    stream.filter(line -> !line.isBlank())
          .forEach(System.out::println);
} catch (IOException e) {
    System.err.println("Error: " + e.getMessage());
}
```

1. `Files.lines()` returns a Stream — always close it with try-with-resources to avoid file handle leaks. This is the correct approach for large files because it reads one line at a time on demand, without loading everything into RAM.

!!! tip "Which method to use?"
    - Small files (< a few MB): `readString()` or `readAllLines()` — simplest
    - Large files (logs, big CSVs): `Files.lines()` + Stream — lazy, memory-efficient

---

## 6. Writing Files

### writeString() — write a String to a file (Java 11+)

```java
import java.nio.file.StandardOpenOption;

Path path = Path.of("data/output.txt");
String content = "Hello, File I/O!";

try {
    // Overwrite (default) — creates file if missing, clears existing content
    Files.writeString(path, content, StandardCharsets.UTF_8);

    // Append — adds to the end of the file
    Files.writeString(path, "\nNew line", StandardCharsets.UTF_8,
                      StandardOpenOption.APPEND);
} catch (IOException e) {
    System.err.println("Cannot write file: " + e.getMessage());
}
```

### write() — write List\<String\> (one element per line)

```java
import java.util.List;

List<String> lines = List.of("Line 1", "Line 2", "Line 3");

try {
    Files.write(path, lines, StandardCharsets.UTF_8);
} catch (IOException e) {
    System.err.println("Error: " + e.getMessage());
}
```

### StandardOpenOption — control write behavior

```java
// Create new file — fails if file already exists
Files.writeString(path, content, StandardOpenOption.CREATE_NEW);

// Overwrite (default behavior)
Files.writeString(path, content, StandardOpenOption.TRUNCATE_EXISTING);

// Append to end
Files.writeString(path, content, StandardOpenOption.APPEND);

// Create if absent, overwrite if present
Files.writeString(path, content,
    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
```

---

## 7. Copy, Move, Delete

```java
Path src = Path.of("data/students.txt");
Path dst = Path.of("backup/students_backup.txt");

// Copy
Files.copy(src, dst);
// Copy and overwrite if dst exists
Files.copy(src, dst, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

// Move (rename or relocate)
Files.move(src, Path.of("data/students_old.txt"));

// Delete
Files.delete(path);          // throws exception if file does not exist
Files.deleteIfExists(path);  // no exception if file does not exist
```

---

## 8. BufferedReader / BufferedWriter — for Large Files

When you need to process large files with more complex logic, use `BufferedReader`/`BufferedWriter` with try-with-resources:

```java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

// Read line by line with BufferedReader
Path input = Path.of("data/large-file.txt");

try (BufferedReader reader = Files.newBufferedReader(input, StandardCharsets.UTF_8)) {
    String line;
    while ((line = reader.readLine()) != null) {
        System.out.println(line);
    }
} catch (IOException e) {
    System.err.println("Read error: " + e.getMessage());
}
```

```java
// Write line by line with BufferedWriter
Path output = Path.of("data/result.txt");

try (BufferedWriter writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8)) {
    writer.write("First line");
    writer.newLine(); // cross-platform line break (\r\n or \n depending on OS)
    writer.write("Second line");
} catch (IOException e) {
    System.err.println("Write error: " + e.getMessage());
}
```

---

## 9. Try-with-resources — Mandatory for I/O

Every I/O resource (`BufferedReader`, `BufferedWriter`, `Stream<String>`, ...) **must be closed** after use to prevent file descriptor leaks. Try-with-resources closes resources automatically even when exceptions occur:

```java
// ❌ Resource not closed — file descriptor leak
BufferedReader reader = Files.newBufferedReader(path);
String content = reader.readLine();
// If an exception occurs here, reader is never closed

// ✅ Try-with-resources — always closes the resource
try (BufferedReader reader = Files.newBufferedReader(path)) {
    String content = reader.readLine();
    // reader.close() is called automatically when leaving this block
}

// Multiple resources — closed in reverse order (writer first, then reader)
try (BufferedReader reader = Files.newBufferedReader(src);
     BufferedWriter writer = Files.newBufferedWriter(dst)) {
    String line;
    while ((line = reader.readLine()) != null) {
        writer.write(line);
        writer.newLine();
    }
}
```

---

## 10. Full Example — Student Data Persistence

A practical example directly relevant to the **Student Grade Manager** project:

!!! info "Verified"
    Full compilable source: [`StudentStorage.java`](https://github.com/minhdao-dev/java-docs/blob/main/examples/src/main/java/fundamentals/fileio/StudentStorage.java)

```java linenums="1"
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class StudentStorage {

    private static final Path DATA_FILE = Path.of("data/students.txt");

    public static void main(String[] args) throws IOException {
        // Ensure the directory exists
        Files.createDirectories(DATA_FILE.getParent());

        // Save a list of students
        List<String> students = List.of(
            "Nguyen Van A,8.5,9.0,7.5",
            "Tran Thi B,9.0,8.5,9.5",
            "Le Van C,6.5,7.0,7.5"
        );
        saveStudents(students);
        System.out.println("Saved " + students.size() + " students.");

        // Reload from file
        List<String[]> loaded = loadStudents();
        System.out.println("\nStudent list:");
        for (String[] parts : loaded) {
            String name = parts[0];
            double avg  = calcAverage(parts);
            System.out.printf("  %-15s GPA: %.2f%n", name, avg);
        }

        // Add one more student (append)
        appendStudent("Pham Thi D,8.0,8.0,9.0");
        System.out.println("\nAdded new student.");

        // Reload to confirm
        System.out.println("Total: " + loadStudents().size() + " students.");
    }

    static void saveStudents(List<String> records) throws IOException { // (1)
        Files.write(DATA_FILE, records, StandardCharsets.UTF_8);
    }

    static List<String[]> loadStudents() throws IOException { // (2)
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
```

1. `Files.write()` with a `List<String>` writes each element on its own line and appends a newline automatically. By default it overwrites the whole file.
2. Always check `Files.exists()` before reading to avoid `NoSuchFileException` — the file may not exist on the first run.

**Output:**
```
Saved 3 students.

Student list:
  Nguyen Van A    GPA: 8.33
  Tran Thi B      GPA: 9.00
  Le Van C        GPA: 7.00

Added new student.
Total: 4 students.
```

---

## 11. Common Mistakes

### Mistake 1 — Not specifying a charset

```java
// ❌ Charset depends on the OS — may produce garbled text on Windows
Files.readString(path); // uses the platform's default charset

// ✅ Always specify UTF-8 explicitly
Files.readString(path, StandardCharsets.UTF_8);
```

### Mistake 2 — Forgetting to create parent directories

```java
Path file = Path.of("data/reports/2026/output.txt");

// ❌ NoSuchFileException if data/reports/2026 does not exist
Files.writeString(file, "content");

// ✅ Create all parent directories first
Files.createDirectories(file.getParent());
Files.writeString(file, "content");
```

### Mistake 3 — Not closing the Stream from Files.lines()

```java
// ❌ Resource leak — Stream is never closed
Stream<String> stream = Files.lines(path);
stream.forEach(System.out::println);
// stream.close() is never called if an exception occurs

// ✅ Try-with-resources guarantees closure
try (Stream<String> stream = Files.lines(path)) {
    stream.forEach(System.out::println);
}
```

### Mistake 4 — Using string concatenation for paths

```java
// ❌ Hard-coded "/" — breaks on Windows
String path = "data" + "/" + "students.txt";

// ✅ Path.of() handles the separator for each OS
Path path = Path.of("data", "students.txt");
```

### Mistake 5 — Overwriting instead of appending to a log file

```java
// ❌ Every call wipes out previous content
Files.writeString(logFile, newEntry); // all old logs lost!

// ✅ Use the APPEND option
Files.writeString(logFile, newEntry, StandardOpenOption.APPEND);
```

---

## 12. Interview Questions

**Q1: What is the difference between `java.io.File` and `java.nio.file.Path`?**

> `java.io.File` is the legacy API from Java 1.0 — many methods return `boolean` instead of throwing exceptions on failure, it has no symbolic link support, and its default charset is platform-dependent. `java.nio.file.Path` (Java 7+) throws exceptions properly, supports all modern OS features, and pairs with the `Files` utility class for a much cleaner API. All new projects should use NIO.

**Q2: Why must you explicitly specify UTF-8 when reading or writing files?**

> Without a specified charset, Java uses the platform default (`Charset.defaultCharset()`). On Windows this is often `windows-1252`; on Linux/Mac it is `UTF-8`. A file written on Windows and read on Linux will produce garbled characters for any non-ASCII content (e.g., Vietnamese text). Explicitly specifying `StandardCharsets.UTF_8` guarantees consistent behavior across all platforms.

**Q3: When should you use `readAllLines()` vs `Files.lines()`?**

> `readAllLines()` loads the entire file into a `List<String>` in memory — simple and fine for small files (under a few MB). `Files.lines()` returns a lazy `Stream<String>` and reads one line at a time on demand — ideal for large files (logs, multi-million-row CSVs) since it doesn't load everything into RAM. Important: `Files.lines()` holds a file handle and must be closed with try-with-resources.

**Q4: How does try-with-resources work with I/O?**

> Try-with-resources (Java 7+) automatically calls `.close()` on any object that implements `AutoCloseable` when leaving the block — even when an exception is thrown. Without it, you'd need a `finally { reader.close(); }` block with complex exception handling. Every I/O resource (`BufferedReader`, `BufferedWriter`, `Stream<String>`, ...) must be closed to prevent file descriptor leaks.

**Q5: What is the difference between `Files.write()` and `Files.writeString()`?**

> `Files.write(path, List<String>)` accepts a `List<String>` and writes each element on a separate line, appending a newline automatically — convenient when you already have a list of lines. `Files.writeString(path, String)` accepts a single `String` and writes it verbatim — convenient when you have pre-formatted content or a custom format. Both support `StandardOpenOption`.

---

## 13. References

| Resource | Content |
|----------|---------|
| [Oracle Docs — java.nio.file.Files](https://docs.oracle.com/en/java/docs/api/java.base/java/nio/file/Files.html) | Full Javadoc |
| [Oracle Tutorial — Basic I/O](https://docs.oracle.com/javase/tutorial/essential/io/index.html) | Official guide |
| [Baeldung — Java NIO2 File API](https://www.baeldung.com/java-nio-2-file-api) | Practical walkthrough |
| *Effective Java* — Joshua Bloch | Item 9: Prefer try-with-resources to try-finally |
