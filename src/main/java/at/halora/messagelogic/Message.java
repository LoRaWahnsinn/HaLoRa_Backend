package at.halora.messagelogic;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
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
