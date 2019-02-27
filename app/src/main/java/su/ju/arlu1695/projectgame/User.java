package su.ju.arlu1695.projectgame;

import java.util.ArrayList;

public class User {
    public String nickname;
    public static String pushId;
    public ArrayList<String> friendsList = new ArrayList<>();


    public User() {

    }

    public User(String nickname) {
        this.nickname = nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void addFriend(String friend) {
        friendsList.add(friend);
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPushId() {
        return pushId;
    }
}
