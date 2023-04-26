package at.halora.persistance;

import at.halora.utils.DeviceType;
import lombok.AllArgsConstructor;

import java.util.HashMap;

@AllArgsConstructor
public class UserEntity {
    Integer id; //id in db
    String username; //HaLoRa username
    HashMap<DeviceType, Long> userIds = new HashMap<>(); //user ids for messaging services (telegramId, devEUI, etc.)
    DeviceType receiveAt; //device to receive messages at

    public UserEntity() {}

    public void setUserId(DeviceType deviceType, Long userId) {
        userIds.put(deviceType, userId);
    }
}
