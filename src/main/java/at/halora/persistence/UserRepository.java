package at.halora.persistence;

import at.halora.utils.MessagingServiceType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class UserRepository implements IUserRepository {

    private Datasource datasource;

    public UserRepository() {
        this.datasource = new Datasource();
    }
    @Override
    public User getUser(String username) {
        User user = new User();
        try (ResultSet result = datasource.select_user_byName(username)) {
            user.setUser_id(result.getInt("user_id"));
            user.setUsername(result.getString("name"));
            user.setReceiveAt(MessagingServiceType.parseValue(result.getInt("receiveAt")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (ResultSet result = datasource.select_user_accounts(user.getUser_id())) {
            HashMap<MessagingServiceType, String> accountIds = new HashMap<>();
            while (result.next()) {
                accountIds.put(MessagingServiceType.parseValue(result.getInt("ms_id")),
                        result.getString("account_id"));
            }
            user.setAccountIds(accountIds);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public User getUserByAccountId(String accountId) {
        return null;
    }


    @Override
    public void createUser(User user) {
        //todo: puh was bekommen wir da alles?
        try {
            datasource.insert_user(user.getUsername());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateUser(User user) {
    }
}
