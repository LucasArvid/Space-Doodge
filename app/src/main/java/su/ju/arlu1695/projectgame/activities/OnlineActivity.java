package su.ju.arlu1695.projectgame.activities;

import android.app.Dialog;

import android.graphics.PorterDuff;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;

import android.view.ContextThemeWrapper;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import com.google.firebase.auth.FirebaseAuth;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import su.ju.arlu1695.projectgame.utils.Constants;
import su.ju.arlu1695.projectgame.R;
import su.ju.arlu1695.projectgame.utils.Util;

import static su.ju.arlu1695.projectgame.utils.Util.getCurrentUserId;


public class OnlineActivity extends AppCompatActivity {

    // Firebase and user var
    private DatabaseReference userRef;
    private DatabaseReference leaderboardRef;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;

    // Getting user list progress bar
    private ProgressBar progressBar;
    // Popup Dialog
    private Dialog friendsDialog;

    //User -List,Arr var
    private ListView userListView;
    private ArrayList<String> mUserName = new ArrayList<>();
    private ArrayList<String> mFriendRank = new ArrayList<>();

    //Friends -List,Arr
    private ListView friendsListView;
    private ArrayList<String> mFriendsName = new ArrayList<>();
    private ArrayList<String> mFriendsId = new ArrayList<>();

    //Information and status TextView
    private TextView infoAndStatus;
    // Variable for changing user list status.
    private TextView userListStatus;

    private TextView findFried;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        getSupportActionBar().hide();

        Constants.startMediaPlayer(0);

        userListStatus = (TextView) findViewById(R.id.user_list_status);
        infoAndStatus = (TextView) findViewById(R.id.tv_online_info);

        // Popup dialog
        friendsDialog = new Dialog(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
        progressBar.bringToFront();

        firebaseAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("User");
        leaderboardRef = FirebaseDatabase.getInstance().getReference("leaderboard");

        userListView = (ListView) findViewById(R.id.user_listView);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_white_text, mUserName);
        userListView.setAdapter(arrayAdapter);

        // Parse Database for online users.
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserName.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("online").getValue(String.class).equals("true")) {
                        for (DataSnapshot dsName : ds.getChildren()) {
                            if (dsName.getKey().equals("nickname") && !(dsName.getValue().equals(Constants.currentUser))) {
                                String name = dsName.getValue().toString();
                                mUserName.add(name);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }

                userListStatus.setText(getResources().getString(R.string.online_users));
                progressBar.setVisibility(View.INVISIBLE);
            }

                @Override
                public void onCancelled (@NonNull DatabaseError databaseError){

                }

        });

        // Send game invite to user clicked via notification.
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userClicked = mUserName.get(position);
                OkHttpClient client = new OkHttpClient();
                infoAndStatus.setText(getResources().getString(R.string.waiting_for_user_response));

                String format = String.format("%s/sendNotification?to=%s&fromName=%s&fromId=%s&type=invite&title=hello&body=body",
                        Constants.FIREBASE_CLOUD_FUNCTIONS_BASE,
                        userClicked,
                        Constants.currentUser,
                        getCurrentUserId());

                Request request = new Request.Builder()
                        .url(format)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                    }
                });
            }

        });

        progressBar.setVisibility(View.VISIBLE);
    }

    public void logoutButtonClicked(View view) {
        userRef.child(Util.getCurrentUserId()).child("online").setValue("false");
        firebaseAuth.signOut();
        finish();
    }

    // Open friends list popup activity
    public void friendsButtonClicked(View view) {

        friendsDialog.setContentView(R.layout.friends_popup);

        friendsListView = (ListView) friendsDialog.findViewById(R.id.lw_friends);
        final ArrayAdapter<String> friendsArrayAdapter = new ArrayAdapter<String>(this, R.layout.list_white_text, mFriendsName);
        friendsListView.setAdapter(friendsArrayAdapter);

        // Parse only current users friends list.
        userRef.child(getCurrentUserId()).child("friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mFriendsName.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        mFriendsName.add(ds.getKey());
                        mFriendsId.add(ds.getValue(String.class));
                        friendsArrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Open friends profile page.
        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                friendsDialog.setContentView(R.layout.friend_profile_popup);
                friendProfilePopUp(view,position);

            }

        });

        friendsDialog.show();
    }
    // Profile popup activity handler.
    public void friendProfilePopUp(View view, int position) {

        // Connect TV,layouts and Buttons.
        TextView tv_friend = (TextView) friendsDialog.findViewById(R.id.tv_friendsName);

        final LinearLayout ll_ranking_layout = (LinearLayout) friendsDialog.findViewById(R.id.friend_ranking_layout);
        Button b_removeFriend = (Button) friendsDialog.findViewById(R.id.b_remove_friend);

        final String friend = mFriendsName.get(position);
        tv_friend.setText(friend + getResources().getString(R.string.s_profile));

        // Reuse progressbar, new context
        progressBar = new ProgressBar(friendsDialog.getContext());
        progressBar = (ProgressBar) friendsDialog.findViewById(R.id.progressBar_profile);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.VISIBLE);

        userRef.child(mFriendsId.get(position)).child("wins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    int wins = dataSnapshot.getValue(Integer.class);
                    TextView tv_friendsWins = (TextView) friendsDialog.findViewById(R.id.friend_total_wins);
                    tv_friendsWins.setText(getResources().getString(R.string.total_wins) + wins);
                    tv_friendsWins.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        leaderboardRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int amountOfLevels = (int) dataSnapshot.getChildrenCount();
                for (int i = 1; i <= amountOfLevels; i++) {
                    final int level = i;
                    leaderboardRef.child("level"+ i).orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                String user = ds.getKey();
                                mFriendRank.add(user);
                            }
                            // reverse list after filled
                            Collections.reverse(mFriendRank);
                            int rank = (mFriendRank.indexOf(friend) + 1);
                            // Only add a TextView with rank if the user actually exists on the leaderboard
                            if (rank > 0) {
                                TextView tv_friend_rank = new TextView(new ContextThemeWrapper(OnlineActivity.this, R.style.myFriendRank_style), null, 0);
                                tv_friend_rank.setText(String.format("Level %d Rank %d", level, rank));

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins(18, 0, 0, 0);
                                // Add TextViewss for each placement on the leaderboard
                                tv_friend_rank.setLayoutParams(params);
                                ll_ranking_layout.addView(tv_friend_rank);
                            }
                            progressBar.setVisibility(View.INVISIBLE);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        b_removeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRef.child(getCurrentUserId()).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (ds.getKey().equals(friend))
                                userRef.child(getCurrentUserId()).child("friends").child(friend).setValue(null);
                                friendsDialog.dismiss();
                                return;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void friendsExitButtonCLicked(View view) {
        friendsDialog.dismiss();
    }

    public void addFriendButtonClicked(View view) {
        findFried = (TextView) friendsDialog.findViewById(R.id.et_addFriend);

        final String friend = findFried.getText().toString().trim();

        // Verify that friend request has a recipient.
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot dsName : ds.getChildren()) {
                        if(dsName.getValue().equals(friend)) {
                            String friendId = ds.getKey();
                            userRef.child(getCurrentUserId()).child("friends").child(friend).setValue(friendId);
                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){

            }

        });
    }



    @Override
    public void onResume() {
        super.onResume();
        infoAndStatus.setText(getResources().getString(R.string.tap_a_user_to_challenge)); // Reached after a finished game
        Constants.startMediaPlayer(0);
    }


}
