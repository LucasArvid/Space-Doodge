package su.ju.arlu1695.projectgame;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import okhttp3.OkHttpClient;

// Class for easy access to functions used often in multiple classes.
public class Util {

    private static ArrayList<String> mLeaderBoardList = new ArrayList<>();
    private static int rank;

    private static String me;

    public static void savePushToken(String refreshedToken, String userId) {
        FirebaseDatabase.getInstance().getReference().child("User")
                .child(userId)
                .child("pushId")
                .setValue(refreshedToken);

    }

    public static void saveNickName(String nickname, String userId) {
        FirebaseDatabase.getInstance().getReference().child("User")
                .child(userId)
                .child("nickname")
                .setValue(nickname);
    }

    public static String getCurrentUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null || currentUser.isAnonymous()) {
            return "";
        } else {
            return currentUser.getUid();
        }
    }

    public static void setCurrentUser() {

        FirebaseDatabase.getInstance().getReference().child("User")
                .child(getCurrentUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getKey().equals("nickname")) {
                            String me = dataSnapshot.getValue().toString();
                            Constants.thisUser.setNickname(me);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public static int getLeaderboardPos(int level, final String name) {


        FirebaseDatabase.getInstance().getReference().child("leaderboard").child("level" + (level+ 1)).orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String user = ds.getKey();
                    mLeaderBoardList.add(user);
                }
                Collections.reverse(mLeaderBoardList);
                mLeaderBoardList.indexOf(name);
                rank = (mLeaderBoardList.indexOf(name) + 1);
                // reverse list after filled
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rank;

    }

}
