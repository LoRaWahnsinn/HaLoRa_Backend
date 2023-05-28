package at.halora.messagelogic;

import at.halora.utils.MessagingServiceType;

public interface IMessageLogic {
    boolean sendMessage(Message message);
    boolean registerUser(Long telegramId);
    void setReceiveMode(String username, MessagingServiceType deviceType);
    boolean registerTTNDevice(String username, String devEUI);
    boolean userExists(String username);
    boolean userExists(Long telegramId);
    String getUsername(Long telegramId);
}
