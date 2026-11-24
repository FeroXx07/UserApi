package cat.itacademy.s04.t01.userapi.utility;

import java.util.UUID;
import java.util.regex.Pattern;

public class UUID_Utility {
    private static final Pattern UUID_REGEX =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    public static UUID fromString (String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("UUID string cannot be null or empty");
        }

        if (!UUID_REGEX.matcher(value).matches()) {
            throw new IllegalArgumentException("UUID string has invalid format");
        }

        return UUID.fromString(value);
    }
}
