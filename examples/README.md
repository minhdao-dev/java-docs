# Java Docs — Examples

Compilable Java 21 source files referenced from the [java-docs](https://github.com/minhdao-dev/java-docs) lessons.

## Prerequisites

- JDK 21+
- Maven 3.9+

Verify: `java -version` and `mvn -version`

## Build all examples

```bash
cd examples
mvn compile
```

## Run a specific example

```bash
mvn exec:java -Dexec.mainClass=fundamentals.jvm.JvmMemoryDemo
```

Replace the class name with any of the following:

| Lesson | Class |
|--------|-------|
| 01 — JVM: How It Works      | `fundamentals.jvm.JvmMemoryDemo` |
| 02 — Variables & Data Types | `fundamentals.variables.DataTypesDemo` |
| 03 — Operators              | `fundamentals.operators.OperatorsDemo` |
| 04 — Control Flow           | `fundamentals.controlflow.ControlFlowDemo` |
| 05 — Loops                  | `fundamentals.loops.LoopsDemo` |
| 06 — Arrays                 | `fundamentals.arrays.ArraysDemo` |
| 07 — Strings                | `fundamentals.strings.StringDemo` |
| 08 — Methods                | `fundamentals.methods.MethodsDemo` |
| 09 — Enums                  | `fundamentals.enums.EnumDemo` |
| 10 — File I/O               | `fundamentals.fileio.StudentStorage` |
| 11 — OOP: Class & Object    | `fundamentals.oop.OopDemo` |
| 12 — Encapsulation          | `fundamentals.encapsulation.UserAccount` |
| 16 — Records                | `fundamentals.records.RecordDemo` |
| 17 — Interfaces             | `fundamentals.interfaces.InterfaceDemo` |
| 18 — instanceof & Casting   | `fundamentals.casting.CastingDemo` |

> Lessons 13, 14, 15 do not have example files yet.

> **Note:** `StudentStorage` writes to `data/students.txt` relative to the working directory.
