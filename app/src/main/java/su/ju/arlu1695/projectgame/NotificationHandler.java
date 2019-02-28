package su.ju.arlu1695.projectgame;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static su.ju.arlu1695.projectgame.Util.getCurrentUserId;

// Directed to from MyFirebaseMessagingService
public class NotificationHandler extends BroadcastReceiver {

    private static final String LOG_TAG = "NotificationHandler";

    @Override
    public void onReceive(Context context, Intent intent) {

       String me = Constants.thisUser.getNickname();
       String to = intent.getExtras().getString("to");
       String fromUserId = intent.getExtras().getString("withId");

       // Open a Http request for sending notification.
       OkHttpClient client = new OkHttpClient();

       String format = String.format("%s/sendNotification?to=%s&fromName=%s&fromId=%s&type=%s&title=hello&body=hello",
               Constants.FIREBASE_CLOUD_FUNCTIONS_BASE,
               to,
               me,
               getCurrentUserId(),
               intent.getAction());

       Request request = new Request.Builder().url(format).build();
       client.newCall(request).enqueue(new Callback() {
           @Override
           public void onFailure(Call call, IOException e) {

           }

           @Override
           public void onResponse(Call call, Response response) throws IOException {

           }
       });


       if (intent.getAction().equals("accept")){
           //handle accept
           String gameId = fromUserId + "-" + getCurrentUserId();
           // Setup game on Firebase database.
       }

    }

}

