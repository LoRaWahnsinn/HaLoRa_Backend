package at.halora.messagelogic;

import at.halora.utils.DeviceType;

public interface IMessageLogic {
    boolean sendMessage(Message message);
    boolean registerUser(Long telegramId);
    boolean registerTTNDevice(String username, Long devEUI);
    void setReceiveMode(String username, DeviceType deviceType);
}
