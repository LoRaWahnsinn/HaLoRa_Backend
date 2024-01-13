package at.halora.persistence;

import java.util.List;

public interface IGroupRepository {

    void createGroup(Group group);
    Group getGroupByName(String name);
    Group getGroupById(Integer group_id);

    void addGroupMember(Group group, User user);
    List<User> getGroupMembers(Group group);

}
