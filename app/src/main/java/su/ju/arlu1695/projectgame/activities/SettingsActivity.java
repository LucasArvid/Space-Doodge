package su.ju.arlu1695.projectgame.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import su.ju.arlu1695.projectgame.utils.Constants;
import su.ju.arlu1695.projectgame.R;


public class SettingsActivity extends AppCompatActivity {

    private Switch allowNotifications;
    private Switch allowMusic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();

        Constants.startMediaPlayer(0);

        // Declaration of switches
        allowNotifications = (Switch) findViewById(R.id.s_notifications);
        allowMusic = (Switch) findViewById(R.id.s_music);
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
                    Constants.startMediaPlayer(R.raw.soft_and_furious_06_and_never_come_back);
                    Constants.ALLOW_MUSIC = true;
                } else {
                    Constants.pauseMediaPlayer();
                    Constants.ALLOW_MUSIC = false;
                }
            }
        });


    }

    // Return to user set settings (returns to default otherwise.)
    private void resumeSettingsActivity() {
        allowNotifications.setChecked(Constants.ALLOW_INVITES);
        allowMusic.setChecked(Constants.ALLOW_MUSIC);

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
