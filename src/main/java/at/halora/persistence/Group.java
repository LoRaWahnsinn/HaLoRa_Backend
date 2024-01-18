package at.halora.persistence;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    private Integer group_id;
    private String group_name;
    private ArrayList<User> members;

    public void addMember(User user){
        this.members.add(user);
    }
}
