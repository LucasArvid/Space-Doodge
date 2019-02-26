package su.ju.arlu1695.projectgame;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.service.autofill.Dataset;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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

    //List,Arr var
    private ListView userListView;
    private ArrayList<String> mUserName = new ArrayList<>();

    // Variable for changing user status.
    private TextView textView;
    private UserList userList;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        getSupportActionBar().hide();

        textView = (TextView) findViewById(R.id.user_list_status);
        userList = new UserList();


        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("User");

        userListView = (ListView) findViewById(R.id.user_listView);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mUserName);
        userListView.setAdapter(arrayAdapter);

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

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userClicked = userList.getUser(position).getNickname();
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

}
