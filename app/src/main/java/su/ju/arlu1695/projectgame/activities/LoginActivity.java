package su.ju.arlu1695.projectgame.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import su.ju.arlu1695.projectgame.databinding.ActivityLoginBinding;
import su.ju.arlu1695.projectgame.utils.Constants;
import su.ju.arlu1695.projectgame.R;
import su.ju.arlu1695.projectgame.utils.Util;

public class LoginActivity extends AppCompatActivity {

    // Google login request code
    private int RC_SIGN_IN = 101;

    // LayoutBinding for easy access to views/buttons etc.
    private ActivityLoginBinding mBinding;
    private int BUTTON_START_X;
    private int BUTTON_END_X;

    // Firebase user tracking
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    // Firebase database reference
    private DatabaseReference databaseReference;
    private DatabaseReference userRef;

    // layout connections
    private EditText editTextEmail;
    private EditText editTextPassword;

    // Google sign in client
    private GoogleSignInClient mGoogleSignInClient;


    // Intent extra for identifying if duel or solo play
    private String mode;

    // Progress dialogs for successful/failed login
    private Dialog nickNameDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        // LayoutBinding
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        // Layout button width, start and animation end
        BUTTON_START_X =  (int)(getResources().getDimension(R.dimen.get_startWidth));
        BUTTON_END_X =  (int)(getResources().getDimension(R.dimen.get_width));

        // Nickname popup layout
        nickNameDialog = new Dialog(this);

        // Unpack intent, get extras
        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");

        // Firebase connections
        firebaseAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("User");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        // If user already logged in, bypass this LoginActivity
        if (firebaseAuth.getCurrentUser() != null){
            setNotificationTopic();
            finish();
            startNextActivity(mode);
        }

        // Google Sign in functionality


        configureGoogleSignIn();


        editTextEmail = (EditText) findViewById(R.id.fieldEmail);
        editTextPassword = (EditText) findViewById(R.id.fieldPassword);


    }


    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            // email empty
            Toast.makeText(this,getResources().getString(R.string.please_enter_email), Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)) {
            // password empty
            Toast.makeText(this,getResources().getString(R.string.please_enter_password), Toast.LENGTH_SHORT).show();
            return;
        }
        // register user
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            animateButtonWidth(mBinding.emailCreateAccountButton, BUTTON_START_X, BUTTON_END_X);
                            fadeOutTextAndSetProgressDialog(mBinding.registerText, mBinding.progressBarRegister);
                            setNickName();
                        }else{
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.error_when_registering),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // email not entered
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, getResources().getString(R.string.please_enter_email), Toast.LENGTH_SHORT).show();
            return;
        }

        // password not entered
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, getResources().getString(R.string.please_enter_password), Toast.LENGTH_SHORT).show();
            return;
        }


        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            setNotificationTopic();
                            animateButtonWidth(mBinding.emailSignInButton, BUTTON_START_X, BUTTON_END_X);
                            fadeOutTextAndSetProgressDialog(mBinding.signInText, mBinding.progressBarSignIn);
                            nextAction(mode,mBinding.progressBarSignIn, mBinding.emailSignInButton);
                        }else{

                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_unsuccessfull),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    // Nickname popup dialog, lets the user set a nickname that
    // saves to the database
    public void setNickName() {
        Button saveUsername;
        nickNameDialog.setContentView(R.layout.nickname_popup);
        saveUsername = (Button) nickNameDialog.findViewById(R.id.b_save_username);

        saveUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText setNickName = (EditText) nickNameDialog.findViewById(R.id.et_nickname);
                    final String nickname = setNickName.getText().toString().trim();
                    final FirebaseUser user = firebaseAuth.getCurrentUser();
                    userRef.child(user.getUid()).child("nickname").setValue(nickname);
                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( LoginActivity.this, new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            setNotificationTopic();
                        }
                    });
                    setupFirebaseUser();
                    nickNameDialog.dismiss();
                    nextAction(mode, mBinding.progressBarRegister, mBinding.emailCreateAccountButton);

                }
            });

        nickNameDialog.show();

    }


    // Subscribe to user personal notification topic
    public void setNotificationTopic () {
        FirebaseUser topicUser = FirebaseAuth.getInstance().getCurrentUser();

        userRef.child(topicUser.getUid()).child("nickname").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey().equals("nickname")) {
                    String topic = dataSnapshot.getValue().toString();
                    Constants.currentUser = topic;
                    FirebaseMessaging.getInstance().subscribeToTopic(topic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void nicknameExitButtonClicked(View view) {
        nickNameDialog.dismiss();
    }


    private void setupFirebaseUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference hsRef = userRef.child(user.getUid()).child("highscore");
        hsRef.child("level1").setValue(0);
        hsRef.child("level2").setValue(0);
        hsRef.child("level3").setValue(0);
        hsRef.child("level4").setValue(0);
        hsRef.child("level5").setValue(0);

    }




    // Google Sign in functions ----
    public void googleSignInClicked(View view) {

        mBinding.tvGoogleSignIn.setBackground(getResources().getDrawable(R.drawable.circle));
        mBinding.progressBarGoogleSignIn.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
        mBinding.progressBarGoogleSignIn.setVisibility(View.VISIBLE);
        googleSignIn();
    }


    private void configureGoogleSignIn() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("336175886504-2nborf0it56gs89bs20gd5nm6vofnbd9.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Build a google sign in client specified by gso
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();

                            if (isNew) {
                                setNickName();
                            } else {
                                mBinding.revealView.setBackground(getResources().getDrawable(R.color.google_sign_in));
                                revealButton(mBinding.googleSignInFrame);
                                delayedStartNextActivity(mode);
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_unsuccessfull),Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, getResources().getString(R.string.google_login_faield), Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }


    public void googleChangeAccountClicked(View view) {
        firebaseAuth.signOut();
        mGoogleSignInClient.signOut();
        googleSignIn();
    }
    // -----------------------------


    private void startNextActivity(String mode) {

        // Set online flag true
        userRef.child(Util.getCurrentUserId()).child("online").setValue("true");

        if(mode.equals("solo"))
            startActivity(new Intent(LoginActivity.this, LevelSelectActivity.class)
                    .putExtra("me","solo"));
        else if(mode.equals("duel"))
            startActivity(new Intent(LoginActivity.this, OnlineActivity.class));
    }

    // The following functions are used for the sign in animation.
    // Delays the start of next activity with 100 ms
    private void delayedStartNextActivity(final String mode) {
        String test = Util.getCurrentUserId();
        userRef.child(Util.getCurrentUserId()).child("online").setValue("true");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                if(mode.equals("solo"))
                    startActivity(new Intent(LoginActivity.this, LevelSelectActivity.class).putExtra("me","solo"));
                else if(mode.equals("duel"))
                    startActivity(new Intent(LoginActivity.this, OnlineActivity.class));
            }
        },200); // needs 2ms delay for the View reveal to finish.
    }


    private void showProgressDialog(ProgressBar progressBar, boolean show) {

        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
        if(show) {
            progressBar.bringToFront();
            progressBar.setVisibility(View.VISIBLE);
        }
        else {
            progressBar.setVisibility(View.INVISIBLE);
        }


    }


    private void fadeOutTextAndSetProgressDialog(TextView signInText, final ProgressBar progressBar) {
        signInText.animate().alpha(0f).setDuration(250).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationCancel(animation);
                showProgressDialog(progressBar, true);
            }
        }).start();
    }

    // Grows or shrinks the button width
    private void animateButtonWidth(final FrameLayout signInButton, int startX, int endX) {
        ValueAnimator anim = ValueAnimator.ofInt(startX, endX);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int value = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = signInButton.getLayoutParams();
                layoutParams.width = value;
                signInButton.requestLayout();
            }
        });

        anim.setDuration(250);
        anim.start();
    }


    private void fadeOutProgressDialog(ProgressBar progressBar) {
        progressBar.animate().alpha(0f).setDuration(200).start();
    }


    private void nextAction(final String mode, final ProgressBar progressBar, final FrameLayout layout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                revealButton(layout);
                fadeOutProgressDialog(progressBar);
                mBinding.revealView.setBackground(getResources().getDrawable(R.color.primaryDark));
                delayedStartNextActivity(mode);
            }
        }, 2000);
    }

    // Creates the circular effect of a activity growing and filling the users screen
    private void revealButton(FrameLayout signInButton) {
        signInButton.setElevation(0f);
        mBinding.revealView.setVisibility(View.VISIBLE);

        int x = mBinding.revealView.getWidth();
        int y = mBinding.revealView.getHeight();

        int startX = (int) ((int)(getResources().getDimension(R.dimen.get_width)) / 2 + signInButton.getX());
        int startY = (int) ((int)(getResources().getDimension(R.dimen.get_width)) / 2 + signInButton.getY());

        float radius = Math.max(x,y) * 1.2f;

        Animator reveal = ViewAnimationUtils.createCircularReveal(mBinding.revealView, startX, startY, (int)(getResources().getDimension(R.dimen.get_width)), radius);
        reveal.setDuration(500);
        reveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationEnd(animation);

            }
        });

        reveal.start();
    }


    // Email sign in or register listener
    public void signInButtonClicked(View view) {
        userLogin();

    }

    public void registerButtonClicked(View view) {
        registerUser();
    }


    @Override
    public void onResume() {
        super.onResume();
        Constants.startMediaPlayer(0);
    }


}
