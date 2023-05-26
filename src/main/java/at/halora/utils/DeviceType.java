package at.halora.utils;

public enum DeviceType {
    TELEGRAM("telegram"),
    DORA("dora");

    final String name;

    DeviceType(String name) {
        this.name = name;
    }
}
