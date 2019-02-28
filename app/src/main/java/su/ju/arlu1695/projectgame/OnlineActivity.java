package su.ju.arlu1695.projectgame;

import android.app.Dialog;
import android.content.Intent;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

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

    //User -List,Arr var
    private ListView userListView;
    private ArrayList<String> mUserName = new ArrayList<>();

    //Friends -List,Arr
    private ListView friendsListView;
    private ArrayList<String> mFriendsName = new ArrayList<>();
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
                textView.setText("Online users:");
            }

                @Override
                public void onCancelled (@NonNull DatabaseError databaseError){

                }

        });

        // Send game invite to user clicked via notification.
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userClicked = userList.getUser(position).getNickname();
                Toast.makeText(OnlineActivity.this, userClicked, Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(this, GameModeActivity.class));

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
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.getValue().equals("True")) {
                        mFriendsName.add(ds.getKey());
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
        LinearLayout friendRankingsLayout = (LinearLayout) friendsDialog.findViewById(R.id.friend_ranking_layout);


        String friend = mFriendsName.get(position);
        tv_friend.setText(friend + "'s profile");

        // ToDO parse Database and add new textViews to layout if user has any top 5 ranking spots.


    }

    public void friendsExitButtonCLicked(View view) {
        friendsDialog.dismiss();
    }

    public void addFriendButtonClicked(View view) {
        findFried = (TextView) friendsDialog.findViewById(R.id.et_addFriend);

        final String friend = findFried.getText().toString().trim();

        // Verify that friend request has a recipient.
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot dsName : ds.getChildren()) {
                        if(dsName.getValue().equals(friend)) {
                            Constants.thisUser.friendsList.add(friend);
                            FirebaseDatabase.getInstance().getReference().child("User").child(getCurrentUserId()).child("friends").child(friend).setValue("True");
                            Toast.makeText(OnlineActivity.this, "Success", Toast.LENGTH_SHORT).show();
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


}
