package at.halora.persistance;

public interface IUserRepository {
    UserEntity getUser(String username);
    void createUser(UserEntity user);
    void updateUser(UserEntity user);
}
