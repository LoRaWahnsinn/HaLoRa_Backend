package at.halora.messagelogic;

import at.halora.persistance.IUserRepository;
import at.halora.services.IMessagingService;
import at.halora.utils.DeviceType;
import org.apache.commons.lang3.NotImplementedException;

import java.util.HashMap;
import java.util.Properties;

public class MessageLogic implements IMessageLogic {

    private IUserRepository userRepository;

    private HashMap<DeviceType, IMessagingService> messagingServices = new HashMap<>();

    @Override
    public boolean sendMessage(Message message) {
        throw new NotImplementedException();
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
    public void setReceiveMode(String username, DeviceType deviceType) {

    }

    @Override
    public boolean userExists(String username) {
        throw new NotImplementedException();
    }

    @Override
    public boolean userExists(Long telegramId) {
        throw new NotImplementedException();
    }

    @Override
    public String getUsername(Long telegramId) {
        throw new NotImplementedException();
    }

    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addMessagingService(DeviceType type, IMessagingService messagingService) {
        messagingServices.put(type, messagingService);
    }

    public void initMessagingServices() {
        for(var i : messagingServices.values()){
            Thread th = new Thread(i);
            th.start();
        }
    }
}
