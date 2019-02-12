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

import java.util.ArrayList;

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

    private Button logoutButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        getSupportActionBar().hide();

        textView = (TextView) findViewById(R.id.user_list_status);
        logoutButton = (Button) findViewById(R.id.b_logout);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("User");

        userListView = (ListView) findViewById(R.id.user_listView);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mUserName);
        userListView.setAdapter(arrayAdapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot dsName : ds.getChildren()) {
                        String value = dsName.getValue().toString();
                        mUserName.add(value);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
                textView.setText("Online users:");
            }

                @Override
                public void onCancelled (@NonNull DatabaseError databaseError){

                }

        });

    }

    public void logoutButtonClicked(View view) {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(this, GameModeActivity.class));

    }

}
