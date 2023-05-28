package at.halora.messagelogic;

import at.halora.persistence.User;
import at.halora.utils.MessagingServiceType;

public interface IMessageLogic {
    boolean sendMessage(Message message);
    boolean registerUser(Long telegramId);
    void setReceiveMode(String username, MessagingServiceType deviceType);
    boolean registerTTNDevice(String username, String devEUI);
    User getUserByAccountId(String accountId);
    User getUserByName(String name);



}
