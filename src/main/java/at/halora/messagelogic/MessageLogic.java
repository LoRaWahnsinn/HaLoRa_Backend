package at.halora.messagelogic;

import at.halora.persistence.Group;
import at.halora.persistence.IGroupRepository;
import at.halora.persistence.IUserRepository;
import at.halora.persistence.User;
import at.halora.services.IMessagingService;
import at.halora.utils.MessagingServiceType;

import java.util.HashMap;

public class MessageLogic implements IMessageLogic {

    private IUserRepository userRepository;
    private IGroupRepository groupRepository;

    private final HashMap<MessagingServiceType, IMessagingService> messagingServices = new HashMap<>();

    @Override
    public boolean sendMessage(Message message) {
        User user = message.getRecipient();
        return messagingServices.get(user.getReceiveAt()).sendMessage(message);
    }

    @Override
    public boolean registerUser(User user) {
        userRepository.createUser(user);
        return true;
    }

    @Override
    public boolean registerTTNDevice(User user) {
        userRepository.updateUser(user);
        return true;
    }

    @Override
    public void setReceiveMode(String username, MessagingServiceType deviceType) {
        System.out.println("Set receive mode of " + username + "to " + deviceType.getName());
        var user = userRepository.getUserByName(username);
        user.setReceiveAt(deviceType);

        userRepository.updateUser(user);
    }

    @Override
    public User getUserByAccountId(String accountId) {
        return userRepository.getUserByAccountId(accountId);
    }

    @Override
    public User getUserByName(String name) {
        return userRepository.getUserByName(name);
    }

    @Override
    public void createGroup(Group group) {
        groupRepository.createGroup(group);
    }

    @Override
    public void addGroupMember(String groupName, String userName) {

    }

    @Override
    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    @Override
    public Group getGroupByName(String name) {
        return groupRepository.getGroupByName(name);
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
