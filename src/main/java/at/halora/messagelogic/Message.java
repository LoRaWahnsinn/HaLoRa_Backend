package at.halora.messagelogic;

import at.halora.persistence.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    String timestamp;
    User sender;
    User recipient;
    String message;

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
