package su.ju.arlu1695.projectgame.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import su.ju.arlu1695.projectgame.R;
import su.ju.arlu1695.projectgame.activities.GameLobbyActivity;
import su.ju.arlu1695.projectgame.utils.Constants;
import su.ju.arlu1695.projectgame.utils.NotificationReceiver;

import static su.ju.arlu1695.projectgame.utils.Util.getCurrentUserId;
/*
    This class is reached when the user receives a notification.
    Notifications are only accepted while user are logged in.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String LOG_TAG = "MyFirebaseMessaging";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Grab data + text from recieved notification.
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        String fromUserId = remoteMessage.getData().get("fromId");
        String fromName = remoteMessage.getData().get("fromName");
        String type = remoteMessage.getData().get("type");


        // Verify type of notification
        // Only accept notifications if user isn't null
        if (Constants.currentUser != null) {
            if (type.equals("invite") && Constants.ALLOW_INVITES) {
                handleInvite(fromUserId, fromName, getApplicationContext());
            } else if (type.equals("accept")) {

                startActivity(new Intent(getBaseContext(), GameLobbyActivity.class)
                        .putExtra("type", "duel")
                        .putExtra("me", "challenger")
                        .putExtra("gameId", getCurrentUserId() + "-" + fromUserId)
                        .putExtra("fromName", fromName));

            } else if (type.equals("reject")) {
                displayNotification(getApplicationContext(), "Ouch!", getResources().getString(R.string.game_invite_has_been_rejected));
            }
        }

    }

    private void handleInvite(String fromUserId, String fromName, Context context) {
        //Reject handling
        Intent rejectIntent = new Intent(getApplicationContext(), NotificationReceiver.class)
                .setAction("reject")
                .putExtra("to", fromName)
                .putExtra("withId",fromUserId);

        PendingIntent pendingRejectIntent = PendingIntent.getBroadcast(this,
                0, rejectIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        //Accept handling
        Intent acceptIntent = new Intent(getApplicationContext(), NotificationReceiver.class)
                .setAction("accept")
                .putExtra("to",fromName)
                .putExtra("withId",fromUserId);

        PendingIntent pendingAcceptIntent = PendingIntent.getBroadcast(this,
                2, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Show Notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, Constants.CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentText(String.format("%s %s", fromName,getResources().getString(R.string.has_invited_you_to_a_game)))
                        .setContentTitle(getResources().getString(R.string.challenged))
                        .setChannelId(Constants.CHANNEL_ID)
                        .addAction(R.mipmap.ic_launcher, getResources().getString(R.string.accept), pendingAcceptIntent)
                        .addAction(R.mipmap.ic_launcher, getResources().getString(R.string.decline), pendingRejectIntent)
                        .setAutoCancel(true)
                        .setVibrate(new long[4000])
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(this);
        mNotificationMgr.notify(1,mBuilder.build());
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

    }

    // Default notification builder.
    public static void displayNotification(Context context, String title, String body) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, Constants.CHANNEL_ID)
                        .setSmallIcon(R.drawable.app_background)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(context);
        mNotificationMgr.notify(1,mBuilder.build());
    }

}
