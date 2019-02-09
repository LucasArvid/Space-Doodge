package su.ju.arlu1695.projectgame;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignin;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();


        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
           /* finish();
            startActivity(new Intent(this,OnlineActivity.class));
             */
        }

        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.emailCreateAccountButton);

        editTextEmail = (EditText) findViewById(R.id.fieldEmail);
        editTextPassword = (EditText) findViewById(R.id.fieldPassword);

        textViewSignin = (TextView) findViewById(R.id.emailSignInButton);



    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            // email empty
            Toast.makeText(this,"Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)) {
            // password empty
            Toast.makeText(this,"Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        // register user

        progressDialog.setMessage("Registering User, please wait...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()) {
                            //user registration and login successful
                            // start profile activity
                            // display toast for now
                            Toast.makeText(LoginActivity.this, "Registered Successfully",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, "Error when registering, please try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            // email empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            // password empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;

        }

        progressDialog.setMessage("Logging in, please wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            finish();
                            startActivity(new Intent(LoginActivity.this,OnlineActivity.class));
                            Toast.makeText(LoginActivity.this, "Login Successfully",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, "Login Unsuccessful, please try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signinOrRegisterClicked(View view) {
        if(view == buttonRegister) {
            registerUser();
        }

        else if (view == textViewSignin) {
            userLogin();
        }
    }

    public void loginButtonClicked(View view) {
        userLogin();
    }

    public void forgottenPasswordClicked(View view) {
        //To be done
        Toast.makeText(this, "Too bad, write it down next time", Toast.LENGTH_SHORT).show();

    }


}
