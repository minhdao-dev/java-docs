package fundamentals.enums;

public class EnumDemo {

    enum Priority { LOW, MEDIUM, HIGH, CRITICAL }

    enum HttpStatus {
        OK(200, "OK"),
        NOT_FOUND(404, "Not Found"),
        INTERNAL_SERVER_ERROR(500, "Internal Server Error");

        private final int    code;
        private final String reason;

        HttpStatus(int code, String reason) {
            this.code   = code;
            this.reason = reason;
        }

        public int    getCode()   { return code; }
        public String getReason() { return reason; }

        public boolean isError() { return code >= 400; }

        public static HttpStatus fromCode(int code) {
            for (HttpStatus s : values()) {
                if (s.code == code) return s;
            }
            throw new IllegalArgumentException("Unknown HTTP status: " + code);
        }
    }

    public static void main(String[] args) {
        // Basic usage
        Priority p = Priority.HIGH;
        System.out.println(p.name());    // HIGH
        System.out.println(p.ordinal()); // 2

        // Enhanced switch
        String label = switch (p) {
            case LOW, MEDIUM -> "Normal";
            case HIGH        -> "Urgent";
            case CRITICAL    -> "Emergency";
        };
        System.out.println(label); // Urgent

        // Enum with fields
        HttpStatus status = HttpStatus.NOT_FOUND;
        System.out.println(status.getCode());   // 404
        System.out.println(status.getReason()); // Not Found
        System.out.println(status.isError());   // true

        // Lookup by value
        HttpStatus found = HttpStatus.fromCode(200);
        System.out.println(found); // OK

        // Iterate all constants
        for (HttpStatus s : HttpStatus.values()) {
            System.out.printf("[%d] %s%n", s.getCode(), s.getReason());
        }
    }
}
