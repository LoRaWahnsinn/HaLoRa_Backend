package at.halora.messagelogic;

import at.halora.persistence.Group;
import at.halora.persistence.User;
import at.halora.utils.MessagingServiceType;

import java.util.ArrayList;

public interface IMessageLogic {
    boolean sendMessage(Message message);
    boolean registerUser(User user);
    void setReceiveMode(String username, MessagingServiceType deviceType);
    boolean registerTTNDevice(User user);
    User getUserByAccountId(String accountId);
    User getUserByName(String name);

    void createGroup(Group group);

    void addGroupMember(String groupName, String userName);

    void updateUser(User user);

    Group getGroupByName(String name);

}
