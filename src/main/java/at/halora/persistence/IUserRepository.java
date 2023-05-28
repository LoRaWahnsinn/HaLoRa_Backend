package at.halora.persistence;

import at.halora.utils.MessagingServiceType;

import java.util.List;

public interface IUserRepository {
    User getUserByName(String username);
    User getUserByAccountId(String accountId);
    void createUser(User user);
    void updateUser(User user);

    List<String> getMSIds(MessagingServiceType messagingServiceType);
}
