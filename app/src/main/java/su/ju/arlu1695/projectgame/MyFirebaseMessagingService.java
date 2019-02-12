package su.ju.arlu1695.projectgame;

import android.nfc.Tag;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String LOG_TAG = "MyFirebaseMessaging";
    public static final String INVITE = "Invite";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(LOG_TAG,"Refreshed token: " + token);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        savePushToken(token, currentUser.getUid());

    }

    public static void savePushToken(String refreshedToken, String userId) {
        FirebaseDatabase.getInstance().getReference().child("User")
                .child(userId)
                .child("pushId")
                .setValue(refreshedToken);
    }
}
