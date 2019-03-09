package su.ju.arlu1695.projectgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class GameModeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextView tv_auth_status;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode);

        firebaseAuth = FirebaseAuth.getInstance();
        tv_auth_status = (TextView) findViewById(R.id.tv_auth_status);

        getAuthStatus();

        getSupportActionBar().hide();


    }

    public void offlineButtonClicked(View view) {
        Intent intent = new Intent(this, LevelSelectActivity.class)
                .putExtra("me","offline");
        startActivity(intent);
    }

    public void soloButtonClicked(View view) {
        Intent intent = new Intent (this, LoginActivity.class)
                .putExtra("mode","solo");
        startActivity(intent);
    }

   public void duelButtonClicked(View view) {
        Intent intent = new Intent(this, LoginActivity.class)
                .putExtra("mode","duel");
        startActivity(intent);
    }

    public void gameModelogoutButtonClicked(View view) {
        firebaseAuth.signOut();
        getAuthStatus();
    }

    private void getAuthStatus() {

        if (firebaseAuth.getCurrentUser() != null) {
            tv_auth_status.setText("Currently logged in");

        }else
            tv_auth_status.setText("Currently not logged in");
    }

}
