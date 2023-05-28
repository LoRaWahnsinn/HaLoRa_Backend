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
        User user = userRepository.getUser(message.getRecipient());
        return messagingServices.get(user.getReceiveAt()).sendMessage(
                user.getAccountIds().get(user.getReceiveAt()), message.getMessage());
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
        var user = userRepository.getUser(username);
       // userRepository.updateUser();
    }

    @Override
    public User getUser(String accountId) {
        return userRepository.getUserByAccountId(accountId);
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
