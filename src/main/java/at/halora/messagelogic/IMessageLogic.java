package at.halora.messagelogic;

import at.halora.utils.MessagingServiceType;

public interface IMessageLogic {
    boolean sendMessage(String recipient, String message);
    boolean registerUser(Long telegramId);
    boolean registerTTNDevice(String username, Long devEUI);
    void setReceiveMode(String username, MessagingServiceType deviceType);
}
