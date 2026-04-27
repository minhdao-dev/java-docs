# Terminal Basics

## 1. What is the terminal and why learn it

The **terminal** (also called command line, command prompt, or shell) is how you give instructions to the computer using text instead of clicking icons.

For Java developers, the terminal is essential for:

- Compiling and running Java without opening an IDE
- Using Maven / Gradle to build projects
- Running servers, checking logs, managing processes
- Using Git to manage source code

You don't need to memorize hundreds of commands. This lesson focuses on the **10 most-used commands** you will type every day.

---

## 2. Opening the terminal

=== "Windows"

    Windows has two main terminals:

    **Command Prompt (CMD)** — the classic terminal:
    - Press `Windows + R`, type `cmd`, press Enter.
    - Or: search "Command Prompt" in the Start Menu.

    **PowerShell** — more modern, supports most Unix commands:
    - Press `Windows + X`, choose **Windows PowerShell** or **Terminal**.
    - Or: search "PowerShell" in the Start Menu.

    !!! tip "Recommendation: use Windows Terminal"
        Install **Windows Terminal** from the Microsoft Store — supports tabs, a clean interface, and integrates both CMD and PowerShell. Search "Windows Terminal" in the Microsoft Store and install for free.

=== "macOS"

    **Terminal** is built in:
    - Press `Cmd + Space`, type `Terminal`, press Enter.
    - Or: **Finder → Applications → Utilities → Terminal**.

    The default shell on macOS is **Zsh** (since macOS Catalina).

    !!! tip "iTerm2 — a better terminal for macOS"
        [iTerm2](https://iterm2.com/) provides more features than the default Terminal: search, split panes, rich color themes. Free download at iterm2.com.

=== "Linux"

    - **Ubuntu:** `Ctrl + Alt + T`
    - **GNOME:** search "Terminal" in the launcher
    - **KDE:** search "Konsole"

---

## 3. Understanding the prompt

When you open the terminal, you see a line like this:

=== "Windows CMD"

    ```
    C:\Users\YourName>_
    ```

    - `C:\Users\YourName` — your current directory
    - `>` — the prompt, waiting for your input
    - `_` — the cursor

=== "macOS / Linux"

    ```
    yourname@MacBook ~ %_
    ```

    - `yourname` — your username
    - `MacBook` — your computer's name
    - `~` — your home directory (shorthand for `/Users/YourName` on macOS)
    - `%` or `$` — the prompt

---

## 4. Navigating directories

### See your current directory

=== "Windows CMD"

    ```cmd
    cd
    ```

=== "macOS / Linux / PowerShell"

    ```bash
    pwd
    ```

    Example output: `/Users/yourname` — this is where you currently are.

---

### List files and directories

=== "Windows CMD"

    ```cmd
    dir
    ```

=== "macOS / Linux / PowerShell"

    ```bash
    ls
    ```

    More detail (size, permissions, date):

    ```bash
    ls -l
    ```

    Include hidden files:

    ```bash
    ls -la
    ```

---

### Move into a directory

```bash
cd directory-name
```

Example — go into `Documents`:

```bash
cd Documents
```

Go into a nested directory:

```bash
cd Documents/java-projects
```

Go up one level to the parent directory:

```bash
cd ..
```

Go back to the home directory:

=== "macOS / Linux"

    ```bash
    cd ~
    ```

=== "Windows CMD"

    ```cmd
    cd %USERPROFILE%
    ```

---

### Absolute vs relative paths

| Type | Example | Meaning |
|------|---------|---------|
| **Absolute** | `cd /Users/yourname/Documents` | Starts from the root, works from any location |
| **Relative** | `cd Documents` | Relative to your current directory |

---

## 5. Managing files and directories

### Create a new directory

```bash
mkdir directory-name
```

Create multiple levels at once:

=== "macOS / Linux"

    ```bash
    mkdir -p projects/java/hello-world
    ```

=== "Windows CMD"

    ```cmd
    mkdir projects\java\hello-world
    ```

---

### Delete a file

=== "macOS / Linux"

    ```bash
    rm file.txt
    ```

    Delete a directory and everything inside it:

    ```bash
    rm -rf directory-name/
    ```

    !!! warning "Be careful with `rm -rf`"
        This permanently deletes files — they do not go to the Trash. Double-check the directory name before running.

=== "Windows CMD"

    ```cmd
    del file.txt
    rmdir /s /q directory-name
    ```

---

### Copy and move files

=== "macOS / Linux"

    ```bash
    cp source.txt destination.txt          # copy file
    cp -r source-dir/ destination-dir/     # copy directory
    mv source.txt destination.txt          # move (or rename)
    ```

=== "Windows CMD"

    ```cmd
    copy source.txt destination.txt
    xcopy source-dir destination-dir /E /I
    move source.txt destination.txt
    ```

---

## 6. Read file contents

=== "macOS / Linux"

    ```bash
    cat file.txt        # print the entire file
    head -20 file.txt   # first 20 lines
    tail -20 file.txt   # last 20 lines
    ```

=== "Windows CMD"

    ```cmd
    type file.txt
    ```

---

## 7. Compile and run Java from the terminal

This is the most important part of this lesson.

### Example directory structure

```
hello-java/
└── HelloWorld.java
```

### Create the Java file

Create the directory and navigate into it:

```bash
mkdir hello-java
cd hello-java
```

Use a text editor to create `HelloWorld.java` with this content:

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

!!! note "The filename must match the class name"
    A file named `HelloWorld.java` must contain `public class HelloWorld`. A mismatch causes `javac` to report an error.

### Compile

```bash
javac HelloWorld.java
```

If there are no errors, this produces a `HelloWorld.class` file in the same directory.

### Run

```bash
java HelloWorld
```

!!! warning "Do not add `.class` when running"
    The correct command is `java HelloWorld`, not `java HelloWorld.class`.

Output:

```
Hello, World!
```

### The full workflow

```
HelloWorld.java  →  javac  →  HelloWorld.class  →  java  →  Output
(source code)      (compile)    (bytecode)          (run)
```

---

## 8. Other useful commands

### Clear the screen

=== "macOS / Linux"

    ```bash
    clear
    ```

=== "Windows CMD"

    ```cmd
    cls
    ```

### View command history

=== "macOS / Linux"

    ```bash
    history
    ```

Press the **↑** key to repeat the previous command — no need to retype it.

### Stop a running program

Press `Ctrl + C` to stop any currently running command (very useful when running a server).

### Search inside a file

=== "macOS / Linux"

    ```bash
    grep "search-term" file.txt
    grep -r "search-term" directory/   # search recursively through a directory
    ```

=== "Windows CMD"

    ```cmd
    findstr "search-term" file.txt
    ```

---

## 9. Essential command reference

| Action | Windows CMD | macOS / Linux |
|--------|------------|---------------|
| Show current directory | `cd` | `pwd` |
| List contents | `dir` | `ls` |
| Enter a directory | `cd dirname` | `cd dirname` |
| Go up one level | `cd ..` | `cd ..` |
| Create directory | `mkdir name` | `mkdir name` |
| Delete file | `del file.txt` | `rm file.txt` |
| Delete directory | `rmdir /s /q name` | `rm -rf name/` |
| Copy file | `copy a.txt b.txt` | `cp a.txt b.txt` |
| Move/rename | `move a.txt b.txt` | `mv a.txt b.txt` |
| Read file | `type file.txt` | `cat file.txt` |
| Clear screen | `cls` | `clear` |
| Stop program | `Ctrl + C` | `Ctrl + C` |
| Compile Java | `javac File.java` | `javac File.java` |
| Run Java | `java ClassName` | `java ClassName` |

---

## 10. Tips for working efficiently in the terminal

- **Tab to autocomplete:** type the first few letters of a file or directory name and press `Tab` — the terminal fills in the rest.
- **↑ ↓ to browse history:** no need to retype previous commands.
- **Ctrl + C to exit:** when any command gets stuck or runs too long — press `Ctrl + C` to stop it immediately.
- **Ctrl + L to clear the screen:** shortcut for `clear` / `cls`.
- **Drag and drop paths:** drag a file or folder from Finder (macOS) or Explorer (Windows) into the terminal window — it automatically pastes the full path.

---

Next: [Git and GitHub](04-git-github.md) — learn how to save your code history and collaborate with others.
