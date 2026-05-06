# Install IDE

## 1. What is an IDE

An **IDE** (Integrated Development Environment) is an all-in-one coding environment that combines:

- **Code editor** with syntax highlighting and autocomplete
- **Integrated compiler/runner** — press one button to build and run your program
- **Debugger** — pause execution at any line to inspect variable values
- **Project management** — organize files, libraries, and build configuration

Writing Java without an IDE is possible, but an IDE catches errors as you type (before you even run the code), saving hours every day.

---

## 2. Which IDE to choose?

| IDE | Best for | Free? |
|-----|----------|-------|
| **IntelliJ IDEA Community** | Beginners and professional developers | ✓ |
| **Eclipse** | Enterprise environments, legacy projects | ✓ |
| **VS Code + Java Extension Pack** | Developers already familiar with VS Code | ✓ |
| **Apache NetBeans** | Beginners, students — friendly UI, Maven built-in | ✓ |

!!! tip "Recommendation for beginners"
    Choose **IntelliJ IDEA Community Edition**. It is the most popular Java IDE, has the most beginner-friendly interface, and most Java learning resources use IntelliJ. You can install other IDEs later once you're comfortable.

---

## 3. Install IntelliJ IDEA Community Edition

=== "Windows"

    ### Step 1 — Download IntelliJ IDEA

    1. Open a browser and go to:  
       [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)
    2. Scroll down to find the **Community Edition** section (the right panel, labeled "For JVM and Android development").
    3. Click the black **Download** button under **Community Edition** (not Ultimate).
    4. The `.exe` file is downloaded (~850 MB).

    ### Step 2 — Run the installer

    1. Open the downloaded `.exe` file from Downloads.
    2. Click **Yes** when prompted to allow changes to the computer.
    3. Welcome screen → click **Next**.
    4. Choose installation directory (keep the default) → click **Next**.
    5. On the **Installation Options** screen, check:
        - ✅ **Create Desktop Shortcut** — desktop icon for quick access
        - ✅ **Add "Open Folder as Project"** — right-click any folder to open in IntelliJ
        - ✅ **Add launchers dir to the PATH** — run IntelliJ from the terminal if needed
        - `.java` — optional (opens `.java` files in IntelliJ when double-clicked)
    6. Click **Next** → **Install** → wait 1–2 minutes → click **Finish**.

    ### Step 3 — Open IntelliJ for the first time

    1. Double-click the **IntelliJ IDEA** icon on the desktop (or search in the Start Menu).
    2. The **Import Settings** screen asks if you want to import old settings — select **Do not import settings** → **OK**.
    3. The **Welcome to IntelliJ IDEA** screen appears — this is the main start screen.

=== "macOS"

    ### Option 1 — Download from JetBrains (recommended)

    1. Go to [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)
    2. Scroll to **Community Edition**, choose the correct build for your chip:
        - **Apple Silicon (M1/M2/M3):** download `.dmg (Apple Silicon)`
        - **Intel:** download `.dmg (Intel)`
    3. Open the `.dmg` file, drag the **IntelliJ IDEA CE** icon into **Applications**.
    4. Open **Finder → Applications → IntelliJ IDEA CE** to launch.

    ### Option 2 — Install via Homebrew

    ```bash
    brew install --cask intellij-idea-ce
    ```

=== "Linux"

    ### Option 1 — Download .tar.gz

    1. Go to [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)
    2. Scroll to **Community Edition**, download the `.tar.gz` file.
    3. Extract and run:

        ```bash
        tar -xzf ideaIC-*.tar.gz -C ~/opt/
        ~/opt/idea-IC-*/bin/idea.sh
        ```

    4. Inside IntelliJ, go to **Tools → Create Desktop Entry** to create an application menu shortcut.

    ### Option 2 — Snap (Ubuntu)

    ```bash
    sudo snap install intellij-idea-community --classic
    ```

### Verify IntelliJ works

After opening IntelliJ IDEA for the first time:

1. Click **New Project** on the Welcome screen.
2. Select **Java** in the left column, ensure **JDK** shows `21` (if not, click the dropdown and choose **Add JDK** → point to the JDK 21 directory).
3. Click **Create** → IntelliJ creates a sample project.
4. Open `Main.java` in the left file tree → click the green **▶ Run** button → the program runs and output appears at the bottom.

!!! success "Success if"
    The **Run** panel at the bottom shows `Hello, World!` (or the sample program's output). IntelliJ is correctly connected to JDK 21.

---

## 4. Install Eclipse IDE

Eclipse is a long-established IDE that remains widely used in enterprise Java environments.

=== "Windows"

    ### Step 1 — Download Eclipse Installer

    1. Go to [https://www.eclipse.org/downloads/](https://www.eclipse.org/downloads/)
    2. Click **Download x86_64** to download the **Eclipse Installer** (~70 MB).

    ### Step 2 — Run Eclipse Installer

    1. Open the downloaded `.exe` file.
    2. The Eclipse type selection screen appears → choose **Eclipse IDE for Java Developers**.
    3. Check the **JVM** field — Eclipse should auto-detect JDK 21. If not, click the field and point it to the JDK 21 directory.
    4. Click **Install** → accept the License → wait for the download (~400 MB) → click **Launch**.

=== "macOS"

    1. Go to [https://www.eclipse.org/downloads/](https://www.eclipse.org/downloads/), download the Eclipse Installer for macOS.
    2. Open the `.dmg` file, run the installer, select **Eclipse IDE for Java Developers**.
    3. Alternatively: `brew install --cask eclipse-java`

=== "Linux"

    ```bash
    # Ubuntu/Debian
    sudo snap install eclipse --classic

    # Or download manually from eclipse.org → extract and run eclipse/eclipse
    ```

### Create your first project in Eclipse

1. Open Eclipse, choose a **Workspace** folder (where your projects will be saved) → click **Launch**.
2. Go to **File → New → Java Project**.
3. Name the project (e.g. `HelloWorld`) → ensure **JRE** shows `JavaSE-21` → click **Finish**.
4. Right-click the `src` folder → **New → Class**.
5. Name it `Main`, check **public static void main(String[] args)** → click **Finish**.
6. Click the **▶ Run** button (or `Ctrl + F11`) to run.

---

## 5. Install VS Code + Java Extension Pack

VS Code is a lightweight editor — less resource-heavy than IntelliJ or Eclipse. To write Java in VS Code you need to install additional extensions.

=== "Windows / macOS"

    ### Step 1 — Install VS Code

    1. Go to [https://code.visualstudio.com/](https://code.visualstudio.com/), click **Download**.
    2. Run the installer and follow the on-screen steps.

    ### Step 2 — Install the Java Extension Pack

    1. Open VS Code.
    2. Press `Ctrl + Shift + X` (Windows) or `Cmd + Shift + X` (macOS) to open **Extensions**.
    3. In the search box, type: `Extension Pack for Java`
    4. Find the extension by **Microsoft** (blue logo) → click **Install**.

    This pack includes 6 components:
    - **Language Support for Java** — syntax highlighting, autocomplete, error detection
    - **Debugger for Java** — integrated debugger
    - **Test Runner for Java** — run JUnit tests
    - **Maven for Java** — Maven project support
    - **Project Manager for Java** — project navigation
    - **IntelliCode** — AI-assisted code suggestions

    ### Step 3 — Open a Java project

    1. Go to **File → Open Folder**, select your project directory.
    2. Open a `.java` file — VS Code automatically activates the Java extension.
    3. Click the **▶ Run** button that appears above the `main` method to run the program.

=== "Linux"

    ```bash
    # Ubuntu/Debian — download .deb from the VS Code website, or:
    sudo snap install code --classic
    ```

    Then install the **Extension Pack for Java** as described above.

!!! warning "VS Code requires JDK to be installed first"
    VS Code does not manage JDK itself — it reads the `JAVA_HOME` variable or auto-detects the JDK from `PATH`. Make sure JDK 21 is installed and `JAVA_HOME` is set correctly before opening VS Code.

---

## 6. Install Apache NetBeans

NetBeans is an open-source IDE developed by the **Apache Software Foundation**. It is widely used in university Java courses thanks to its straightforward UI, built-in Maven support, and zero configuration required to get started.

=== "Windows"

    ### Step 1 — Download NetBeans

    1. Open a browser and go to:  
       [https://netbeans.apache.org/front/main/download/](https://netbeans.apache.org/front/main/download/)
    2. Select the latest release (e.g. **Apache NetBeans 25**).
    3. Download the **Windows Installer** (`.exe`, ~600 MB).

    ### Step 2 — Run the installer

    1. Open the downloaded `.exe` file.
    2. Click **Next** on the welcome screen.
    3. Choose the installation directory (keep the default) → **Next**.
    4. Verify the **JDK** field points to JDK 21 → **Next**.
    5. Click **Install** → wait for completion → click **Finish**.

    ### Step 3 — Open NetBeans for the first time

    1. Find **Apache NetBeans IDE** in the Start Menu or double-click the desktop icon.
    2. First launch may take 30–60 seconds.

=== "macOS"

    ### Option 1 — Download the installer

    1. Go to [https://netbeans.apache.org/front/main/download/](https://netbeans.apache.org/front/main/download/), download the **macOS Installer** (`.dmg`).
    2. Open the `.dmg` file, drag **Apache NetBeans** to **Applications**.

    ### Option 2 — Install via Homebrew

    ```bash
    brew install --cask netbeans
    ```

=== "Linux"

    ### Option 1 — Download .sh installer

    1. Go to [https://netbeans.apache.org/front/main/download/](https://netbeans.apache.org/front/main/download/), download the **Linux Installer** (`.sh`).
    2. Open a terminal, make it executable and run:

        ```bash
        chmod +x apache-netbeans-*.sh
        ./apache-netbeans-*.sh
        ```

    3. Follow the on-screen instructions.

    ### Option 2 — Snap (Ubuntu)

    ```bash
    sudo snap install netbeans --classic
    ```

### Create your first project in NetBeans

1. Click **New Project** (or **File → New Project**).
2. Select **Java with Maven → Java Application** → click **Next**.
3. Enter a project name (e.g. `HelloWorld`) → click **Finish**.
4. NetBeans creates `App.java` in the `src` directory.
5. Click **▶ Run Project** (or press `F6`) to run.

!!! success "Success if"
    The **Output** panel at the bottom shows the program's output. NetBeans is correctly connected to JDK 21.

---

## 7. Quick Comparison

| Feature | IntelliJ IDEA CE | Eclipse | VS Code | NetBeans |
|---------|-----------------|---------|---------|---------|
| Startup speed | Slower | Medium | Fast | Medium |
| RAM usage | ~500–800 MB | ~400–600 MB | ~200–400 MB | ~300–500 MB |
| Smart autocomplete | ★★★★★ | ★★★ | ★★★★ | ★★★★ |
| Maven/Gradle integration | Excellent | Good | Good | Good (Maven built-in) |
| Community popularity | Very high | High (enterprise) | High (frontend) | Medium (academic) |
| Plugins/Extensions | Rich | Rich | Rich | Moderate |

---

## 8. Keyboard shortcuts to learn now

=== "IntelliJ IDEA"

    | Action | Windows / Linux | macOS |
    |--------|-----------------|-------|
    | Run program | `Shift + F10` | `Ctrl + R` |
    | Debug program | `Shift + F9` | `Ctrl + D` |
    | Autocomplete suggestions | `Ctrl + Space` | `Ctrl + Space` |
    | Comment/uncomment line | `Ctrl + /` | `Cmd + /` |
    | Search everything | `Shift Shift` | `Shift Shift` |
    | Quick fix | `Alt + Enter` | `Option + Enter` |
    | Auto-format code | `Ctrl + Alt + L` | `Cmd + Option + L` |

=== "Eclipse"

    | Action | Windows / Linux | macOS |
    |--------|-----------------|-------|
    | Run program | `Ctrl + F11` | `Cmd + F11` |
    | Debug program | `F11` | `F11` |
    | Autocomplete suggestions | `Ctrl + Space` | `Ctrl + Space` |
    | Comment/uncomment line | `Ctrl + /` | `Cmd + /` |
    | Auto-format code | `Ctrl + Shift + F` | `Cmd + Shift + F` |
    | Auto-import | `Ctrl + Shift + O` | `Cmd + Shift + O` |

=== "VS Code"

    | Action | Windows / Linux | macOS |
    |--------|-----------------|-------|
    | Run/Debug program | `F5` | `F5` |
    | Autocomplete suggestions | `Ctrl + Space` | `Ctrl + Space` |
    | Comment/uncomment line | `Ctrl + /` | `Cmd + /` |
    | Auto-format code | `Shift + Alt + F` | `Shift + Option + F` |
    | Open Command Palette | `Ctrl + Shift + P` | `Cmd + Shift + P` |

=== "NetBeans"

    | Action | Windows / Linux | macOS |
    |--------|-----------------|-------|
    | Run project | `F6` | `F6` |
    | Debug project | `F5` | `F5` |
    | Autocomplete suggestions | `Ctrl + Space` | `Ctrl + Space` |
    | Comment/uncomment line | `Ctrl + /` | `Cmd + /` |
    | Auto-format code | `Alt + Shift + F` | `Option + Shift + F` |
    | Fix imports automatically | `Ctrl + Shift + I` | `Cmd + Shift + I` |
    | Build project | `F11` | `F11` |

---

## 9. Troubleshooting

### IntelliJ does not recognize the JDK

**Symptom:** IntelliJ shows "No JDK configured" or all Java code is underlined in red.

**Fix:**
1. Go to **File → Project Structure** (`Ctrl + Alt + Shift + S`).
2. Select **Project** in the left column.
3. In the **SDK** field, click **Add SDK → JDK** → point it to the JDK 21 directory.

### VS Code can't find Java

**Symptom:** Message says "Java runtime could not be located".

**Fix:**  
Open VS Code Settings (`Ctrl + ,`), search for `java.jdt.ls.java.home`, and set the value to the JDK 21 path (e.g. `C:\Program Files\Eclipse Adoptium\jdk-21.0.3.9-hotspot`).

### Eclipse runs very slowly (Windows)

**Cause:** Eclipse needs more memory.

**Fix:** Open the `eclipse.ini` file in the Eclipse installation folder, find the `-Xmx` line and increase it to `-Xmx2g` (2 GB maximum RAM).

### NetBeans does not recognize the JDK

**Symptom:** NetBeans shows "No Java Platform defined" or cannot build any project.

**Fix:**
1. Go to **Tools → Java Platforms**.
2. Click **Add Platform** → select **Java Standard Edition** → **Next**.
3. Point to the JDK 21 directory → **Next** → **Finish**.

### NetBeans starts very slowly

**Cause:** The default memory allocation for NetBeans is low.

**Fix:** Open the `netbeans.conf` file in the installation directory (usually `C:\Program Files\NetBeans-25\netbeans\etc\`), find the `netbeans_default_options` line, and append `-J-Xmx2g` to the end of the options string.

---

With your IDE working correctly, the next step is [Terminal Basics](03-terminal-basics.md) — learning to use the command line to run Java without an IDE.
