# Cài đặt JDK 21

## 1. JDK là gì

**JDK** (Java Development Kit) là bộ công cụ cần thiết để viết và chạy chương trình Java. Nó bao gồm:

- **`javac`** — trình biên dịch, chuyển code Java bạn viết thành bytecode
- **`java`** — máy ảo (JVM), chạy bytecode đó
- Các thư viện chuẩn và công cụ debug đi kèm

Không có JDK, máy tính không thể hiểu hay thực thi bất kỳ dòng Java nào.

### Tại sao chọn JDK 21?

JDK 21 là phiên bản **LTS (Long-Term Support)** mới nhất — được Oracle hỗ trợ bản vá bảo mật đến ít nhất năm 2031. Đây là lựa chọn chuẩn cho dự án mới và hầu hết môi trường doanh nghiệp hiện tại.

### Temurin là gì?

**Eclipse Temurin** (do Adoptium cung cấp) là bản phân phối JDK miễn phí, mã nguồn mở, được cộng đồng và nhiều công ty lớn (Microsoft, Red Hat, IBM) tin dùng. Tương đương Oracle JDK về hiệu năng và tính năng, nhưng không có điều khoản thương mại ràng buộc.

---

## 2. Cài đặt

=== "Windows"

    ### Bước 1 — Tải installer

    1. Mở trình duyệt, truy cập trang tải Temurin:  
       [https://adoptium.net/temurin/releases/](https://adoptium.net/temurin/releases/)
    2. Ở bộ lọc phía trên, chọn:
        - **Version:** `21 - LTS`
        - **OS:** `Windows`
        - **Architecture:** `x64` (máy tính thông thường 64-bit; nếu dùng máy ARM như Surface Pro X thì chọn `aarch64`)
        - **Package Type:** `JDK`
        - **File type:** `.msi`
    3. Nhấn **Download** để tải file `.msi` về máy (khoảng 170 MB).

    ### Bước 2 — Chạy installer

    1. Tìm file `.msi` vừa tải (thường nằm ở thư mục `Downloads`), nhấn đúp để mở.
    2. Màn hình **User Account Control** hỏi "Do you want to allow this app to make changes?" — nhấn **Yes**.
    3. Màn hình chào mừng của installer hiện ra — nhấn **Next**.
    4. Màn hình **Custom Setup** hiện ra — giữ nguyên mọi tùy chọn mặc định, **không cần thay đổi gì**.

        !!! tip "Mẹo quan trọng"
            Đảm bảo tùy chọn **"Add to PATH"** và **"Set JAVA_HOME variable"** đang được tick (màu xanh). Installer Temurin tự xử lý việc này — bạn chỉ cần không bỏ tick chúng.

    5. Nhấn **Next** → **Install** → chờ khoảng 30–60 giây → nhấn **Finish**.

    ### Bước 3 — Kiểm tra cài đặt

    1. Nhấn phím **Windows**, gõ `cmd`, nhấn Enter để mở Command Prompt.
    2. Gõ lệnh sau rồi nhấn Enter:

        ```cmd
        java -version
        ```

    3. Kết quả hiển thị đúng sẽ trông như thế này:

        ```
        openjdk version "21.0.x" 2024-xx-xx LTS
        OpenJDK Runtime Environment Temurin-21.0.x+xx (build 21.0.x+xx)
        OpenJDK 64-Bit Server VM Temurin-21.0.x+xx (build 21.0.x+xx, mixed mode, sharing)
        ```

    4. Tiếp theo kiểm tra trình biên dịch:

        ```cmd
        javac -version
        ```

        Kết quả: `javac 21.0.x`

    !!! success "Cài đặt thành công nếu"
        Cả `java -version` và `javac -version` đều hiển thị `21.x.x`. Bạn có thể bỏ qua phần xử lý lỗi bên dưới.

=== "macOS"

    ### Bước 1 — Cài Homebrew (nếu chưa có)

    **Homebrew** là trình quản lý gói phổ biến nhất trên macOS — tương tự App Store nhưng dành cho công cụ lập trình.

    1. Mở **Terminal**: nhấn `Cmd + Space`, gõ `Terminal`, nhấn Enter.
    2. Chạy lệnh sau (sao chép và dán nguyên):

        ```bash
        /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
    	```

    3. Nhập mật khẩu macOS khi được hỏi (con trỏ sẽ không di chuyển — đây là bình thường). Nhấn Enter.
    4. Quá trình cài đặt mất 2–5 phút. Khi xong, terminal hiển thị `Installation successful!`.

    !!! note "Đã có Homebrew rồi?"
        Chạy `brew --version` để kiểm tra. Nếu thấy số phiên bản hiện ra thì bỏ qua bước này.

    ### Bước 2 — Cài JDK 21 qua Homebrew

    ```bash
    brew install --cask temurin@21
    ```

    Lệnh này tải và cài Temurin JDK 21. Quá trình mất 2–5 phút tùy tốc độ mạng.

    ### Bước 3 — Kiểm tra cài đặt

    ```bash
    java -version
    javac -version
    ```

    Kết quả đúng:

    ```
    openjdk version "21.0.x" 2024-xx-xx LTS
    OpenJDK Runtime Environment Temurin-21.0.x+xx (build 21.0.x+xx)
    OpenJDK 64-Bit Server VM Temurin-21.0.x+xx (build 21.0.x+xx, mixed mode)
    ```

    ### Cài đặt thủ công (không dùng Homebrew)

    Nếu không muốn dùng Homebrew:

    1. Vào [https://adoptium.net/temurin/releases/](https://adoptium.net/temurin/releases/)
    2. Chọn **Version: 21 - LTS**, **OS: macOS**, **Architecture: aarch64** (nếu dùng chip Apple Silicon M1/M2/M3) hoặc **x64** (Intel), **Package Type: JDK**, **File type: `.pkg`**
    3. Tải và chạy file `.pkg` → làm theo hướng dẫn trên màn hình.

=== "Linux"

    ### Ubuntu / Debian (APT)

    ```bash
    sudo apt update
    sudo apt install -y wget apt-transport-https gpg

    # Thêm kho Adoptium
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

    !!! note "Fedora dùng OpenJDK từ kho mặc định"
        Phiên bản này tương đương Temurin về hiệu năng.

    ### Arch Linux (Pacman)

    ```bash
    sudo pacman -S jdk21-openjdk
    ```

    ### Kiểm tra

    ```bash
    java -version
    javac -version
    ```

    ### SDKMAN — Cài đặt và quản lý nhiều phiên bản JDK

    **SDKMAN** cho phép cài và chuyển đổi giữa nhiều phiên bản JDK chỉ bằng một lệnh — rất hữu ích khi cần làm việc với nhiều dự án dùng JDK khác nhau.

    ```bash
    # Cài SDKMAN
    curl -s "https://get.sdkman.io" | bash
    source "$HOME/.sdkman/bin/sdkman-init.sh"

    # Cài Temurin 21
    sdk install java 21.0.3-tem

    # Kiểm tra
    java -version
    ```

---

## 3. JAVA_HOME là gì và tại sao cần thiết

`JAVA_HOME` là một biến môi trường (environment variable) — một cái nhãn hệ thống trỏ đến thư mục cài đặt JDK. Nhiều công cụ (Maven, Gradle, IDE) đọc biến này để biết JDK nằm ở đâu.

### Kiểm tra JAVA_HOME đã được đặt chưa

=== "Windows"

    Mở Command Prompt:

    ```cmd
    echo %JAVA_HOME%
    ```

    Kết quả đúng (ví dụ):

    ```
    C:\Program Files\Eclipse Adoptium\jdk-21.0.3.9-hotspot
    ```

    Nếu kết quả trống (chỉ có `%JAVA_HOME%` hoặc dòng trắng) — xem phần xử lý lỗi bên dưới.

=== "macOS / Linux"

    ```bash
    echo $JAVA_HOME
    ```

    Kết quả đúng (ví dụ):

    ```
    /Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home
    ```

### Đặt JAVA_HOME thủ công (nếu thiếu)

=== "Windows"

    1. Nhấn `Windows + R`, gõ `sysdm.cpl`, nhấn Enter → tab **Advanced** → **Environment Variables**.
    2. Ở **System variables**, nhấn **New**:
        - **Variable name:** `JAVA_HOME`
        - **Variable value:** đường dẫn đến thư mục JDK (ví dụ: `C:\Program Files\Eclipse Adoptium\jdk-21.0.3.9-hotspot`)
    3. Tìm biến `Path` trong **System variables**, nhấn **Edit** → **New**, thêm: `%JAVA_HOME%\bin`
    4. Nhấn **OK** tất cả các cửa sổ. Mở Command Prompt mới để áp dụng.

=== "macOS / Linux"

    Mở file cấu hình shell:

    - **Zsh (macOS mặc định):** `~/.zshrc`
    - **Bash:** `~/.bashrc` hoặc `~/.bash_profile`

    Thêm hai dòng sau vào cuối file:

    ```bash
    export JAVA_HOME=$(/usr/libexec/java_home -v 21)  # macOS
    # hoặc trên Linux:
    # export JAVA_HOME=/usr/lib/jvm/temurin-21-amd64

    export PATH=$JAVA_HOME/bin:$PATH
    ```

    Áp dụng ngay:

    ```bash
    source ~/.zshrc   # hoặc source ~/.bashrc
    ```

---

## 4. Xử lý lỗi thường gặp

### `java` is not recognized as an internal or external command (Windows)

**Nguyên nhân:** JDK chưa được thêm vào `PATH`.

**Cách sửa:**
1. Kiểm tra `JAVA_HOME` đã được đặt chưa (xem mục trên).
2. Đảm bảo `%JAVA_HOME%\bin` có trong biến `Path`.
3. **Quan trọng:** Mở một cửa sổ Command Prompt hoàn toàn mới — CMD cũ không nhận biến môi trường mới.

### `java -version` hiển thị phiên bản khác (không phải 21)

**Nguyên nhân:** Máy có nhiều phiên bản JDK, phiên bản cũ đang được ưu tiên.

=== "Windows"

    Kiểm tra vị trí `java` đang dùng:

    ```cmd
    where java
    ```

    Đường dẫn đầu tiên trong danh sách là phiên bản đang được dùng. Đảm bảo đường dẫn đến JDK 21 xuất hiện trước.

=== "macOS / Linux"

    ```bash
    which java
    ```

### `brew: command not found` (macOS)

Homebrew chưa được cài. Chạy lại lệnh cài Homebrew ở Bước 1.

### Lỗi permission denied (Linux)

Thêm `sudo` vào đầu lệnh. Ví dụ: `sudo apt install -y temurin-21-jdk`.

---

## 5. Tổng kết

| Lệnh | Ý nghĩa |
|------|---------|
| `java -version` | Kiểm tra JVM đã cài (chạy chương trình) |
| `javac -version` | Kiểm tra trình biên dịch đã cài |
| `echo %JAVA_HOME%` | Kiểm tra biến môi trường (Windows) |
| `echo $JAVA_HOME` | Kiểm tra biến môi trường (macOS/Linux) |

Sau khi cả `java -version` và `javac -version` đều hiển thị `21.x.x`, bạn đã sẵn sàng cho bài tiếp theo: [Cài đặt IDE](02-install-ide.md).
