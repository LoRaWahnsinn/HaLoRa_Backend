package at.halora.persistence;

import java.util.List;

public interface IGroupRepository {

    void createGroup(Group group);
    Group getGroupByName(String name);
    Group getGroupById(Integer group_id);

    void updateGroup(Group group);

}
