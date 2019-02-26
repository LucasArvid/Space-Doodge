package su.ju.arlu1695.projectgame;

public class User {
    public String nickname;
    public static String pushId;


    public User() {

    }

    public User(String nickname) {
        this.nickname = nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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
