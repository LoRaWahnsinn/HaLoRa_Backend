package at.halora.persistence;

public interface IUserRepository {
    User getUser(String username);
    User getUserByAccountId(String accountId);
    void createUser(User user);
    void updateUser(User user);
}
