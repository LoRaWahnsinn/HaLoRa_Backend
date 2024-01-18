package at.halora.persistence;

import at.halora.utils.MessagingServiceType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupRepository implements IGroupRepository{

    private final Datasource datasource;

    public GroupRepository() {
        this.datasource = new Datasource();
    }

    @Override
    public void createGroup(Group group) {
        datasource.insert_group(group.getGroup_name());
        group.setGroup_id(getGroupByName(group.getGroup_name()).getGroup_id());
        group.getMembers().forEach(user ->  {
            datasource.insert_group_users(group.getGroup_id(), user.getUser_id());
        });
    }
    @Override
    public Group getGroupByName(String name) {

        return null;
    }

    @Override
    public Group getGroupById(Integer group_id) {
        return null;
    }

    @Override
    public void updateGroup(Group group) {
        var oldGroup = getGroupByName(group.getGroup_name());

        for (User member : group.getMembers()) {
            boolean n = false;
            for (User oldMember : oldGroup.getMembers()) {
                if (member.getUser_id().equals(oldMember.getUser_id())) {
                    n = true;
                    break;
                }
            }
            if (!n) {
                datasource.insert_group_users(group.getGroup_id(), member.getUser_id());
            }
        }

    }

    /*@Override
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

        for (User userContact : user.getUserContacts()) {
            boolean n = false;
            for (User oldUserContact : oldUser.getUserContacts()) {
                if (userContact.getUser_id().equals(oldUserContact.getUser_id())) {
                    n = true;
                    break;
                }
            }
            if (!n) {
                datasource.insert_user_user_contacts(user.getUser_id(), userContact.getUser_id());
            }
        }

        for (Group groupContact : user.getGroupContacts()) {
            boolean n = false;
            for (Group oldGroupContact : oldUser.getGroupContacts()) {
                if (groupContact.getGroup_id().equals(oldGroupContact.getGroup_id())) {
                    n = true;
                    break;
                }
            }
            if (!n) {
                datasource.insert_user_group_contacts(user.getUser_id(), groupContact.getGroup_id());
            }
        }

        user.getGroupContacts().forEach(groupContact -> {
            if (!oldUser.getGroupContacts().contains(groupContact)) {
                datasource.insert_user_group_contacts(user.getUser_id(), groupContact.getGroup_id());
            }
        });
    }*/


    private Group getGroup(ResultSet r) throws SQLException {
        Group group;
        if (!r.next()) {
            return null;
        }

        try (ResultSet result = r) {
            group = createGroupFromResultSet(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (ResultSet result = datasource.select_group_users(group.getGroup_id())) {
            group.setMembers(setGroupMembers(result));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return group;
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
    private ArrayList<User> setGroupMembers(ResultSet result) throws SQLException {
        ArrayList<User> groupMembers = new ArrayList<>();
        while (result.next()) {
            groupMembers.add(createUserFromResultSet(
                    datasource.select_user_by_userId(result.getInt("user_id"))));
        }
        return groupMembers;
    }
}
