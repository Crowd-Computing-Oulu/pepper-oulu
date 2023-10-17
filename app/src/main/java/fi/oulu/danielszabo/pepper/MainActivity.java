package fi.oulu.danielszabo.pepper;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.conversation.BodyLanguageOption;
import com.aldebaran.qi.sdk.object.conversation.SpeechEngine;

import fi.oulu.danielszabo.pepper.applications.Help;
import fi.oulu.danielszabo.pepper.applications.control.ControlFragment;
import fi.oulu.danielszabo.pepper.applications.pepper_study_promotion.PepperStudyPromotionFragment;
import fi.oulu.danielszabo.pepper.tools.SimpleController;
import fi.oulu.danielszabo.pepper.tools.SpeechInput;
import fi.oulu.danielszabo.pepper.applications.gpt_prototype.GPTFragment;
import fi.oulu.danielszabo.pepper.applications.home.HomeFragment;
import fi.oulu.danielszabo.pepper.applications.itee_promotion_offline.ITEEPromotionFragment;
import fi.oulu.danielszabo.pepper.log.LOG;
import fi.oulu.danielszabo.pepper.log.LogFragment;
import fi.oulu.danielszabo.pepper.applications.sona_promotion_gpt.SonaPromotionGPTFragment;
import fi.oulu.danielszabo.pepper.applications.action.Action;


public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks, LogFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener, ControlFragment.OnFragmentInteractionListener, Action.OnFragmentInteractionListener, Help.OnFragmentInteractionListener {


    private Fragment fragment;
    private ImageButton btn_help, btnVolume;
    private boolean isMuted = false;
    private SpeechEngine speechEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LOG.debug(this, "onCreate");

        // Register the RobotLifecycleCallbacks to this Activity.
        QiSDK.register(this, this);

        // Set activity layout
        setContentView(R.layout.activity_main);

        this.fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);

        // Add mute button and display skip button
        btnVolume = findViewById(R.id.btn_volume);
        btnVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMuted = !isMuted;
                if (isMuted) {
                    btnVolume.setImageResource(R.drawable.ic_volume_off);
                    setSpeechOutputVolume(0.0f);
                } else {
                    btnVolume.setImageResource(R.drawable.ic_volume_on);
                    setSpeechOutputVolume(0.6f);
                }
            }

            private void setSpeechOutputVolume(float volume) {
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int targetVolume = (int) (volume * maxVolume);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, targetVolume, 0);
            }
        });

        btn_help = findViewById(R.id.btn_help);
        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAppSelectorPressed(v);
            }
        });
    }

    public void onAppSelectorPressed(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        Fragment newFragment = null;
        int id = view.getId();
        if (id == R.id.btn_home) {
            newFragment = new HomeFragment();
        } else if (id == R.id.btn_gpt_prototype) {
            newFragment = new GPTFragment();
        } else if (id == R.id.btn_sona) {
            newFragment = new SonaPromotionGPTFragment();
        } else if (id == R.id.btn_control) {
            newFragment = new ControlFragment();
        } else if (id == R.id.btn_offline) {
            newFragment = new ITEEPromotionFragment();
        } else if (id == R.id.btn_help) {
            newFragment = new Help();
        } else if (id == R.id.btn_study) {
            newFragment = new PepperStudyPromotionFragment();
        } else if (id == R.id.btn_action) {
            newFragment = new Action();
        } else {
            newFragment = new HomeFragment();
        }
        fragmentTransaction.replace(R.id.fragment, newFragment, this.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        // Unregister the RobotLifecycleCallbacks for this Activity.
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        LOG.debug(this, "onRobotFocusGained");

        // Store context object in global application state, initialize the whole app
        PepperApplication.initialize(qiContext);

        LOG.debug(this, "Qi Context Created");

        // Set default fragment
        onAppSelectorPressed(findViewById(R.id.btn_study));
    }

    @Override
    public void onRobotFocusLost() {
        LOG.debug(this, "onRobotFocusLost");
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        LOG.debug(this, "onRobotFocusRefused: " + reason);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        LOG.debug(this, "onFragmentInteraction: " + uri);
    }
}
