package at.halora.messagelogic;

import java.util.Date;

public record Message(
        Date timestamp,
        String sender,
        String recipient,
        String message
) {
    @Override
    public String toString() {
        return "Message{" +
                "timestamp=" + timestamp +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
