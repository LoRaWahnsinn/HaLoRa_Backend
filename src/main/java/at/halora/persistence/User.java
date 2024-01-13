package at.halora.persistence;

import at.halora.utils.MessagingServiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer user_id; //id in db
    private String username; //HaLoRa username
    private HashMap<MessagingServiceType, String> accountIds = new HashMap<>(); //user ids for messaging services (telegramId, devEUI, etc.)
    private MessagingServiceType receiveAt; //device to receive messages
    private ArrayList<User> userContacts;
    private ArrayList<Group> groupContacts;

    public void addAccount(MessagingServiceType messagingServiceType, String accountId){
        accountIds.put(messagingServiceType, accountId);
    }

    public void addUserContact(User user){
        this.userContacts.add(user);
    }

    public void addGroupContact(Group group) {
        this.groupContacts.add(group);
    }

}
