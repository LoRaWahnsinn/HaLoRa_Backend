package at.halora.messagelogic;

import at.halora.persistance.IUserRepository;
import at.halora.services.IMessagingService;
import at.halora.utils.DeviceType;

public class MessageLogic implements IMessageLogic {

    private IMessagingService telegramBot;
    private IMessagingService ttnClient;
    private IUserRepository userRepository;

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
    public void setReceiveMode(DeviceType deviceType) {

    }

    public void setTelegramBot(IMessagingService telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void setTtnClient(IMessagingService ttnClient) {
        this.ttnClient = ttnClient;
    }

    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
