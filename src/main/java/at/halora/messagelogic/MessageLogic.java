package at.halora.messagelogic;

import at.halora.persistance.IUserRepository;
import at.halora.services.IMessagingService;
import at.halora.utils.DeviceType;

import java.util.HashMap;
import java.util.Properties;

public class MessageLogic implements IMessageLogic {

    private IUserRepository userRepository;

    private HashMap<DeviceType, IMessagingService> messagingServices = new HashMap<>();

    @Override
    public boolean sendMessage(String recipient, String message) {
        return false;
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
    public void setReceiveMode(String username, DeviceType deviceType) {

    }

    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addMessagingService(DeviceType type, IMessagingService messagingService) {
        messagingServices.put(type, messagingService);
    }
}
