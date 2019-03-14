package su.ju.arlu1695.projectgame;

import android.app.Dialog;
import android.content.Intent;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static su.ju.arlu1695.projectgame.Util.getCurrentUserId;


public class OnlineActivity extends AppCompatActivity {

    // Firebase var
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;

    //User -List,Arr var
    private ListView userListView;
    private ArrayList<String> mUserName = new ArrayList<>();
    private ArrayList<String> mFriendRank = new ArrayList<>();

    //Friends -List,Arr
    private ListView friendsListView;
    private ArrayList<String> mFriendsName = new ArrayList<>();
    private ArrayList<String> mFriendsId = new ArrayList<>();
    private TextView findFried;

    // Variable for changing user status.
    private TextView textView;
    private UserList userList;

    // Popup Dialog
    private Dialog friendsDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        getSupportActionBar().hide();

        Constants.startMediaPlayer(0);

        textView = (TextView) findViewById(R.id.user_list_status);
        userList = new UserList();


        // Popup dialog
        friendsDialog = new Dialog(this);


        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("User");



        userListView = (ListView) findViewById(R.id.user_listView);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mUserName);
        userListView.setAdapter(arrayAdapter);

        // Parse Database for users.
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserName.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot dsName : ds.getChildren()) {
                        if(dsName.getKey().equals("nickname")) {
                            String value = dsName.getValue().toString();
                            User user = new User();
                            user.setNickname(value);
                            userList.addUsers(user);
                            mUserName.add(value);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
                textView.setText(getResources().getString(R.string.online_users));
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
                String format = String.format("%s/sendNotification?to=%s&fromName=%s&fromId=%s&type=invite&title=hello&body=body",
                        Constants.FIREBASE_CLOUD_FUNCTIONS_BASE,
                        userClicked,
                        Constants.thisUser.getNickname(),
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
    }

    public void logoutButtonClicked(View view) {
        firebaseAuth.signOut();
        finish();
        // startActivity(new Intent(this, GameModeActivity.class));

    }

    // Open friends list popup activity
    public void friendsButtonClicked(View view) {

        friendsDialog.setContentView(R.layout.friends_popup);

        friendsListView = (ListView) friendsDialog.findViewById(R.id.lw_friends);
        final ArrayAdapter<String> friendsArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mFriendsName);
        friendsListView.setAdapter(friendsArrayAdapter);

        // Parse only current users friends list.
        FirebaseDatabase.getInstance().getReference().child("User").child(getCurrentUserId()).child("friends").addValueEventListener(new ValueEventListener() {
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

        FirebaseDatabase.getInstance().getReference().child("User").child(mFriendsId.get(position)).child("wins").addListenerForSingleValueEvent(new ValueEventListener() {
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


        FirebaseDatabase.getInstance().getReference().child("leaderboard").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int amountOfLevels = (int) dataSnapshot.getChildrenCount();
                for (int i = 1; i <= amountOfLevels; i++) {
                    final int level = i;
                    FirebaseDatabase.getInstance().getReference().child("leaderboard").child("level"+ i).orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                String user = ds.getKey();
                                mFriendRank.add(user);
                            }
                            // reverse list after filled
                            Collections.reverse(mFriendRank);
                            int rank = (mFriendRank.indexOf(friend) + 1);
                            if (rank > 0) {
                                TextView tv_friend_rank = new TextView(new ContextThemeWrapper(OnlineActivity.this, R.style.myFriendRank_style), null, 0);
                                tv_friend_rank.setText(String.format("Level %d Rank %d",
                                        level,
                                        rank));

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins(18, 0, 0, 0);
                                tv_friend_rank.setLayoutParams(params);
                                ll_ranking_layout.addView(tv_friend_rank);
                            }

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
                FirebaseDatabase.getInstance().getReference().child("User").child(getCurrentUserId()).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (ds.getKey().equals(friend))
                                FirebaseDatabase.getInstance().getReference().child("User").child(getCurrentUserId()).child("friends").child(friend).setValue(null);
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
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot dsName : ds.getChildren()) {
                        if(dsName.getValue().equals(friend)) {
                            Constants.thisUser.friendsList.add(friend);
                            String friendId = ds.getKey();
                            FirebaseDatabase.getInstance().getReference().child("User").child(getCurrentUserId()).child("friends").child(friend).setValue(friendId);
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
    public void onPause() {
        super.onPause();
        Constants.pauseMediaPlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        Constants.startMediaPlayer(0);
    }


}
