package su.ju.arlu1695.projectgame.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import su.ju.arlu1695.projectgame.activities.GameLobbyActivity;
import su.ju.arlu1695.projectgame.utils.Constants;

import static su.ju.arlu1695.projectgame.utils.Util.getCurrentUserId;

// Directed to from MyFirebaseMessagingService
public class NotificationHandler extends BroadcastReceiver {

    private static final String LOG_TAG = "NotificationHandler";

    private DatabaseReference gamesRef;

    @Override
    public void onReceive(Context context, Intent intent) {


       String me = Constants.currentUser;
       String to = intent.getExtras().getString("to");
       String fromUserId = intent.getExtras().getString("withId");

       String gameId = fromUserId + "-" + getCurrentUserId();
       gamesRef = FirebaseDatabase.getInstance().getReference().child("Games").child(gameId);

       // Dismiss notification
       NotificationManagerCompat.from(context).cancel(1);

       // Open a Http request for sending notification reply.
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

       //handle accept
       if (intent.getAction().equals("accept")){

           // Setup game on Firebase database.
           gamesRef.child("challenger").child("Score").setValue("0");
           gamesRef.child("challenged").child("Score").setValue("0");
           gamesRef.child("challenged").child("Dead").setValue("false");
           gamesRef.child("challenger").child("Dead").setValue("false");
           gamesRef.child("playerOneReady").setValue("false");
           gamesRef.child("playerTwoReady").setValue("false");
           gamesRef.child("startGame").setValue("false");
           gamesRef.child("level").setValue("0");

           context.startActivity(new Intent(context, GameLobbyActivity.class)
                   .putExtra("type", "duel")
                   .putExtra("me", "challenged")
                   .putExtra("gameId", fromUserId + "-" + getCurrentUserId())
                   .putExtra("fromName", to));
       }


    }

}

