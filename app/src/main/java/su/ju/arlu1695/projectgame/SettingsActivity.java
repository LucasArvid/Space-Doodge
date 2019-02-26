package su.ju.arlu1695.projectgame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private Switch allowNotifications;
    private Switch allowMusic;
    private Switch allowSound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();


        allowNotifications = (Switch) findViewById(R.id.s_notifications);
        allowMusic = (Switch) findViewById(R.id.s_music);
        allowSound = (Switch) findViewById(R.id.s_sound);
        resumeSettingsActivity();


        allowNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked() == true) {
                    Constants.ALLOW_INVITES = true;
                } else {
                    Constants.ALLOW_INVITES = false;
                }
            }
        });

        allowMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked() == true) {
                    Constants.ALLOW_MUSIC = true;
                } else {
                    Constants.ALLOW_MUSIC = false;
                }
            }
        });

        allowSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked() == true) {
                    Constants.ALLOW_SOUND = true;
                } else {
                    Constants.ALLOW_SOUND = false;
                }
            }
        });

    }

    private void resumeSettingsActivity() {
        allowNotifications.setChecked(Constants.ALLOW_INVITES);
        allowMusic.setChecked(Constants.ALLOW_MUSIC);
        allowSound.setChecked(Constants.ALLOW_SOUND);

    }
}
