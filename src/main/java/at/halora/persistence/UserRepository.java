package at.halora.persistence;

import at.halora.utils.MessagingServiceType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserRepository implements IUserRepository {

    private Datasource datasource;

    public UserRepository() {
        this.datasource = new Datasource();
    }

    @Override
    public User getUserByName(String username) {
        try {
            return getUser(datasource.select_user_byName(username));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUserByAccountId(String accountId) {
        try {
            return getUser(datasource.select_user_by_accountId(accountId));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void createUser(User user) {
        //todo: puh was bekommen wir da alles?
        try {
            datasource.insert_user(user.getUsername());
            user.setUser_id(getUserByName(user.getUsername()).getUser_id());
            user.getAccountIds().forEach((k, v) -> {
                try {
                    datasource.insert_user_accounts(user.getUser_id(), k.getName(), v);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateUser(User user) {
        var oldUser = getUserByName(user.getUsername());
        if (!user.getReceiveAt().equals(oldUser.getReceiveAt())) {
            try {
                datasource.updateReceiveAt(user.getUsername(), user.getReceiveAt().getName());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        user.getAccountIds().forEach((k, v) -> {
            if (!oldUser.getAccountIds().containsKey(k)) {
                try {
                    datasource.insert_user_accounts(user.getUser_id(), k.getName(), v);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if (!oldUser.getAccountIds().get(k).equals(v)) {
                try {
                    datasource.update_user_accounts(user.getUser_id(), k.getName(), v);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public List<String> getMSIds(MessagingServiceType messagingServiceType) {
        var ids = new ArrayList<String>();
        ResultSet result;
        try {
            result = datasource.selectMSIds(messagingServiceType);
            while (result.next()) {
                ids.add(result.getString("account_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ids;
    }

    private User getUser(ResultSet r) throws SQLException {
        User user;
        if (!r.next()) {
            return null;
        }
        try (ResultSet result = r) {
            user = createUserFromResultSet(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (ResultSet result = datasource.select_user_accounts(user.getUser_id())) {
            user.setAccountIds(setAccounts(result));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    private User createUserFromResultSet(ResultSet result) throws SQLException {
        var user = new User();
        user.setUser_id(result.getInt("user_id"));
        user.setUsername(result.getString("name"));
        user.setReceiveAt(MessagingServiceType.parseValue(result.getInt("receiveAt")));
        return user;
    }

    private HashMap<MessagingServiceType, String> setAccounts(ResultSet result) throws SQLException {
        HashMap<MessagingServiceType, String> accountIds = new HashMap<>();
        while (result.next()) {
            accountIds.put(MessagingServiceType.parseValue(result.getInt("ms_id")),
                    result.getString("account_id"));
        }
        return accountIds;
    }
}
