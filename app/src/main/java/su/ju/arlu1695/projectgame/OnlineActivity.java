package su.ju.arlu1695.projectgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OnlineActivity extends AppCompatActivity {

    private EditText editTextName;
    private TextView editTextView;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    UserInformation userInformation;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null) {
            finish();

            startActivity(new Intent(this,LoginActivity.class));
        }

        editTextView = (TextView) findViewById(R.id.tv_welcomeuser);
        editTextName = (EditText) findViewById(R.id.et_nickname);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        editTextView.setText("Welcome " + user.getEmail());
    }

    public void saveUserInformation(View view) {

        String nickname = editTextName.getText().toString().trim();
        userInformation = new UserInformation(nickname);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child(user.getUid()).setValue(userInformation);

        Toast.makeText(this, "Information Saved...", Toast.LENGTH_SHORT).show();
    }
}
