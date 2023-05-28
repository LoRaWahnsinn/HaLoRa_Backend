package at.halora.persistence;

public interface IUserRepository {
    UserEntity getUser(String username);
    void createUser(UserEntity user);
    void updateUser(UserEntity user);
}
