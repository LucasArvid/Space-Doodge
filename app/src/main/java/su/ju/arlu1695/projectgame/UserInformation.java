package su.ju.arlu1695.projectgame;

public class UserInformation {
    public String nickname;
    private String pushId;


    public UserInformation() {

    }

    public UserInformation(String nickname) {
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
