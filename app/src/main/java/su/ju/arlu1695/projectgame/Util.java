package su.ju.arlu1695.projectgame;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import okhttp3.OkHttpClient;

public class Util {

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

}
