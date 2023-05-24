package at.halora.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public enum MessagingServiceType {
    TELEGRAM(1, "Telegram"),
    DORA(2, "Dora");

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private int code;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private String name;

    MessagingServiceType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static MessagingServiceType parseValue(int code) {
        for (MessagingServiceType t : MessagingServiceType.values()) {
            if (t.getCode() == code) {
                return t;
            }
        }
        return null;
    }
}
