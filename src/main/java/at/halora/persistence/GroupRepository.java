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
    public void addGroupMember(Group group, User user) {

    }

    @Override
    public List<User> getGroupMembers(Group group) {
        return null;
    }

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
