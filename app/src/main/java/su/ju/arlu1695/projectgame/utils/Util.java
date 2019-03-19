package su.ju.arlu1695.projectgame.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import okhttp3.OkHttpClient;
import su.ju.arlu1695.projectgame.MainActivity;
import su.ju.arlu1695.projectgame.R;

import static su.ju.arlu1695.projectgame.utils.Constants.CHANNEL_DESC;
import static su.ju.arlu1695.projectgame.utils.Constants.CHANNEL_ID;
import static su.ju.arlu1695.projectgame.utils.Constants.CHANNEL_NAME;

// Class for easy access to functions used often in multiple classes.
public class Util {

    public static String getCurrentUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null || currentUser.isAnonymous()) {
            return "";
        } else {
            return currentUser.getUid();
        }
    }

    public static void setCurrentUserName() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null || currentUser.isAnonymous()) {
            return;
        } else {
            FirebaseDatabase.getInstance().getReference().child("User").child(getCurrentUserId()).child("nickname").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   if (dataSnapshot.exists()) {
                       Constants.currentUser = dataSnapshot.getValue(String.class);
                   }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    /* Create the NotificationChannel for API 26+ because
   the NotificationChannel class is new and not in the support library */
    public static void createNotificationChannel(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = CHANNEL_DESC;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            channel.setShowBadge(true);

            // Register the channel with the system
            NotificationManager notificationManager =
                    context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

    public static ArrayList<String> getLevelList (Context context) {
        ArrayList<String> levels = new ArrayList<>();
        levels.add((context.getResources().getString(R.string.level) + "1"));
        levels.add(( context.getResources().getString(R.string.level) + "2"));
        levels.add(( context.getResources().getString(R.string.level) + "3"));
        levels.add(( context.getResources().getString(R.string.level) + "4"));
        levels.add(( context.getResources().getString(R.string.level) + "5"));

        return levels;
    }




}
