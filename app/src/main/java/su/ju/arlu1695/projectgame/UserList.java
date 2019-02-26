package su.ju.arlu1695.projectgame;

import java.util.ArrayList;
import java.util.List;

public class UserList {
    private ArrayList<User> users;

    public UserList() {
        users = new ArrayList<>();
    }

    public UserList(User user ){
        users = new ArrayList<>();
        users.add(user);
    }

    public User getUser(int index) {
        return users.get(index);
    }

    public void addUsers(User user) {
        users.add(user);
    }

    public void clearUserList() {
        users.clear();
    }
}
