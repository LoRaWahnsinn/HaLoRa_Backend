package at.halora.persistence;

import at.halora.utils.MessagingServiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    Integer user_id; //id in db
    String username; //HaLoRa username
    HashMap<MessagingServiceType, String> accountIds = new HashMap<>(); //user ids for messaging services (telegramId, devEUI, etc.)
    MessagingServiceType receiveAt; //device to receive messages at


}
