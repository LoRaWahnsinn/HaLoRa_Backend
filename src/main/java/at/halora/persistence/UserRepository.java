package at.halora.persistence;

import at.halora.utils.MessagingServiceType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserRepository implements IUserRepository {

    private final Datasource datasource;

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
        datasource.insert_user(user.getUsername());
        user.setUser_id(getUserByName(user.getUsername()).getUser_id());
        user.getAccountIds().forEach((k, v) -> {
            datasource.insert_user_accounts(user.getUser_id(), k.getName(), v);
        });
    }

    @Override
    public void updateUser(User user) {
        var oldUser = getUserByName(user.getUsername());
        if (!user.getReceiveAt().equals(oldUser.getReceiveAt())) {
            datasource.updateReceiveAt(user.getUsername(), user.getReceiveAt().getName());
        }

        user.getAccountIds().forEach((k, v) -> {
            if (!oldUser.getAccountIds().containsKey(k)) {
                datasource.insert_user_accounts(user.getUser_id(), k.getName(), v);
            } else if (!oldUser.getAccountIds().get(k).equals(v)) {
                datasource.update_user_accounts(user.getUser_id(), k.getName(), v);
            }
        });

        user.getUserContacts().forEach(userContact -> {
            if (!oldUser.getUserContacts().contains(userContact)) {
                datasource.insert_user_user_contacts(user.getUser_id(), userContact.getUser_id());
            }
        });
        user.getGroupContacts().forEach(groupContact -> {
            if (!oldUser.getGroupContacts().contains(groupContact)) {
                datasource.insert_user_group_contacts(user.getUser_id(), groupContact.getGroup_id());
            }
        });
    }

    @Override
    public List<String> getMSIds(MessagingServiceType messagingServiceType) {
        var ids = new ArrayList<String>();
        try (ResultSet result = datasource.selectMSIds(messagingServiceType)){
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
        try (ResultSet result = datasource.select_user_user_contacts(user.getUser_id()) ) {
            user.setUserContacts(setUserContacts(result));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (ResultSet result = datasource.select_user_group_contacts(user.getUser_id()) ) {
            user.setGroupContacts(setGroupContacts(result));
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

    private Group createGroupFromResultSet(ResultSet result) throws SQLException {
        var group = new Group();
        group.setGroup_id(result.getInt("group_id"));
        group.setGroup_name(result.getString("name"));
        return group;
    }

    private HashMap<MessagingServiceType, String> setAccounts(ResultSet result) throws SQLException {
        HashMap<MessagingServiceType, String> accountIds = new HashMap<>();
        while (result.next()) {
            accountIds.put(MessagingServiceType.parseValue(result.getInt("ms_id")),
                    result.getString("account_id"));
        }
        return accountIds;
    }

    private ArrayList<User> setUserContacts(ResultSet result) throws SQLException {
        ArrayList<User> userContacts = new ArrayList<>();
        while (result.next()) {
            userContacts.add(createUserFromResultSet(
                    datasource.select_user_by_userId(result.getInt("user_id"))));
        }
        return userContacts;
    }

    private ArrayList<Group> setGroupContacts(ResultSet result) throws SQLException {
        ArrayList<Group> groupContacts = new ArrayList<>();
        while (result.next()) {
            groupContacts.add(createGroupFromResultSet(
                    datasource.select_group_by_id(result.getInt("group_id"))));
        }
        return groupContacts;
    }

}
