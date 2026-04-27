# Install JDK 21

## 1. What is the JDK

The **JDK** (Java Development Kit) is the toolset required to write and run Java programs. It includes:

- **`javac`** — the compiler, which translates your Java source code into bytecode
- **`java`** — the virtual machine (JVM), which runs that bytecode
- The standard library and debugging tools

Without the JDK, your computer cannot understand or execute any Java code.

### Why JDK 21?

JDK 21 is the latest **LTS (Long-Term Support)** release — Oracle guarantees security patches until at least 2031. It is the standard choice for new projects and most current enterprise environments.

### What is Temurin?

**Eclipse Temurin** (provided by Adoptium) is a free, open-source JDK distribution trusted by the community and major companies (Microsoft, Red Hat, IBM). It is equivalent to Oracle JDK in performance and features, with no commercial licensing restrictions.

---

## 2. Installation

=== "Windows"

    ### Step 1 — Download the installer

    1. Open a browser and go to the Temurin download page:  
       [https://adoptium.net/temurin/releases/](https://adoptium.net/temurin/releases/)
    2. Set the filters at the top:
        - **Version:** `21 - LTS`
        - **OS:** `Windows`
        - **Architecture:** `x64` (standard 64-bit PC; select `aarch64` for ARM devices like Surface Pro X)
        - **Package Type:** `JDK`
        - **File type:** `.msi`
    3. Click **Download** to save the `.msi` file (about 170 MB).

    ### Step 2 — Run the installer

    1. Locate the downloaded `.msi` file (usually in `Downloads`), double-click to open it.
    2. The **User Account Control** prompt asks "Do you want to allow this app to make changes?" — click **Yes**.
    3. The installer welcome screen appears — click **Next**.
    4. The **Custom Setup** screen appears — leave all options at their defaults, **no changes needed**.

        !!! tip "Important"
            Make sure **"Add to PATH"** and **"Set JAVA_HOME variable"** are checked (shown in green). The Temurin installer handles this automatically — just don't uncheck them.

    5. Click **Next** → **Install** → wait 30–60 seconds → click **Finish**.

    ### Step 3 — Verify the installation

    1. Press the **Windows** key, type `cmd`, press Enter to open Command Prompt.
    2. Type the following and press Enter:

        ```cmd
        java -version
        ```

    3. The correct output looks like this:

        ```
        openjdk version "21.0.x" 2024-xx-xx LTS
        OpenJDK Runtime Environment Temurin-21.0.x+xx (build 21.0.x+xx)
        OpenJDK 64-Bit Server VM Temurin-21.0.x+xx (build 21.0.x+xx, mixed mode, sharing)
        ```

    4. Also verify the compiler:

        ```cmd
        javac -version
        ```

        Expected output: `javac 21.0.x`

    !!! success "Installation successful if"
        Both `java -version` and `javac -version` show `21.x.x`. You can skip the troubleshooting section below.

=== "macOS"

    ### Step 1 — Install Homebrew (if not already installed)

    **Homebrew** is the most popular package manager for macOS — similar to an app store but for developer tools.

    1. Open **Terminal**: press `Cmd + Space`, type `Terminal`, press Enter.
    2. Run the following command (copy and paste the entire line):

        ```bash
        /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
        ```

    3. Enter your macOS password when prompted (the cursor will not move — this is normal). Press Enter.
    4. Installation takes 2–5 minutes. When complete, the terminal shows `Installation successful!`.

    !!! note "Already have Homebrew?"
        Run `brew --version` to check. If a version number appears, skip this step.

    ### Step 2 — Install JDK 21 via Homebrew

    ```bash
    brew install --cask temurin@21
    ```

    This downloads and installs Temurin JDK 21. It takes 2–5 minutes depending on your connection speed.

    ### Step 3 — Verify the installation

    ```bash
    java -version
    javac -version
    ```

    Expected output:

    ```
    openjdk version "21.0.x" 2024-xx-xx LTS
    OpenJDK Runtime Environment Temurin-21.0.x+xx (build 21.0.x+xx)
    OpenJDK 64-Bit Server VM Temurin-21.0.x+xx (build 21.0.x+xx, mixed mode)
    ```

    ### Manual installation (without Homebrew)

    If you prefer not to use Homebrew:

    1. Go to [https://adoptium.net/temurin/releases/](https://adoptium.net/temurin/releases/)
    2. Select **Version: 21 - LTS**, **OS: macOS**, **Architecture: aarch64** (Apple Silicon M1/M2/M3) or **x64** (Intel), **Package Type: JDK**, **File type: `.pkg`**
    3. Download and run the `.pkg` file → follow the on-screen instructions.

=== "Linux"

    ### Ubuntu / Debian (APT)

    ```bash
    sudo apt update
    sudo apt install -y wget apt-transport-https gpg

    # Add the Adoptium repository
    wget -qO - https://packages.adoptium.net/artifactory/api/gpg/key/public \
      | gpg --dearmor \
      | sudo tee /etc/apt/trusted.gpg.d/adoptium.gpg > /dev/null

    echo "deb https://packages.adoptium.net/artifactory/deb \
      $(awk -F= '/^VERSION_CODENAME/{print$2}' /etc/os-release) main" \
      | sudo tee /etc/apt/sources.list.d/adoptium.list

    sudo apt update
    sudo apt install -y temurin-21-jdk
    ```

    ### Fedora / RHEL / CentOS (DNF)

    ```bash
    sudo dnf install -y java-21-openjdk-devel
    ```

    !!! note "Fedora uses OpenJDK from its default repository"
        This version is equivalent to Temurin in performance.

    ### Arch Linux (Pacman)

    ```bash
    sudo pacman -S jdk21-openjdk
    ```

    ### Verify

    ```bash
    java -version
    javac -version
    ```

    ### SDKMAN — Install and manage multiple JDK versions

    **SDKMAN** lets you install and switch between multiple JDK versions with a single command — useful when working on projects that require different Java versions.

    ```bash
    # Install SDKMAN
    curl -s "https://get.sdkman.io" | bash
    source "$HOME/.sdkman/bin/sdkman-init.sh"

    # Install Temurin 21
    sdk install java 21.0.3-tem

    # Verify
    java -version
    ```

---

## 3. What is JAVA_HOME and why it matters

`JAVA_HOME` is an environment variable — a system label pointing to the JDK installation directory. Many tools (Maven, Gradle, IDEs) read this variable to locate the JDK.

### Check if JAVA_HOME is already set

=== "Windows"

    Open Command Prompt:

    ```cmd
    echo %JAVA_HOME%
    ```

    Correct output (example):

    ```
    C:\Program Files\Eclipse Adoptium\jdk-21.0.3.9-hotspot
    ```

    If the output is empty (just `%JAVA_HOME%` or a blank line) — see the troubleshooting section below.

=== "macOS / Linux"

    ```bash
    echo $JAVA_HOME
    ```

    Correct output (example):

    ```
    /Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home
    ```

### Set JAVA_HOME manually (if missing)

=== "Windows"

    1. Press `Windows + R`, type `sysdm.cpl`, press Enter → **Advanced** tab → **Environment Variables**.
    2. Under **System variables**, click **New**:
        - **Variable name:** `JAVA_HOME`
        - **Variable value:** path to the JDK folder (e.g. `C:\Program Files\Eclipse Adoptium\jdk-21.0.3.9-hotspot`)
    3. Find the `Path` variable in **System variables**, click **Edit** → **New**, add: `%JAVA_HOME%\bin`
    4. Click **OK** on all windows. Open a new Command Prompt to apply the changes.

=== "macOS / Linux"

    Open your shell config file:

    - **Zsh (macOS default):** `~/.zshrc`
    - **Bash:** `~/.bashrc` or `~/.bash_profile`

    Add these two lines at the end of the file:

    ```bash
    export JAVA_HOME=$(/usr/libexec/java_home -v 21)  # macOS
    # or on Linux:
    # export JAVA_HOME=/usr/lib/jvm/temurin-21-amd64

    export PATH=$JAVA_HOME/bin:$PATH
    ```

    Apply immediately:

    ```bash
    source ~/.zshrc   # or source ~/.bashrc
    ```

---

## 4. Troubleshooting

### `java` is not recognized as an internal or external command (Windows)

**Cause:** The JDK has not been added to `PATH`.

**Fix:**
1. Check that `JAVA_HOME` is set (see the section above).
2. Make sure `%JAVA_HOME%\bin` is included in the `Path` variable.
3. **Important:** Open a completely new Command Prompt window — the old one won't pick up the new environment variables.

### `java -version` shows a different version (not 21)

**Cause:** Multiple JDK versions are installed and an older one takes priority.

=== "Windows"

    Check which `java` executable is being used:

    ```cmd
    where java
    ```

    The first path in the list is the active version. Make sure the path to JDK 21 appears first.

=== "macOS / Linux"

    ```bash
    which java
    ```

### `brew: command not found` (macOS)

Homebrew is not installed. Re-run the Homebrew installation command in Step 1.

### Permission denied error (Linux)

Prepend `sudo` to the command. Example: `sudo apt install -y temurin-21-jdk`.

---

## 5. Summary

| Command | Purpose |
|---------|---------|
| `java -version` | Verify the JVM is installed (runs programs) |
| `javac -version` | Verify the compiler is installed |
| `echo %JAVA_HOME%` | Check the environment variable (Windows) |
| `echo $JAVA_HOME` | Check the environment variable (macOS/Linux) |

Once both `java -version` and `javac -version` show `21.x.x`, you are ready for the next lesson: [Install IDE](02-install-ide.md).
