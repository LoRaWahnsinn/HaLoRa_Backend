package at.halora.messagelogic;

import at.halora.persistence.IUserRepository;
import at.halora.persistence.UserEntity;
import at.halora.services.IMessagingService;
import at.halora.utils.MessagingServiceType;

import java.util.HashMap;

public class MessageLogic implements IMessageLogic {

    private IUserRepository userRepository;

    private HashMap<MessagingServiceType, IMessagingService> messagingServices = new HashMap<>();

    @Override
    public boolean sendMessage(String recipient, String message) {
        UserEntity user = userRepository.getUser(recipient);
        return messagingServices.get(user.getReceiveAt()).sendMessage(
                user.getAccountIds().get(user.getReceiveAt()), message);
    }

    @Override
    public boolean registerUser(Long telegramId) {
        return false;
    }

    @Override
    public boolean registerTTNDevice(String username, Long devEUI) {
        return false;
    }

    @Override
    public void setReceiveMode(String username, MessagingServiceType deviceType) {
        var user = userRepository.getUser(username);
       // userRepository.updateUser();
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
