package at.halora.messagelogic;

import at.halora.persistence.IUserRepository;
import at.halora.persistence.User;
import at.halora.services.IMessagingService;
import at.halora.utils.MessagingServiceType;
import org.apache.commons.lang3.NotImplementedException;

import java.util.HashMap;

public class MessageLogic implements IMessageLogic {

    private IUserRepository userRepository;

    private HashMap<MessagingServiceType, IMessagingService> messagingServices = new HashMap<>();

    @Override
    public boolean sendMessage(Message message) {
        User user = message.getRecipient();
        return messagingServices.get(user.getReceiveAt()).sendMessage(message);
    }

    @Override
    public boolean registerUser(Long telegramId) {
        throw new NotImplementedException();
    }

    @Override
    public boolean registerTTNDevice(String username, String devEUI) {
        throw new NotImplementedException();
    }

    @Override
    public void setReceiveMode(String username, MessagingServiceType deviceType) {
        var user = userRepository.getUserByName(username);
       // userRepository.updateUser();
    }

    @Override
    public User getUserByAccountId(String accountId) {
        return userRepository.getUserByAccountId(accountId);
    }

    @Override
    public User getUserByName(String name) {
        return userRepository.getUserByName(name);
    }


    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addMessagingService(MessagingServiceType type, IMessagingService messagingService) {
        messagingServices.put(type, messagingService);
    }

    public void initMessagingServices() {
        for(var i : messagingServices.values()){
            Thread th = new Thread(i);
            th.start();
        }
    }
}
